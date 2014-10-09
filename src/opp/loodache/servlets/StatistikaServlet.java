package opp.loodache.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Izdanje;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.Ocjena;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.repository.RangListe.CasopisOcjena;
import opp.loodache.repository.RangListe.RadOcjena;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Sluzi za generiranje korisnicke statistike.
 */
@WebServlet("/stat")
public class StatistikaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null) {
			ErrorSender.sendError("Morate biti ulogirani za generiranje statistike", req, resp);
			return;
		}

		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
				&& !trenutni.getClanarina()) {
			ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
			return;
		}

		// dohvatim sve radove i ocjene svih radova od ovog korisnik
		List<Ocjena> ocjene = DAOProvider.getOcjenaDAO().sortedOcjene(trenutni);
		ocjene = ocjene.subList(0, Math.min(5, ocjene.size()));
		List<CasopisOcjenaS> ocjeneCasopisa = getCasopisOcjene(trenutni);
		ocjeneCasopisa = ocjeneCasopisa.subList(0, Math.min(5, ocjeneCasopisa.size()));

		// racunam bodove s koeficijentima za casopise
		Set<ZnanstveniCasopis> casopisi = populateCasopisi(trenutni);
		List<CasopisOcjena> slozenoCasopis = RangListeServlet.generirajCasopise(casopisi, false,
				trenutni, req, resp);
		slozenoCasopis = slozenoCasopis.subList(0, Math.min(5, slozenoCasopis.size()));

		// racunam bodove s koeficijentima za radove
		Set<ZnanstveniRad> radovi = populateRadovi(trenutni);
		List<RadOcjena> slozenoRadovi = RangListeServlet.generirajRadove(radovi, false, trenutni,
				req, resp);
		slozenoRadovi = slozenoRadovi.subList(0, Math.min(5, slozenoRadovi.size()));

		DnevnikUpdate.update(trenutni, "Izgenerirane korisnicke rang liste.");
		req.setAttribute("ocjene", ocjene);
		req.setAttribute("casopisi", ocjeneCasopisa);
		req.setAttribute("slCasopisi", slozenoCasopis);
		req.setAttribute("slRadovi", slozenoRadovi);
		req.getRequestDispatcher("/WEB-INF/pages/Statistika.jsp").forward(req, resp);
	}

	/**
	 * Pronadje sve radove koje je korisnik ocjenio.
	 * @param korisnik korisnik
	 * @return skup radova
	 */
	private Set<ZnanstveniRad> populateRadovi(Korisnik korisnik) {
		Set<ZnanstveniRad> radovi = new HashSet<>();

		Set<Ocjena> ocjene = DAOProvider.getOcjenaDAO().findOcjene(korisnik);
		for (Ocjena o : ocjene) {
			radovi.add(o.getZnanstveniRad());
		}
		return radovi;
	}

	/**
	 * Pronadje sve casopise u kojima je dani korisnik dao barem jednu ocjenu.
	 * @param korisnik korisnik
	 * @return skup casopisa
	 */
	private Set<ZnanstveniCasopis> populateCasopisi(Korisnik korisnik) {
		Set<ZnanstveniCasopis> casopisi = new HashSet<>();

		Set<Ocjena> ocjene = DAOProvider.getOcjenaDAO().findOcjene(korisnik);
		// populacija radova
		Set<ZnanstveniRad> radovi = new HashSet<>();
		for (Ocjena o : ocjene) {
			radovi.add(o.getZnanstveniRad());
		}

		// populacija izdanja
		Set<Izdanje> izdanja = new HashSet<>();
		for (ZnanstveniRad r : radovi) {
			izdanja.add(r.getIzdanje());
		}

		// populacija casopisa
		for (Izdanje i : izdanja) {
			casopisi.add(i.getZnanstveniCasopis());
		}
		return casopisi;
	}

	/**
	 * Dohvati sve korisnikove casopise i izracuna ocjene.
	 * @param korisnik korisnik
	 * @return listu casopisa i ocjena
	 */
	private static List<CasopisOcjenaS> getCasopisOcjene(Korisnik korisnik) {
		// pronalazim sve casopise
		Set<Ocjena> ocjene = DAOProvider.getOcjenaDAO().findOcjene(korisnik);
		Set<ZnanstveniCasopis> casopisi = new HashSet<>();
		for (Ocjena o : ocjene) {
			casopisi.add(o.getZnanstveniRad().getIzdanje().getZnanstveniCasopis());
		}

		// iteriram po casopisima i trazim ocjene
		List<CasopisOcjenaS> temp = new ArrayList<>();
		for (ZnanstveniCasopis c : casopisi) {
			// prelistam kroz sva izdanja
			Set<Izdanje> izdanja = DAOProvider.getIzdanjeDAO().getIzdanja(c);
			Set<ZnanstveniRad> radovi = new HashSet<>();
			for (Izdanje i : izdanja) {
				radovi.addAll(DAOProvider.getRadDAO().findZnanstveniRad(i));
			}

			// prelistam kroz sve radove
			Set<Ocjena> ocjeneCasopisa = new HashSet<>();
			for (ZnanstveniRad r : radovi) {
				ocjeneCasopisa.addAll(DAOProvider.getOcjenaDAO().findOcjene(r));
			}

			// nadjem prosjecnu ocjenu casopisa
			double ocjena = 0;
			int n = 0;
			for (Ocjena o : ocjeneCasopisa) {
				n++;
				ocjena += o.getOcjena();
			}
			if (n == 0) {
				temp.add(new CasopisOcjenaS(c, 0.0));
			} else {
				temp.add(new CasopisOcjenaS(c, ocjena / n));
			}

		}

		// reverse sort
		Collections.sort(temp, new Comparator<CasopisOcjenaS>() {

			@Override
			public int compare(CasopisOcjenaS o1, CasopisOcjenaS o2) {
				return Double.compare(o2.ocjena, o1.ocjena);
			}
		});

		return temp;
	}

	/**
	 * Razred za ocjene i casopise.
	 */
	public static class CasopisOcjenaS {

		private ZnanstveniCasopis casopis;
		private Double ocjena;

		/**
		 * Konstruktor.
		 * @param casopis znanstveni casopis
		 * @param ocjena prosjecna ocjena radova casopisa
		 */
		public CasopisOcjenaS(ZnanstveniCasopis casopis, Double ocjena) {
			super();
			this.casopis = casopis;
			this.ocjena = ocjena;
		}

		public ZnanstveniCasopis getCasopis() {
			return casopis;
		}

		public Double getOcjena() {
			return ocjena;
		}
	}
}

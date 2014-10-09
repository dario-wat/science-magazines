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
import opp.loodache.model.Referenca;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.repository.RangListe;
import opp.loodache.repository.RangListe.CasopisOcjena;
import opp.loodache.repository.RangListe.RadOcjena;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Generira sluzene rang liste. Moze pokrenuti samo strucna osoba sustava.
 */
@WebServlet("/rang")
public class RangListeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null || !trenutni.getKorisnikVrsta().equals(KorisnikVrsta.strucna)) {
			ErrorSender.sendError("Nemate prava za generiranje rang liste", req, resp);
			return;
		}

		RangListe.casopisi.clear();
		RangListe.radovi.clear();

		Set<ZnanstveniCasopis> casopisi = DAOProvider.getCasopisDAO().sviCasopisi();
		RangListe.casopisi = generirajCasopise(casopisi, true, trenutni, req, resp);

		Set<ZnanstveniRad> radovi = DAOProvider.getRadDAO().sviRadovi();
		RangListe.radovi = generirajRadove(radovi, true, trenutni, req, resp);

		DnevnikUpdate.update(trenutni, "Izgenerirane sluzbene rang liste.");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
	}

	/**
	 * Napravim rang listu radova.
	 * @param radovi skup radova koji se rangira
	 * @param sluzbeni govori koriste li se sluzbeni koeficijenti pri rangiranju
	 * @param req http request
	 * @param resp http response
	 * @return listu radova
	 */
	public static List<RadOcjena> generirajRadove(Set<ZnanstveniRad> radovi, boolean sluzbeni,
			Korisnik korisnik, HttpServletRequest req, HttpServletResponse resp) {

		List<RadOcjena> ocjene = new ArrayList<>();

		// racunam sve pomalo
		for (ZnanstveniRad r : radovi) {
			// postaim rad
			RadOcjena ocj = new RadOcjena();
			ocj.setRad(r);

			// izracunam broj citata
			Integer brojCitata = DAOProvider.getReferencaDAO().findReferencirajuMe(r).size();
			ocj.setBrojCitata(brojCitata);

			// racunam prosjecnu ocjenu rada
			Set<Ocjena> ocjeneRada = DAOProvider.getOcjenaDAO().findOcjene(r);
			Double avgRada = prosjecnaRada(ocjeneRada);
			ocj.setAvgKorisnika(avgRada);

			// racunam prosjecnu ocjenu casopisa
			ZnanstveniCasopis casopis = r.getIzdanje().getZnanstveniCasopis();
			Double avgCasopis = racunajProsjekKorisnika(casopis);
			ocj.setAvgCasopisa(avgCasopis);

			// racunam bodove casopisa
			CasopisOcjena cocj = new CasopisOcjena();
			cocj.setCasopis(casopis);
			cocj.setBrojCitata(racunajBrojCitata(casopis));
			cocj.setAvgKorisnika(racunajProsjekKorisnika(casopis));
			cocj.setBodovi(sluzbeni, korisnik);
			ocj.setBodoviCasopisa(cocj.getBodovi());

			// racunam bodove rada i dodajem
			ocj.setBodoviRada(sluzbeni, korisnik);
			ocjene.add(ocj);
		}

		// sortiram po broju bodova
		Collections.sort(ocjene, new Comparator<RadOcjena>() {

			@Override
			public int compare(RadOcjena o1, RadOcjena o2) {
				return Double.compare(o2.getBodovi(), o1.getBodovi());
			}
		});

		return ocjene.subList(0, Math.min(5, ocjene.size()));
	}

	/**
	 * Izracunam prosjecnu ocjenu rada
	 * @param ocjeneRada sve ocjene rada
	 * @return prosjek
	 */
	private static Double prosjecnaRada(Set<Ocjena> ocjeneRada) {
		Integer n = ocjeneRada.size();
		if (n == 0) {
			return 0.0;
		}
		Double suma = 0.0;
		for (Ocjena o : ocjeneRada) {
			suma += o.getOcjena();
		}
		return suma / n;
	}

	/**
	 * Generira rang listu casopisa.
	 * @param casopisi casopisi iz kojih se generiraju rang liste
	 * @param sluzbeni govori koriste li se sluzbeni koeficijenti pri rangiranju
	 * @param req http request
	 * @param resp http response
	 * @return listu casopisa
	 */
	public static List<CasopisOcjena> generirajCasopise(Set<ZnanstveniCasopis> casopisi,
			boolean sluzbeni, Korisnik korisnik, HttpServletRequest req, HttpServletResponse resp) {

		List<CasopisOcjena> ocjene = new ArrayList<>();

		// racunam sve podatke
		for (ZnanstveniCasopis c : casopisi) {
			// postavim casopis
			CasopisOcjena ocj = new CasopisOcjena();
			ocj.setCasopis(c);

			// racunam broj citata
			Integer brojCitata = racunajBrojCitata(c);
			ocj.setBrojCitata(brojCitata);

			// racunam prosjecnu ocjenu korisnika
			Double prosjecna = racunajProsjekKorisnika(c);
			ocj.setAvgKorisnika(prosjecna);

			ocj.setBodovi(sluzbeni, korisnik);
			ocjene.add(ocj);
		}

		// sortiram po najvecoj ocjeni
		Collections.sort(ocjene, new Comparator<CasopisOcjena>() {

			@Override
			public int compare(CasopisOcjena o1, CasopisOcjena o2) {
				return Double.compare(o2.getBodovi(), o1.getBodovi());
			}
		});

		return ocjene.subList(0, Math.min(5, ocjene.size()));
	}

	/**
	 * Racuna prosjek ocjena casopisa.
	 * @param c casopis
	 * @return prosjek
	 */
	private static Double racunajProsjekKorisnika(ZnanstveniCasopis c) {
		Set<Ocjena> ocjene = new HashSet<>();

		Set<Izdanje> izdanja = DAOProvider.getIzdanjeDAO().getIzdanja(c);
		Set<ZnanstveniRad> radovi = new HashSet<>();
		// populacija radova
		for (Izdanje i : izdanja) {
			radovi.addAll(DAOProvider.getRadDAO().findZnanstveniRad(i));
		}

		// populacija ocjena
		for (ZnanstveniRad r : radovi) {
			ocjene.addAll(DAOProvider.getOcjenaDAO().findOcjene(r));
		}

		Integer n = ocjene.size();
		if (n == 0) {
			return 0.0;
		}

		Double suma = 0.0;
		for (Ocjena o : ocjene) {
			suma += o.getOcjena();
		}

		return suma / n;
	}

	/**
	 * Racunam broj citata casopisa.
	 * @param c casopis
	 * @return broj citata
	 */
	private static Integer racunajBrojCitata(ZnanstveniCasopis c) {
		Set<Referenca> ref = new HashSet<>();

		Set<Izdanje> izdanja = DAOProvider.getIzdanjeDAO().getIzdanja(c);
		Set<ZnanstveniRad> radovi = new HashSet<>();
		// populacija radova
		for (Izdanje i : izdanja) {
			radovi.addAll(DAOProvider.getRadDAO().findZnanstveniRad(i));
		}

		// populacija referenci
		for (ZnanstveniRad r : radovi) {
			ref.addAll(DAOProvider.getReferencaDAO().findReferencirajuMe(r));
		}

		return ref.size();
	}

}

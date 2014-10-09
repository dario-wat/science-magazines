package opp.loodache.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Autor;
import opp.loodache.model.Izdanje;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.Referenca;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;
import opp.loodache.utilities.ParserRadova;
import opp.loodache.utilities.ParserRadova.ReferencaRada;

/**
 * Servlet za dodavanje radova u bazu.
 */
@WebServlet("/dodaj")
public class DodajRadoveServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		processDodaj(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		processDodaj(req, resp);
	}

	/**
	 * Izvrsava dodavanje radova u bazu podataka.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processDodaj(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		req.setCharacterEncoding("UTF-8");

		// provjera korisnika
		if (trenutni == null) {
			ErrorSender.sendError("Za dodavanje radova morate biti prijavljeni.", req, resp);
			return;
		}
		if (!trenutni.getKorisnikVrsta().equals(KorisnikVrsta.ovlastenCas)) {
			ErrorSender.sendError("Nemate prava za dodavanje radova.", req, resp);
			return;
		}

		// odredjivanje sto je kliknuto
		String method = req.getParameter("method");
		if (method == null) {
			req.getRequestDispatcher("/WEB-INF/pages/Dodavanje.jsp").forward(req, resp);
			return;
		}
		if (method.equals("Odustani")) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
			return;
		}
		if (method.equals("Spremi")) {
			try {
				processDodavanjeRadova(req, resp);
			} catch (Exception e) {
				ErrorSender.sendError("Greska u parsiranju.", req, resp);
			}
			return;
		}

		req.getRequestDispatcher("/WEB-INF/pages/Dodavanje.jsp").forward(req, resp);
	}

	/**
	 * Obavi dodavanje radova.
	 * @param req http request
	 * @param resp http response
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void processDodavanjeRadova(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String tekst = req.getParameter("tekst");
		if (tekst == null) {
			ErrorSender.sendError("Pogreška u tekstu.", req, resp);
			return;
		}

		try {
			// hvatam sve greske jer mi ih se neda rucno
			List<ParserRadova> radovi = ParserRadova.parseRadovi(tekst);
			for (ParserRadova r : radovi) {
				dodajRad(r, req, resp);
			}
		} catch (Exception e) {
			ErrorSender.sendError(
					"Dogodila se greska pri parsiranju teksta.\n" + e.getLocalizedMessage(), req,
					resp);
			return;
		}

		resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
	}

	/**
	 * Doda rad u bazu podataka.
	 * @param r rad
	 * @param req http request
	 * @param resp http response
	 */
	private void dodajRad(ParserRadova r, HttpServletRequest req, HttpServletResponse resp) {
		Set<ZnanstveniCasopis> casopisi = DAOProvider.getCasopisDAO().findByName(
				r.getNazivCasopisa(), r.getSkrNazivCasopisa());
		ZnanstveniCasopis casopis = null;
		if (casopisi.size() == 0) {		// casopis nije u bazi pa ga dodajem
			casopis = new ZnanstveniCasopis(r.getNazivCasopisa(), r.getSkrNazivCasopisa());
			DAOProvider.getCasopisDAO().insert(casopis);
		} else {		// inace postoji, ali cu uzet prvi jer me nije briga
			casopis = (ZnanstveniCasopis) casopisi.toArray()[0];
		}

		// casopis bi u ovom trenutku trebao biti persistant
		Set<Izdanje> izdanja = DAOProvider.getIzdanjeDAO().findBy(r.getRbrIzdanje(),
				r.getRbrSvezak(), r.getIsbn(), r.getIssn(), r.getDatumIzlaska());
		Izdanje izdanje = null;
		if (izdanja.size() == 0) {		// izdanje ne postoji
			izdanje = new Izdanje(r.getRbrIzdanje(), r.getRbrSvezak(), r.getIsbn(), r.getIssn(),
					r.getDatumIzlaska());
			izdanje.setZnanstveniCasopis(casopis);
			DAOProvider.getIzdanjeDAO().insert(izdanje);
		} else {			// postoji ali cu uzet samo prvo
			izdanje = (Izdanje) izdanja.toArray()[0];
		}

		// izdanje bi tu trebalo biti persistant
		Set<ZnanstveniRad> radovi = DAOProvider.getRadDAO().findBy(r.getNaslov(), r.getSazetak(),
				r.getKljucneRijeci(), r.getPdfUrl());
		ZnanstveniRad rad = null;
		if (radovi.size() == 0) {		// nema ga pa ga dodajem
			rad = new ZnanstveniRad(r.getNaslov(), r.getSazetak(), r.getKljucneRijeci(),
					r.getPdfUrl());
			rad.setIzdanje(izdanje);
			DAOProvider.getRadDAO().insert(rad);
		} else {
			rad = (ZnanstveniRad) radovi.toArray()[0];
		}

		// dodajem autora, rad bi trebao bi persistant
		for (String a : r.getAutori()) {
			String[] splitted = a.split("[ ]+");
			Korisnik kautor = null;
			if (splitted.length == 2) {
				kautor = DAOProvider.getKorisnikDAO().findByName(splitted[0].trim(),
						splitted[1].trim());
			}
			Autor autor = new Autor(rad, kautor, a);
			DAOProvider.getAutorDAO().insert(autor);
		}

		// dodajem reference
		for (ReferencaRada rr : r.getReference()) {
			rr.parse();
			String naziv = rr.getNazivRada();
			Set<ZnanstveniRad> nadjeni = DAOProvider.getRadDAO().findByNaslov(naziv);
			if (nadjeni.size() == 0) {
				break;
			}
			ZnanstveniRad prvi = (ZnanstveniRad) nadjeni.toArray()[0];
			Referenca ref = new Referenca(rad, prvi);
			DAOProvider.getReferencaDAO().insert(ref);
		}

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		DnevnikUpdate.update(trenutni, "Izvrseno dodavanje radova u sustav.");
	}
}

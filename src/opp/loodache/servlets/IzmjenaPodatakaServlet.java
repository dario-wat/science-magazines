package opp.loodache.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.repository.DatumINaknada;
import opp.loodache.repository.SluzbeniKoeficijenti;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;
import opp.loodache.utilities.ParseException;
import opp.loodache.utilities.ParserKonfiguracije;

/**
 * Sluzi za izmjenu osnovnih podataka poput sluzbenih koeficijenata, rokova i
 * naknada.
 */
@WebServlet("/uredi")
public class IzmjenaPodatakaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		processUredi(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		processUredi(req, resp);
	}

	/**
	 * Obavi izmjenu podataka.
	 * @param req http request
	 * @param resp http response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void processUredi(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");

		// provjera korisnik
		if (trenutni == null) {
			ErrorSender.sendError("Morate biti prijavljeni za izmjenu podataka.", req, resp);
			return;
		}
		if (!trenutni.getKorisnikVrsta().equals(KorisnikVrsta.ovlastenSus)
				&& !trenutni.getKorisnikVrsta().equals(KorisnikVrsta.strucna)) {
			ErrorSender.sendError("Nemate prava za uređivanjem podataka.", req, resp);
			return;
		}

		String method = req.getParameter("method");
		if (method == null) {					// prvo otvaranje
			openIzmjena(req, resp);
			return;
		}
		if (method.equals("Odustani")) {		// kliknuo na Odustani
			resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
			return;
		}
		if (method.equals("Spremi")) {			// kliknuo na Spremi
			updatePodaci(req, resp);
			return;
		}

		// do ovdje nikada ne bi trebalo doci, ali i ako dodje ispise nesto
		req.getRequestDispatcher("/WEB-INF/pages/Izmjena.jsp").forward(req, resp);
	}

	/**
	 * Osvjezi podatke u radnoj memoriji.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void updatePodaci(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Double koef1 = null, koef2 = null, koef3 = null, koef4 = null;
		Double ckoef1 = null, ckoef2 = null, naknada = null;
		String datum = null, origDatum = null;
		try {
			koef1 = Double.parseDouble(req.getParameter("koef1"));
			koef2 = Double.parseDouble(req.getParameter("koef2"));
			koef3 = Double.parseDouble(req.getParameter("koef3"));
			koef4 = Double.parseDouble(req.getParameter("koef4"));
			ckoef1 = Double.parseDouble(req.getParameter("ckoef1"));
			ckoef2 = Double.parseDouble(req.getParameter("ckoef2"));

			naknada = Double.parseDouble(req.getParameter("naknada"));
			if (naknada < 0) {
				ErrorSender.sendError("Naknada ne može biti negativan broj.", req, resp);
				return;
			}

			ParserKonfiguracije pk = new ParserKonfiguracije();
			pk.parseDate(req.getParameter("rok"));
			datum = pk.getRok();
			origDatum = pk.getOrigRok();
		} catch (NumberFormatException | ParseException e) {
			ErrorSender.sendError("Pogrešan unos kod podataka.\n"
					+ "Provjerite da ste dobro unijeli koeficijente.", req, resp);
			return;
		}

		SluzbeniKoeficijenti.koef1 = koef1;
		SluzbeniKoeficijenti.koef2 = koef2;
		SluzbeniKoeficijenti.koef3 = koef3;
		SluzbeniKoeficijenti.koef4 = koef4;
		SluzbeniKoeficijenti.ckoef1 = ckoef1;
		SluzbeniKoeficijenti.ckoef2 = ckoef2;
		DatumINaknada.naknada = naknada;
		DatumINaknada.origRok = origDatum;
		DatumINaknada.rok = datum;
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		DnevnikUpdate.update(trenutni, "Obavljena izmjena sluzbenih podataka.");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
	}

	/**
	 * Slanje potrebnih parametara stranici.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void openIzmjena(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setAttribute("naknada", DatumINaknada.naknada);
		req.setAttribute("rok", DatumINaknada.origRok);
		req.setAttribute("koef1", SluzbeniKoeficijenti.koef1);
		req.setAttribute("koef2", SluzbeniKoeficijenti.koef2);
		req.setAttribute("koef3", SluzbeniKoeficijenti.koef3);
		req.setAttribute("koef4", SluzbeniKoeficijenti.koef4);
		req.setAttribute("ckoef1", SluzbeniKoeficijenti.ckoef1);
		req.setAttribute("ckoef2", SluzbeniKoeficijenti.ckoef2);
		req.getRequestDispatcher("/WEB-INF/pages/Izmjena.jsp").forward(req, resp);
	}
}

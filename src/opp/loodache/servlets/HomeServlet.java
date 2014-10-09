package opp.loodache.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Autor;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.repository.DatumINaknada;
import opp.loodache.repository.RangListe;
import opp.loodache.repository.SluzbeniKoeficijenti;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet za home page. Prikazuje neke osnovne podatke korisnika ukoliko je
 * prijavljen. Prikazuje trazilicu.
 */
@WebServlet("/index")
public class HomeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		openHome(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		openHome(req, resp);
	}

	/**
	 * Obradi otvaranje pocetne stranice.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void openHome(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");

		String method = req.getParameter("method");
		// nema pretrazivanja i otvara se pocetna stranica
		if (method == null) {
			req.setAttribute("casopisv", "");
			req.setAttribute("radv", "");
			processHome(req, resp);
			return;
		}

		// ima pretrazivanja i pretrazuju se casopisi (po imenu)
		if (method.equals("Pretraži časopise")) {
			String input = req.getParameter("trazi.casopis");
			if (input == null || input.isEmpty()) {
				processHome(req, resp);
				return;
			}
			processSearchCasopis(req, resp, input);
			return;
		}

		// ima pretrazivanja i pretrazuju se radovi (po nekom od 3 parametra)
		if (method.equals("Pretraži radove")) {
			String input = req.getParameter("trazi.rad");
			if (input == null || input.isEmpty()) {
				processHome(req, resp);
				return;
			}
			processSearchRad(req, resp, input);
			return;
		}

		processHome(req, resp);
	}

	/**
	 * Obavlja pretrazvanje radova.
	 * @param req http request
	 * @param resp http response
	 * @param input upis u polje za pretragu
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processSearchRad(HttpServletRequest req, HttpServletResponse resp, String input)
			throws ServletException, IOException {

		req.setAttribute("radv", input);

		String[] words = input.split("[ ]+");
		List<String> wordsList = new ArrayList<>();
		fillList(wordsList, words);

		Set<ZnanstveniRad> radovi = null;
		String pretraziPo = req.getParameter("trazi.rad.po");
		if (pretraziPo.equals("naslov")) {
			radovi = DAOProvider.getRadDAO().searchByName(wordsList);
		} else if (pretraziPo.equals("autor")) {
			Set<Autor> autori = DAOProvider.getAutorDAO().findBy(wordsList);
			radovi = new HashSet<>();
			for (Autor a : autori) {
				radovi.add(a.getZnanstveniRad());
			}
		} else if (pretraziPo.equals("rijec")) {
			radovi = DAOProvider.getRadDAO().searchByKeyword(wordsList);
		} else {
			ErrorSender.sendError("Potrebno je odabrati kategoriju pretraživanja.", req, resp);
			return;
		}

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		DnevnikUpdate.update(trenutni, "Obavljena pretraga radova.");
		req.setAttribute("radovi", radovi);
		req.getRequestDispatcher("/WEB-INF/pages/SearchResults.jsp").forward(req, resp);
	}

	/**
	 * Obavlja pretrazivanje casopisa.
	 * @param req http request
	 * @param resp http response
	 * @param input upis u polje za pretragu
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processSearchCasopis(HttpServletRequest req, HttpServletResponse resp, String input)
			throws ServletException, IOException {

		req.setAttribute("casopisv", input);

		String[] words = input.split("[ ]+");
		List<String> wordsList = new ArrayList<>();
		fillList(wordsList, words);
		Set<ZnanstveniCasopis> casopisi = (Set<ZnanstveniCasopis>) DAOProvider.getCasopisDAO()
				.searchByName(wordsList);

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		DnevnikUpdate.update(trenutni, "Obavljena pretraga casopisa.");
		req.setAttribute("casopisi", casopisi);
		req.getRequestDispatcher("/WEB-INF/pages/SearchResults.jsp").forward(req, resp);
	}

	/**
	 * Procesira otvaranje pocetne stranice (ovaj dio se poziva kada nema
	 * pretrazivanja).
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processHome(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		KorisnikVrsta strucna = KorisnikVrsta.strucna;
		KorisnikVrsta ovlastena = KorisnikVrsta.ovlastenSus;
		KorisnikVrsta ovlCasopis = KorisnikVrsta.ovlastenCas;
		DnevnikUpdate.update(trenutni, "Otvoren home page.");
		req.setAttribute("trenutni", trenutni);
		req.setAttribute("strucna", strucna);
		req.setAttribute("ovlastena", ovlastena);
		req.setAttribute("ovlCasopis", ovlCasopis);
		req.setAttribute("koef1", SluzbeniKoeficijenti.koef1);
		req.setAttribute("koef2", SluzbeniKoeficijenti.koef2);
		req.setAttribute("koef3", SluzbeniKoeficijenti.koef3);
		req.setAttribute("koef4", SluzbeniKoeficijenti.koef4);
		req.setAttribute("ckoef1", SluzbeniKoeficijenti.ckoef1);
		req.setAttribute("ckoef2", SluzbeniKoeficijenti.ckoef2);
		req.setAttribute("naknada", DatumINaknada.naknada);
		req.setAttribute("rok", DatumINaknada.rok);
		req.setAttribute("casopisi", RangListe.casopisi);
		req.setAttribute("radovi", RangListe.radovi);
		req.getRequestDispatcher("/WEB-INF/pages/index.jsp").forward(req, resp);
	}

	/**
	 * Napuni listu sa elementima polja.
	 * @param list lista
	 * @param array polje
	 */
	private void fillList(List<String> list, String[] array) {
		for (String s : array) {
			list.add(s);
		}
	}
}

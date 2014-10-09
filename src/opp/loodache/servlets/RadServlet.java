package opp.loodache.servlets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Autor;
import opp.loodache.model.Izdanje;
import opp.loodache.model.KorKategorijaRad;
import opp.loodache.model.KorisnickaKategorija;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.Ocjena;
import opp.loodache.model.Referenca;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet za prikaz informacija o radu.
 */
@WebServlet("/rad/*")
public class RadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private boolean preusmjereno = false;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		processRad(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		processRad(req, resp);
	}

	/**
	 * Izvrsi prikaz rada.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processRad(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		preusmjereno = false;

		// dohvati id rada
		String stringId = req.getPathInfo();
		if (stringId == null || stringId.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za rad.", req, resp);
			return;
		}
		stringId = stringId.substring(1);

		// parsiraj id
		Long id = null;
		try {
			id = Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogrešan unos za rad.", req, resp);
			return;
		}

		// dohvati rad iz baze
		ZnanstveniRad rad = DAOProvider.getRadDAO().find(id, ZnanstveniRad.class);
		if (rad == null) {		// rad s ovim id-em ne postoji
			ErrorSender.sendError("Traženi rad ne postoji.", req, resp);
			return;
		}

		// pa dohvatim korisnika
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");

		// ovo tu je za dodavanje rada u kategoriju i ocjenjivanje
		String method = req.getParameter("method");
		if (method != null) {
			if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
					&& !trenutni.getClanarina()) {
				ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
				return;
			}

			if (method.equals("Dodaj")) {
				processDodajUKategoriju(rad, req, resp);
			} else if (method.equals("Ocjeni")) {
				processOcjena(rad, trenutni, req, resp);
			}
		}
		if (preusmjereno) {
			return;
		}

		// inace rad postoji
		String kws = splitKeyWords(rad.getKljucneRijeci());
		Izdanje izd = rad.getIzdanje();
		Ocjena ocjena = DAOProvider.getOcjenaDAO().findOcjena(trenutni, rad);
		Set<KorisnickaKategorija> kategorije = DAOProvider.getKorisnickaKategorijaDAO()
				.findKategorije(trenutni);
		Set<Autor> autori = DAOProvider.getAutorDAO().findByRad(rad);
		Set<Referenca> reference = DAOProvider.getReferencaDAO().findReferenciram(rad);

		Set<Double> ocjene = new HashSet<>();
		Set<String> oznake = new HashSet<>();
		Boolean mojrad = updateOcjene(trenutni, rad, ocjene, oznake);
		String ocj = intercalateDouble(ocjene);
		String ozn = intercalateString(oznake);
		DnevnikUpdate.update(trenutni, "Otvorena stranica rada.");

		req.setAttribute("rad", rad);
		req.setAttribute("kws", kws);
		req.setAttribute("izdanje", izd);
		req.setAttribute("korisnik", trenutni);
		req.setAttribute("ocjena", ocjena);
		req.setAttribute("kategorije", kategorije);
		req.setAttribute("autori", autori);
		req.setAttribute("reference", reference);
		req.setAttribute("mojrad", mojrad);
		req.setAttribute("ocj", ocj);
		req.setAttribute("ozn", ozn);
		req.getRequestDispatcher("/WEB-INF/pages/ZnanstveniRad.jsp").forward(req, resp);
	}

	/**
	 * Doda zareze i razmake izmedju stringova.
	 * @param oznake oznake
	 * @return string
	 */
	private String intercalateString(Set<String> oznake) {
		if (oznake.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (String s : oznake) {
			sb.append(s).append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	/**
	 * Doda zareze i razmake izmedju.
	 * @param ocjene skup ocjena
	 * @return string
	 */
	private String intercalateDouble(Set<Double> ocjene) {
		if (ocjene.size() == 0) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Double o : ocjene) {
			sb.append(o).append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		return sb.toString();
	}

	/**
	 * Ako je ulogirani korisnik autor biranog rada onda se pronadju sve ocjene
	 * i oznake.
	 * @param trenutni korisnik
	 * @param rad znanstveni rad
	 * @param ocjene skup ocjena
	 * @param oznake skup oznaka
	 * @return true ako je korisnik autor, false inace
	 */
	private boolean updateOcjene(Korisnik trenutni, ZnanstveniRad rad, Set<Double> ocjene,
			Set<String> oznake) {

		Autor autor = DAOProvider.getAutorDAO().find(rad, trenutni);
		if (autor == null) {
			return false;
		}

		for (Ocjena o : DAOProvider.getOcjenaDAO().findOcjene(rad)) {
			if (o.getOcjena() != null) {
				ocjene.add(o.getOcjena());
			}
			if (o.getOznaka() != null) {
				oznake.add(o.getOznaka());
			}
		}
		return true;
	}

	/**
	 * Obavi dodavanje ocjene radu.
	 * @param rad znanstveni rad (sigurno nije null)
	 * @param req http request
	 * @param resp http response
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void processOcjena(ZnanstveniRad rad, Korisnik trenutni, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		if (trenutni == null) {
			return;
		}

		if (DAOProvider.getAutorDAO().find(rad, trenutni) != null) {
			ErrorSender.sendError("Ne možete ocjeniti svoje radove.", req, resp);
			preusmjereno = true;
			return;
		}

		// dohvatim ocjenu iz liste sa stranice
		String ocjenaStr = req.getParameter("ocjena");
		String oznaka = req.getParameter("oznaka");
		if (ocjenaStr == null || oznaka == null) {
			return;
		}
		if (ocjenaStr.isEmpty() && oznaka.isEmpty()) {
			return;
		}

		// parsiram ocjenu
		Double ocjena = null;
		if (!ocjenaStr.equals("null") && !ocjenaStr.isEmpty()) {
			try {
				ocjena = Double.parseDouble(ocjenaStr);
			} catch (NumberFormatException e) {
				return;
			}
		}
		if (oznaka.length() > 50) {
			ErrorSender.sendError("Oznaka ne smije biti duža od 50 znakova.", req, resp);
			preusmjereno = true;
			return;
		}
		if (oznaka.isEmpty()) {
			oznaka = null;
		}

		Ocjena ocjenaEntity = new Ocjena(ocjena, oznaka, rad, trenutni);
		dodajOcjenuUBazu(ocjenaEntity, req, resp);
		DnevnikUpdate.update(trenutni, "Dodana ocjena.");
	}

	/**
	 * Izvrsi sigurno dodavanje u bazu. Ako su i ocjena i oznaka postavljene na
	 * null onda ce izbrisati to iz baze.
	 * @param ocjenaEntity objekt za dodavanje u bazu
	 * @param req http request
	 * @param resp http response
	 * @throws IOException exc
	 */
	private void dodajOcjenuUBazu(Ocjena ocjenaEntity, HttpServletRequest req,
			HttpServletResponse resp) throws IOException {

		if (ocjenaEntity.getOcjena() == null && ocjenaEntity.getOznaka() == null) {
			DAOProvider.getOcjenaDAO().delete(ocjenaEntity.getKorisnik(),
					ocjenaEntity.getZnanstveniRad());
		} else {
			DAOProvider.getOcjenaDAO().saveOrUpdate(ocjenaEntity);
		}

		preusmjereno = true;
		resp.sendRedirect(req.getServletContext().getContextPath() + "/user/"
				+ ocjenaEntity.getKorisnik().getId());
	}

	/**
	 * Obavlja dodavanje rada u kategoriju.
	 * @param radId id rada
	 * @param req http request
	 * @param resp http response
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void processDodajUKategoriju(ZnanstveniRad rad, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		// dohvatim id iz list objekta sa stranice i izadjem ako nesto ne valja
		String kat = req.getParameter("kategorija");
		if (kat == null || kat.isEmpty()) {
			return;
		}

		// parsiram id (ovo bi uvijek trebalo proci)
		Long idKat = null;
		try {
			idKat = Long.parseLong(kat);
		} catch (NumberFormatException e) {
			return;
		}

		// dohvatim kategoriju iz baze (uvijk bi trebala postojat)
		KorisnickaKategorija kategorija = DAOProvider.getKorisnickaKategorijaDAO().find(idKat,
				KorisnickaKategorija.class);
		if (kategorija == null) {
			return;
		}

		// dodam u bazu novi par kategorija - rad
		KorKategorijaRad kkr = new KorKategorijaRad(kategorija, rad);
		dodajKKRUBazu(kkr, req, resp);
	}

	/**
	 * Sigurno dodavanje u bazu
	 * @param kkr korisnicka kategorija - rad
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void dodajKKRUBazu(KorKategorijaRad kkr, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		if (DAOProvider.getKorKategorijaRadDAO().find(kkr.getKorisnickaKategorija(),
				kkr.getZnanstveniRad()) != null) {
			ErrorSender.sendError("Rad već postoji u odabranoj kategoriji.", req, resp);
		} else {
			DAOProvider.getKorKategorijaRadDAO().insert(kkr);
			Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
			DnevnikUpdate.update(trenutni, "Rad dodan u kategoriju.");
			resp.sendRedirect(req.getServletContext().getContextPath() + "/kategorija/"
					+ kkr.getKorisnickaKategorija().getId());
		}
		preusmjereno = true;
	}

	/**
	 * Funkcija razdvaja kljucne rijeci u string bez znaka ;
	 * @param keyWords kljucne rijeci
	 * @return string kljucnih rijeci odvojenih zarezom
	 */
	private static String splitKeyWords(String keyWords) {
		String[] splitted = keyWords.split(";");
		StringBuilder sb = new StringBuilder();
		for (String s : splitted) {
			sb.append(s).append(", ");
		}

		int len = sb.length();
		if (len >= 2) {
			sb.delete(len - 2, len);
		}
		return sb.toString();
	}
}

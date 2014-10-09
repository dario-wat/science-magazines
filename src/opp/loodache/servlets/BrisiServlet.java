package opp.loodache.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.KorKategorijaRad;
import opp.loodache.model.KorisnickaKategorija;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Sluzi za brisanje bilo kojeg podatka iz baze.
 */
@WebServlet("/del")
public class BrisiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private boolean preusmjereno = false;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		String p = req.getParameter("p");
		String stringId = req.getParameter("id");
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");

		// provjera parametara
		if (trenutni == null) {
			ErrorSender.sendError(
					"Morate biti prijavljeni da biste mogli brisati podatke iz baze.", req, resp);
			return;
		}

		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
				&& !trenutni.getClanarina()) {
			ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
			return;
		}

		if (p == null || p.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za podatak koji se briše.", req, resp);
			return;
		}
		if (stringId == null || stringId.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos identifikacije.", req, resp);
			return;
		}

		// parsiraj id
		Long id = null;
		try {
			id = Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogrešan unos identifikacije.", req, resp);
			return;
		}

		obrisiPodatak(p, id, trenutni, req, resp);

		if (!preusmjereno) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
		}
	}

	/**
	 * Obrisi podatak u vezi s danim parametrima.
	 * @param p parametar za vrstu podatka koji se brise
	 * @param id identifikacija podatka koji se brise
	 * @param korisnik trenutno ulogirani korisnik (o ovome ovisi oce li podatak
	 *            biti obrisan)
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void obrisiPodatak(String p, Long id, Korisnik trenutni, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		if (p.equals("kkat")) {
			obrisiKorisnickuKategoriju(id, trenutni, req, resp);
			return;
		} else if (p.equals("kkr")) {
			obrisiKorKatRad(id, trenutni, req, resp);
			return;
		}

		ErrorSender.sendError("Parametar za brisanje ne postoji.", req, resp);
		preusmjereno = true;
	}

	/**
	 * Izbrise korkat - rad iz baze.
	 * @param id id rada
	 * @param trenutni trenutni korisnik
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void obrisiKorKatRad(Long id, Korisnik trenutni, HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		// dohvat zadnje parametra
		String sid2 = req.getParameter("id2");
		if (sid2 == null || sid2.isEmpty()) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			preusmjereno = true;
			return;
		}

		// parsiranje parametra
		Long id2 = null;
		try {
			id2 = Long.parseLong(sid2);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			preusmjereno = true;
			return;
		}

		// provjera dohvacenih entiteta
		ZnanstveniRad rad = DAOProvider.getRadDAO().find(id, ZnanstveniRad.class);
		KorisnickaKategorija kat = DAOProvider.getKorisnickaKategorijaDAO().find(id2,
				KorisnickaKategorija.class);
		if (rad == null || kat == null) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			preusmjereno = true;
			return;
		}

		// dohvat kkr-a
		KorKategorijaRad kkr = DAOProvider.getKorKategorijaRadDAO().find(kat, rad);
		if (kkr == null) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			preusmjereno = true;
			return;
		}

		Korisnik kor = kat.getKorisnik();
		if (trenutni.equals(kor) || trenutni.getKorisnikVrsta().equals(KorisnikVrsta.ovlastenSus)) {
			DAOProvider.getKorKategorijaRadDAO().remove(kkr);
			DnevnikUpdate.update(trenutni, "Izbrisan rad iz korisnicke kategorije.");
		}

		resp.sendRedirect(req.getServletContext().getContextPath() + "/kategorija/" + kat.getId());
		preusmjereno = true;
	}

	/**
	 * Brisanje korisnicke kategorije.
	 * @param id identifikator
	 * @param trenutni trenutni korisnik
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void obrisiKorisnickuKategoriju(Long id, Korisnik trenutni, HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		KorisnickaKategorija kat = DAOProvider.getKorisnickaKategorijaDAO().find(id,
				KorisnickaKategorija.class);
		if (kat == null) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			preusmjereno = true;
			return;
		}
		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.strucna)
				|| trenutni.equals(kat.getKorisnik())) {

			DAOProvider.getKorisnickaKategorijaDAO().remove(id);
			DnevnikUpdate.update(trenutni, "Izbrisana korisnicka kategorija.");
		}

		resp.sendRedirect(req.getServletContext().getContextPath() + "/user/" + trenutni.getId());
		preusmjereno = true;
	}
}

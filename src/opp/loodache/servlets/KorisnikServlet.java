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
import opp.loodache.model.Interes;
import opp.loodache.model.KorisnickaKategorija;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.Ocjena;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet za korisnike. Prikaz profila korisnika.
 */
@WebServlet("/user/*")
public class KorisnikServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		processKorisnik(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		processKorisnik(req, resp);
	}

	/**
	 * Procesira zahtjev za korisnickim profilom.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processKorisnik(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// dohvati id
		String stringId = req.getPathInfo();
		if (stringId == null || stringId.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za korisnika.", req, resp);
			return;
		}
		stringId = stringId.substring(1);

		// parsiraj id
		Long id = null;
		try {
			id = Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogrešan unos za korisnika.", req, resp);
			return;
		}

		// dohvati korisnika
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null || !trenutni.getId().equals(id)) {
			ErrorSender.sendError("Nemate dozvolu gledati tuđe profile", req, resp);
			return;
		}

		// dodavanje korisnicke kategorije ako je to odabrano
		String method = req.getParameter("method");
		if (method != null) {	// dodavanje nove kategorije
			if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
					&& !trenutni.getClanarina()) {
				ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
				return;
			}
			
			String naziv = req.getParameter("nk");
			if (naziv != null && !naziv.isEmpty()) {
				if (naziv.length() > 50) {
					ErrorSender.sendError("Naziv može imati najviše 50 znakova.", req, resp);
					return;
				}
				if (existsKategorija(naziv, trenutni)) {
					ErrorSender.sendError("Kategorija s tim nazivom već postoji.", req, resp);
					return;
				}
				KorisnickaKategorija kat = new KorisnickaKategorija(naziv);
				kat.setKorisnik(trenutni);
				DAOProvider.getKorisnickaKategorijaDAO().insert(kat);
			}
		}

		// sve je proslo u redu. korisnik zeli pregledat svoj profil
		KorisnikVrsta vrsta = trenutni.getKorisnikVrsta();
		Set<KorisnickaKategorija> kategorije = DAOProvider.getKorisnickaKategorijaDAO()
				.findKategorije(trenutni);
		Set<Ocjena> ocjene = DAOProvider.getOcjenaDAO().findOcjene(trenutni);

		Set<Autor> autorstvo = DAOProvider.getAutorDAO().findByKorisnik(trenutni);
		// populacija korisnikovih radova
		Set<ZnanstveniRad> mojiRadovi = new HashSet<>();
		for (Autor a : autorstvo) {
			mojiRadovi.add(a.getZnanstveniRad());
		}

		Set<Interes> interesi = DAOProvider.getInteresDAO().findByKorisnik(trenutni);
		// populacija casopisa
		Set<ZnanstveniCasopis> casopisi = new HashSet<>();
		for (Interes i : interesi) {
			casopisi.add(i.getZnanstveniCasopis());
		}

		DnevnikUpdate.update(trenutni, "Otvoren profil.");
		Set<Korisnik> zamolbe = DAOProvider.getKorisnikDAO().getZamolbe();
		req.setAttribute("zamolbe", zamolbe);
		req.setAttribute("ovlsus", KorisnikVrsta.ovlastenSus);
		req.setAttribute("ovlcas", KorisnikVrsta.ovlastenCas);
		req.setAttribute("strucna", KorisnikVrsta.strucna);
		req.setAttribute("korisnik", trenutni);
		req.setAttribute("vrsta", vrsta);
		req.setAttribute("kategorije", kategorije);
		req.setAttribute("ocjene", ocjene);
		req.setAttribute("radovi", mojiRadovi);
		req.setAttribute("casopisi", casopisi);
		req.getRequestDispatcher("/WEB-INF/pages/Profil.jsp").forward(req, resp);
	}

	/**
	 * Funkcija provjeri postoji li vec korisnicka kategorija s danim imenom.
	 * @param naziv naziv kategorije
	 * @param trenutni trenutni korisnik
	 * @return true ako postoji, false inace
	 */
	private boolean existsKategorija(String naziv, Korisnik trenutni) {
		return DAOProvider.getKorisnickaKategorijaDAO().findByNazivAndKorisnik(naziv, trenutni) != null;
	}
}

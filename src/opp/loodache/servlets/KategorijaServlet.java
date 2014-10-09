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
import opp.loodache.model.KorKategorijaRad;
import opp.loodache.model.KorisnickaKategorija;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet se brine za prikaz sadrzaja korisnickih kategorija.
 */
@WebServlet("/kategorija/*")
public class KategorijaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		// provjera je li ulogiran
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null) {
			ErrorSender.sendError(
					"Morate biti prijavljeni da biste mogli pregledavati kategorije.", req, resp);
			return;
		}

		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
				&& !trenutni.getClanarina()) {
			ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
			return;
		}

		// dohvat id-a
		String stringId = req.getPathInfo();
		if (stringId == null || stringId.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za korisničku kategoriju.", req, resp);
			return;
		}
		stringId = stringId.substring(1);

		// parsiram id
		Long id = null;
		try {
			id = Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogrešan unos za korisničku kategoriju.", req, resp);
			return;
		}

		// dohvat kategorije
		KorisnickaKategorija kategorija = DAOProvider.getKorisnickaKategorijaDAO().find(id,
				KorisnickaKategorija.class);
		if (kategorija == null) {
			ErrorSender.sendError("Kategorija ne postoji.", req, resp);
			return;
		}
		if (!trenutni.equals(kategorija.getKorisnik())) {
			ErrorSender.sendError("Ne možete pregledavati tuđe kategorije", req, resp);
			return;
		}

		// dohvat radova iz kategorije
		DnevnikUpdate.update(trenutni, "Otvorena stranica kategorije.");
		Set<KorKategorijaRad> korkatrad = DAOProvider.getKorKategorijaRadDAO().findKKRs(kategorija);
		Set<ZnanstveniRad> radovi = new HashSet<>();
		for (KorKategorijaRad k : korkatrad) {
			radovi.add(k.getZnanstveniRad());
		}
		req.setAttribute("kategorija", kategorija);
		req.setAttribute("radovi", radovi);
		req.setAttribute("korisnik", trenutni);
		req.getRequestDispatcher("/WEB-INF/pages/Kategorija.jsp").forward(req, resp);
	}
}

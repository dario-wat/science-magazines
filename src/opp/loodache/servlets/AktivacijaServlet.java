package opp.loodache.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet sluzi za aktivaciju clanarine korisnika.
 */
@WebServlet("/aktiviraj")
public class AktivacijaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		// dohvacanje prijavljenog
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null || !trenutni.getKorisnikVrsta().equals(KorisnikVrsta.ovlastenSus)) {
			ErrorSender.sendError("Nemate prava za izvršavanje ove radnje.", req, resp);
			return;
		}

		// dohvatim parametar
		String ids = req.getParameter("id");
		if (ids == null) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			return;
		}

		// parsiram parametar
		Long id = null;
		try {
			id = Long.parseLong(ids);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			return;
		}

		// updejtanje
		Korisnik zaAktivaciju = DAOProvider.getKorisnikDAO().find(id, Korisnik.class);
		zaAktivaciju.setClanarina(true);
		zaAktivaciju.setZahtjev(false);
		DAOProvider.getKorisnikDAO().updateKorisnik(zaAktivaciju);

		DnevnikUpdate.update(trenutni, "Odobrena zamolba.");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/user/" + trenutni.getId());
	}
}

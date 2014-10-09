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
 * Servlet za obavljanje zamolbe
 */
@WebServlet("/molba")
public class ZamolbaServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null) {
			ErrorSender.sendError("Morate biti prijavljeni za obavljanje ove radnje.", req, resp);
			return;
		}

		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.strucna)
				|| trenutni.getKorisnikVrsta().equals(KorisnikVrsta.ovlastenSus)
				|| trenutni.getKorisnikVrsta().equals(KorisnikVrsta.ovlastenCas)) {
			ErrorSender.sendError("Vaša članarina je plaćena.", req, resp);
			return;
		}

		if (trenutni.getClanarina()) {
			ErrorSender.sendError("Vaša članarina je plaćena.", req, resp);
			return;
		}

		trenutni.setZahtjev(true);
		DAOProvider.getKorisnikDAO().updateKorisnik(trenutni);
		DnevnikUpdate.update(trenutni, "Poslana zamolba.");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/user/" + trenutni.getId());
	}
}

package opp.loodache.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Interes;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet sluzi za dodavanje i brisanje interesa iz baze.
 */
@WebServlet("/interes")
public class DodajInteresServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		// provjera korisnika
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null) {
			ErrorSender.sendError("Za ovu radnju morate biti prijavljeni.", req, resp);
			return;
		}

		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
				&& !trenutni.getClanarina()) {
			ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
			return;
		}

		// pocetna provjera parametara
		String p = req.getParameter("p");
		String sid = req.getParameter("i");
		if (p == null || sid == null || p.isEmpty() || sid.isEmpty()) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			return;
		}

		// parsiranje id-a
		Long id = null;
		try {
			id = Long.parseLong(sid);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			return;
		}

		// izvodim potrebnu operaciju
		if (p.equals("u")) {		// ukloni
			ZnanstveniCasopis casopis = DAOProvider.getCasopisDAO().find(id,
					ZnanstveniCasopis.class);
			if (casopis != null) {
				DAOProvider.getInteresDAO().remove(new Interes(casopis, trenutni));
				DnevnikUpdate.update(trenutni, "Casopis uklonjen iz interesa.");
			}
		} else if (p.equals("d")) {		// dodaj
			ZnanstveniCasopis casopis = DAOProvider.getCasopisDAO().find(id,
					ZnanstveniCasopis.class);
			if (casopis == null) {
				ErrorSender.sendError("Pogreška u parametrima.", req, resp);
				return;
			}
			DAOProvider.getInteresDAO().insert(new Interes(casopis, trenutni));
			DnevnikUpdate.update(trenutni, "Casopis dodan u interese.");
		} else {
			ErrorSender.sendError("Pogreška u parametrima.", req, resp);
			return;
		}

		resp.sendRedirect(req.getServletContext().getContextPath() + "/user/" + trenutni.getId());
	}
}

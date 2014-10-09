package opp.loodache.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Izdanje;
import opp.loodache.model.Korisnik;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet za obavljanje funkcionalnosti s izdanjima.
 */
@WebServlet("/izdanje/*")
public class IzdanjeServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		// trazim id
		String stringId = req.getPathInfo();
		if (stringId == null || stringId.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za izdanje.", req, resp);
			return;
		}
		stringId = stringId.substring(1);

		// parsiram id
		Long id = null;
		try {
			id = Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogrešan unos za izdanje.", req, resp);
			return;
		}

		// dohvacam izdanje iz baze
		Izdanje izd = DAOProvider.getIzdanjeDAO().find(id, Izdanje.class);
		if (izd == null) {		// izdanje ne postoji
			ErrorSender.sendError("Traženo izdanje ne postoji.", req, resp);
			return;
		}

		// inace postoji
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		DnevnikUpdate.update(trenutni, "Otvorena stranica izdanja.");
		ZnanstveniCasopis casopis = izd.getZnanstveniCasopis();
		Set<ZnanstveniRad> radovi = DAOProvider.getRadDAO().findZnanstveniRad(izd);
		req.setAttribute("izdanje", izd);
		req.setAttribute("casopis", casopis);
		req.setAttribute("radovi", radovi);
		req.getRequestDispatcher("/WEB-INF/pages/Izdanje.jsp").forward(req, resp);
	}
}

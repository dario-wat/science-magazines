package opp.loodache.servlets;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Interes;
import opp.loodache.model.Izdanje;
import opp.loodache.model.Kategorija;
import opp.loodache.model.Korisnik;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Kontroler za prikaz casopisa.
 */
@WebServlet("/casopis/*")
public class CasopisServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		String stringId = req.getPathInfo();
		if (stringId == null || stringId.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za časopis.", req, resp);
			return;
		}
		stringId = stringId.substring(1);
		Long id = null;
		try {
			id = Long.parseLong(stringId);
		} catch (NumberFormatException e) {
			ErrorSender.sendError("Pogrešan unos za časopis.", req, resp);
			return;
		}

		ZnanstveniCasopis casopis = DAOProvider.getCasopisDAO().find(id, ZnanstveniCasopis.class);
		if (casopis == null) {		// casopis ne postoji
			ErrorSender.sendError("Traženi časopis ne postoji.", req, resp);
			return;
		}

		// inace casopis postoji
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		Kategorija kategorija = casopis.getKategorija();
		Set<Izdanje> izdanja = DAOProvider.getIzdanjeDAO().getIzdanja(casopis);
		Interes interes = null;
		if (trenutni != null) {
			interes = DAOProvider.getInteresDAO().find(casopis, trenutni);
		}

		DnevnikUpdate.update(trenutni, "Otvorena stranica casopisa.");
		req.setAttribute("casopis", casopis);
		req.setAttribute("kategorija", kategorija);
		req.setAttribute("izdanja", izdanja);
		req.setAttribute("interes", interes);
		req.setAttribute("trenutni", trenutni);
		req.getRequestDispatcher("/WEB-INF/pages/Casopis.jsp").forward(req, resp);
	}
}

package opp.loodache.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.model.Korisnik;
import opp.loodache.utilities.DnevnikUpdate;

/**
 * Servlet za odjavu.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
			req.getSession().invalidate();
			DnevnikUpdate.update(trenutni, "Odjava iz sustava.");
		} catch (IllegalStateException ignorable) {
		}
		resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
	}
}

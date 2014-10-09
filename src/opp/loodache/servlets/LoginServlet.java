package opp.loodache.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Korisnik;
import opp.loodache.utilities.Crypter;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet za prijavljivanje korisnika.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {
		processLogin(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		processLogin(req, resp);
	}

	/**
	 * Obavi akciju prijave.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void processLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		req.setCharacterEncoding("UTF-8");

		String method = req.getParameter("method");
		if (method != null && method.equals("Login")) {
			checkUserInfo(req, resp);
		} else {
			req.setAttribute("username", "");
			req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
			return;
		}

		req.getRequestDispatcher("/WEB-INF/pages/Login.jsp").forward(req, resp);
	}

	/**
	 * Provjeri upisane podatke i prijavi korisnika ako su podaci ispravni.
	 * @param req http request
	 * @param resp http response
	 * @throws ServletException exc
	 * @throws IOException exc
	 */
	private void checkUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String username = req.getParameter("username");
		String password = req.getParameter("password");
		if (username == null || password == null) {
			ErrorSender.sendError("Potrebno je upisati i korisnicko ime i lozinku.", req, resp);
			return;
		}

		Korisnik korisnik = DAOProvider.getKorisnikDAO().findByUsername(username);
		if (korisnik != null) {
			String passwordHash = Crypter.calcHash(password);
			if (passwordHash.equals(korisnik.getLozinkaHash())) {
				req.getSession().setAttribute("trenutni.korisnik", korisnik);
				DnevnikUpdate.update(korisnik, "Prijava u sustav.");
				return;
			}
		}

		DnevnikUpdate.update(null, "Neuspjela prijava.");
		req.setAttribute("username", username);
		req.setAttribute("error", "Pogresno korisnicko ime ili lozinka.");
	}
}

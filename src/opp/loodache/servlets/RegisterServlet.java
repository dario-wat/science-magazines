package opp.loodache.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Korisnik;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;
import opp.loodache.utilities.UserForm;

/**
 * Procesira registraciju korisnika na server.
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		processRegistration(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		processRegistration(req, resp);
	}

	/**
	 * Procesira registraciju korsnika.
	 * @param req http request
	 * @param resp http response
	 * @throws IOException exc
	 * @throws ServletException exc
	 */
	private void processRegistration(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		Korisnik prijavljen = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (prijavljen != null) {
			ErrorSender.sendError("Vec ste registrirani.", req, resp);
			return;
		}

		req.setCharacterEncoding("UTF-8");

		//slucaj prvog otvaranja stranice
		String method = req.getParameter("method");
		if (method == null) {
			UserForm forma = UserForm.emptyForm();
			req.setAttribute("forma", forma);
			req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
			return;
		}
		
		//slucaj da je pritisnuto Odustani
		if (method.equals("Odustani")) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
			return;
		}
		
		//slucaj kad je pritisnuto Registriraj se
		UserForm forma = new UserForm();
		forma.fillFromHttpRequest(req);
		forma.validate();
		
		//ako registracija nije uspjela
		if (forma.hasErrors()) {
			req.setAttribute("forma", forma);
			req.getRequestDispatcher("/WEB-INF/pages/Register.jsp").forward(req, resp);
			return;
		}
		
		//ako je uspjela
		Korisnik korisnik = forma.createUser();
		DAOProvider.getKorisnikDAO().insert(korisnik);
		DnevnikUpdate.update(korisnik, "Obavljena registracija.");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/index");
	}
}

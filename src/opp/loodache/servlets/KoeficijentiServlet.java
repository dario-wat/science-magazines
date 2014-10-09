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
import opp.loodache.utilities.KoefForm;

/**
 * Sluzi za izmjenu korisnicki definiranih koeficijenata.
 */
@WebServlet("/koef")
public class KoeficijentiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		processKoefChange(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		processKoefChange(req, resp);
	}

	/**
	 * Izvrsi izmjenu koeficijenata.
	 * @param req http request
	 * @param resp http reponse
	 * @throws IOException exc
	 * @throws ServletException
	 */
	private void processKoefChange(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null) {
			ErrorSender.sendError("Za izmjenu koeficijenata morate biti prijavljeni.", req, resp);
			return;
		}
		
		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani) && !trenutni.getClanarina()) {
			ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
			return;
		}

		req.setCharacterEncoding("UTF-8");

		// slucaj prvog otvaranja stranice
		String method = req.getParameter("method");
		if (method == null) {
			KoefForm forma = new KoefForm();
			forma.fillFromKorisnik(trenutni);
			req.setAttribute("form", forma);
			req.getRequestDispatcher("/WEB-INF/pages/Koeficijenti.jsp").forward(req, resp);
			return;
		}

		// u slucaju da je korisnik lupio na Odustani
		if (method.equals("Odustani")) {
			resp.sendRedirect(req.getServletContext().getContextPath() + "/user/"
					+ trenutni.getId());
			return;
		}

		// slucaj kada je pritisnuo Potvrdi
		KoefForm forma = new KoefForm();
		forma.fillFromHttpRequest(req);
		forma.validate();

		// u slucaju da nije uspjelo (ima gresaka u koeficijentima)
		if (forma.hasErrors()) {
			req.setAttribute("form", forma);
			req.getRequestDispatcher("/WEB-INF/pages/Koeficijenti.jsp").forward(req, resp);
			return;
		}

		// ako nema gresaka
		Double koef1 = forma.getKoef1();
		Double koef2 = forma.getKoef2();
		Double koef3 = forma.getKoef3();
		Double koef4 = forma.getKoef4();
		trenutni.setKoef1(koef1);
		trenutni.setKoef2(koef2);
		trenutni.setKoef3(koef3);
		trenutni.setKoef4(koef4);
		DAOProvider.getKorisnikDAO().updateKorisnik(trenutni);
		DnevnikUpdate.update(trenutni, "Izmjenjeni korisnicki koeficijenti.");
		resp.sendRedirect(req.getServletContext().getContextPath() + "/user/" + trenutni.getId());
	}
}

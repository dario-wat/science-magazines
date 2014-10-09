package opp.loodache.utilities;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sluzi samo za slanje greske. Olaksava posao.
 */
public class ErrorSender {

	public static void sendError(String error, HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		req.setAttribute("error", error);
		req.getRequestDispatcher("/WEB-INF/pages/ErrorPage.jsp").forward(req, resp);
	}
}

package opp.loodache.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;
import opp.loodache.utilities.DnevnikUpdate;
import opp.loodache.utilities.ErrorSender;

/**
 * Servlet za skidanje radova.
 */
@WebServlet("/download/*")
public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
			IOException {

		// provjeravam korisnika
		Korisnik trenutni = (Korisnik) req.getSession().getAttribute("trenutni.korisnik");
		if (trenutni == null) {
			ErrorSender.sendError("Morate biti prijavljeni za preuzimanje radova.", req, resp);
			return;
		}

		if (trenutni.getKorisnikVrsta().equals(KorisnikVrsta.registrirani)
				&& !trenutni.getClanarina()) {
			ErrorSender.sendError("Morate platiti članarinu za ove usluge.", req, resp);
			return;
		}

		// trazim naziv rada
		String naziv = req.getPathInfo();
		if (naziv == null || naziv.isEmpty()) {
			ErrorSender.sendError("Pogrešan unos za url rada.", req, resp);
			return;
		}
		naziv = naziv.substring(1);

		String path = req.getServletContext().getRealPath(File.separator);
		String fullPath = path + "/WEB-INF/radovi/" + naziv;

		// pronalazimfile
		File file = new File(fullPath);
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			ErrorSender.sendError("Datoteka nije pronađena.", req, resp);
			return;
		}
		
		// izvrsavam skidanje
		resp.setContentType("application/pdf");
		resp.setHeader("Content-disposition", "attachment; filename=" + naziv);
		OutputStream out = resp.getOutputStream();

		// saljem na out stream
		byte[] buffer = new byte[4096];
		int len;
		while ((len = in.read(buffer)) > 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.flush();
		
		DnevnikUpdate.update(trenutni, "Preuzeo datoteku.");
	}
}

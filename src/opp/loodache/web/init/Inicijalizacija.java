package opp.loodache.web.init;

import java.io.File;
import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import opp.loodache.dao.jpa.JPAEMFProvider;
import opp.loodache.repository.DatumINaknada;
import opp.loodache.repository.SluzbeniKoeficijenti;
import opp.loodache.utilities.ParseException;
import opp.loodache.utilities.ParserKonfiguracije;

/**
 * Listener koji otvara i zatvara vezu prema bazi te ucitava i sprema
 * konfiguracijsku datoteku.
 */
@WebListener
public class Inicijalizacija implements ServletContextListener {

	private String fileName;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		fileName = sce.getServletContext().getRealPath(File.separator)
				+ "/WEB-INF/konfiguracija.txt";

		try {
			readConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("baza.podataka.za.casopise");
		sce.getServletContext().setAttribute("my.application.emf", emf);
		JPAEMFProvider.setEmf(emf);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		try {
			saveConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
		}

		JPAEMFProvider.setEmf(null);
		EntityManagerFactory emf = (EntityManagerFactory) sce.getServletContext().getAttribute(
				"my.application.emf");
		if (emf != null) {
			emf.close();
		}
	}

	/**
	 * Ucita konfiguracijsku datoteku i spremi sve podatke u radnu memoriju.
	 */
	private void readConfiguration() {
		final File file = new File(fileName);
		ParserKonfiguracije pk = new ParserKonfiguracije();
		try {
			pk.parseFile(file);
		} catch (ParseException | IOException e) {
			throw new ParseException(e.getLocalizedMessage());
		}

		SluzbeniKoeficijenti.koef1 = pk.getKoef1();
		SluzbeniKoeficijenti.koef2 = pk.getKoef2();
		SluzbeniKoeficijenti.koef3 = pk.getKoef3();
		SluzbeniKoeficijenti.koef4 = pk.getKoef4();
		SluzbeniKoeficijenti.ckoef1 = pk.getCkoef1();
		SluzbeniKoeficijenti.ckoef2 = pk.getCkoef2();

		DatumINaknada.naknada = pk.getNaknada();
		DatumINaknada.rok = pk.getRok();
		DatumINaknada.origRok = pk.getOrigRok();
	}

	/**
	 * Spremi trenutnu radnu memoriju u datoteku za buduce pokretanje.
	 * @throws IOException exc
	 */
	private void saveConfiguration() throws IOException {
		File file = new File(fileName);
		ParserKonfiguracije pk = new ParserKonfiguracije();

		pk.setCkoef1(SluzbeniKoeficijenti.ckoef1);
		pk.setCkoef2(SluzbeniKoeficijenti.ckoef2);
		pk.setKoef1(SluzbeniKoeficijenti.koef1);
		pk.setKoef2(SluzbeniKoeficijenti.koef2);
		pk.setKoef3(SluzbeniKoeficijenti.koef3);
		pk.setKoef4(SluzbeniKoeficijenti.koef4);

		pk.setNaknada(DatumINaknada.naknada);
		pk.setRok(DatumINaknada.rok);
		pk.setOrigRok(DatumINaknada.origRok);

		pk.saveConfiguration(file);
	}
}
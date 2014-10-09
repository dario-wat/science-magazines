package opp.loodache.utilities;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Dnevnik;
import opp.loodache.model.Korisnik;

/**
 * Klasa za dodavanje zapisa i denvnik.
 */
public class DnevnikUpdate {

	/**
	 * Doda zapis u dnevnik. Olaksava posao.
	 * @param korisnik korisnik
	 * @param akcija akcija
	 * @return
	 */
	public static void update(Korisnik korisnik, String akcija) {
		if (korisnik == null) {
			DAOProvider.getDnevnikDAO().insert(new Dnevnik(null, akcija));
		} else {
			DAOProvider.getDnevnikDAO().insert(new Dnevnik(korisnik.getUsername(), akcija));
		}
	}
}

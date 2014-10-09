package opp.loodache.dao.jpa;

import opp.loodache.model.Dnevnik;

/**
 * Veza s bazom za dnevnik promjena.
 */
public class DnevnikDAO {

	/**
	 * Doda zapis u dnevnik.
	 * @param zapis zapis
	 */
	public void insert(Dnevnik zapis) {
		JPAEMProvider.getEntityManager().persist(zapis);
		JPAEMProvider.close();
	}
}

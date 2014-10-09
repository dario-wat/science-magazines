package opp.loodache.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * <code>EntityManagerFactory</code> provider.
 */
public class JPAEMFProvider {

	public static EntityManagerFactory emf;
	
	/**
	 * Getter for emf.
	 * @return emf
	 */
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	/**
	 * Setter for emf.
	 * @param emf emf
	 */
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}
}
package opp.loodache.dao.jpa;


import javax.persistence.EntityManager;

import opp.loodache.dao.DAOException;

/**
 * <code>EntityManager</code> provider.
 */
public class JPAEMProvider {

	private static ThreadLocal<LocalData> locals = new ThreadLocal<>();

	/**
	 * Creates <code>EntityManager</code> and starts transaction.
	 * @return entity manager
	 */
	public static EntityManager getEntityManager() {
		LocalData ldata = locals.get();
		if(ldata==null) {
			ldata = new LocalData();
			ldata.em = JPAEMFProvider.getEmf().createEntityManager();
			ldata.em.getTransaction().begin();
			locals.set(ldata);
		}
		return ldata.em;
	}

	/**
	 * Commits transaction and closes entity manager.
	 * @throws DAOException if commiting transaction is not possible
	 */
	public static void close() throws DAOException {
		LocalData ldata = locals.get();
		if(ldata==null) {
			return;
		}
		DAOException dex = null;
		try {
			ldata.em.getTransaction().commit();
		} catch(Exception ex) {
			dex = new DAOException("Unable to commit transaction.", ex);
		}
		try {
			ldata.em.close();
		} catch(Exception ex) {
			if(dex!=null) {
				dex = new DAOException("Unable to close entity manager.", ex);
			}
		}
		locals.remove();
		if(dex!=null) throw dex;
	}
	
	/**
	 * Helper class.
	 */
	private static class LocalData {
		EntityManager em;
	}
	
}
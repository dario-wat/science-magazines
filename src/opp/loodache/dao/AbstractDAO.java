package opp.loodache.dao;

import javax.persistence.EntityManager;

import opp.loodache.dao.jpa.JPAEMProvider;

/**
 * Razred koji implementira metode insert i delete iz sucelja DAO.
 * @param <T> tip objekta koji se brise iz baze
 */
public abstract class AbstractDAO<T> implements DAO<T> {

	@Override
	public void insert(T data) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(data);
		JPAEMProvider.close();
	}

	@Override
	public T find(Long id, Class<T> entityClass) {
		EntityManager em = JPAEMProvider.getEntityManager();
		return em.find(entityClass, id);
	}

	@Override
	public abstract void remove(Long id);

}

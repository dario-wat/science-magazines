package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import opp.loodache.model.Referenca;
import opp.loodache.model.ZnanstveniRad;

/**
 * Veza s bazom za reference.
 */
public class ReferencaDAO {

	/**
	 * Pronadje sve radove koji referenciraju ovaj rad.
	 * @param rad znanstveni rad
	 * @return skup radova
	 */
	public Set<Referenca> findReferencirajuMe(ZnanstveniRad rad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Referenca> ref = new HashSet<>(em
				.createQuery("select r from Referenca as r where r.referenca=:z")
				.setParameter("z", rad).getResultList());
		JPAEMProvider.close();
		return ref;
	}
	
	/**
	 * Ubaci referencu u bazu.
	 * @param ref referenca
	 */
	public void insert(Referenca ref) {
		JPAEMProvider.getEntityManager().merge(ref);
		JPAEMProvider.close();
	}
	
	/**
	 * Pronadje sve radove koje ovaj rad referencira.
	 * @param rad znanstveni rad
	 * @return skup radova
	 */
	public Set<Referenca> findReferenciram(ZnanstveniRad rad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Referenca> ref = new HashSet<>(em
				.createQuery("select r from Referenca as r where r.znanstveniRad=:z")
				.setParameter("z", rad).getResultList());
		JPAEMProvider.close();
		return ref;
	}
}

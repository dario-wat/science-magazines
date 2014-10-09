package opp.loodache.dao.jpa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import opp.loodache.model.Korisnik;
import opp.loodache.model.Ocjena;
import opp.loodache.model.ZnanstveniRad;

/**
 * Veza s bazom za ocjene.
 */
public class OcjenaDAO {

	/**
	 * Nadje ocjene po korisniku.
	 * @param korisnik korisnik
	 * @return skup ocjena
	 */
	public Set<Ocjena> findOcjene(Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Ocjena> ocjene = new HashSet<>(em
				.createQuery("select o from Ocjena as o where o.korisnik=:k")
				.setParameter("k", korisnik).getResultList());
		JPAEMProvider.close();
		return ocjene;
	}
	
	/**
	 * Pronadje sve ocjene po radu.
	 * @param rad znanstveni rad
	 * @return skup ocjena
	 */
	public Set<Ocjena> findOcjene(ZnanstveniRad rad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Ocjena> ocjene = new HashSet<>(em
				.createQuery("select o from Ocjena as o where o.znanstveniRad=:k")
				.setParameter("k", rad).getResultList());
		JPAEMProvider.close();
		return ocjene;
	}

	/**
	 * Pronadje ocjenu korisnika za neki rad.
	 * @param korisnik korisnik
	 * @param znanstveniRad rad
	 * @return ocjenu ako postoji, null inace
	 */
	public Ocjena findOcjena(Korisnik korisnik, ZnanstveniRad znanstveniRad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			return (Ocjena) em
					.createQuery(
							"select o from Ocjena as o where o.korisnik=:k and o.znanstveniRad=:r")
					.setParameter("k", korisnik).setParameter("r", znanstveniRad).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			JPAEMProvider.close();
		}
	}

	/**
	 * Izbrise ocjenu iz baze koju je dao korisnik nekom radu.
	 * @param korisnik korisnik
	 * @param znanstveniRad rad
	 */
	public void delete(Korisnik korisnik, ZnanstveniRad znanstveniRad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from Ocjena as o where o.korisnik=:k and o.znanstveniRad=:r")
				.setParameter("k", korisnik).setParameter("r", znanstveniRad).executeUpdate();
		JPAEMProvider.close();
	}

	/**
	 * Doda ocjenu u bazu ili updejta ako vec postoji.
	 * @param ocjena ocjena
	 */
	public void saveOrUpdate(Ocjena ocjena) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.merge(ocjena);
		JPAEMProvider.close();
	}

	/**
	 * Dohvati ocjene radova nekog korisnika sortirane.
	 * @param korisnik korisnik
	 * @return listu ocjena
	 */
	public List<Ocjena> sortedOcjene(Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		List<Ocjena> ocjene = new ArrayList<>(em
				.createQuery(
						"select o from Ocjena as o where o.ocjena is not null and o.korisnik=:k "
								+ "order by o.ocjena desc").setParameter("k", korisnik)
				.getResultList());
		JPAEMProvider.close();
		return ocjene;
	}
}

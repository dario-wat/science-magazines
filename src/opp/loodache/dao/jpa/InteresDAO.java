package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import opp.loodache.model.Interes;
import opp.loodache.model.Korisnik;
import opp.loodache.model.ZnanstveniCasopis;

/**
 * Veza s bazom za interese.
 */
public class InteresDAO {

	/**
	 * Pronadje sve interese nekog korisnika.
	 * @param korisnik korisnik
	 * @return skup interesa
	 */
	public Set<Interes> findByKorisnik(Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Interes> interesi = new HashSet<>(em
				.createQuery("select i from Interes as i where i.korisnik=:k")
				.setParameter("k", korisnik).getResultList());
		JPAEMProvider.close();
		return interesi;
	}

	/**
	 * Pronadje sve interese za neki casopis.
	 * @param casopis znanstveni casopis
	 * @return skup interesa
	 */
	public Set<Interes> findByCasopis(ZnanstveniCasopis casopis) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Interes> interesi = new HashSet<>(em
				.createQuery("select i from Interes as i where i.znanstveniCasopis=:c")
				.setParameter("c", casopis).getResultList());
		JPAEMProvider.close();
		return interesi;
	}

	/**
	 * Pronadje interes nekog korisnika za neki casopis.
	 * @param casopis znanstveni casopis
	 * @param korisnik korisnik
	 * @return interes
	 */
	public Interes find(ZnanstveniCasopis casopis, Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			return (Interes) em
					.createQuery(
							"select i from Interes as i where i.korisnik=:k and i.znanstveniCasopis=:c")
					.setParameter("k", korisnik).setParameter("c", casopis).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	/**
	 * Obrise dani interes iz baze.
	 * @param interes interes
	 */
	public void remove(Interes interes) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from Interes as i where i.korisnik=:k and i.znanstveniCasopis=:c")
				.setParameter("k", interes.getKorisnik())
				.setParameter("c", interes.getZnanstveniCasopis()).executeUpdate();
		JPAEMProvider.close();
	}
	
	/**
	 * Dodam interes u bazu.
	 * @param interes interes
	 */
	public void insert(Interes interes) {
		JPAEMProvider.getEntityManager().merge(interes);
		JPAEMProvider.close();
	}
}

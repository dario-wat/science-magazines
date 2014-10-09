package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;

import opp.loodache.dao.AbstractDAO;
import opp.loodache.model.Korisnik;

public class KorisnikDAO extends AbstractDAO<Korisnik> {

	/**
	 * Pronalazi korisnika po korisnickom imenu.
	 * @param username korisnicko ime
	 * @return vraca pronadjednog korisnika ako postoji, null inace
	 */
	public Korisnik findByUsername(String username) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			Korisnik korisnik = (Korisnik) em.createNamedQuery("Korisnik.username")
					.setParameter("username", username).getSingleResult();
			return korisnik;
		} catch (NoResultException e) {
			return null;
		} finally {
			JPAEMProvider.close();
		}
	}

	/**
	 * Updejta korisnika (koristi se za osvjezavanje koeficijenata).
	 * @param korisnik korisnik
	 */
	public void updateKorisnik(Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.merge(korisnik);
		JPAEMProvider.close();
	}

	@Override
	public void remove(Long id) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from Korisnik as k where k.id=:id").setParameter("id", id)
				.executeUpdate();
		JPAEMProvider.close();
	}

	/**
	 * Pronadje korisnika po imenu i prezimenu.
	 * @param name ime
	 * @param prezime prezime
	 * @return korisnika
	 */
	public Korisnik findByName(String ime, String prezime) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			return (Korisnik) em
					.createQuery("select k from Korisnik as k where k.ime=:i and k.prezime=:p")
					.setParameter("i", ime).setParameter("p", prezime).getSingleResult();
		} catch (NoResultException | NonUniqueResultException e) {
			return null;
		}
	}

	/**
	 * Dohvati sve korisnike sa aktivnim zamolbama.
	 * @return skup korisnika
	 */
	public Set<Korisnik> getZamolbe() {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Korisnik> korisnici = new HashSet<>(em.createQuery(
				"select k from Korisnik as k where lower(k.zahtjev)='y'").getResultList());
		JPAEMProvider.close();
		return korisnici;
	}
}

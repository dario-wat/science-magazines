package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import opp.loodache.dao.AbstractDAO;
import opp.loodache.model.KorisnickaKategorija;
import opp.loodache.model.Korisnik;

/**
 * Veza s bazom za korisnicke kategorije.
 */
public class KorisnickaKategorijaDAO extends AbstractDAO<KorisnickaKategorija> {

	/**
	 * Pronadje korisnicku kategoriju po nazivu i korisniku.
	 * @param naziv naziv kategorije
	 * @param korisnik korisnik
	 * @return korisnicku kategoriju
	 */
	public KorisnickaKategorija findByNazivAndKorisnik(String naziv, Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			return (KorisnickaKategorija) em.createNamedQuery("KorKat.nazivKorisnik")
					.setParameter("n", naziv).setParameter("kor", korisnik).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			JPAEMProvider.close();
		}
	}

	@Override
	public void remove(Long id) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from KorisnickaKategorija as k where k.id=:id")
				.setParameter("id", id).executeUpdate();
		JPAEMProvider.close();
	}

	/**
	 * Dohvati sve korisnicke kategorje danog korisnika.
	 * @param korisnik korisnik
	 * @return skup korisnickih kategorija
	 */
	public Set<KorisnickaKategorija> findKategorije(Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<KorisnickaKategorija> kategorije = new HashSet<>(em
				.createQuery("select k from KorisnickaKategorija as k where k.korisnik=:kor")
				.setParameter("kor", korisnik).getResultList());
		JPAEMProvider.close();
		return kategorije;
	}
}

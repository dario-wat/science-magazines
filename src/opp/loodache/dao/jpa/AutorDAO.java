package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import opp.loodache.model.Autor;
import opp.loodache.model.Korisnik;
import opp.loodache.model.ZnanstveniRad;

/**
 * Veza s bazom za autore.
 */
public class AutorDAO {

	/**
	 * Ubacim autora u bazu za neki rad.
	 * @param autor autor
	 */
	public void insert(Autor autor) {
		JPAEMProvider.getEntityManager().merge(autor);
		JPAEMProvider.close();
	}

	/**
	 * Pronadje sve autore nekog rada.
	 * @param rad znanstveni rad
	 * @return skup autora
	 */
	public Set<Autor> findByRad(ZnanstveniRad rad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Autor> autori = new HashSet<>(em
				.createQuery("select a from Autor as a where a.znanstveniRad=:r")
				.setParameter("r", rad).getResultList());
		JPAEMProvider.close();
		return autori;
	}

	/**
	 * Pronadje sva autorstva nekog korisnika.
	 * @param korisnik korisnik
	 * @return autorstva
	 */
	public Set<Autor> findByKorisnik(Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Autor> autori = new HashSet<>(em
				.createQuery("select a from Autor as a where a.korisnik=:k")
				.setParameter("k", korisnik).getResultList());
		JPAEMProvider.close();
		return autori;
	}

	/**
	 * Pronadje autore prema listi rijeci.
	 * @param names imena (rijeci)
	 * @return skup autora
	 */
	public Set<Autor> findBy(List<String> wordsList) {
		EntityManager em = JPAEMProvider.getEntityManager();
		Set<Autor> autori = new HashSet<>();
		for (String s : wordsList) {
			@SuppressWarnings("unchecked")
			List<Autor> nadjeni = em
					.createQuery("select a from Autor as a where lower(a.autor) like lower(:word)")
					.setParameter("word", "%" + s + "%").getResultList();
			autori.addAll(nadjeni);
		}
		return autori;
	}

	/**
	 * Pronadje autorstvo po korisniku i radu.
	 * @param rad znanstveni rad
	 * @param korisnik korisnik
	 * @return autorstvo
	 */
	public Autor find(ZnanstveniRad rad, Korisnik korisnik) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			return (Autor) em
					.createQuery(
							"select a from Autor as a where a.korisnik=:k and a.znanstveniRad=:r")
					.setParameter("k", korisnik).setParameter("r", rad).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}

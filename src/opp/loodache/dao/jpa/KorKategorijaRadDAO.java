package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import opp.loodache.model.KorKategorijaRad;
import opp.loodache.model.KorisnickaKategorija;
import opp.loodache.model.ZnanstveniRad;

/**
 * Veza s bazom za dodavanje radova u korisnicke kategorije.
 */
public class KorKategorijaRadDAO {

	/**
	 * Pronadje relaciju vezanu uz znanstveni rad koji se nalazi u odredjenoj
	 * korisnickoj kategoriji.
	 * @param kategorija korisnicka kategorija
	 * @param rad znanstveni rad
	 * @return relaciju vezanu uz rad i korisnicku kategoriju
	 */
	public KorKategorijaRad find(KorisnickaKategorija kategorija, ZnanstveniRad rad) {
		EntityManager em = JPAEMProvider.getEntityManager();
		try {
			return (KorKategorijaRad) em
					.createQuery(
							"select k from KorKategorijaRad as k where k.korisnickaKategorija=:kk and k.znanstveniRad=:r")
					.setParameter("kk", kategorija).setParameter("r", rad).getSingleResult();
		} catch (NoResultException e) {
			return null;
		} finally {
			JPAEMProvider.close();
		}
	}

	/**
	 * Ubacim kkr u bazu podataka.
	 * @param kkr korisnicka kategorija - rad
	 */
	public void insert(KorKategorijaRad kkr) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.merge(kkr);
		JPAEMProvider.close();
	}

	/**
	 * Dohvati iz baze sve radove (relaciju korkatrad) unutar jedne korisnicke
	 * kategorije.
	 * @param kategorija korisnicka kategorija
	 * @return vrati sve radove iz kategorije
	 */
	public Set<KorKategorijaRad> findKKRs(KorisnickaKategorija kategorija) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<KorKategorijaRad> kkrs = new HashSet<>(
				em.createQuery(
						"select kkr from KorKategorijaRad as kkr where kkr.korisnickaKategorija=:k")
						.setParameter("k", kategorija).getResultList());
		JPAEMProvider.close();
		return kkrs;
	}

	/**
	 * Ukloni rad iz korisnicke kategorije.
	 * @param kkr kkr
	 */
	public void remove(KorKategorijaRad kkr) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery(
				"delete from KorKategorijaRad as k where "
						+ "k.korisnickaKategorija=:kat and k.znanstveniRad=:rad")
				.setParameter("kat", kkr.getKorisnickaKategorija())
				.setParameter("rad", kkr.getZnanstveniRad()).executeUpdate();
		JPAEMProvider.close();
	}
}

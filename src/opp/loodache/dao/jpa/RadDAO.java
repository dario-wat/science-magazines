package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import opp.loodache.dao.AbstractDAO;
import opp.loodache.model.Izdanje;
import opp.loodache.model.ZnanstveniRad;

/**
 * Veza sa radovima iz baze.
 */
public class RadDAO extends AbstractDAO<ZnanstveniRad> {

	/**
	 * Pronadje skup radova koji sadrze barem jednu rijec iz liste.
	 * @param words lista rijeci
	 * @return skup radova
	 */
	public Set<ZnanstveniRad> searchByName(List<String> words) {
		EntityManager em = JPAEMProvider.getEntityManager();
		Set<ZnanstveniRad> radovi = new HashSet<>();
		for (String s : words) {
			@SuppressWarnings("unchecked")
			List<ZnanstveniRad> nadjeni = em
					.createQuery(
							"select r from ZnanstveniRad as r where lower(r.naslov) like lower(:word)")
					.setParameter("word", "%" + s + "%").getResultList();
			radovi.addAll(nadjeni);
		}
		return radovi;
	}

	/**
	 * Pretrazi radove po kljucnim rijecima.
	 * @param keywords lista kljucnih rijeci
	 * @return skup radova
	 */
	public Set<ZnanstveniRad> searchByKeyword(List<String> keywords) {
		EntityManager em = JPAEMProvider.getEntityManager();
		Set<ZnanstveniRad> radovi = new HashSet<>();
		for (String s : keywords) {
			@SuppressWarnings("unchecked")
			List<ZnanstveniRad> nadjeni = em
					.createQuery("select r from ZnanstveniRad as r where r.kljucneRijeci like :kw")
					.setParameter("kw", "%" + s + "%").getResultList();
			radovi.addAll(nadjeni);
		}
		return radovi;
	}

	@Override
	public void remove(Long id) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from ZnanstveniRad as r where r.id=:id").setParameter("id", id)
				.executeUpdate();
		JPAEMProvider.close();
	}

	/**
	 * Dohvati iz baze sve radove u nekom izdanju.
	 * @param izdanje izdanje
	 * @return skup radova
	 */
	public Set<ZnanstveniRad> findZnanstveniRad(Izdanje izdanje) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<ZnanstveniRad> radovi = new HashSet<>(em
				.createQuery("select r from ZnanstveniRad as r where r.izdanje=:i")
				.setParameter("i", izdanje).getResultList());
		JPAEMProvider.close();
		return radovi;
	}

	/**
	 * Pronadje radove po iducim kriterijima.
	 * @param naslov naslov rada
	 * @param sazetak sazetak rada
	 * @param kljucneRijeci kljucne rijeci rada
	 * @param pdfUrl url pdf-a
	 * @return skup radova
	 */
	public Set<ZnanstveniRad> findBy(String naslov, String sazetak, String kljucneRijeci,
			String pdfUrl) {

		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<ZnanstveniRad> radovi = new HashSet<>(em
				.createQuery(
						"select r from ZnanstveniRad as r where r.naslov=:n "
								+ "and r.sazetak=:s and r.kljucneRijeci=:k and r.pdfUrl=:p")
				.setParameter("n", naslov).setParameter("s", sazetak)
				.setParameter("k", kljucneRijeci).setParameter("p", pdfUrl).getResultList());
		JPAEMProvider.close();
		return radovi;
	}

	/**
	 * Dohvati sve radove iz baze.
	 * @return skup radova
	 */
	public Set<ZnanstveniRad> sviRadovi() {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<ZnanstveniRad> radovi = new HashSet<>(em
				.createQuery("select r from ZnanstveniRad as r").getResultList());
		JPAEMProvider.close();
		return radovi;
	}

	/**
	 * Pronadje sve radove s danim naslovom.
	 * @param naslov naslov rada
	 * @return
	 */
	public Set<ZnanstveniRad> findByNaslov(String naslov) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<ZnanstveniRad> radovi = new HashSet<>(em
				.createQuery("select r from ZnanstveniRad as r where r.naslov=:n")
				.setParameter("n", naslov).getResultList());
		JPAEMProvider.close();
		return radovi;
	}
}

package opp.loodache.dao.jpa;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;

import opp.loodache.dao.AbstractDAO;
import opp.loodache.model.ZnanstveniCasopis;

/**
 * Veza sa casopisima iz baze.
 */
public class CasopisDAO extends AbstractDAO<ZnanstveniCasopis> {

	/**
	 * Pronadje skup casopisa koji sadrze bar jednu rijec u svom imenu.
	 * @param words rijeci za pronalazak
	 * @return skup pronadjenih casopisa
	 */
	public Set<ZnanstveniCasopis> searchByName(List<String> words) {
		EntityManager em = JPAEMProvider.getEntityManager();
		Set<ZnanstveniCasopis> casopisi = new HashSet<>();
		for (String s : words) {
			@SuppressWarnings("unchecked")
			List<ZnanstveniCasopis> nadjeni = em
					.createQuery(
							"select c from ZnanstveniCasopis as c where lower(c.naziv) like lower(:word)")
					.setParameter("word", "%" + s + "%").getResultList();
			casopisi.addAll(nadjeni);
		}
		return casopisi;
	}

	@Override
	public void remove(Long id) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from ZnanstveniCasopis as c where c.id=:id").setParameter("id", id)
				.executeUpdate();
		JPAEMProvider.close();
	}

	/**
	 * Dohvati sve casopise po nazivu i skracenom nazivu.
	 * @param naziv naziv
	 * @param skrNaziv skraceni naziv
	 * @return skup casopisa
	 */
	public Set<ZnanstveniCasopis> findByName(String naziv, String skrNaziv) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<ZnanstveniCasopis> casopisi = new HashSet<>(em
				.createQuery(
						"select c from ZnanstveniCasopis as c where c.naziv=:n and c.skrNaziv=:s")
				.setParameter("n", naziv).setParameter("s", skrNaziv).getResultList());
		JPAEMProvider.close();
		return casopisi;
	}

	/**
	 * Dohvatim sve casopise iz baze.
	 * @return skup casopisa
	 */
	public Set<ZnanstveniCasopis> sviCasopisi() {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<ZnanstveniCasopis> casopisi = new HashSet<>(em.createQuery(
				"select c from ZnanstveniCasopis as c").getResultList());
		JPAEMProvider.close();
		return casopisi;
	}
}

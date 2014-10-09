package opp.loodache.dao.jpa;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;

import opp.loodache.dao.AbstractDAO;
import opp.loodache.model.Izdanje;
import opp.loodache.model.ZnanstveniCasopis;

/**
 * Veza za izdanja s bazom.
 */
public class IzdanjeDAO extends AbstractDAO<Izdanje> {

	@Override
	public void remove(Long id) {
		EntityManager em = JPAEMProvider.getEntityManager();
		em.createQuery("delete from Izdanje as i where i.id=:id").setParameter("id", id)
				.executeUpdate();
		JPAEMProvider.close();
	}

	/**
	 * Dohvati iz baze podataka sva izdanja nekog casopisa.
	 * @param casopis casopis
	 * @return izdanja
	 */
	public Set<Izdanje> getIzdanja(ZnanstveniCasopis casopis) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Izdanje> izdanja = new HashSet<>(em
				.createQuery("select i from Izdanje as i where i.znanstveniCasopis=:c")
				.setParameter("c", casopis).getResultList());
		JPAEMProvider.close();
		return izdanja;
	}

	/**
	 * Pronalazi izdanja po iducim parametrima.
	 * @param rbrIzdanje redni broj izdanja
	 * @param rbrSvezak redni broj sveska
	 * @param isbn isbn
	 * @param issn issn
	 * @param datum datum izdanja
	 * @return skup izdanja
	 */
	public Set<Izdanje> findBy(Long rbrIzdanje, Long rbrSvezak, String isbn, String issn, Date datum) {
		EntityManager em = JPAEMProvider.getEntityManager();
		@SuppressWarnings("unchecked")
		Set<Izdanje> izdanja = new HashSet<>(em
				.createQuery(
						"select i from Izdanje as i where i.rbrIzdanje=:ri and i.rbrSvezak=:rs "
								+ "and i.isbn=:isbn and i.issn=:issn and i.datumIzdanja=:d")
				.setParameter("ri", rbrIzdanje).setParameter("rs", rbrSvezak)
				.setParameter("isbn", isbn).setParameter("issn", issn).setParameter("d", datum)
				.getResultList());
		JPAEMProvider.close();
		return izdanja;
	}
}

package opp.loodache.utilities;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import opp.loodache.dao.jpa.JPAEMFProvider;
import opp.loodache.dao.jpa.JPAEMProvider;
import opp.loodache.model.Kategorija;
import opp.loodache.model.Podrucje;

/**
 * Ucita sve kategorije i podrucja u bazu. Pozove se samo prvi put kad se baza
 * napravi.
 */
public class PodrucjeKategorijaLoader {

	public static void main(String[] args) throws IOException {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("baza.podataka.za.casopise");
		JPAEMFProvider.setEmf(emf);

		EntityManager em = JPAEMProvider.getEntityManager();
		List<ParserKategorije.Redak> retci = ParserKategorije.parseKategorija();
		dodajSve(em, retci);
		JPAEMProvider.close();

		JPAEMFProvider.setEmf(null);
		if (emf != null) {
			emf.close();
		}
	}

	/**
	 * Doda sva podrucja i kategorije u bazu.
	 * @param em entity manager
	 * @param retci lista redaka.
	 */
	private static void dodajSve(EntityManager em, List<ParserKategorije.Redak> retci) {
		for (ParserKategorije.Redak r : retci) {
			Podrucje podrucje = findByName(em, r.getPodrucjeHrv());
			if (podrucje == null) {
				podrucje = new Podrucje(r.getPodrucjeHrv(), r.getPodrucjeEng());
				em.persist(podrucje);
			}

			Kategorija kategorija = new Kategorija(r.getKategorijaHrv(), r.getKategorijaEng(),
					r.getKategorijaSkr());
			kategorija.setPodrucje(podrucje);
			em.persist(kategorija);
		}
	}

	/**
	 * Pronadje podrucje u bazi.
	 * @param em entity manager
	 * @param name naziv
	 * @return podrucje ako postoji, null inace
	 */
	private static Podrucje findByName(EntityManager em, String name) {
		try {
			return (Podrucje) em.createQuery("select p from Podrucje as p where p.nazivHrv=:n")
					.setParameter("n", name).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}

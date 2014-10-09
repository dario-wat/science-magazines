package opp.loodache.utilities;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import opp.loodache.dao.jpa.JPAEMFProvider;
import opp.loodache.dao.jpa.JPAEMProvider;
import opp.loodache.model.KorisnikVrsta;

/**
 * Ucita sve vrste korisnika u bazu. Pokrenuti samo prvi put kad se stvori baza.
 */
public class KorisnikVrstaLoader {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("baza.podataka.za.casopise");
		JPAEMFProvider.setEmf(emf);
		
		EntityManager em = JPAEMProvider.getEntityManager();
		em.persist(KorisnikVrsta.neregistrirani);
		em.persist(KorisnikVrsta.registrirani);
		em.persist(KorisnikVrsta.ovlastenCas);
		em.persist(KorisnikVrsta.ovlastenSus);
		em.persist(KorisnikVrsta.strucna);
		JPAEMProvider.close();
		
		JPAEMFProvider.setEmf(null);
		if (emf != null) {
			emf.close();
		}
	}
}

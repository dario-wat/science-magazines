package opp.loodache.console;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import opp.loodache.dao.jpa.JPAEMFProvider;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.casopise");
//		JPAEMFProvider.setEmf(emf);
//
//		EntityManager em = JPAEMProvider.getEntityManager();
//
//		Korisnik k = new Korisnik();
//		k.setClanarina(true);
//		k.setEmail("mail");
//		k.setIme("Imeime");
//		k.setLozinkaHash("ovojelozinka");
//		k.setPrezime("imaprezime");
//		k.setUsername("user");
//		em.persist(k);
//		JPAEMProvider.close();

		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("baza.podataka.za.casopise");
		//sce.getServletContext().setAttribute("my.application.emf", emf);
		JPAEMFProvider.setEmf(emf);
	}
}

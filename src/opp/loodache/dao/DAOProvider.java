package opp.loodache.dao;

import opp.loodache.dao.jpa.AutorDAO;
import opp.loodache.dao.jpa.CasopisDAO;
import opp.loodache.dao.jpa.DnevnikDAO;
import opp.loodache.dao.jpa.InteresDAO;
import opp.loodache.dao.jpa.IzdanjeDAO;
import opp.loodache.dao.jpa.KorKategorijaRadDAO;
import opp.loodache.dao.jpa.KorisnickaKategorijaDAO;
import opp.loodache.dao.jpa.KorisnikDAO;
import opp.loodache.dao.jpa.OcjenaDAO;
import opp.loodache.dao.jpa.RadDAO;
import opp.loodache.dao.jpa.ReferencaDAO;

/**
 * Provider za DAO.
 */
public class DAOProvider {

	private static KorisnikDAO korisnikDAO = new KorisnikDAO();
	private static CasopisDAO casopisDAO = new CasopisDAO();
	private static RadDAO radDAO = new RadDAO();
	private static IzdanjeDAO izdanjeDAO = new IzdanjeDAO();
	private static KorisnickaKategorijaDAO korisnickaKategorijaDAO = new KorisnickaKategorijaDAO();
	private static OcjenaDAO ocjenaDAO = new OcjenaDAO();
	private static KorKategorijaRadDAO korKategorijaRadDAO = new KorKategorijaRadDAO();
	private static ReferencaDAO referencaDAO = new ReferencaDAO();
	private static AutorDAO autorDAO = new AutorDAO();
	private static InteresDAO interesDAO = new InteresDAO();
	private static DnevnikDAO dnevnikDAO = new DnevnikDAO();

	public static KorisnikDAO getKorisnikDAO() {
		return korisnikDAO;
	}

	public static CasopisDAO getCasopisDAO() {
		return casopisDAO;
	}

	public static RadDAO getRadDAO() {
		return radDAO;
	}

	public static IzdanjeDAO getIzdanjeDAO() {
		return izdanjeDAO;
	}

	public static KorisnickaKategorijaDAO getKorisnickaKategorijaDAO() {
		return korisnickaKategorijaDAO;
	}

	public static OcjenaDAO getOcjenaDAO() {
		return ocjenaDAO;
	}

	public static KorKategorijaRadDAO getKorKategorijaRadDAO() {
		return korKategorijaRadDAO;
	}

	public static ReferencaDAO getReferencaDAO() {
		return referencaDAO;
	}

	public static AutorDAO getAutorDAO() {
		return autorDAO;
	}

	public static InteresDAO getInteresDAO() {
		return interesDAO;
	}

	public static DnevnikDAO getDnevnikDAO() {
		return dnevnikDAO;
	}
}
package opp.loodache.dao;

public interface DAO<T> {

	/**
	 * Unese podatak u bazu.
	 * @param data podatak
	 */
	void insert(T data);
	
	/**
	 * Pronadje podatak iz baze sa identifikacijom id.
	 * @param id identifikacija
	 * @param entityClass razred koji ce funkcija vratit
	 * @return pronadjeni podatak
	 */
	T find(Long id, Class<T> entityClass);
	
	/**
	 * Izbrise iz baze podatak s identifikacijom id.
	 * @param id identifikacija
	 */
	void remove(Long id);
}
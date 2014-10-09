package opp.loodache.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model podrucja za svrstavanje znanstvenih casopisa.
 * @author dario
 */
@Entity
@Table(name = "podrucje")
public class Podrucje {

	private Long id;
	private String nazivHrv;
	private String nazivEng;

	private Set<Kategorija> kategorije = new HashSet<>();

	/**
	 * Javni konstruktor sa svim potrebnim parametrima.
	 * @param nazivHrv naziv podrucja na hrvatskom jeziku
	 * @param nazivEng naziv podrucja na engleskom jeziku
	 */
	public Podrucje(String nazivHrv, String nazivEng) {
		super();
		this.nazivHrv = nazivHrv;
		this.nazivEng = nazivEng;
	}

	/**
	 * Defaultni javni konstruktor.
	 */
	public Podrucje() {
		super();
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 50, nullable = false)
	public String getNazivEng() {
		return nazivEng;
	}

	public void setNazivEng(String nazivEng) {
		this.nazivEng = nazivEng;
	}

	@Column(length = 50, nullable = false)
	public String getNazivHrv() {
		return nazivHrv;
	}

	public void setNazivHrv(String nazivHrv) {
		this.nazivHrv = nazivHrv;
	}

	@OneToMany(mappedBy = "podrucje", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Kategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(Set<Kategorija> kategorije) {
		this.kategorije = kategorije;
	}
}

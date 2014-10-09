package opp.loodache.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Model vrste/razine korisnika.
 * @author dario
 */
@Entity
@Table(name = "korisnik_vrsta")
public class KorisnikVrsta {

	public static final KorisnikVrsta neregistrirani = new KorisnikVrsta(Long.valueOf(1),
			"Neregistrirani");
	public static final KorisnikVrsta registrirani = new KorisnikVrsta(Long.valueOf(2),
			"Registrirani");
	public static final KorisnikVrsta ovlastenCas = new KorisnikVrsta(Long.valueOf(3),
			"Ovlastena osoba casopisa");
	public static final KorisnikVrsta ovlastenSus = new KorisnikVrsta(Long.valueOf(4),
			"Ovlastena osoba sustava");
	public static final KorisnikVrsta strucna = new KorisnikVrsta(Long.valueOf(5),
			"Strucna osoba sustava");

	private Long id;
	private String naziv;

	private Set<Korisnik> korisnici = new HashSet<>();

	public KorisnikVrsta() {
	}

	/**
	 * Privatni konstruktor koji se koristi za oblikovanje unaprijed definiranih
	 * vrsta.
	 * @param id id
	 * @param naziv naziv
	 */
	private KorisnikVrsta(Long id, String naziv) {
		super();
		this.id = id;
		this.naziv = naziv;
	}

	@Id
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 30, nullable = false)
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	@OneToMany(mappedBy = "korisnikVrsta", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(Set<Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KorisnikVrsta other = (KorisnikVrsta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

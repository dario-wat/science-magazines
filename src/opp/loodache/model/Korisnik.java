package opp.loodache.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Type;

/**
 * Model korisnika.
 */
@Entity
@Table(name = "korisnik", uniqueConstraints = @UniqueConstraint(columnNames = { "ime", "prezime" }))
@NamedQueries({
		@NamedQuery(name = "Korisnik.username", query = "select u from Korisnik as u where u.username=:username"),
		@NamedQuery(name = "Korisnik.id", query = "select u from Korisnik as u where u.id=:id") })
public class Korisnik {

	private Long id;
	private String ime;
	private String prezime;
	private String username;
	private String lozinkaHash;
	private String email;
	private Boolean clanarina = false;
	private Boolean zahtjev = false;

	private KorisnikVrsta korisnikVrsta;
	private Set<Interes> interesi = new HashSet<>();
	private Set<Autor> autori = new HashSet<>();
	private Set<Ocjena> ocjene = new HashSet<>();
	private Set<KorisnickaKategorija> kategorije = new HashSet<>();

	private Double koef1;
	private Double koef2;
	private Double koef3;
	private Double koef4;

	/**
	 * Javni konstruktor za sve parametre.
	 * @param ime ime
	 * @param prezime prezime
	 * @param username username
	 * @param lozinkaHash hash lozinke
	 * @param email e-mail
	 * @param clanarina zastavica za clanarinu
	 */
	public Korisnik(String ime, String prezime, String username, String lozinkaHash, String email,
			Boolean clanarina) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.username = username;
		this.lozinkaHash = lozinkaHash;
		this.email = email;
		this.clanarina = clanarina;
	}

	/**
	 * Javni default konstruktor.
	 */
	public Korisnik() {
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
	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	@Column(length = 50, nullable = false)
	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	@Column(length = 30, nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(length = 40, nullable = false)
	public String getLozinkaHash() {
		return lozinkaHash;
	}

	public void setLozinkaHash(String lozinkaHash) {
		this.lozinkaHash = lozinkaHash;
	}

	@Column(length = 50, nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Type(type = "yes_no")
	@Column(nullable = false)
	public Boolean getClanarina() {
		return clanarina;
	}

	public void setClanarina(Boolean clanarina) {
		this.clanarina = clanarina;
	}

	@Type(type = "yes_no")
	@Column(nullable = false)
	public Boolean getZahtjev() {
		return zahtjev;
	}

	public void setZahtjev(Boolean zahtjev) {
		this.zahtjev = zahtjev;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public KorisnikVrsta getKorisnikVrsta() {
		return this.korisnikVrsta;
	}

	public void setKorisnikVrsta(KorisnikVrsta korisnikVrsta) {
		this.korisnikVrsta = korisnikVrsta;
	}

	@OneToMany(mappedBy = "korisnik", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Interes> getInteresi() {
		return interesi;
	}

	public void setInteresi(Set<Interes> interesi) {
		this.interesi = interesi;
	}

	@OneToMany(mappedBy = "korisnik", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Autor> getAutori() {
		return autori;
	}

	public void setAutori(Set<Autor> autori) {
		this.autori = autori;
	}

	@OneToMany(mappedBy = "korisnik", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Ocjena> getOcjene() {
		return ocjene;
	}

	public void setOcjene(Set<Ocjena> ocjene) {
		this.ocjene = ocjene;
	}

	@OneToMany(mappedBy = "korisnik", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<KorisnickaKategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(Set<KorisnickaKategorija> kategorije) {
		this.kategorije = kategorije;
	}

	@Column(nullable = true)
	public Double getKoef1() {
		return koef1;
	}

	public void setKoef1(Double koef1) {
		this.koef1 = koef1;
	}

	@Column(nullable = true)
	public Double getKoef2() {
		return koef2;
	}

	public void setKoef2(Double koef2) {
		this.koef2 = koef2;
	}

	@Column(nullable = true)
	public Double getKoef3() {
		return koef3;
	}

	public void setKoef3(Double koef3) {
		this.koef3 = koef3;
	}

	@Column(nullable = true)
	public Double getKoef4() {
		return koef4;
	}

	public void setKoef4(Double koef4) {
		this.koef4 = koef4;
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
		Korisnik other = (Korisnik) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

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

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "korisnicka_kategorija")
@NamedQueries({
	@NamedQuery(name = "KorKat.nazivKorisnik",
			query = "select k from KorisnickaKategorija as k where k.naziv=:n and k.korisnik=:kor")
})
public class KorisnickaKategorija {

	private Long id;
	private String naziv;

	private Korisnik korisnik;
	private Set<KorKategorijaRad> korKategorijaRad = new HashSet<>();

	public KorisnickaKategorija(String naziv) {
		super();
		this.naziv = naziv;
	}

	public KorisnickaKategorija() {
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
	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	@OneToMany(mappedBy = "korisnickaKategorija", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	public Set<KorKategorijaRad> getKorKategorijaRad() {
		return korKategorijaRad;
	}

	public void setKorKategorijaRad(Set<KorKategorijaRad> korKategorijaRad) {
		this.korKategorijaRad = korKategorijaRad;
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
		KorisnickaKategorija other = (KorisnickaKategorija) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

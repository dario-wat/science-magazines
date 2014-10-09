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

@Entity
@Table(name = "znanstveni_casopis")
@NamedQueries({
	@NamedQuery(name = "Casopis.id", query = "select c from ZnanstveniCasopis as c where c.id=:id")
})
public class ZnanstveniCasopis {

	private Long id;
	private String naziv;
	private String izdavac;
	private String skrNaziv;

	private Kategorija kategorija;
	private Set<Izdanje> izdanja = new HashSet<>();
	private Set<Interes> interesi = new HashSet<>();

	public ZnanstveniCasopis(String naziv, String skrNaziv) {
		super();
		this.naziv = naziv;
		this.skrNaziv = skrNaziv;
	}
	
	public ZnanstveniCasopis(){
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

	@Column(length = 10, nullable = false)
	public String getSkrNaziv() {
		return skrNaziv;
	}

	public void setSkrNaziv(String skrNaziv) {
		this.skrNaziv = skrNaziv;
	}

	@Column(length = 30, nullable = true)
	public String getIzdavac() {
		return izdavac;
	}

	public void setIzdavac(String izdavac) {
		this.izdavac = izdavac;
	}

	@ManyToOne
	@JoinColumn(nullable = true)
	public Kategorija getKategorija() {
		return kategorija;
	}

	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
	}

	@OneToMany(mappedBy = "znanstveniCasopis", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Izdanje> getIzdanja() {
		return izdanja;
	}

	public void setIzdanja(Set<Izdanje> izdanja) {
		this.izdanja = izdanja;
	}

	@OneToMany(mappedBy = "znanstveniCasopis", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Interes> getInteresi() {
		return interesi;
	}

	public void setInteresi(Set<Interes> interesi) {
		this.interesi = interesi;
	}
}

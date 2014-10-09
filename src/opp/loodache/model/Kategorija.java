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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "kategorija")
public class Kategorija {

	private Long id;
	private String nazivHrv;
	private String nazivEng;
	private String skraceniNaziv;

	private Podrucje podrucje;
	private Set<ZnanstveniCasopis> casopisi = new HashSet<>();

	public Kategorija(String nazivHrv, String nazivEng, String skraceniNaziv) {
		super();
		this.nazivHrv = nazivHrv;
		this.nazivEng = nazivEng;
		this.skraceniNaziv = skraceniNaziv;
	}

	public Kategorija() {
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

	@Column(length = 20, nullable = false)
	public String getSkraceniNaziv() {
		return skraceniNaziv;
	}

	public void setSkraceniNaziv(String skraceniNaziv) {
		this.skraceniNaziv = skraceniNaziv;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public Podrucje getPodrucje() {
		return podrucje;
	}

	public void setPodrucje(Podrucje podrucje) {
		this.podrucje = podrucje;
	}

	@OneToMany(mappedBy = "kategorija", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<ZnanstveniCasopis> getCasopisi() {
		return casopisi;
	}

	public void setCasopisi(Set<ZnanstveniCasopis> casopisi) {
		this.casopisi = casopisi;
	}
}

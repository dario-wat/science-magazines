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
@Table(name = "znanstveni_rad")
@NamedQueries({
	@NamedQuery(name = "Rad.id", query = "select r from ZnanstveniRad as r where r.id=:id")
})
public class ZnanstveniRad {

	private Long id;
	private String naslov;
	private String sazetak;
	private String kljucneRijeci;
	private String pdfUrl;

	private Izdanje izdanje;
	private Set<Autor> autori = new HashSet<>();
	private Set<Ocjena> ocjene = new HashSet<>();
	private Set<Referenca> reference = new HashSet<>();	//ovaj rad sadrzi sljedece reference
	private Set<Referenca> referenciram = new HashSet<>();	//radovi koje ovaj rad referencira
	private Set<KorKategorijaRad> korKategorijaRad = new HashSet<>();

	public ZnanstveniRad(String naslov, String sazetak, String kljucneRijeci, String pdfUrl){
		super();
		this.naslov = naslov;
		this.sazetak = sazetak;
		this.kljucneRijeci = kljucneRijeci;
		this.pdfUrl = pdfUrl;
	}
	
	public ZnanstveniRad() {
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 100, nullable = false)
	public String getNaslov() {
		return naslov;
	}

	public void setNaslov(String naslov) {
		this.naslov = naslov;
	}

	//@Type(type = "text")
	@Column(length = 4096, nullable = false)
	public String getSazetak() {
		return sazetak;
	}

	public void setSazetak(String sazetak) {
		this.sazetak = sazetak;
	}

	@Column(length = 200, nullable = false)
	public String getKljucneRijeci() {
		return kljucneRijeci;
	}

	public void setKljucneRijeci(String kljucneRijeci) {
		this.kljucneRijeci = kljucneRijeci;
	}

	@Column(length = 100, nullable = false)
	public String getPdfUrl() {
		return pdfUrl;
	}

	public void setPdfUrl(String pdfUrl) {
		this.pdfUrl = pdfUrl;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public Izdanje getIzdanje() {
		return izdanje;
	}

	public void setIzdanje(Izdanje izdanje) {
		this.izdanje = izdanje;
	}

	@OneToMany(mappedBy = "znanstveniRad", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Autor> getAutori() {
		return autori;
	}

	public void setAutori(Set<Autor> autori) {
		this.autori = autori;
	}

	@OneToMany(mappedBy = "znanstveniRad", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Ocjena> getOcjene() {
		return ocjene;
	}

	public void setOcjene(Set<Ocjena> ocjene) {
		this.ocjene = ocjene;
	}

	@OneToMany(mappedBy = "znanstveniRad", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Referenca> getReference() {
		return reference;
	}

	public void setReference(Set<Referenca> reference) {
		this.reference = reference;
	}

	@OneToMany(mappedBy = "referenca", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<Referenca> getReferenciram() {
		return referenciram;
	}

	public void setReferenciram(Set<Referenca> referenciram) {
		this.referenciram = referenciram;
	}

	@OneToMany(mappedBy = "znanstveniRad", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
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
		ZnanstveniRad other = (ZnanstveniRad) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}

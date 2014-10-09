package opp.loodache.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "izdanje")
@NamedQueries({ @NamedQuery(name = "Izdanje.id", query = "select i from Izdanje as i where i.id=:id") })
public class Izdanje {

	private Long id;
	private Long rbrIzdanje;
	private Long rbrSvezak;
	private String isbn;
	private String issn;
	private Date datumIzdanja;

	private ZnanstveniCasopis znanstveniCasopis;
	private Set<ZnanstveniRad> znanstveniRadovi = new HashSet<>();

	public Izdanje(Long rbrIzdanje, Long rbrSvezak, String isbn, String issn, Date datumIzdanja) {
		super();
		this.isbn = isbn;
		this.issn = issn;
		this.rbrIzdanje = rbrIzdanje;
		this.rbrSvezak = rbrSvezak;
		this.datumIzdanja = datumIzdanja;
	}

	/**
	 * Javni defaultni konstruktor.
	 */
	public Izdanje() {
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(nullable = false)
	public Long getRbrIzdanje() {
		return rbrIzdanje;
	}

	public void setRbrIzdanje(Long rbrIzdanje) {
		this.rbrIzdanje = rbrIzdanje;
	}

	@Column(nullable = false)
	public Long getRbrSvezak() {
		return rbrSvezak;
	}

	public void setRbrSvezak(Long rbrSvezak) {
		this.rbrSvezak = rbrSvezak;
	}

	@Column(length = 17, nullable = false)
	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	@Column(length = 9, nullable = false)
	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	public Date getDatumIzdanja() {
		return datumIzdanja;
	}

	public void setDatumIzdanja(Date datumIzdanja) {
		this.datumIzdanja = datumIzdanja;
	}

	@ManyToOne
	@JoinColumn(nullable = false)
	public ZnanstveniCasopis getZnanstveniCasopis() {
		return znanstveniCasopis;
	}

	public void setZnanstveniCasopis(ZnanstveniCasopis znanstveniCasopis) {
		this.znanstveniCasopis = znanstveniCasopis;
	}

	@OneToMany(mappedBy = "izdanje", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	public Set<ZnanstveniRad> getZnanstveniRadovi() {
		return znanstveniRadovi;
	}

	public void setZnanstveniRadovi(Set<ZnanstveniRad> znanstveniRadovi) {
		this.znanstveniRadovi = znanstveniRadovi;
	}
}

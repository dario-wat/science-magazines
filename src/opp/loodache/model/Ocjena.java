package opp.loodache.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ocjena")
@IdClass(OcjenaPK.class)
public class Ocjena {

	private Double ocjena;
	private String oznaka;

	private ZnanstveniRad znanstveniRad;
	private Korisnik korisnik;

	public Ocjena(Double ocjena, String oznaka) {
		super();
		this.ocjena = ocjena;
		this.oznaka = oznaka;
	}

	public Ocjena() {
	}

	public Ocjena(Double ocjena, String oznaka, ZnanstveniRad znanstveniRad, Korisnik korisnik) {
		super();
		this.ocjena = ocjena;
		this.oznaka = oznaka;
		this.znanstveniRad = znanstveniRad;
		this.korisnik = korisnik;
	}

	@Column(nullable = true)
	public Double getOcjena() {
		return ocjena;
	}

	public void setOcjena(Double ocjena) {
		this.ocjena = ocjena;
	}

	@Column(length = 50, nullable = true)
	public String getOznaka() {
		return oznaka;
	}

	public void setOznaka(String oznaka) {
		this.oznaka = oznaka;
	}

	@Id
	@ManyToOne
	@JoinColumn(nullable = false)
	public ZnanstveniRad getZnanstveniRad() {
		return znanstveniRad;
	}

	public void setZnanstveniRad(ZnanstveniRad znanstveniRad) {
		this.znanstveniRad = znanstveniRad;
	}

	@Id
	@ManyToOne
	@JoinColumn(nullable = false)
	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((znanstveniRad == null || korisnik == null) ? 0
						: (znanstveniRad.hashCode() + korisnik.hashCode()));
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
		Ocjena other = (Ocjena) obj;

		if (korisnik == null) {
			if (other.korisnik != null) {
				return false;
			}
		}
		if (znanstveniRad == null) {
			if (other.znanstveniRad != null) {
				return false;
			}
		}
		if (!znanstveniRad.equals(other.znanstveniRad) || !korisnik.equals(other.korisnik)) {
			return false;
		}
		return true;
	}

}

package opp.loodache.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "korkategorija_rad")
@IdClass(KorKategorijaRadPK.class)
public class KorKategorijaRad {

	private KorisnickaKategorija korisnickaKategorija;
	private ZnanstveniRad znanstveniRad;

	public KorKategorijaRad(KorisnickaKategorija korisnickaKategorija, ZnanstveniRad znanstveniRad) {
		super();
		this.korisnickaKategorija = korisnickaKategorija;
		this.znanstveniRad = znanstveniRad;
	}

	public KorKategorijaRad() {
	}

	@Id
	@ManyToOne
	@JoinColumn(nullable = false)
	public KorisnickaKategorija getKorisnickaKategorija() {
		return korisnickaKategorija;
	}

	public void setKorisnickaKategorija(KorisnickaKategorija korisnickaKategorija) {
		this.korisnickaKategorija = korisnickaKategorija;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KorKategorijaRad other = (KorKategorijaRad) obj;

		if (korisnickaKategorija == null) {
			if (other.korisnickaKategorija != null) {
				return false;
			}
		}
		if (znanstveniRad == null) {
			if (other.znanstveniRad != null) {
				return false;
			}
		}
		if (!znanstveniRad.equals(other.znanstveniRad)
				|| !korisnickaKategorija.equals(other.korisnickaKategorija)) {
			return false;
		}
		return true;
	}

}

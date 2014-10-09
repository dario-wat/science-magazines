package opp.loodache.model;

import java.io.Serializable;

/**
 * Ovaj razred se koristi kao primarni kljuc za razred KorKategorijaRad.
 */
public class KorKategorijaRadPK implements Serializable {

	private static final long serialVersionUID = 2813372214524671614L;

	private KorisnickaKategorija korisnickaKategorija;
	private ZnanstveniRad znanstveniRad;

	public KorKategorijaRadPK() {
	}

	public KorKategorijaRadPK(KorisnickaKategorija korisnickaKategorija, ZnanstveniRad znanstveniRad) {
		super();
		this.korisnickaKategorija = korisnickaKategorija;
		this.znanstveniRad = znanstveniRad;
	}

	public KorisnickaKategorija getKorisnickaKategorija() {
		return korisnickaKategorija;
	}

	public void setKorisnickaKategorija(KorisnickaKategorija korisnickaKategorija) {
		this.korisnickaKategorija = korisnickaKategorija;
	}

	public ZnanstveniRad getZnanstveniRad() {
		return znanstveniRad;
	}

	public void setZnanstveniRad(ZnanstveniRad znanstveniRad) {
		this.znanstveniRad = znanstveniRad;
	}

	@Override
	public int hashCode() {
		return znanstveniRad.hashCode() + korisnickaKategorija.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof KorKategorijaRadPK) {
			KorKategorijaRadPK korKategorijaRadPK = (KorKategorijaRadPK) obj;
			return this.znanstveniRad.equals(korKategorijaRadPK.getZnanstveniRad())
					&& this.korisnickaKategorija.equals(korKategorijaRadPK.getKorisnickaKategorija());
		}

		return false;
	}
}

package opp.loodache.model;

import java.io.Serializable;

/**
 * Razred koji sluzi kao primarni kljuc za razred Ocjena.
 */
public class OcjenaPK implements Serializable {

	private static final long serialVersionUID = 2520909142289245899L;

	private ZnanstveniRad znanstveniRad;
	private Korisnik korisnik;

	public OcjenaPK() {
	}

	public OcjenaPK(ZnanstveniRad znanstveniRad, Korisnik korisnik) {
		super();
		this.znanstveniRad = znanstveniRad;
		this.korisnik = korisnik;
	}

	public ZnanstveniRad getZnanstveniRad() {
		return znanstveniRad;
	}

	public void setZnanstveniRad(ZnanstveniRad znanstveniRad) {
		this.znanstveniRad = znanstveniRad;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	@Override
	public int hashCode() {
		return znanstveniRad.hashCode() + korisnik.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof OcjenaPK) {
			OcjenaPK ocjenaPK = (OcjenaPK) obj;
			return this.znanstveniRad.equals(ocjenaPK.getZnanstveniRad())
					&& this.korisnik.equals(ocjenaPK.getKorisnik());
		}

		return false;
	}
}

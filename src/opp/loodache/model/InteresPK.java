package opp.loodache.model;

import java.io.Serializable;

/**
 * Klasa se koristi kao primarni kljuc za klasu Interes.
 */
class InteresPK implements Serializable {

	private static final long serialVersionUID = -4474082014429924525L;

	private ZnanstveniCasopis znanstveniCasopis;
	private Korisnik korisnik;

	public InteresPK() {
	}

	public InteresPK(ZnanstveniCasopis znanstveniCasopis, Korisnik korisnik) {
		super();
		this.znanstveniCasopis = znanstveniCasopis;
		this.korisnik = korisnik;
	}

	public ZnanstveniCasopis getZnanstveniCasopis() {
		return znanstveniCasopis;
	}

	public void setZnanstveniCasopis(ZnanstveniCasopis znanstveniCasopis) {
		this.znanstveniCasopis = znanstveniCasopis;
	}

	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

	@Override
	public int hashCode() {
		return znanstveniCasopis.hashCode() + korisnik.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof InteresPK) {
			InteresPK interesPK = (InteresPK) obj;
			return this.znanstveniCasopis.equals(interesPK.getZnanstveniCasopis())
					&& this.korisnik.equals(interesPK.getKorisnik());
		}

		return false;
	}
}
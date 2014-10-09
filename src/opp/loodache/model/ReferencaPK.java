package opp.loodache.model;

import java.io.Serializable;

/**
 * Ovaj razred se koristi kao primarni kljuc za razred Referenca.
 */
public class ReferencaPK implements Serializable {

	private static final long serialVersionUID = -4728616831163663423L;

	private ZnanstveniRad znanstveniRad;
	private ZnanstveniRad referenca;

	public ReferencaPK() {
	}

	public ReferencaPK(ZnanstveniRad znanstveniRad, ZnanstveniRad referenca) {
		super();
		this.znanstveniRad = znanstveniRad;
		this.referenca = referenca;
	}

	public ZnanstveniRad getZnanstveniRad() {
		return znanstveniRad;
	}

	public void setZnanstveniRad(ZnanstveniRad znanstveniRad) {
		this.znanstveniRad = znanstveniRad;
	}

	public ZnanstveniRad getReferenca() {
		return referenca;
	}

	public void setReferenca(ZnanstveniRad referenca) {
		this.referenca = referenca;
	}

	@Override
	public int hashCode() {
		return znanstveniRad.hashCode() + referenca.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ReferencaPK) {
			ReferencaPK referencaPK = (ReferencaPK) obj;
			return this.znanstveniRad.equals(referencaPK.getZnanstveniRad())
					&& this.referenca.equals(referencaPK.getReferenca());
		}

		return false;
	}
}

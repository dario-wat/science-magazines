package opp.loodache.model;

import java.io.Serializable;

/**
 * Razred se koristi kao primarni kljuc za razred Autor.
 */
public class AutorPK implements Serializable {

	private static final long serialVersionUID = -852621953635368829L;

	private ZnanstveniRad znanstveniRad;
	private String autor;

	public AutorPK() {
	}

	public AutorPK(ZnanstveniRad znanstveniRad, String autor) {
		super();
		this.znanstveniRad = znanstveniRad;
		this.autor = autor;
	}

	public ZnanstveniRad getZnanstveniRad() {
		return znanstveniRad;
	}

	public void setZnanstveniRad(ZnanstveniRad znanstveniRad) {
		this.znanstveniRad = znanstveniRad;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	@Override
	public int hashCode() {
		return autor.hashCode() + znanstveniRad.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AutorPK) {
			AutorPK autorPK = (AutorPK) obj;
			return this.znanstveniRad.equals(autorPK.getZnanstveniRad())
					&& this.autor.equals(autorPK.getAutor());
		}

		return false;
	}
}

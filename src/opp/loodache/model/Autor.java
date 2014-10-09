package opp.loodache.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "autor")
@IdClass(AutorPK.class)
public class Autor {

	private ZnanstveniRad znanstveniRad;
	private Korisnik korisnik;
	private String autor;

	public Autor(ZnanstveniRad znanstveniRad, Korisnik korisnik, String autor) {
		super();
		this.znanstveniRad = znanstveniRad;
		this.korisnik = korisnik;
		this.autor = autor;
	}

	public Autor() {
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
	@Column(length = 50)
	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	@ManyToOne
	@JoinColumn(nullable = true)
	public Korisnik getKorisnik() {
		return korisnik;
	}

	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}

}

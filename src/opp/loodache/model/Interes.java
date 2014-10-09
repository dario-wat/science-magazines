package opp.loodache.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "interes")
@IdClass(InteresPK.class)
public class Interes {

	private ZnanstveniCasopis znanstveniCasopis;
	private Korisnik korisnik;

	public Interes(InteresPK interesPK) {
		this.korisnik = interesPK.getKorisnik();
		this.znanstveniCasopis = interesPK.getZnanstveniCasopis();
	}
	
	public Interes(ZnanstveniCasopis znanstveniCasopis, Korisnik korisnik) {
		super();
		this.znanstveniCasopis = znanstveniCasopis;
		this.korisnik = korisnik;
	}

	public Interes() {
	}

	@Id
	@ManyToOne
	@JoinColumn(nullable = false)
	public ZnanstveniCasopis getZnanstveniCasopis() {
		return znanstveniCasopis;
	}

	public void setZnanstveniCasopis(ZnanstveniCasopis znanstveniCasopis) {
		this.znanstveniCasopis = znanstveniCasopis;
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

}

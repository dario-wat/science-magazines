package opp.loodache.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "referenca")
@IdClass(ReferencaPK.class)
public class Referenca {

	private ZnanstveniRad znanstveniRad;
	private ZnanstveniRad referenca;

	public Referenca() {
	}

	public Referenca(ZnanstveniRad znanstveniRad, ZnanstveniRad referenca) {
		super();
		this.znanstveniRad = znanstveniRad;
		this.referenca = referenca;
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
	public ZnanstveniRad getReferenca() {
		return referenca;
	}

	public void setReferenca(ZnanstveniRad referenca) {
		this.referenca = referenca;
	}

}

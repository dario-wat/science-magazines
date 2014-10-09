package opp.loodache.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "dnevnik")
public class Dnevnik {

	private Long id;
	private String username;
	private String akcija;
	private Date vrijeme;

	public Dnevnik(String username, String akcija) {
		super();
		this.username = username;
		this.akcija = akcija;
		this.vrijeme = new Date();
	}

	public Dnevnik() {
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(length = 50, nullable = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(length = 100, nullable = false)
	public String getAkcija() {
		return akcija;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	public void setAkcija(String akcija) {
		this.akcija = akcija;
	}

	public Date getVrijeme() {
		return vrijeme;
	}

	public void setVrijeme(Date vrijeme) {
		this.vrijeme = vrijeme;
	}
}

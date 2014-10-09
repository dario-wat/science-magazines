package opp.loodache.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import opp.loodache.dao.DAOProvider;
import opp.loodache.model.Korisnik;
import opp.loodache.model.KorisnikVrsta;

/**
 * Form class for user registration.
 */
public class UserForm {

	private String ime;
	private String prezime;
	private String username;
	private String email;
	private String password;
	private String repassword;

	private Map<String, String> errors = new HashMap<>();

	/**
	 * Getter za pogresku.
	 * @param name ime pogreske
	 * @return pogreska
	 */
	public String getError(String name) {
		return errors.get(name);
	}

	/**
	 * Provjera ima li gresaka.
	 * @return true ako ima gresaka, false inace
	 */
	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	/**
	 * Provjera postoji li greska odredjenog tipa.
	 * @param name ime pogreske
	 * @return true ako je istina, false inace
	 */
	public boolean hasErrorType(String name) {
		return errors.containsKey(name);
	}

	/**
	 * Metoda popuni formu iz http requesta.
	 * @param req http request
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.ime = prepare(req.getParameter("ime"));
		this.prezime = prepare(req.getParameter("prezime"));
		this.username = prepare(req.getParameter("username"));
		this.email = prepare(req.getParameter("email"));
		this.password = Crypter.calcHash(req.getParameter("password"));
		this.repassword = Crypter.calcHash(req.getParameter("repassword"));
	}

	/**
	 * MMetoda stvori novog korisnika.
	 * @return korisnik
	 */
	public Korisnik createUser() {
		Korisnik tmp = new Korisnik();
		tmp.setIme(this.ime);
		tmp.setPrezime(this.prezime);
		tmp.setUsername(this.username);
		tmp.setLozinkaHash(this.password);
		tmp.setEmail(this.email);
		tmp.setClanarina(false);
		tmp.setKorisnikVrsta(KorisnikVrsta.registrirani);
		return tmp;
	}

	/**
	 * Metoda stvori praznu formu.
	 * @return vraca formu
	 */
	public static UserForm emptyForm() {
		UserForm tmp = new UserForm();
		tmp.setIme("");
		tmp.setPrezime("");
		tmp.setUsername("");
		tmp.setPassword("");
		tmp.setRepassword("");
		tmp.setEmail("");
		return tmp;
	}

	/**
	 * Metoda validira sve podatke unesene u formu.
	 */
	public void validate() {
		errors.clear();

		if (this.ime.isEmpty()) {
			errors.put("ime", "Ime nedostaje.");
		}

		if (this.prezime.isEmpty()) {
			errors.put("prezime", "Prezime nedostaje.");
		}

		if (this.email.isEmpty()) {
			errors.put("email", "E-mail nedostaje.");
		} else {
			int l = this.email.length();
			int p = this.email.indexOf('@');
			if (l < 3 || p == -1 || p == 0 || p == l - 1) {
				errors.put("email", "Pogresan format e-maila.");
			}
		}

		if (this.password.equals(Crypter.calcHash(""))) {
			errors.put("password", "Lozinka nedostaje.");
		} else if (!this.password.equals(this.repassword)) {
			errors.put("password", "Pogrijesan unos lozinke.");
		}

		if (this.username.isEmpty()) {
			errors.put("username", "Korisnicko ime nedostaje.");
		} else {
			checkNickname(this.username);
		}

		if (this.username.length() > 30) {
			errors.put("username", "Korisnicko ime ne smije biti dulje od 30 znakova.");
		}

		if (this.ime.length() > 50) {
			errors.put("ime", "Ime ne smije biti dulje od 50 znakova.");
		}

		if (this.prezime.length() > 50) {
			errors.put("prezime", "Prezime ne smije biti dulje od 50 znakova.");
		}

		if (this.email.length() > 50) {
			errors.put("email", "E-mail ne smije biti dulji od 50 znakova.");
		}
	}

	/**
	 * Metoda provjerava postoji li korisnicko ime vec u bazi.
	 * @param nick nickname
	 */
	private void checkNickname(String username) {
		Korisnik korisnik = DAOProvider.getKorisnikDAO().findByUsername(username);
		if (korisnik != null) {
			errors.put("username", "Korisnicko ime vec postoji.");
		}
	}

	/**
	 * Metoda pripremi string. Izbrise sve suvisne razmake.
	 * @param s string
	 * @return string
	 */
	private String prepare(String s) {
		if (s == null) {
			return "";
		}
		return s.trim();
	}

	/*
	 * Getteri i setteri.
	 */

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Dohvati mapu s greskama.
	 * @return greske
	 */
	public Map<String, String> getErrors() {
		return errors;
	}

	/**
	 * Postavi mapu gresaka.
	 * @param errors greske
	 */
	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

}

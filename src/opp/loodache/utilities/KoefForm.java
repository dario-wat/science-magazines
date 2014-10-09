package opp.loodache.utilities;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import opp.loodache.model.Korisnik;

/**
 * Forma za koeficijente.
 */
public class KoefForm {

	private Double koef1;
	private Double koef2;
	private Double koef3;
	private Double koef4;

	private String skoef1;
	private String skoef2;
	private String skoef3;
	private String skoef4;

	private Map<String, String> errors = new HashMap<>();

	public String getError(String name) {
		return errors.get(name);
	}

	public boolean hasErrors() {
		return !errors.isEmpty();
	}

	public boolean hasErrorType(String name) {
		return errors.containsKey(name);
	}

	/**
	 * Napravi praznu formu.
	 * @return prazna forma
	 */
	public static KoefForm emptyForm() {
		KoefForm forma = new KoefForm();
		forma.skoef1 = "";
		forma.skoef2 = "";
		forma.skoef3 = "";
		forma.skoef4 = "";
		return forma;
	}
	
	/**
	 * Popuni formu iz korisnikovih podataka.
	 * @param korisnik korisnik
	 */
	public void fillFromKorisnik(Korisnik korisnik) {
		this.skoef1 = prepare(korisnik.getKoef1());
		this.skoef2 = prepare(korisnik.getKoef2());
		this.skoef3 = prepare(korisnik.getKoef3());
		this.skoef4 = prepare(korisnik.getKoef4());
	}
	
	/**
	 * Pripremi koeficijent iz Double vrijednosti.
	 * @param koef koeficijent
	 * @return double kao string ako nije null, inace vrati prazan string
	 */
	private String prepare(Double koef) {
		if (koef == null) {
			return "";
		}
		return koef.toString();
	}

	/**
	 * Popuni formu iz khttp requesta.
	 * @param req http request
	 */
	public void fillFromHttpRequest(HttpServletRequest req) {
		this.skoef1 = req.getParameter("koef1");
		this.skoef2 = req.getParameter("koef2");
		this.skoef3 = req.getParameter("koef3");
		this.skoef4 = req.getParameter("koef4");
	}

	/**
	 * Provjeri sve i doda greske ako je potrebno. Updejta sve koeficijente.
	 */
	public void validate() {
		errors.clear();

		if (this.skoef1.isEmpty()) {
			this.koef1 = null;
		} else {
			try {
				this.koef1 = Double.parseDouble(this.skoef1);
			} catch (NumberFormatException e) {
				errors.put("koef1", "Koeficijent 1 ima neispravan format");
			}
		}

		if (this.skoef2.isEmpty()) {
			this.koef2 = null;
		} else {
			try {
				this.koef2 = Double.parseDouble(this.skoef2);
			} catch (NumberFormatException e) {
				errors.put("koef2", "Koeficijent 2 ima neispravan format");
			}
		}

		if (this.skoef3.isEmpty()) {
			this.koef3 = null;
		} else {
			try {
				this.koef3 = Double.parseDouble(this.skoef3);
			} catch (NumberFormatException e) {
				errors.put("koef3", "Koeficijent 3 ima neispravan format");
			}
		}

		if (this.skoef4.isEmpty()) {
			this.koef4 = null;
		} else {
			try {
				this.koef4 = Double.parseDouble(this.skoef4);
			} catch (NumberFormatException e) {
				errors.put("koef4", "Koeficijent 4 ima neispravan format");
			}
		}
	}

	public Double getKoef1() {
		return koef1;
	}

	public Double getKoef2() {
		return koef2;
	}

	public Double getKoef3() {
		return koef3;
	}

	public Double getKoef4() {
		return koef4;
	}

	public String getSkoef1() {
		return skoef1;
	}

	public String getSkoef2() {
		return skoef2;
	}

	public String getSkoef3() {
		return skoef3;
	}

	public String getSkoef4() {
		return skoef4;
	}

}

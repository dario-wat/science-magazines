package opp.loodache.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Razred sluzi za parsiranje radova dobivenih od ovlastenih korisnika. Nazalost
 * ovaj parser ne otkriva greske jer ipak zelim malo zivota sebi ostavit. Ovaj
 * parser radi tako da ako se dogodi ijedna greska sve pada u vodu.
 */
public class ParserRadova {

	private String rad;

	private List<String> autori = new ArrayList<>();
	private String naslov;
	private String nazivCasopisa;
	private String skrNazivCasopisa;
	private Long rbrSvezak;
	private Long rbrIzdanje;
	private String isbn;
	private String issn;
	private Date datumIzlaska;
	private String sazetak;
	private String kljucneRijeci;
	private String pdfUrl;
	private List<ReferencaRada> reference = new ArrayList<>();

	/**
	 * Konstuktor.
	 * @param rad tekst rada
	 */
	public ParserRadova(String rad) {
		super();
		this.rad = rad;
	}

	/**
	 * Ova funkcija odvaja radove iz teksta i salje ih parseru.
	 * @param tekst tekst radova
	 */
	public static List<ParserRadova> parseRadovi(String tekst) {
		List<ParserRadova> lista = new ArrayList<>();
		String[] radovi = tekst.split("Rad:\r\n");
		for (String r : radovi) {
			if (r.isEmpty()) {
				continue;
			}
			ParserRadova pr = new ParserRadova(r);
			pr.parseRad();
			lista.add(pr);
		}
		return lista;
	}

	public List<String> getAutori() {
		return autori;
	}

	public String getNaslov() {
		return naslov;
	}

	public String getNazivCasopisa() {
		return nazivCasopisa;
	}

	public String getSkrNazivCasopisa() {
		return skrNazivCasopisa;
	}

	public Long getRbrSvezak() {
		return rbrSvezak;
	}

	public Long getRbrIzdanje() {
		return rbrIzdanje;
	}

	public String getIsbn() {
		return isbn;
	}

	public String getIssn() {
		return issn;
	}

	public Date getDatumIzlaska() {
		return datumIzlaska;
	}

	public String getSazetak() {
		return sazetak;
	}

	public String getKljucneRijeci() {
		return kljucneRijeci;
	}

	public String getPdfUrl() {
		return pdfUrl;
	}

	public List<ReferencaRada> getReference() {
		return reference;
	}

	/**
	 * Funkcija sluzi za parsiranje jednog rada.
	 */
	private void parseRad() {
		String[] retci = this.rad.split("\r\n");
		if (retci.length < 15) {
			throw new ParseException("Pogreska u tekstu.");
		}

		// autori
		String[] aut = retci[0].split(",");
		for (String a : aut) {
			this.autori.add(a.trim());
		}

		this.naslov = retci[1];
		this.nazivCasopisa = retci[2];
		this.skrNazivCasopisa = retci[3];

		try {
			this.rbrSvezak = Long.parseLong(retci[4]);
			this.rbrIzdanje = Long.parseLong(retci[5]);
		} catch (NumberFormatException e) {
			throw new ParseException("Pogreska pri parsiranju broja sveska i izdanja.");
		}

		this.isbn = retci[6];
		this.issn = retci[7];
		this.datumIzlaska = parseDate(retci[8]);

		this.sazetak = retci[10];
		this.kljucneRijeci = keywords(retci[12]);
		this.pdfUrl = pdfurl(retci[13]);

		// reference
		for (int i = 15; i < retci.length; i++) {
			if (retci[i].isEmpty()) {
				continue;
			}
			this.reference.add(new ReferencaRada(retci[i].trim()));
		}
	}

	/**
	 * Parsiram datum.
	 * @param string string datum
	 * @return datum
	 */
	private Date parseDate(String string) {
		DateFormat df = new SimpleDateFormat("dd.mm.yyyy.");
		try {
			return df.parse(string);
		} catch (java.text.ParseException e) {
			throw new ParseException("Datum neispravan.");
		}
	}

	/**
	 * Izvuce url iz retka.
	 * @param string redak
	 * @return url
	 */
	private String pdfurl(String string) {
		if (!string.startsWith("Tekst: ")) {
			throw new ParseException("Pdf url nije ispravan.");
		}
		return string.substring("Tekst: ".length());
	}

	/**
	 * Kljucne rijeci pretvori u jedan string odvojen sa ;
	 * @param string redak za parsiranje
	 * @return string
	 */
	private String keywords(String string) {
		String[] words = string.split(",");
		StringBuilder sb = new StringBuilder();
		for (String s : words) {
			sb.append(s.trim()).append(";");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	/**
	 * Ova klasa sluzi za parsiranje referenci.
	 */
	public class ReferencaRada {

		private String ref;

		private String nazivRada;

		/*
		 * private List<String> autori; private String nazivCasopisa; private
		 * Long rbrSvezak; private Long rbrIzdanje; private Long strOd; private
		 * Long strDo; private Long mjesec; private Long godina;
		 */

		public ReferencaRada(String ref) {
			super();
			this.ref = ref;
		}

		/**
		 * Parsira tako da trazi samo naziv rada. Ostalo ignorira.
		 */
		public void parse() {
			int s = this.ref.indexOf('„');
			int e = this.ref.indexOf('“');
			if (s == -1 || e == -1 || s >= e) {
				throw new ParseException("Pogreška u referencama.");
			}

			this.nazivRada = this.ref.substring(s+1, e);
		}

		public String getNazivRada() {
			return nazivRada;
		}
	}

	/*
	 * ovo radi samo na serveru, ne ovdje public static void main(String[] args)
	 * throws IOException { String filename = "radovi.txt"; File file = new
	 * File(filename); BufferedReader reader = new BufferedReader(new
	 * InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
	 * 
	 * StringBuilder sb = new StringBuilder(); while (true) { String line =
	 * reader.readLine(); if (line == null) { break; }
	 * sb.append(line).append("\n"); }
	 * 
	 * String tekst = sb.toString(); parseRadovi(tekst); reader.close(); }
	 */
}

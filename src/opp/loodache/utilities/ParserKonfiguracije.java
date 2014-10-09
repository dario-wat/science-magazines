package opp.loodache.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Parsira konfiguracijsku datoteku.
 */
public class ParserKonfiguracije {

	/** Clanarina u kunama. */
	private Double naknada;

	/** Rok za placanje. */
	private String rok;
	private String origRok;

	// koeficijenti za casopise
	private Double ckoef1;
	private Double ckoef2;

	// koeficijenti za radove
	private Double koef1;
	private Double koef2;
	private Double koef3;
	private Double koef4;

	private static List<String> dani = Arrays.asList("pon", "uto", "sri", "cet", "pet", "sub",
			"ned");

	public Double getNaknada() {
		return naknada;
	}

	public String getRok() {
		return rok;
	}

	public Double getCkoef1() {
		return ckoef1;
	}

	public Double getCkoef2() {
		return ckoef2;
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

	public String getOrigRok() {
		return origRok;
	}

	public void setNaknada(Double naknada) {
		this.naknada = naknada;
	}

	public void setRok(String rok) {
		this.rok = rok;
	}

	public void setCkoef1(Double ckoef1) {
		this.ckoef1 = ckoef1;
	}

	public void setCkoef2(Double ckoef2) {
		this.ckoef2 = ckoef2;
	}

	public void setKoef1(Double koef1) {
		this.koef1 = koef1;
	}

	public void setKoef2(Double koef2) {
		this.koef2 = koef2;
	}

	public void setKoef3(Double koef3) {
		this.koef3 = koef3;
	}

	public void setKoef4(Double koef4) {
		this.koef4 = koef4;
	}

	public void setOrigRok(String origRok) {
		this.origRok = origRok;
	}

	/**
	 * Parsira konfiguracijsku datoteku.
	 * @param file datoteka
	 * @throws IOException iznimka pri citanju datoteke.
	 */
	public void parseFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
				StandardCharsets.UTF_8));

		String line = reader.readLine();
		parseFirstLine(line);

		line = reader.readLine();
		parseSecondLine(line);

		line = reader.readLine();	// prazan redak
		parseThirdLine(line);

		line = reader.readLine();
		parseFourthLine(line);

		line = reader.readLine();
		parseFifthLine(line);

		line = reader.readLine();
		parseSixthLine(line);
		reader.close();
	}

	/**
	 * Parsira prvi redak datoteke konfiguracija.txt. Vadi cijenu clanarine.
	 * @param line redak
	 * @throws ParseException iznimka pri parsiranju
	 */
	private void parseFirstLine(String line) throws ParseException {
		if (line == null || line.isEmpty()) {
			throw new ParseException("Pogresna prva linija konfiguracije.");
		}

		String[] splitted = line.split("\t");
		if (splitted.length != 2 || !splitted[0].equals("Mjesečna naknada")) {
			throw new ParseException("Pogresna prva linija konfiguracije.");
		}

		String[] cijena = splitted[1].split(" ");
		if (cijena.length != 2 || !cijena[1].equals("kn")) {
			throw new ParseException("Pogresan format cijene.");
		}

		try {
			naknada = Double.parseDouble(cijena[0]);
		} catch (NumberFormatException e) {
			throw new ParseException("Pogresan iznos cijene.");
		}
	}

	/**
	 * Parsira drugi redak konfiguracije.
	 * @param line redak
	 * @throws ParseException iznimka pri parsiranju
	 */
	private void parseSecondLine(String line) throws ParseException {
		if (line == null || !line.startsWith("Rok plaćanja u mjesecu ")) {
			throw new ParseException("Pogresna druga linija konfiguracije.");
		}

		final int len = "Rok plaćanja u mjesecu ".length();
		String datum = line.substring(len);

		if (!datum.startsWith("„") || !datum.endsWith("“")) {
			throw new ParseException("Datumu fale navodnici.");
		}

		datum = datum.substring(1, datum.length() - 1);

		parseDate(datum);
	}

	/**
	 * Parsira datum.
	 * @param date datum u formatu stringa.
	 * @throws ParseException iznimka pri parsiranju datuma
	 */
	public void parseDate(String date) throws ParseException {
		if (date.length() < 3) {
			throw new ParseException("Pogresan format datuma.");
		}
		this.origRok = date;

		//datum verzija
		if (date.startsWith("datum")) {
			String dan = date.substring("datum".length());
			int broj;
			try {
				broj = Integer.parseInt(dan);
			} catch (NumberFormatException e) {
				throw new ParseException("Pogresan format datuma.");
			}

			if (broj < 1 || broj > 28) {
				throw new ParseException("Dozvoljeni su datumi izmedju 1. i 28.");
			}

			rok = broj + ". dan u mjesecu";
			return;
		}

		//verzija sa danim u tjednu
		if (dani.contains(date.substring(0, 3))) {
			String dan = date.substring(3);
			int broj;
			try {
				broj = Integer.parseInt(dan);
			} catch (NumberFormatException e) {
				throw new ParseException("Pogresan format datuma.");
			}

			if (broj < 1 || broj > 4) {
				throw new ParseException("Dozvoljeni su dani izmedju 1. i 4.");
			}

			rok = broj + ". " + date.substring(0, 3) + " u mjesecu.";
			return;
		}

		//ako nije nista onda ne valja nista
		throw new ParseException("Pogresan format datuma.");
	}

	/**
	 * Parsira treci redak koji bi trebao biti prazan.
	 * @param line redak
	 */
	private void parseThirdLine(String line) {
		if (line == null || !line.isEmpty()) {
			throw new ParseException("Treci redak treba biti prazan.");
		}
	}

	/**
	 * Parsira cetvrti redak konfiguracije.
	 * @param line redak
	 */
	private void parseFourthLine(String line) {
		if (line == null || !line.equals("Koeficijenti rangiranja:")) {
			throw new ParseException("Cetvrti redak nije ispravan.");
		}
	}

	/**
	 * Parsiram peti redak.
	 * @param line redak
	 */
	private void parseFifthLine(String line) {
		if (line == null) {
			throw new ParseException("Petak redak nije ispravan.");
		}

		String[] splitted = line.split("[ ]+");
		if (splitted.length != 9) {
			throw new ParseException("Peti redak nije ispravan.");
		}

		if (!splitted[0].equals("bodovi_casopisa") || !splitted[1].equals("=")
				|| !splitted[3].equals("*") || !splitted[4].equals("broj_citata")
				|| !splitted[5].equals("+") || !splitted[7].equals("*")
				|| !splitted[8].equals("prosjecna_ocjena_korisnika")) {

			throw new ParseException("Peti redak nije ispravan.");
		}

		String sckoef1 = splitted[2];
		this.ckoef1 = parseKoef(sckoef1);
		String sckoef2 = splitted[6];
		this.ckoef2 = parseKoef(sckoef2);
	}

	/**
	 * Parsiram sesti redak.
	 * @param line redak
	 */
	private void parseSixthLine(String line) {
		if (line == null) {
			throw new ParseException("Sesti redak nije ispravan.");
		}

		String[] splitted = line.split("[ ]+");
		if (splitted.length != 17) {
			throw new ParseException("Sesti redak nije ispravan.");
		}

		if (!splitted[0].equals("bodovi_rada") || !splitted[1].equals("=")
				|| !splitted[3].equals("*") || !splitted[4].equals("broj_citata")
				|| !splitted[5].equals("+") || !splitted[7].equals("*")
				|| !splitted[8].equals("prosjecna_ocjena_korisnika") || !splitted[9].equals("+")
				|| !splitted[11].equals("*") || !splitted[12].equals("prosjecna_ocjena_skupa")
				|| !splitted[13].equals("+") || !splitted[15].equals("*")
				|| !splitted[16].equals("bodovi_casopisa")) {

			throw new ParseException("Sesti redak nije ispravan");
		}

		String skoef1 = splitted[2];
		String skoef2 = splitted[6];
		String skoef3 = splitted[10];
		String skoef4 = splitted[14];
		this.koef1 = parseKoef(skoef1);
		this.koef2 = parseKoef(skoef2);
		this.koef3 = parseKoef(skoef3);
		this.koef4 = parseKoef(skoef4);
	}

	/**
	 * Parsira koeficijent iz stringa u double. Ako nesto ne valja baci parse
	 * exception.
	 * @param koef koeficijent
	 * @return double
	 */
	private Double parseKoef(String skoef) {
		try {
			return Double.parseDouble(skoef);
		} catch (NumberFormatException e) {
			throw new ParseException("Pogreska pri parsiranju koeficijenta.");
		}
	}

	/**
	 * Zapise konfiguraciju nazad u datoteku.
	 * @param file datoteka
	 * @throws IOException exc
	 */
	public void saveConfiguration(File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(file), StandardCharsets.UTF_8));

		writer.write("Mjesečna naknada\t" + this.naknada + " kn\n");
		writer.write("Rok plaćanja u mjesecu „" + origRok + "“\n");
		writer.write("\n");
		writer.write("Koeficijenti rangiranja:\n");
		writer.write("bodovi_casopisa = " + ckoef1 + " * broj_citata + " + ckoef2
				+ " * prosjecna_ocjena_korisnika\n");
		writer.write("bodovi_rada = " + koef1 + " * broj_citata + " + koef2
				+ " * prosjecna_ocjena_korisnika + " + koef3 + " * prosjecna_ocjena_skupa + "
				+ koef4 + " * bodovi_casopisa\n");

		writer.close();
	}
}

package opp.loodache.utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Parsira fajl kategorije.txt.
 */
public class ParserKategorije {

	/**
	 * Parsira kategorije.
	 * @return listu kategorija
	 * @throws IOException exc
	 */
	public static List<Redak> parseKategorija() throws IOException {
		String filename = "kategorije.txt";
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(
				filename), StandardCharsets.UTF_8));

		reader.readLine();
		reader.readLine(); 		// citanje 2 nevazne linije

		List<Redak> retci = new ArrayList<>();
		while (true) {
			String line = reader.readLine();
			if (line == null) {
				break;
			}

			List<String> categories = splitCategories(line);	//ovo ovdje su odvojene kategorije
			retci.add(createRedak(categories));
			//printStrings(categories);
		}

		reader.close();
		return retci;
	}

	/**
	 * Stvori instancu razreda redak.
	 * @param categories kategorija
	 * @return redak
	 */
	private static Redak createRedak(List<String> categories) {
		return new Redak(categories.get(0), categories.get(1), categories.get(2),
				categories.get(3), categories.get(4));
	}

	/**
	 * Ispis liste stringova.
	 * @param strings lista
	 */
	@SuppressWarnings("unused")
	private static void printStrings(List<String> strings) {
		for (String s : strings) {
			System.out.print(s + "\t");
		}
		System.out.println();
	}

	/**
	 * Funkcija parsira liniju fajla
	 * @param line redak
	 * @return lista stringova
	 */
	private static List<String> splitCategories(String line) {
		List<String> strings = new ArrayList<>();

		final int len = line.length();
		boolean inside = false;
		StringBuilder cat = new StringBuilder();

		// magicno parsiranje
		for (int i = 0; i < len; i++) {
			char c = line.charAt(i);
			if (c == '"') {
				if (!inside) {
					inside = true;
				} else {
					inside = false;
					strings.add(cat.toString());
					cat = new StringBuilder();
				}
			} else {
				if (inside) {
					cat.append(c);
				} else if (c != ' ' && c != '\t') {
					inside = true;
					cat.append(c);
				}
			}
		}

		if (inside) {
			strings.add(cat.toString());
		}

		return strings;
	}

	/**
	 * Razred za jedan redak datoteke s kategorijama.
	 */
	public static class Redak {

		private String podrucjeHrv;
		private String podrucjeEng;
		private String kategorijaHrv;
		private String kategorijaEng;
		private String kategorijaSkr;

		public Redak(String podrucjeHrv, String podrucjeEng, String kategorijaHrv,
				String kategorijaEng, String kategorijaSkr) {
			super();
			this.podrucjeHrv = podrucjeHrv;
			this.podrucjeEng = podrucjeEng;
			this.kategorijaHrv = kategorijaHrv;
			this.kategorijaEng = kategorijaEng;
			this.kategorijaSkr = kategorijaSkr;
		}

		public String getPodrucjeHrv() {
			return podrucjeHrv;
		}

		public String getPodrucjeEng() {
			return podrucjeEng;
		}

		public String getKategorijaHrv() {
			return kategorijaHrv;
		}

		public String getKategorijaEng() {
			return kategorijaEng;
		}

		public String getKategorijaSkr() {
			return kategorijaSkr;
		}

	}

}

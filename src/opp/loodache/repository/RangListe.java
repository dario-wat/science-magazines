package opp.loodache.repository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import opp.loodache.model.Korisnik;
import opp.loodache.model.ZnanstveniCasopis;
import opp.loodache.model.ZnanstveniRad;

/**
 * Rang liste.
 */
public class RangListe {

	public static List<CasopisOcjena> casopisi = new ArrayList<>();
	public static List<RadOcjena> radovi = new ArrayList<>();

	/**
	 * Razred sluzi za dodjeljivanje bodova radovima.
	 */
	public static class RadOcjena {

		private ZnanstveniRad rad;
		private Integer brojCitata;
		private Double avgKorisnika;
		private Double avgCasopisa;
		private Double bodoviCasopisa;

		private Double bodovi;
		private String fBod;

		public RadOcjena() {
		}

		/**
		 * Izracunam bodove rada.
		 * @return double bodovi
		 */
		public void setBodoviRada(boolean sluzbeni, Korisnik korisnik) {
			Double k1 = null, k2 = null, k3 = null, k4 = null;
			if (sluzbeni) {
				k1 = SluzbeniKoeficijenti.koef1;
				k2 = SluzbeniKoeficijenti.koef2;
				k3 = SluzbeniKoeficijenti.koef3;
				k4 = SluzbeniKoeficijenti.koef4;
			} else {
				k1 = korisnik.getKoef1() == null ? SluzbeniKoeficijenti.koef1 : korisnik.getKoef1();
				k2 = korisnik.getKoef2() == null ? SluzbeniKoeficijenti.koef4 : korisnik.getKoef2();
				k3 = korisnik.getKoef3() == null ? SluzbeniKoeficijenti.koef3 : korisnik.getKoef3();
				k4 = korisnik.getKoef4() == null ? SluzbeniKoeficijenti.koef2 : korisnik.getKoef4();
			}
			this.bodovi = k1 * brojCitata + k2 * avgKorisnika + k3 * avgCasopisa + k4
					* bodoviCasopisa;
			this.fBod = new DecimalFormat("0.00").format(this.bodovi);
		}

		public Double getBodovi() {
			return bodovi;
		}

		public String getfBod() {
			return fBod;
		}

		public ZnanstveniRad getRad() {
			return rad;
		}

		public void setRad(ZnanstveniRad rad) {
			this.rad = rad;
		}

		public void setBrojCitata(Integer brojCitata) {
			this.brojCitata = brojCitata;
		}

		public void setAvgKorisnika(Double avgKorisnika) {
			this.avgKorisnika = avgKorisnika;
		}

		public void setAvgCasopisa(Double avgCasopisa) {
			this.avgCasopisa = avgCasopisa;
		}

		public void setBodoviCasopisa(Double bodoviCasopisa) {
			this.bodoviCasopisa = bodoviCasopisa;
		}
	}

	/**
	 * Razred sluzi za bodovanje casopisa.
	 */
	public static class CasopisOcjena {

		private ZnanstveniCasopis casopis;
		private Integer brojCitata;
		private Double avgKorisnika;

		private Double bodovi;
		private String fBod;

		public CasopisOcjena() {
		}

		/**
		 * Izracuna broj bodova casopisa.
		 */
		public void setBodovi(boolean sluzbeni, Korisnik korisnik) {
			Double ck1 = null, ck2 = null;
			if (sluzbeni) {
				ck1 = SluzbeniKoeficijenti.ckoef1;
				ck2 = SluzbeniKoeficijenti.ckoef2;
			} else {
				ck1 = korisnik.getKoef1() == null ? SluzbeniKoeficijenti.ckoef1 : korisnik
						.getKoef1();
				ck2 = korisnik.getKoef2() == null ? SluzbeniKoeficijenti.ckoef2 : korisnik
						.getKoef2();
			}
			this.bodovi = ck1 * brojCitata + ck2 * avgKorisnika;
			this.fBod = new DecimalFormat("0.00").format(this.bodovi);
		}

		public ZnanstveniCasopis getCasopis() {
			return casopis;
		}

		public String getfBod() {
			return fBod;
		}

		public Double getBodovi() {
			return bodovi;
		}

		public void setCasopis(ZnanstveniCasopis casopis) {
			this.casopis = casopis;
		}

		public void setBrojCitata(Integer brojCitata) {
			this.brojCitata = brojCitata;
		}

		public void setAvgKorisnika(Double avgKorisnika) {
			this.avgKorisnika = avgKorisnika;
		}
	}
}

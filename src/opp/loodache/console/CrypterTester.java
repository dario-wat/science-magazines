package opp.loodache.console;

import opp.loodache.utilities.Crypter;


public class CrypterTester {
	
	public static void main(String[] args) {
		String s = "pero";
		String hash = Crypter.calcHash(s);
		System.out.println(hash);
	}

}

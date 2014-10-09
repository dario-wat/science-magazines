package opp.loodache.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Razred za kriptiranje lozinke.
 */
public class Crypter {

	/**
	 * Metoda racuna hash danog stringa.
	 * @param s string za hashiranje
	 * @return kriptirani string
	 */
	public static String calcHash(String s) {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ignorable) {
		}

		byte[] sBytes = s.getBytes();
		sha.update(sBytes, 0, sBytes.length);
		byte[] hash = sha.digest();

		return hexEncode(hash);
	}

	/**
	 * Pomocna funkcija za kodiranje bajtova u heksadecimalne znakove.
	 * @param bytes bajtovi za kodiranje
	 * @return string heksadecimalna reprezentacija
	 */
	private static String hexEncode(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}

		return sb.toString();
	}
}

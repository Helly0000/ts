package ts;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tools {

	// Ctrl+Shift+f ×Ô¶¯Ëõ½ø
	/**
	 * Encodes a string 2 MD5
	 * 
	 * @param str String to encode
	 * @return Encoded String
	 * @throws NoSuchAlgorithmException
	 */
	public static String crypt(String str) {
		if (str == null || str.length() == 0) {
			throw new IllegalArgumentException("String to encript cannot be null or zero length");
		}
		StringBuffer hexString = new StringBuffer();
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] hash = md.digest();
			for (int i = 0; i < hash.length; i++) {
				if ((0xff & hash[i]) < 0x10) {
					hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
				} else {
					hexString.append(Integer.toHexString(0xFF & hash[i]));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hexString.toString();
	}

	public static Boolean wordfilter(String str) {
		int ascii = 0;
		int non_ascii = 0;
		// if (s==r'.' or s==r'|' or s==r',' or s==r'. ' or s==r'| ' or s==r', ' or
		// s==r'\n' or s==r': ' or s==r' '):
		// return False
		if (str.length()==1) {
			return false;
		}
		char[] cc=str.toCharArray();
		for (char c : cc) {
			if (Character.isLowerCase(c) || Character.isUpperCase(c)) {
				ascii = ascii + 1;
			} else {
				non_ascii = non_ascii + 1;
			}
		}
		if (ascii > non_ascii) {// && non_ascii < 5
			return true;
		} else {
			return false;
		}
	}
}

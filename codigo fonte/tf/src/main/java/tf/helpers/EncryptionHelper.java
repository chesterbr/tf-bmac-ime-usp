package tf.helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class EncryptionHelper {

	private static EncryptionHelper instance;

	private EncryptionHelper() {
	}

	public synchronized String gera_hash_senha(String plainText) {
		MessageDigest md = null;
		String saltedText = plainText + "@lg1sAL][";
		try {
			md = MessageDigest.getInstance("SHA"); // step 2
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Erro ao gerar hash: " + e.getMessage());
		}
		try {
			md.update(saltedText.getBytes("UTF-8")); // step 3
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Erro ao gerar hash: " + e.getMessage());
		}
		byte raw[] = md.digest(); // step 4
		String hash = (new BASE64Encoder()).encode(raw); // step 5
		return hash; // step 6
	}

	public static synchronized EncryptionHelper getInstance() // step 1
	{
		if (instance == null) {
			return new EncryptionHelper();
		} else {
			return instance;
		}
	}
}
package com.madhu.utility;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CommonUtils {

	private static final String ALGORITHM = "AES";

	private static final String defaultKey = "1234567812345678";

	public static String encrypt(String plainText, String key) throws Exception {

		if (key == null || key.isEmpty() || key.isBlank())
			key = defaultKey;

		Cipher cipher = Cipher.getInstance(ALGORITHM);

		SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);

		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] encryptedText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedText);
	}

	public static String decrypt(String encryptedText, String key) throws Exception {

		if (key == null || key.isEmpty() || key.isBlank())
			key = defaultKey;

		encryptedText = encryptedText.trim();

		encryptedText = encryptedText.replace(' ', '+');

		System.out.println(encryptedText + " and size is " + encryptedText.length());

		Cipher cipher = Cipher.getInstance(ALGORITHM);

		SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText.getBytes());

		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		return new String(decryptedBytes, StandardCharsets.UTF_8);

	}

}

package com.limitlessmobility.iVendGateway.controller.amazonpay;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

public class AmazonPayEnvelopeEncryptionController {

	
	private static final SecureRandom SECURE_RANDOM = new SecureRandom();
	
	/*
	 * This method is used for encrypt data in Base64. Mostly used in Amazonpay API.
	 */
	public static Map<String, String> encrypt(String stringToBeEncrypted) {
		System.out.println("stringToBeEncrypted in controller "+stringToBeEncrypted);
		byte[] rawKey = new byte[16];
		SECURE_RANDOM.nextBytes(rawKey);
		System.out.println("secureRandom.nextBytes(rawKey) ");
		Map<String, String> result = new HashMap<>(3);
		System.out.println("secureRandom.nextBytes(rawKey)1 ");
		try {
			System.out.println("try... ");
			// Bouncycastle is preferred provider, Sun JCE can also be used
			Security.addProvider(new BouncyCastleProvider());
			System.out.println("secureRandom.nextBytes(rawKey)2 ");
			Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", BouncyCastleProvider.PROVIDER_NAME);
			// 1. Generate the session Key which will be used for encrypting the
			// payload
			System.out.println("secureRandom.nextBytes(rawKey)3 ");
			SecretKey key = new SecretKeySpec(rawKey, "AES");
			System.out.println("AES.......... ");
			// 2. Generate the initialization vector
			byte[] iv = new byte[cipher.getBlockSize()];
			// 3. Generate the AES-GCM encrypted payload
			byte[] encryptedData = null;
			SECURE_RANDOM.nextBytes(iv);
			AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
			cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
			System.out.println("ENCRYPT_MODE... ");
			encryptedData = cipher.doFinal(stringToBeEncrypted.getBytes());
			// 4. RSA encrypt the session key
			byte[] cipherText = null;
			PublicKey publicKey;
			// In a production implementation, load this key from
			// https://amazonpay.amazon.in/getDynamicConfig?key=serverSideSDKJava
			String encodedKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq92yAzXaCQbGIid0mMBfulkGK8HqvAardDowtgbfGUZ+hIx6lhYKFMrluTr7bIlQ4qgJY85c9adkZSxHtr/DhTV/ch5CCHDET3YC/DaFTKDp5t2uHKQAIb2Rl/73HQOd/pgImTiaLHPBr/gyz4iztYmlJQIm0vVuPktIANDGpK8qhizdztA3as1bLtILQZ5VtOjNn/xl1HQ+JDtBhUVr13BuJPosecQz6ouhEtR+5i/grg6sUzayqPD1dY6AGRLR9ao/6DCeHT5arSYjlkx6BECuKoiARo7ItDfLameXJ1gLd8lkMzArIG275jbxAiPd4OchHEfcqBADYB51FYDTwQIDAQAB";
			System.out.println("encodedKey... "+encodedKey);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.decode(encodedKey));
			publicKey = keyFactory.generatePublic(publicKeySpec);
			Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding","BC");
			rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherText = rsaCipher.doFinal(rawKey);
			result.put("IV", new String(Base64.encode(iv)));
			System.out.println("IV C "+result.put("IV", new String(Base64.encode(iv))));
			result.put("KEY", new String(Base64.encode(cipherText)));
			System.out.println("KEY C "+result.put("KEY", new String(Base64.encode(cipherText))));
			result.put("PAYLOAD", new String(Base64.encode(encryptedData)));
			System.out.println("PAYLOAD C "+result.put("PAYLOAD", new String(Base64.encode(encryptedData))));
			System.out.println("result= "+result);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}

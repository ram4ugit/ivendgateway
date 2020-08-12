package com.limitlessmobility.iVendGateway.services.amazonpay;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NonNull;

public class CryptoForMerchant {
	
	private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
	private static final String AES = "AES";
	private static final String RSA = "RSA";
	private static final String RSA_WITH_NO_PADDING = "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
	private static final SecureRandom RANDOM; 
	private static final Encoder urlEncoder;
	private static final ObjectMapper objectMapper;
	/** * Reuse cryptographic ciphers for performance, one per thread. * Note: Ciphers by themselves are not thread safe. */ 
	private static final ThreadLocal<Cipher> AEC_GCM_THREAD_CIPHER;
	private static final ThreadLocal<Cipher> RSA_THREAD_CIPHER; 
	static {
		/*22 January 2019 Amazon Pay Integration Guide 20*/
		try {
			objectMapper = new ObjectMapper();
			urlEncoder = Base64.getUrlEncoder();
			RANDOM = new SecureRandom();
			Security.addProvider(new BouncyCastleProvider());
			AEC_GCM_THREAD_CIPHER = ThreadLocal.withInitial(() -> { 
				try {
					return Cipher.getInstance(AES_GCM_NO_PADDING, BouncyCastleProvider.PROVIDER_NAME);
					} 
				catch (Exception e) { 
					throw new ExceptionInInitializerError(e); 
					} 
				});
			RSA_THREAD_CIPHER = ThreadLocal.withInitial(() -> {
				try { 
					return Cipher.getInstance(RSA_WITH_NO_PADDING, BouncyCastleProvider.PROVIDER_NAME); 
					} catch (Exception e) { 
						throw new ExceptionInInitializerError(e); 
						} 
				}); 
			}
		catch (Exception e){
			throw new ExceptionInInitializerError(e); 
			} 
		}
	  public static Map<String, String> encrypt(Map<String, Object> map) throws JsonProcessingException, GeneralSecurityException { 
		  Map<String, String> pdata=new HashMap<>();
		  byte[] payload = objectMapper.writeValueAsBytes(map);
		  SecureRandom secureRandom = new SecureRandom();
		  byte[] randomKey = new byte[16]; 
		  secureRandom.nextBytes(randomKey);
		  return encrypt(payload, randomKey);
		  } 
	   /* Encrypt the byte array using the secret key and algorithm: {@value #AES_GCM_NO_PADDING},
	    allowing for prefix data to be written directly to output later. 
	    Output will contain a new randomly-generated initialization vector and then the cipher/encrypted bytes.
	    @param payload plain bytes to encrypt. * @param key the secret key byte array of length 16, 24 or 32.
	    @return encrypted byte array that includes a random initialization vector up front.*/
	  
	  public static Map<String, String> encrypt(@NonNull final byte[] payload, @NonNull final byte[] key) throws GeneralSecurityException { 
		  Map<String , String> result=new HashMap<>();
		  SecretKeySpec codingKey = new SecretKeySpec(key, AES);
		  Cipher cipher = AEC_GCM_THREAD_CIPHER.get();
		  byte[] iv = new byte[cipher.getBlockSize()];
		  RANDOM.nextBytes(iv);
		/*22 January 2019 Amazon Pay Integration Guide 21*/
	    cipher.init(Cipher.ENCRYPT_MODE, codingKey, new IvParameterSpec(iv));
		final byte[] encryptedPayload = cipher.doFinal(payload);
		byte[] encryptMerchantKey = encryptMerchantKey(key);
		result.put("payload",encodeToUrlString(encryptedPayload)); // final payload
		result.put("iv",encodeToUrlString(iv));// final iv
		result.put("key", encodeToUrlString(encryptMerchantKey));// final key
		return result;
		} 
	  
	  
        private static byte[] encryptMerchantKey(final byte[] key) throws InvalidKeyException, NoSuchAlgorithmException,
        InvalidKeySpecException,BadPaddingException, IllegalBlockSizeException {
        KeyFactory keyFact = KeyFactory.getInstance(RSA);
        String encodeKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq92yAzXaCQbGIid0mMBfulkGK8HqvAardDowtgbfGUZ+hIx6lhYKFMrluTr7bIlQ4qgJY85c9adkZSxHtr/DhTV/ch5CCHDET3YC/DaFTKDp5t2uHKQAIb2Rl/73HQOd/pgImTiaLHPBr/gyz4iztYmlJQIm0vVuPktIANDGpK8qhizdztA3as1bLtILQZ5VtOjNn/xl1HQ+JDtBhUVr13BuJPosecQz6ouhEtR+5i/grg6sUzayqPD1dY6AGRLR9ao/6DCeHT5arSYjlkx6BECuKoiARo7ItDfLameXJ1gLd8lkMzArIG275jbxAiPd4OchHEfcqBADYB51FYDTwQIDAQAB";
        KeySpec spec = new X509EncodedKeySpec(org.bouncycastle.util.encoders.Base64.decode(encodeKey)); 
        PublicKey publicKey = keyFact.generatePublic(spec);
        Cipher cipher = RSA_THREAD_CIPHER.get();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(key);
        }
        
        
        /** * Encodes to base-64 URL UTF-8 Sting. */
        private static String encodeToUrlString(byte[] array) {
        	return new String(urlEncoder.encode(array), UTF_8);
        	}

}

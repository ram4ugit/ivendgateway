package com.limitlessmobility.iVendGateway.controller.amazonpay;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.limitlessmobility.iVendGateway.model.amazonpay.RequestToSign;





 
public class SignatureUtil {
	 private static final String ALGORITHM = "AWS4-HMAC-SHA384";
	    private static final String SHA_384 = "SHA-384";
	    private static final String DATE_TIME_FORMAT = "YYYYMMdd'T'HHmmss'Z'";
	    private static final String NEW_LINE_CHARACTER = "\n";
	    private static final String TERMINATION_STRING = "aws4_request";
	    private static final String UTC_TIME_ZONE = "UTC";
	    private static final String SERVICE_NAME = "AmazonPay";
	    private static final String HMAC_ALGORITHM = "HmacSHA384";
	    //base64url encoder
	    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
	    private static Mac mac;

	    static {
	        try {
	            mac = Mac.getInstance(HMAC_ALGORITHM);
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	            throw new RuntimeException(e);
	        }
	    }

	    private static String getDateTimeStringFromTimeStamp(Long timeStamp) {
	        Date date = new Date(timeStamp);
	        SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
	        sdf.setTimeZone(TimeZone.getTimeZone(UTC_TIME_ZONE));
	        return sdf.format(date);
	    }

	    /**
	     * kSecret = your secret access key
	     * kDate = HMAC("AWS4" + kSecret, Date)
	     * kRegion = HMAC(kDate, Region)
	     * kService = HMAC(kRegion, Service)
	     * kSigning = HMAC(kService, "aws4_request")
	     **/
	    static byte[] getSigningKey(String key, String dateStamp, String
	            regionName, String serviceName) throws Exception {
	        byte[] kSecret = ("AWS4" + key).getBytes(StandardCharsets.UTF_8);
	        byte[] kDate = HmacSHA384(dateStamp, kSecret);
	        byte[] kRegion = HmacSHA384(regionName, kDate);
	        byte[] kService = HmacSHA384(serviceName, kRegion);
	        byte[] kSigning = HmacSHA384("aws4_request", kService);
	        return kSigning;
	    }

	    static byte[] HmacSHA384(String data, byte[] key) throws Exception {
	        try {
	            mac.init(new SecretKeySpec(key, HMAC_ALGORITHM));
	            
	        } catch (Exception e) {
	            throw new Exception("Invalid key exception while mac init", e);
	        }
	        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
	    }

	    /**
	     * 20181130/eu-west-1/AmazonPay/aws4_request
	     **/
	    private static String computeCredentialScope(String dateStamp) {
	        return new StringBuilder().append(dateStamp).append("/")
	                .append("eu-west-1").append("/")
	                .append(SERVICE_NAME).append("/")
	                .append(TERMINATION_STRING).toString();
	    }

	    private static String percentEncodeRfc3986(final String s) {
	        String out;
	        try {
	            out = URLEncoder.encode(s, StandardCharsets.UTF_8.name())
	                    .replace("+", "%20")
	                    .replace("*", "%2A")
	                    .replace("%7E", "~");
	        } catch (UnsupportedEncodingException e) {
	            throw new RuntimeException(e);
	        }
	        return out;
	    }

	    /**
	     * StringToSign =
	     * Algorithm + \n +
	     * RequestDateTime + \n +
	     * CredentialScope + \n +
	     * HashedCanonicalRequest
	     **/
	    public static String createStringToSign(RequestToSign request, String
	            dateTimeStamp, String dateStamp) throws UnsupportedEncodingException,
	            NoSuchAlgorithmException {
	        StringBuilder stringToSting = new StringBuilder();
	        stringToSting.append(ALGORITHM).append(NEW_LINE_CHARACTER);
	        stringToSting.append(dateTimeStamp).append(NEW_LINE_CHARACTER);

	        stringToSting.append(computeCredentialScope(dateStamp)).append(NEW_LINE_CHARACTER);

	        stringToSting.append(getHashedCanonicalRequest(createCanonicalRequest(request)
	        ));
	        System.out.println("==================StringToSign=========");
	        System.out.println(stringToSting.toString());
	        return stringToSting.toString();
	    }

	    public static String generateSignatures(RequestToSign request, String secret) throws Exception {
	    	    
	    	    String secretKey = secret;
	    	    /*String secretKey = "wr6wFy4qAcDarK9xkUoCtyUYaVnt4NHM_tgcE574yZq7nTYobMdig8I0VaAhDLCg";*/
		        System.out.println(Long.valueOf(request.getParameters().get("timeStamp").toString()));
		        String dateTimeStamp =
		                getDateTimeStringFromTimeStamp(Long.valueOf(request.getParameters().get("timeStamp").toString()));
		        System.out.println("timestamp::" +dateTimeStamp);
		        String dateStamp = dateTimeStamp.split("T")[0];

		        String stringToSign = null;
		        try {
		            stringToSign = createStringToSign(request, dateTimeStamp,
		                    dateStamp);
		        } catch (UnsupportedEncodingException e) {
		            //todo handle the exeption
		        } catch (NoSuchAlgorithmException e) {
		            //todo handle the exeption
		        }

		        byte[] signingKey = getSigningKey(secretKey, dateStamp,
		                "eu-west-1", SERVICE_NAME);
		        return new String(base64Encoder.encode(HmacSHA384(stringToSign,
		                signingKey)), StandardCharsets.UTF_8);
	    }

	    /**
	     * CanonicalRequest =
	     * HTTPRequestMethod + '\n' +
	     * hostname/uri + '\n' +
	     * formattedRequestParameters
	     **/
	    private static String createCanonicalRequest(RequestToSign request) {
	        StringBuilder data = new StringBuilder();
	        data.append(request.getMethod()).append(NEW_LINE_CHARACTER);

	        data.append(request.getHost()).append(request.getuRI()).append(NEW_LINE_CHARACTER);
	        data.append(formatParameters(request.getParameters()));
	        System.out.println(data.toString());
	        return data.toString();
	    }

	    private static String formatParameters(final Map<String, Object> parameters) {
	        Map<String, Object> sorted = new TreeMap<>();
	        sorted.putAll(parameters);
	        Iterator<Map.Entry<String, Object>> pairs =
	                sorted.entrySet().iterator();
	        StringBuilder queryStringBuilder = new StringBuilder();
	        while (pairs.hasNext()) {
	            Map.Entry<String, Object> pair = pairs.next();
	            String key = pair.getKey();
	            queryStringBuilder.append(percentEncodeRfc3986(key));
	            queryStringBuilder.append("=");
	            String value = pair.getValue().toString();
	            queryStringBuilder.append(percentEncodeRfc3986(value));
	            if (pairs.hasNext()) {
	                queryStringBuilder.append("&");
	            }
	        }
	        return queryStringBuilder.toString();
	    }

	    private static String getHashedCanonicalRequest(String canonicalRequest) throws
	            NoSuchAlgorithmException, UnsupportedEncodingException {
	        MessageDigest md = MessageDigest.getInstance(SHA_384);
	        md.reset();
	        md.update(canonicalRequest.getBytes(StandardCharsets.UTF_8));
	        return Hex.encodeHexString(md.digest());
	    }
}

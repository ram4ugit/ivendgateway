package com.limitlessmobil.ivendgateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.io.UnsupportedEncodingException;

public class JwtTokenDecode {

	public static TokenAuthModel validateJwt(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		TokenAuthModel tokenAuthModel = new TokenAuthModel();
		try {
		  Jws<Claims> jwsClaims = Jwts.parser()
			.setSigningKey("1234567890123456".getBytes("UTF-8"))
			.parseClaimsJws(token);

		  Claims claims = jwsClaims.getBody();
		  System.out.println(claims);
		  String operatorId = (String) claims.get("OperatorId");
		  tokenAuthModel.setOperatorId(operatorId);
		  String userId = (String) claims.get("UserId");
		  tokenAuthModel.setUserId(userId);
		  System.out.println(operatorId);
		    //OK, we can trust this JWT

		} catch (SignatureException e) {

			System.out.println("Tonen error - "+e);
		    //don't trust the JWT!
			return tokenAuthModel;
		}
		return tokenAuthModel;
	}
	
	public static void main(String[] args) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException, UnsupportedEncodingException {
		/*TokenAuthModel t = new TokenAuthModel();
		try{
			String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiYW1hbkBnbWFpbC5jb20iLCJPcGVyYXRvcklkIjoiMSIsIlVzZXJJZCI6IjEiLCJleHAiOjE1MzY2NDcyMTgsImlzcyI6IkxMTSBWTVMgSWRlbnRpdHkgQVBJIiwiYXVkIjoiTExNIFZNUyAgVUkifQ.N5iSsWJRQ9YPLrjRCLlfLu9TwulspZJBb1_YStvhtlE";
				t = new JwtTokenDecode().validateJwt(token);
				System.out.println(t.getOperatorId());
			} catch(Exception e){
				System.out.println(e);
			}
		if(CommonValidationUtility.isEmpty(t.getOperatorId())){
			System.out.println("token authentication failed!!");
		} else {
			System.out.println("token authentication success!!");
			System.out.println(t.getOperatorId());
		}*/
	}
}

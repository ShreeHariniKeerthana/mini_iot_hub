package com.qubercomm.jwt.resource;

import java.io.Serializable;

public class JwtTokenResponse implements Serializable {

	private static final long serialVersionUID = 8317676219297719109L;

	private String result = "";

//	public JwtTokenResponse(String token) {
//		this.token = token;
//	}
//
//	public String getToken() {
//		return this.token;
//	}
	
	public JwtTokenResponse(String result) {
		this.result = result;
	}
	
	public String getResult() {
		return this.result;
	}
}
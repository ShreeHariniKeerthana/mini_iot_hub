package com.qubercomm.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

	private static final String PATTERN = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";

	public boolean validatePattern(String uid) {
		Pattern pattern = Pattern.compile(PATTERN);
		Matcher matcher = pattern.matcher(uid);
		return matcher.matches();
	}
}

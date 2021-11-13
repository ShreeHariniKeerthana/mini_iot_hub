package com.qubercomm.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.qubercomm.jwt.JwtTokenUtil;
import com.qubercomm.multipledb.model.h2.UserSession;
import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.multipledb.repository.h2.UserSessionRepository;
import com.qubercomm.multipledb.repository.postgres.UserAccountRepository;

@Service
public class UserSessionService {
	
	@Value("${jwt.http.request.header}")
	private String tokenHeader;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	UserAccountService userAccountService;
	
	@Autowired
	UserAccountRepository userAccountRepository;
	
	@Autowired
	UserSessionRepository userSessionRepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	public String saveUserSession(String userName, UserDetails userDetails) {
		UserSession userSession = userSessionRepository.findByUserName(userName);
		String token = userSession.getAuthToken();
		
		if (StringUtils.isNotEmpty(token)) {
			try {
				jwtTokenUtil.canTokenBeRefreshed(token);
			} catch (Exception e){
				token = jwtTokenUtil.generateToken(userDetails);
				userAccountService.updateAuthToken(userName, token);
				updateToken(userName, token);
				return token;
			}
		} 
		return token;
	}
	
	public UserSession save(UserAccount user) {
		UserSession userSession = new UserSession();
		userSession.setUserName(user.getUsername());
		userSession.setPassword(bcryptEncoder.encode(user.getPassword()));
		userSession.setRole(user.getRole());
		return userSessionRepository.save(userSession);
	}
	
	public UserSession updateToken(String userName, String token) {
		UserSession userSession = userSessionRepository.findByUserName(userName);
		userSession.setAuthToken(token);
		return userSessionRepository.save(userSession);
	}
	
	public boolean isUserRegistered(String userName) {
		UserAccount userAccount = userAccountRepository.findByUsername(userName);
		if(userAccount == null) {
			return false;
		}
		return true;
	}
	
	public boolean isUserAsSession(String userName) {
		UserSession userSession = userSessionRepository.findByUserName(userName);
		if(userSession != null && StringUtils.isEmpty(userSession.getAuthToken())) {
			return true;
		}
		return false;
	}
	

}

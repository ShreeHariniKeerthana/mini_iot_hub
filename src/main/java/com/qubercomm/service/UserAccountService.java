package com.qubercomm.service;

import java.util.Date;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.multipledb.repository.h2.UserSessionRepository;
import com.qubercomm.multipledb.repository.postgres.UserAccountRepository;

@Service
public class UserAccountService {
	
	@Value("${jwt.http.request.header}")
	private String tokenHeader;
	
	@Autowired
	UserAccountRepository userAccountRepository;
	
	@Autowired
	UserSessionRepository userSessionrepository;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	@Autowired
    private RuntimeService runtimeService;
	
	public UserAccount updatePassword(String username, UserAccount userDetails) {
		
		UserAccount userAccount = userAccountRepository.findByUsername(username);
		userAccount.setUpdatedOn(new Date());
		userAccount.setPassword(bcryptEncoder.encode(userDetails.getPassword()));
		return userAccountRepository.save(userAccount);
	}
	
	public UserAccount updateAuthToken(String username, String token) {
		
		UserAccount userAccount = userAccountRepository.findByUsername(username);
		userAccount.setAuthToken(token);
		return userAccountRepository.save(userAccount);
	}
	
	public UserAccount save(UserAccount user) {
		UserAccount userAccount = new UserAccount();
		userAccount.setUsername(user.getUsername());
		userAccount.setPassword(bcryptEncoder.encode(user.getPassword()));
		userAccount.setRole(user.getRole());
		userAccount.setCreatedOn(new Date());
		userAccount.setUpdatedOn(new Date());
		return userAccountRepository.save(userAccount);
	}
	
	public void initiateMessage(String processInstanceId, String message) {
			runtimeService
	        .createMessageCorrelation(message)
	        .processInstanceId(processInstanceId)
	        .correlate();
	}
	
	public boolean isUserAsAccount(String userName) {
		UserAccount userAccount = userAccountRepository.findByUsername(userName);
		if(userAccount != null) {
			return true;
		}
		return false;
	}
	

}

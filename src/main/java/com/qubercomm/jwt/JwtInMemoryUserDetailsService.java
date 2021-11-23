package com.qubercomm.jwt;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.qubercomm.multipledb.model.h2.UserSession;
import com.qubercomm.multipledb.repository.h2.UserSessionRepository;

@Service
public class JwtInMemoryUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserSessionRepository userSessionRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserSession> UserSessionList = new ArrayList<>();
		UserSession userSession = new UserSession();
		UserSessionList = userSessionRepository.findAll();
		List<GrantedAuthority> authorities = null;
		boolean isPresent = false;
		
		for(UserSession user: UserSessionList) {
			if(user.getUserName().equals(username)) {
				userSession = user;
				isPresent = true;
				authorities = buildUserAuthority(userSession.getRole());
			}
		}
		if (!isPresent) {
			throw new UsernameNotFoundException(String.format("USER_NOT_FOUND '%s'.", username));
		}
		return new org.springframework.security.core.userdetails.User(userSession.getUserName(), userSession.getPassword(),
				authorities);
	}
	
	public List<GrantedAuthority> buildUserAuthority(String userRole) {

	    Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
	    
	    //Hardcoded the list of roles
	    ArrayList<String> userRoles = new ArrayList<String>();
	    userRoles.add("appadmin");
	    userRoles.add("appuser");
	    
	    // Build user's authorities
	    if(userRole == null) {
	    	for (String user : userRoles) {
		    	 setAuths.add(new SimpleGrantedAuthority(user));
		       
		    }
	    } else {
		    setAuths.add(new SimpleGrantedAuthority(userRole));
	    }
	    
	    List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);

	    return Result;
	}

}

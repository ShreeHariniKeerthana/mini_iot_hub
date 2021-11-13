package com.qubercomm.jwt.resource;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubercomm.jwt.JwtInMemoryUserDetailsService;
import com.qubercomm.jwt.JwtTokenUtil;
import com.qubercomm.jwt.JwtUserDetails;
import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.multipledb.repository.h2.UserSessionRepository;
import com.qubercomm.service.UserAccountService;
import com.qubercomm.service.UserSessionService;


@RestController
public class JwtAuthenticationRestController {

	@Value("${jwt.http.request.header}")
	private String tokenHeader;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;
	
	@Autowired
	private JwtInMemoryUserDetailsService _jwtInMemoryUserDetailsService;
	
	@Autowired 
	private UserAccountService userAccountService;
	
	@Autowired 
	private UserSessionService userSessionService;
	
	@RequestMapping(value = "/authenticate/{process_id}", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(HttpServletRequest request, @RequestBody JwtTokenRequest authenticationRequest, @PathVariable(value = "process_id") String process_id)
			throws AuthenticationException {

		String token = null;
		
		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword(), authenticationRequest.getRole());

			final UserDetails userDetails = jwtInMemoryUserDetailsService
					.loadUserByUsername(authenticationRequest.getUsername());
			String userName = userDetails.getUsername();
							
				if(userAccountService.isUserAsAccount(userName) && userSessionService.isUserAsSession(userName)) {
					token = jwtTokenUtil.generateToken(userDetails);
					userSessionService.updateToken(userName, token);
					userAccountService.updateAuthToken(userName, token);
				} else {
					token = userSessionService.saveUserSession(userName, userDetails);
				}
				
			userAccountService.initiateMessage(process_id, "User Authenticated");
		} catch(Exception e) {
			return ResponseEntity.badRequest().body("An error occurred while authenticating the user. " + e.getMessage());
		}
		
		return ResponseEntity.ok(new JwtTokenResponse(token));
	}
	
	@RequestMapping(value = "/register/{process_id}", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserAccount user,  @PathVariable(value = "process_id") String process_id) throws Exception {
		
		// Check User is registered or not
		
		try {
			if(!userSessionService.isUserRegistered(user.getUsername())) {
				userSessionService.save(user);
				user = userAccountService.save(user);
				userAccountService.initiateMessage(process_id, "User Registered");
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.ok("User Already Registered");
			}
			
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("An error occurred while registering the user. " + e.getMessage());
		}
	
	}

	@RequestMapping(value = "${jwt.refresh.token.uri}", method = RequestMethod.GET)
	public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
		String authToken = request.getHeader(tokenHeader);
		final String token = authToken.substring(7);
		String username = jwtTokenUtil.getUsernameFromToken(token);
		jwtInMemoryUserDetailsService.loadUserByUsername(username);

		if (jwtTokenUtil.canTokenBeRefreshed(token)) {
			String refreshedToken = jwtTokenUtil.refreshToken(token);
			return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
		} else {
			return ResponseEntity.badRequest().body(null);
		}
	}

	@ExceptionHandler({ AuthenticationException.class })
	public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	private void authenticate(String username, String password, String role) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);
		List<GrantedAuthority> authorities = _jwtInMemoryUserDetailsService.buildUserAuthority(role);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, authorities));
		} catch (DisabledException e) {
			throw new AuthenticationException("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new AuthenticationException("INVALID_CREDENTIALS", e);
		}
	}
}

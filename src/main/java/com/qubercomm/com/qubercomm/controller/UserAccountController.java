package com.qubercomm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.service.UserAccountService;

@RestController
@RequestMapping("/rest/api/useraccount/")
public class UserAccountController {
	
	@Autowired
	private UserAccountService userAccountService;
	
	@RequestMapping(value = "/update/{username}", method = RequestMethod.PATCH)
	public UserAccount updatePassword(@PathVariable(value = "username") String username, @RequestBody UserAccount userDetails) {
		return userAccountService.updatePassword(username, userDetails);
		
	}

}

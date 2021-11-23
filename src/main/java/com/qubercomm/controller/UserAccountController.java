package com.qubercomm.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
	public Map<String, Object> updatePassword(@PathVariable(value = "username") String username, @RequestBody UserAccount userDetails) {
		UserAccount updatedAccount = userAccountService.updatePassword(username, userDetails);
		Map<String, Object> result = new HashMap<>();
		if(Objects.nonNull(updatedAccount)) {
			result.put("result", "Password update successful");
		} else {
			result.put("result", "Password update failed");
		}
		return result;
	}

}

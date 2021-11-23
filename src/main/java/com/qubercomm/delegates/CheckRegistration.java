package com.qubercomm.delegates;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.multipledb.repository.postgres.UserAccountRepository;

@Service("CheckRegistration")
public class CheckRegistration implements JavaDelegate {
	
	@Autowired
	UserAccountRepository userAccountRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String username = (String) execution.getVariable("username");
		UserAccount user = userAccountRepository.findByUsername(username);
		
		if(user != null) {
			execution.setVariable("UserRegistration", true);
		} else {
			execution.setVariable("UserRegistration", false);
		}
	}

}

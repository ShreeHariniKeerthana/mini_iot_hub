package com.qubercomm.multipledb.repository.h2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qubercomm.multipledb.model.h2.UserSession;
import com.qubercomm.multipledb.model.postgres.UserAccount;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession, Long> {
	
	public UserSession findByUserName(String username);
	
}

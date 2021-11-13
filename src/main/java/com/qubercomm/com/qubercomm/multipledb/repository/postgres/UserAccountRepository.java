package com.qubercomm.multipledb.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qubercomm.multipledb.model.postgres.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	
	public UserAccount findByUsername(String username);
	
	public UserAccount findByUserId(Long userId);
}

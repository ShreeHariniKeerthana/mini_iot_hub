package com.qubercomm.multipledb.repository.postgres;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qubercomm.multipledb.model.postgres.Gateway;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, String>{
	
	public Gateway findByGatewayEuid(String gateway_uid);
	
	public List<Gateway> findByUserId(Long user_id);

}

package com.qubercomm.service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qubercomm.multipledb.model.postgres.Gateway;
import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.multipledb.repository.postgres.GatewayRepository;
import com.qubercomm.multipledb.repository.postgres.UserAccountRepository;

@Service
public class GatewayService {

	@Autowired
	GatewayRepository gatewayRepository;

	@Autowired
	UserAccountRepository userAccountRepository;

	public String onboardGateway(Gateway gateway) {

		String result = "";
		List<Gateway> gatewayList = getAllGateways();
		if(gatewayList.size() > 0) {
			for(Gateway loopGateway : gatewayList) {
				if(loopGateway.getGatewayEuid().equalsIgnoreCase(gateway.getGatewayEuid())) {
					return "Gateway euid provided has been already configured";
				}
			}
		} else {
			UserAccount optUserAccount = userAccountRepository.findByUserId(gateway.getUserId());
			if(Objects.isNull(optUserAccount)) {
				return "User id associated with this gateway not found";
			}
		}
			gateway.setCreatedOn(new Date());
			gateway.setUpdatedOn(new Date());
			gateway = gatewayRepository.save(gateway);

			if(Objects.nonNull(gateway)) {
				result = "Gateway onboarded successfully";
			} else {
				result = "Gateway onboarding failed";
			}
			return result;
		}

		public Optional<Gateway> getGateway(String gateway_euid) {
			return gatewayRepository.findById(gateway_euid);
		}

		public List<Gateway> getAllGateways() {
			return gatewayRepository.findAll();
		}

		public Gateway updateGateway(String gateway_euid, Gateway gatewayDetails) {
			Optional<Gateway> optGateway = gatewayRepository.findById(gateway_euid);
			Gateway gateway = null;
			if(optGateway.isPresent()) {
				gateway = optGateway.get();
			}
			gateway.setApproved(gatewayDetails.isApproved());
			gateway.setGatewayEuid(gatewayDetails.getGatewayEuid());
			gateway.setGatewayName(gatewayDetails.getGatewayName());
			gateway.setGatewayTags(gatewayDetails.getGatewayTags());
			gateway.setUpdatedOn(new Date());
			return gatewayRepository.save(gateway);
		}

		public boolean deleteGateway(String gateway_euid) {
			List<Gateway> gatewayList = getAllGateways();
			for(Gateway loopGateway : gatewayList) {
				if(loopGateway.getGatewayEuid().equalsIgnoreCase(gateway_euid)) {
					gatewayRepository.deleteById(gateway_euid);
					return true;
				}
			}
			return false;
		}

		public boolean approveGateway(String gateway_euid, Gateway gatewayDetails) {
			boolean result;
			Optional<Gateway> optGateway = gatewayRepository.findById(gateway_euid);
			Gateway existingGateway = null;
			Gateway newGateway = null;
			if(optGateway.isPresent()) {
				existingGateway = optGateway.get();
			} 
			if(!existingGateway.isApproved() && gatewayDetails.isApproved()) {
				existingGateway.setApproved(gatewayDetails.isApproved());
				newGateway = gatewayRepository.save(existingGateway);
			}

			if(Objects.nonNull(newGateway)) {
				result = true;
			} else {
				result = false;
			}
			return result;

		}


	}

package com.qubercomm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qubercomm.multipledb.model.postgres.Device;
import com.qubercomm.multipledb.model.postgres.Gateway;
import com.qubercomm.multipledb.model.postgres.UserAccount;
import com.qubercomm.multipledb.repository.postgres.DeviceRepository;
import com.qubercomm.multipledb.repository.postgres.GatewayRepository;
import com.qubercomm.multipledb.repository.postgres.UserAccountRepository;

@Service
public class GatewayService {

	@Autowired
	GatewayRepository gatewayRepository;

	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	UserAccountRepository userAccountRepository;

	public Map<String, Object> onboardGateway(Gateway gateway) {
		Map<String, Object> result = new HashMap<>();
		List<Gateway> gatewayList = getAllGateways();
		if(gatewayList.size() > 0) {
			for(Gateway loopGateway : gatewayList) {
				if(loopGateway.getGatewayEuid().equalsIgnoreCase(gateway.getGatewayEuid())) {
					result.put("result", "Gateway uid provided has been already configured");
					return result;
				} else {
					UserAccount optUserAccount = userAccountRepository.findByUserId(gateway.getUserId());
					if(Objects.isNull(optUserAccount)) {
						result.put("result", "User id associated with this gateway not found");
						return result;
					}
				}
			} 
		}
		gateway.setCreatedOn(new Date());
		gateway.setUpdatedOn(new Date());
		gateway = gatewayRepository.save(gateway);

		if(Objects.nonNull(gateway)) {
			result.put("result", "Gateway onboard successful");
		} else {
			result.put("result", "Gateway onboard failed");
		}
		return result;
	}

	public Optional<Gateway> getGateway(String gateway_euid) {
		return gatewayRepository.findById(gateway_euid);
	}

	public List<Gateway> getAllGateways() {
		return gatewayRepository.findAll();
	}

	public Map<String, Object> updateGateway(String gateway_euid, Gateway gatewayDetails) {
		Optional<Gateway> optGateway = gatewayRepository.findById(gateway_euid);
		Gateway gateway = null;
		Map<String, Object> result = new HashMap<>();
		if(optGateway.isPresent()) {
			gateway = optGateway.get();
			gateway.setApproved(gatewayDetails.isApproved());
			gateway.setGatewayEuid(gatewayDetails.getGatewayEuid());
			gateway.setGatewayName(gatewayDetails.getGatewayName());
			gateway.setGatewayTags(gatewayDetails.getGatewayTags());
			gateway.setUpdatedOn(new Date());
			Gateway updatedGateway = gatewayRepository.save(gateway);
			if(Objects.nonNull(updatedGateway)) {
				result.put("result", updatedGateway);
			} else {
				result.put("result", "Gateway update failed");
			}
		} else {
			result.put("result", "No device found with the uid specified");
		}
		return result;
	}

	public Map<String, Object> deleteGateway(String gateway_euid) {
		List<Gateway> gatewayList = getAllGateways();
		Map<String, Object> result = new HashMap<>();
		for(Gateway loopGateway : gatewayList) {
			if(loopGateway.getGatewayEuid().equalsIgnoreCase(gateway_euid)) {
				List<Device> deviceList = deviceRepository.findByGatewayEuid(gateway_euid);
				if(CollectionUtils.isEmpty(deviceList)) {
					gatewayRepository.deleteById(gateway_euid);
					result.put("result", "Gateway delete successful");
				} else {
					result.put("result", "There are devices configured under this gateway.Delete the devices before deleting the gateway");
				}
				return result;
			}
		}
		result.put("result", "No gateway found with the uid specified");
		return result;
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

	public Map<String, Object> getGatewaysByUserId(Long user_id){
		List<Gateway> gatewayList = gatewayRepository.findByUserId(user_id);
		Map<String, Object> result = new HashMap<>();
		if(CollectionUtils.isEmpty(gatewayList)) {
			result.put("result", "No gateways found for user id specified");
		} else {
			result.put("result", gatewayList);
		}
		return result;
	}

	public Map<String, Object> deleteAllGateways() {
		List<Gateway> gatewayList = getAllGateways();
		Map<String, Object> result = new HashMap<>();
		if(CollectionUtils.isEmpty(gatewayList)) {
			gatewayRepository.deleteAll();
			result.put("result", "No gateways found to be deleted");
		} else {
			result.put("result", "All the gateways deleted successfully");
		}
		return result;
	}

}

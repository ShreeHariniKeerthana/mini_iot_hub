package com.qubercomm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubercomm.multipledb.model.postgres.Gateway;
import com.qubercomm.service.GatewayService;
import com.qubercomm.service.ValidationService;

@RestController
@RequestMapping("/rest/api/gateway/")
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;
	
	@Autowired
	private ValidationService validationService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, Object> onboardGateway(@RequestBody Gateway gateway) {
		Map<String, Object> result = new HashMap<>();
		if(validationService.validatePattern(gateway.getGatewayEuid())) {
			result = gatewayService.onboardGateway(gateway);	
		} else {
			result.put("result", "Please provide a valid gateway uid.");
		}
		return result;
	}

	@RequestMapping(value = "/get/{gateway_euid}", method = RequestMethod.GET)
	public Map<String,Object> getGateway(@PathVariable(value = "gateway_euid") String gateway_euid) {
		Optional<Gateway> optGateway = gatewayService.getGateway(gateway_euid);
		Map<String,Object> result = new HashMap<>();
		if(optGateway.isPresent()) {
			result.put("result",optGateway.get());
		} else {
			result.put("result", "No gateway found for the specified uid");
		}
		return result;
	}

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Map<String,Object> getAllGateways() {
		List<Gateway> gatewayList = gatewayService.getAllGateways();
		Map<String,Object> result = new HashMap<>();
		if(gatewayList.isEmpty()) {
			result.put("result", "No gateways found");
		} else {
			result.put("result", gatewayList);
		}
		return result;
	}

	@RequestMapping(value = "/update/{gateway_euid}", method = RequestMethod.PUT)
	public Map<String,Object> updateGateway(@PathVariable(value = "gateway_euid") String gateway_euid, @RequestBody Gateway gatewayDetails) {
		Map<String,Object> result = gatewayService.updateGateway(gateway_euid, gatewayDetails);
		return result;
	}

	@RequestMapping(value = "/delete/{gateway_euid}", method = RequestMethod.DELETE)
	public Map<String,Object> deleteGateway(@PathVariable(value = "gateway_euid") String gateway_euid) {
		Map<String,Object> result = new HashMap<>();
		result = gatewayService.deleteGateway(gateway_euid);
		return result;
	}

	@RequestMapping(value = "/approve/{gateway_euid}", method = RequestMethod.PATCH)
	public Map<String,Object> approveGateway(@PathVariable(value = "gateway_euid") String gateway_euid, @RequestBody Gateway gatewayDetails) {
		Map<String,Object> resultMap = new HashMap<>();
		boolean result = false;
		result = gatewayService.approveGateway(gateway_euid, gatewayDetails);
		if(result) {
			resultMap.put("result","Gateway approved");
		} else {
			resultMap.put("result","Gateway approval failed");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/getByUser/{user_id}", method = RequestMethod.GET)
	public Map<String,Object> getGatewaysByUserId(@PathVariable(value = "user_id") Long user_id) {
		Map<String,Object> result = gatewayService.getGatewaysByUserId(user_id);
		return result;
	}
	
	@RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
	public Map<String,Object> deleteAllGateways() {
		Map<String,Object> result = new HashMap<>();
		result = gatewayService.deleteAllGateways();
		return result;
	}
}

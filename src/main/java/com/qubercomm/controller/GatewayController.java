package com.qubercomm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubercomm.multipledb.model.postgres.Gateway;
import com.qubercomm.service.GatewayService;

@RestController
@RequestMapping("/rest/api/gateway/")
public class GatewayController {

	@Autowired
	private GatewayService gatewayService;

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String onboardGateway(@RequestBody Gateway gateway) {
		String result = "";
		result = gatewayService.onboardGateway(gateway);	
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
		if(Objects.nonNull(gatewayList)) {
			result.put("result", gatewayList);
		} else {
			result.put("result", "No gateways found");
		}
		return result;
	}

	@RequestMapping(value = "/update/{gateway_euid}", method = RequestMethod.PUT)
	public Map<String,Object> updateGateway(@PathVariable(value = "gateway_euid") String gateway_euid, @RequestBody Gateway gatewayDetails) {
		Gateway updatedGateway = gatewayService.updateGateway(gateway_euid, gatewayDetails);
		Map<String,Object> result = new HashMap<>();
		if(Objects.nonNull(updatedGateway)) {
			result.put("result", updatedGateway);
		} else {
			result.put("result", "Update of gateway failed");
		}
		return result;
	}

	@RequestMapping(value = "/delete/{gateway_euid}", method = RequestMethod.DELETE)
	public String deleteGateway(@PathVariable(value = "gateway_euid") String gateway_euid) {
		boolean result = false;
		result = gatewayService.deleteGateway(gateway_euid);
		if(result) {
			return "Gateway delete successful.";
		} else {
			return "Gateway delete failed.";
		}
	}

	@RequestMapping(value = "/approve/{gateway_euid}", method = RequestMethod.PATCH)
	public String approveGateway(@PathVariable(value = "gateway_euid") String gateway_euid, @RequestBody Gateway gatewayDetails) {
		boolean result = false;
		result = gatewayService.approveGateway(gateway_euid, gatewayDetails);
		if(result) {
			return "Gateway approved.";
		} else {
			return "Gateway approval failed.";
		}
	}
}

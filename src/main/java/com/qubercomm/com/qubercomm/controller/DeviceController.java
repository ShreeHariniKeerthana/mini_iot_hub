package com.qubercomm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubercomm.multipledb.model.postgres.Device;
import com.qubercomm.service.DeviceService;
import com.qubercomm.service.ValidationService;

@RestController
@RequestMapping("/rest/api/device/")
public class DeviceController {
	
	@Autowired
	private DeviceService deviceService;
	
	@Autowired
	private ValidationService validationService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public Map<String, Object> onboardDevice(@RequestBody Device device) { 
		Map<String, Object> result = new HashMap<>();
		if(validationService.validatePattern(device.getDeviceEuid())) {
			result = deviceService.onboardDevice(device);		
		} else {
			result.put("result", "Please provide a valid device uid");
		}
		return result;
	}
	
	@RequestMapping(value = "/get/{device_euid}", method = RequestMethod.GET)
	public Map<String,Object> getDevice(@PathVariable(value = "device_euid") String device_euid) {
		Optional<Device> optDevice = deviceService.getDevice(device_euid);
		Map<String,Object> result = new HashMap<>();
		if(optDevice.isPresent()) {
			result.put("result",optDevice.get());
		} else {
			result.put("result", "No device found for the specified uid");
		}
		return result;
	}
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public Map<String,Object> getAllDevices() {
		List<Device> deviceList = deviceService.getAllDevices();
		Map<String,Object> result = new HashMap<>();
		if(CollectionUtils.isEmpty(deviceList)) {
			result.put("result", "No devices found");
		} else {
			result.put("result", deviceList);
		}
		return result;
	}
	
	@RequestMapping(value = "/update/{device_euid}", method = RequestMethod.PUT)
	public Map<String, Object> updateDevice(@PathVariable(value = "device_euid") String device_euid, @RequestBody Device deviceDetails) {
		Map<String,Object> result = deviceService.updateDevice(device_euid, deviceDetails);	
		return result;
	}
	
	@RequestMapping(value = "/delete/{device_euid}", method = RequestMethod.DELETE)
	public Map<String,Object> deleteDevice(@PathVariable(value = "device_euid") String device_euid) {
		Map<String,Object> result = new HashMap<>();
		result = deviceService.deleteDevice(device_euid);
		return result;
	}
	
	@RequestMapping(value = "/updateProperties/{device_euid}", method = RequestMethod.PATCH)
	public Map<String,Object> updateProperties(@PathVariable(value = "device_euid") String device_euid, @RequestBody JSONObject properties) {
		Map<String,Object> result = new HashMap<>();
		result = deviceService.updateProperties(device_euid, properties);
		return result;
	}
	
	@RequestMapping(value = "/getByGateway/{gateway_euid}", method = RequestMethod.GET)
	public Map<String,Object> getDevicesByGatewayId(@PathVariable(value = "gateway_euid") String gateway_euid) {
		Map<String,Object> result = deviceService.getDevicesByGatewayId(gateway_euid);
		return result;
	}
	
	@RequestMapping(value = "/deleteAll", method = RequestMethod.DELETE)
	public Map<String,Object> deleteAllDevices() {
		Map<String,Object> result = new HashMap<>();
		result = deviceService.deleteAllDevices();
		return result;
	}
}

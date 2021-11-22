package com.qubercomm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.qubercomm.multipledb.model.postgres.Device;
import com.qubercomm.service.DeviceService;

@RestController
@RequestMapping("/rest/api/device/")
public class DeviceController {
	
	@Autowired
	private DeviceService deviceService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String onboardDevice(@RequestBody Device device) { 
		String result = "";
		result = deviceService.onboardDevice(device);		
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
		if(Objects.nonNull(deviceList)) {
			result.put("result", deviceList);
		} else {
			result.put("result", "No devices found");
		}
		return result;
	}
	
	@RequestMapping(value = "/update/{device_euid}", method = RequestMethod.PUT)
	public Map<String, Object> updateDevice(@PathVariable(value = "device_euid") String device_euid, @RequestBody Device deviceDetails) {
		Device updatedDevice = deviceService.updateDevice(device_euid, deviceDetails);	
		Map<String,Object> result = new HashMap<>();
		if(Objects.nonNull(updatedDevice)) {
			result.put("result", updatedDevice);
		} else {
			result.put("result", "Update of device failed");
		}
		return result;
	}
	
	@RequestMapping(value = "/delete/{device_euid}", method = RequestMethod.DELETE)
	public String deleteDevice(@PathVariable(value = "device_euid") String device_euid) {
		boolean result = false;
		result = deviceService.deleteDevice(device_euid);
		if(result) {
			return "Device delete successful.";
		} else {
			return "Device delete failed.";
		}
	}
	
	@RequestMapping(value = "/updateProperties/{device_euid}", method = RequestMethod.PATCH)
	public Map<String,Object> updateProperties(@PathVariable(value = "device_euid") String device_euid, @RequestBody JSONObject properties) {
		Map<String,Object> result = new HashMap<>();
		result = deviceService.updateProperties(device_euid, properties);
		return result;
	}
}

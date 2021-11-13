package com.qubercomm.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qubercomm.multipledb.model.postgres.Device;
import com.qubercomm.multipledb.model.postgres.Gateway;
import com.qubercomm.multipledb.repository.postgres.DeviceRepository;
import com.qubercomm.multipledb.repository.postgres.GatewayRepository;

@Service
public class DeviceService {

	private String POWER_ON = "on";

	private String POWER_OFF = "off";


	@Autowired
	DeviceRepository deviceRepository;

	@Autowired
	GatewayRepository gatewayRepository;

	public String onboardDevice(Device device) {
		String result = "";
		List<Device> deviceList = getAllDevices();
		if(deviceList.size() > 0){
			for(Device loopDevice : deviceList) {
				if(loopDevice.getDeviceEuid().equalsIgnoreCase(device.getDeviceEuid())) {
					return "Device euid provided has been already configured";
				} 
			}
		} else {
			Gateway optGateway = gatewayRepository.findByGatewayEuid(device.getGatewayEuid());
			if(Objects.isNull(optGateway) || optGateway.isApproved() == false) {
				return "Gateway id associated with this device not found/ not approved";
			}
		}
		device.setCreatedOn(new Date());
		device.setUpdatedOn(new Date());
		device = deviceRepository.save(device);	
		if(Objects.nonNull(device)) {
			result = "Device onboarded successfully";
		} else {
			result = "Device onboarding failed";
		}
		return result;
	}

	public Optional<Device> getDevice(String device_euid) {
		return deviceRepository.findById(device_euid);
	}

	public List<Device> getAllDevices() {
		return deviceRepository.findAll();
	}

	public Device updateDevice(String device_euid, Device deviceDetails) {
		Optional<Device> optDevice = deviceRepository.findById(device_euid);
		Device device = null;
		if(optDevice.isPresent()) {
			device = optDevice.get();
		}
		device.setEnabled(deviceDetails.isEnabled());
		device.setDeviceType(deviceDetails.getDeviceType());
		device.setDeviceEuid(deviceDetails.getDeviceEuid());
		device.setDeviceName(deviceDetails.getDeviceName());
		device.setDeviceTags(deviceDetails.getDeviceTags());
		device.setUpdatedOn(new Date());
		return deviceRepository.save(device);
	}

	public boolean deleteDevice(String device_euid) {
		List<Device> deviceList = getAllDevices();
		for(Device loopDevice : deviceList) {
			if(loopDevice.getDeviceEuid().equalsIgnoreCase(device_euid)) {
				deviceRepository.deleteById(device_euid);
				return true;
			}
		}
		return false;

	}

	public Map<String, Object> updateProperties(String device_euid, JSONObject newProperties) {
		List<Device> deviceList = getAllDevices();
		Map<String, Object> result = new HashMap<>();
		for(Device loopDevice : deviceList) {
			if(loopDevice.getDeviceEuid().equalsIgnoreCase(device_euid)) {
				result = updateDeviceProperties(loopDevice, newProperties);
			}	
		}
		return result;
	}

	private Map<String, Object> updateDeviceProperties(Device device, JSONObject newProperties) {
		String deviceType = device.getDeviceType();
		Device updatedDevice = null;
		JSONObject properties = (JSONObject) device.getProperties();
		JSONObject disableProperties = new JSONObject();
		HashMap<String, Object> result = new HashMap<>();
		if(Objects.nonNull(newProperties) && device.isEnabled()) {
			if(newProperties.get("power").equals(POWER_ON)) {
				switch(deviceType) {
					case "bulb":{
						properties.put("color", newProperties.get("color"));
						properties.put("brightness", newProperties.get("brightness"));
						break;
					}	
					case "speaker":{
						properties.put("volume", newProperties.get("volume	"));
						break;
					}	
					case "door":{
						properties.put("action", newProperties.get("action"));
						break;
					}
				}
				properties.put("power", newProperties.get("power"));
				device.setProperties(properties);

			} else if(newProperties.get("power").equals(POWER_OFF)) {
				disableProperties.put("power", newProperties.get("power"));
				device.setProperties(disableProperties);
			}
			device.setUpdatedOn(new Date());
			updatedDevice = deviceRepository.save(device);
		} else {
			result.put("result","Device properties update failed because device is not enabled");
			return result;
		}
		
		if(Objects.nonNull(updatedDevice)) {
			result.put("result", updatedDevice);
		} else {
			result.put("result", "Update of device properties failed");
		}
		return result;

	}
}

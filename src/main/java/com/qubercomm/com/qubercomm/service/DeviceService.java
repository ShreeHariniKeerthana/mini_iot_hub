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

	public Map<String, Object> onboardDevice(Device device) {
		List<Device> deviceList = getAllDevices();
		Map<String, Object> result = new HashMap<>();
		if(deviceList.size() > 0){
			for(Device loopDevice : deviceList) {
				if(loopDevice.getDeviceEuid().equalsIgnoreCase(device.getDeviceEuid())) {
					result.put("result","Device uid provided has been already configured");
					return result;
				} 
			}
		} 
		Gateway optGateway = gatewayRepository.findByGatewayEuid(device.getGatewayEuid());
		if(Objects.isNull(optGateway)) {
			result.put("result","Gateway id associated with this device not found");
			return result;
		} else if(optGateway.isApproved() == false) {
			result.put("result","Gateway id associated with this device not approved");
			return result;
		}
		

		device.setCreatedOn(new Date());
		device.setUpdatedOn(new Date());
		JSONObject properties = device.getProperties();
		JSONObject disableProperties = new JSONObject();
		if(properties.get("power").equals(POWER_OFF)) {
			disableProperties.put("power", properties.get("power"));
		}
		device.setProperties(disableProperties);
		device = deviceRepository.save(device);	
		if(Objects.nonNull(device)) {
			result.put("result", "Device onboard successful");
		} else {
			result.put("result", "Device onboard failed");
		}
		return result;
	}

	public Optional<Device> getDevice(String device_euid) {
		return deviceRepository.findById(device_euid);
	}

	public List<Device> getAllDevices() {
		return deviceRepository.findAll();
	}

	public Map<String, Object> updateDevice(String device_euid, Device deviceDetails) {
		Optional<Device> optDevice = deviceRepository.findById(device_euid);
		Device device = null;
		Map<String, Object> result = new HashMap<>();
		if(optDevice.isPresent()) {
			device = optDevice.get();
			device.setEnabled(deviceDetails.isEnabled());
			device.setDeviceType(deviceDetails.getDeviceType());
			device.setDeviceEuid(deviceDetails.getDeviceEuid());
			device.setDeviceName(deviceDetails.getDeviceName());
			device.setDeviceTags(deviceDetails.getDeviceTags());
			device.setUpdatedOn(new Date());
			Device updatedDevice = deviceRepository.save(device);
			if(Objects.nonNull(updatedDevice)) {
				result.put("result", updatedDevice);
			} else {
				result.put("result", "Device update failed");
			}
		} else {
			result.put("result", "No device found with the uid specified");
		}
		return result;
	}


	public Map<String, Object> deleteDevice(String device_euid) {
		List<Device> deviceList = getAllDevices();
		Map<String, Object> result = new HashMap<>();
		for(Device loopDevice : deviceList) {
			if(loopDevice.getDeviceEuid().equalsIgnoreCase(device_euid)) {
				deviceRepository.deleteById(device_euid);
				result.put("result", "Device delete successful");
			} else {
				result.put("result", "No device found with the uid specified");
			}
		}
		return result;

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
			result.put("result", "Device properties update failed");
		}
		return result;

	}

	public Map<String, Object> getDevicesByGatewayId(String gateway_euid){
		List<Device> deviceList = deviceRepository.findByGatewayEuid(gateway_euid);
		Map<String, Object> result = new HashMap<>();
		if(deviceList.isEmpty()) {
			result.put("result", "No devices found for gateway id specified");
		} else {
			result.put("result", deviceList);
		}
		return result;
	}

	public Map<String, Object> deleteAllDevices() {
		List<Device> deviceList = getAllDevices();
		Map<String, Object> result = new HashMap<>();
		if(deviceList.isEmpty()) {
			result.put("result", "No devices found to be deleted");
		} else {
			deviceRepository.deleteAll();
			result.put("result", "All the devices deleted successfully");
			
		}
		return result;
	}
}

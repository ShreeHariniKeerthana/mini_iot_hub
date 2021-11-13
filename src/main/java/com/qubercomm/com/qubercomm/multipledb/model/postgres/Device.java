package com.qubercomm.multipledb.model.postgres;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.json.simple.JSONObject;

@Entity
@Audited
@Table(name = "device")
public class Device {

	@Id
	@Column(name = "device_euid", unique = true)
	private String deviceEuid;
	
	@Column(name = "gateway_euid")
	private String gatewayEuid;
	
	@Column(name = "device_name")
	private String deviceName;
	
	@Column(name = "device_type")
	private String deviceType;
	
	@Column(name = "device_tags")
	private String[] deviceTags;
	
	@Column(name = "is_enabled")
	private boolean isEnabled;
	
	@Column(name = "created_on")
	private Date createdOn;
	
	@Column(name = "updated_on")
	private Date updatedOn;
	
	@Column(name = "properties")
	private JSONObject properties;

	public String getDeviceEuid() {
		return deviceEuid;
	}

	public void setDeviceEuid(String deviceEuid) {
		this.deviceEuid = deviceEuid;
	}

	public String getGatewayEuid() {
		return gatewayEuid;
	}

	public void setGatewayEuid(String gatewayEuid) {
		this.gatewayEuid = gatewayEuid;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String[] getDeviceTags() {
		return deviceTags;
	}

	public void setDeviceTags(String[] deviceTags) {
		this.deviceTags = deviceTags;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

	public JSONObject getProperties() {
		return properties;
	}

	public void setProperties(JSONObject properties) {
		this.properties = properties;
	}
	
}


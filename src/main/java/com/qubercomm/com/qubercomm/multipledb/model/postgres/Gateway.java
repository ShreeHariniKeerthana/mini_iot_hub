package com.qubercomm.multipledb.model.postgres;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "gateway")
public class Gateway {
	
	@Id
	@Column(name = "gateway_euid", unique = true)
	private String gatewayEuid;
	
	@Column(name = "gateway_name")
	private String gatewayName;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "gateway_tags")
	private String[] gatewayTags;
	
	@Column(name = "is_approved")
	private boolean isApproved;
	
	@Column(name = "created_on")
	private Date createdOn;

	@Column(name = "updated_on")
	private Date updatedOn;

	public String getGatewayEuid() {
		return gatewayEuid;
	}

	public void setGatewayEuid(String gatewayEuid) {
		this.gatewayEuid = gatewayEuid;
	}

	public String getGatewayName() {
		return gatewayName;
	}

	public void setGatewayName(String gatewayName) {
		this.gatewayName = gatewayName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String[] getGatewayTags() {
		return gatewayTags;
	}

	public void setGatewayTags(String[] gatewayTags) {
		this.gatewayTags = gatewayTags;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
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
	
	
}


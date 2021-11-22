package com.qubercomm.multipledb.repository.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qubercomm.multipledb.model.postgres.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, String> {

	public Device findByDeviceEuid(String device_euid);

}

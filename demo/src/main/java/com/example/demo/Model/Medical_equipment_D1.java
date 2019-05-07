package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Medical_equipment_D1 {
	@Id
	private Integer equipmentId;
	private String equipmentName;
	private String inventory;
	private String deviceCountingUnit;
	private byte[] file;
	
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	public String getEquipmentName() {
		return equipmentName;
	}
	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	public String getInventory() {
		return inventory;
	}
	public void setInventory(String inventory) {
		this.inventory = inventory;
	}
	public String getDeviceCountingUnit() {
		return deviceCountingUnit;
	}
	public void setDeviceCountingUnit(String deviceCountingUnit) {
		this.deviceCountingUnit = deviceCountingUnit;
	}
	public byte[] getFile() {
		return file;
	}
	public void setFile(byte[] file) {
		this.file = file;
	}
}

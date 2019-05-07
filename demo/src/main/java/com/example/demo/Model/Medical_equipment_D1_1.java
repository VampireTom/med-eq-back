package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Medical_equipment_D1_1 {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer equipmentId_1;
	private Integer equipmentId;
	private String no;
	private String serialNo;
	private String status;
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getEquipmentId_1() {
		return equipmentId_1;
	}
	public void setEquipmentId_1(Integer equipmentId_1) {
		this.equipmentId_1 = equipmentId_1;
	}
	public Integer getEquipmentId() {
		return equipmentId;
	}
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}
	
}

package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Type {
	@Id
	private String typeId;
	private String typeName;
	private byte[] typeFile;
	private Integer typeTotal;
	private Integer typeBorrow;
	private Integer typeBooking;
	private String typeNum;
	private Integer borrowing;
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public byte[] getTypeFile() {
		return typeFile;
	}
	public void setTypeFile(byte[] typeFile) {
		this.typeFile = typeFile;
	}
	public Integer getTypeTotal() {
		return typeTotal;
	}
	public void setTypeTotal(Integer typeTotal) {
		this.typeTotal = typeTotal;
	}
	public Integer getTypeBorrow() {
		return typeBorrow;
	}
	public void setTypeBorrow(Integer typeBorrow) {
		this.typeBorrow = typeBorrow;
	}
	public Integer getTypeBooking() {
		return typeBooking;
	}
	public void setTypeBooking(Integer typeBooking) {
		this.typeBooking = typeBooking;
	}
	public String getTypeNum() {
		return typeNum;
	}
	public void setTypeNum(String typeNum) {
		this.typeNum = typeNum;
	}
	public Integer getBorrowing() {
		return borrowing;
	}
	public void setBorrowing(Integer borrowing) {
		this.borrowing = borrowing;
	}
}

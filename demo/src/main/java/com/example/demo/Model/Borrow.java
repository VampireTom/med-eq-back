package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Borrow {
	@Id
	private String borrowId;
	private String borrowDate;
	private String empId;
	private String typeId;
	private Integer borNum;
	private String status;
	private String revertDate;
	private Integer tmpBorNum;
	public String getBorrowId() {
		return borrowId;
	}
	public void setBorrowId(String borrowId) {
		this.borrowId = borrowId;
	}
	public String getBorrowDate() {
		return borrowDate;
	}
	public void setBorrowDate(String borrowDate) {
		this.borrowDate = borrowDate;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public Integer getBorNum() {
		return borNum;
	}
	public void setBorNum(Integer borNum) {
		this.borNum = borNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRevertDate() {
		return revertDate;
	}
	public void setRevertDate(String revertDate) {
		this.revertDate = revertDate;
	}
	public Integer getTmpBorNum() {
		return tmpBorNum;
	}
	public void setTmpBorNum(Integer tmpBorNum) {
		this.tmpBorNum = tmpBorNum;
	}
	
}

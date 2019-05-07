package com.example.demo.Model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Booking_D5_1 {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer bookingId_1;
	private Integer bookingId;
	private String serialNo;
	private String status;
	private Integer count;
	private String date;
	public Integer getBookingId_1() {
		return bookingId_1;
	}
	public void setBookingId_1(Integer bookingId_1) {
		this.bookingId_1 = bookingId_1;
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Integer getBookingId() {
		return bookingId;
	}
	public void setBookingId(Integer bookingId) {
		this.bookingId = bookingId;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
}

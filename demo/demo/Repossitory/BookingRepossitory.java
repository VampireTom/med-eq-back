package com.example.demo.Repossitory;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Model.Booking;

public interface BookingRepossitory extends CrudRepository<Booking, String>{

}

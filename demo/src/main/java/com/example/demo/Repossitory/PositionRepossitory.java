package com.example.demo.Repossitory;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Model.Position;

public interface PositionRepossitory extends CrudRepository<Position, String>{
	
}

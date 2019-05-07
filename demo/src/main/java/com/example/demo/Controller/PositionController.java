package com.example.demo.Controller;

import java.util.List;

import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Position_D2;
import com.example.demo.Model.Req;
import com.example.demo.Repossitory.PositionRepossitory;

@RestController
public class PositionController {
	@Autowired
	private PositionRepossitory positionRepossitory;
	
	@RequestMapping(value = "/findAllPosition" , method = RequestMethod.POST)
	public List<Position_D2> getAllPosition(@RequestBody Req req){
		try {
			Iterable<Position_D2> iterable = positionRepossitory.findAll();
			List<Position_D2> positionList = Lists.newArrayList(iterable);
			return positionList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

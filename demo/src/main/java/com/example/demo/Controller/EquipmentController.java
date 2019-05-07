package com.example.demo.Controller;

import java.util.List;

import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Department;
import com.example.demo.Model.Equipment;
import com.example.demo.Model.Req;
import com.example.demo.Repossitory.EquipmentRepossitory;

@RestController
public class EquipmentController {
	@Autowired
	private EquipmentRepossitory equipmentRepossitory;

	@RequestMapping(value = "/findAllequipment", method = RequestMethod.POST)
	public List<Equipment> getAllEquipment(@RequestBody Req req) {
		try {
			Iterable<Equipment> iterable = equipmentRepossitory.findAll();
			List<Equipment> eqList = Lists.newArrayList(iterable);
			return eqList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
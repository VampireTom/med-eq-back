package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.List;

import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Medical_equipment_D1;
import com.example.demo.Model.Medical_equipment_D1_1;
import com.example.demo.Model.Req;
import com.example.demo.Repossitory.MedicalEquipment1Repossitory;
import com.example.demo.Repossitory.MedicalEquipmentRepossitory;

@RestController
public class MedicalEquipmentController {
	@Autowired
	private MedicalEquipmentRepossitory medEqRepossitory;
	@Autowired
	private MedicalEquipment1Repossitory medEq1Repossitory;

	@RequestMapping(value = "/findAllMedEq", method = RequestMethod.POST)
	public List<Medical_equipment_D1> getAllMedEq(@RequestBody Req req) {
		try {
			Iterable<Medical_equipment_D1> iterable1 = medEqRepossitory.findAll();
			List<Medical_equipment_D1> medEqList = Lists.newArrayList(iterable1);
			Iterable<Medical_equipment_D1_1> iterable2 = medEq1Repossitory.findAll();
			List<Medical_equipment_D1_1> medEq1List = Lists.newArrayList(iterable2);
			List<Medical_equipment_D1> resList = new ArrayList<Medical_equipment_D1>();
			for (Medical_equipment_D1 medEq : medEqList) {
				int i = 0;
				Integer inventory = Integer.valueOf(medEq.getInventory());
				for (Medical_equipment_D1_1 medEq1 : medEq1List) {
					if (medEq.getEquipmentId() == medEq1.getEquipmentId()) {
						i++;
					}
				}
				inventory = inventory - i;
				medEq.setInventory(inventory.toString());
				resList.add(medEq);
			}
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

package com.example.demo.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Employee_D3;
import com.example.demo.Model.Req;
import com.example.demo.Repossitory.EmployeeRepossitory;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeRepossitory employeeRepossitory;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String getBookingById(@RequestBody Req req) {
		System.out.println("req = " + req);
		Object object = req.getBody();
		Map<String, Object> map = (Map<String, Object>) object;
		Integer username = map.get("userName") != null ? Integer.valueOf(map.get("userName").toString() ): 0;
		String password = map.get("password") != null ? map.get("password").toString() : "";
		Optional<Employee_D3> optional = employeeRepossitory.findById(username);
		Employee_D3 employee = new Employee_D3();
		try {
			if (optional.isPresent()) {
				employee = optional.get();
				if (employee != null) {
					if (password.equals(employee.getEmpPass())) {
						return employee.getPosition();
					} else {
						return "false";
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String getRegis(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			System.out.println("req = " + object);
			Map<String, Object> map = (Map<String, Object>) object;
			Employee_D3 employee_D3 = new Employee_D3();
			employee_D3.setEmpId(map.get("empId") != null ? Integer.valueOf(map.get("empId").toString()) : 0);
			employee_D3.setEmpName(map.get("empName") != null ? map.get("empName").toString() : "");
			employee_D3.setIdCard(map.get("empCId") != null ? map.get("empCId").toString() : "");
			employee_D3.setPhone(map.get("empPhone") != null ? map.get("empPhone").toString() : "");
			employee_D3.setEmpPass(map.get("empPass") != null ? map.get("empPass").toString() : "");
			employee_D3.setPosition(map.get("empPosition") != null ? map.get("empPosition").toString() : "");
			employee_D3.setDepartmentId(map.get("empDepartment") != null ? Integer.valueOf(map.get("empDepartment").toString()) : 0);
			employeeRepossitory.save(employee_D3);
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}
}

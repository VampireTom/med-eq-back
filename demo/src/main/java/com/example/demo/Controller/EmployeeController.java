package com.example.demo.Controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Department;
import com.example.demo.Model.Employee;
import com.example.demo.Model.Position;
import com.example.demo.Model.Req;
import com.example.demo.Model.Res;
import com.example.demo.Repossitory.DepartmentRepossitory;
import com.example.demo.Repossitory.EmployeeRepossitory;
import com.example.demo.Repossitory.PositionRepossitory;

@RestController
public class EmployeeController {
	@Autowired
	private EmployeeRepossitory employeeRepossitory;

	@Autowired
	private PositionRepossitory positionRepossitory;

	@Autowired
	private DepartmentRepossitory departmentRepossitor;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Res login(@RequestBody Req req) {
		Object object = req.getBody();
		Map<String, Object> map = (Map<String, Object>) object;
		System.out.println("map = " + map);
		String username = map.get("userName") != null ? map.get("userName").toString() : "";
		String password = map.get("password") != null ? map.get("password").toString() : "";
		Optional<Employee> empOpt = employeeRepossitory.findById(username);
		Employee employee = new Employee();
		Res res = new Res();
		try {
			if (empOpt.isPresent()) {
				employee = empOpt.get();
				if (employee != null) {
					if (password.equals(employee.getEmpPass())) {
						res.setEmpName(employee.getEmpName());
						res.setPositionId(employee.getPositionId());

						Optional<Position> posOpt = positionRepossitory.findById(employee.getPositionId());
						Position position = posOpt.isPresent() ? posOpt.get() : null;
						res.setPositionName(position != null ? position.getPositionName() : "");

						Optional<Department> depOpt = departmentRepossitor.findById(employee.getDepartmentId());
						Department department = depOpt.isPresent() ? depOpt.get() : null;
						res.setDepartmentName(department != null ? department.getDepartmentName() : "");

						return res;
					} else {
						return null;
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
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			Employee employee = new Employee();
			employee.setEmpId(map.get("empId") != null ? map.get("empId").toString() : "");
			employee.setEmpName(map.get("empName") != null ? map.get("empName").toString() : "");
			employee.setEmpPhone(map.get("empPhone") != null ? map.get("empPhone").toString() : "");
			employee.setEmpPass(map.get("empPass") != null ? map.get("empPass").toString() : "");
			employee.setPositionId(map.get("empPosition") != null ? map.get("empPosition").toString() : "");
			employee.setDepartmentId(map.get("empDepartment") != null ? map.get("empDepartment").toString() : "");
			employeeRepossitory.save(employee);
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "false";
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getprofile", method = RequestMethod.POST)
	public Res getProfile(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			String username = map.get("userName") != null ? map.get("userName").toString() : "";
			Optional<Employee> empOpt = employeeRepossitory.findById(username);
			Employee employee = new Employee();
			Res res = new Res();
			if (empOpt.isPresent()) {
				employee = empOpt.get();
				if (employee != null) {
					res.setEmpId(username);
					res.setEmpName(employee.getEmpName());
					res.setEmpPhone(employee.getEmpPhone());

					Optional<Position> posOpt = positionRepossitory.findById(employee.getPositionId());
					Position position = posOpt.isPresent() ? posOpt.get() : null;
					res.setPositionId(position != null ? position.getPositionId() : "");
					res.setPositionName(position != null ? position.getPositionName() : "");

					Optional<Department> depOpt = departmentRepossitor.findById(employee.getDepartmentId());
					Department department = depOpt.isPresent() ? depOpt.get() : null;
					res.setDepartmentId(department != null ? department.getDepartmentId() : "");
					res.setDepartmentName(department != null ? department.getDepartmentName() : "");

					return res;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/editprofile", method = RequestMethod.POST)
	public Res editProfile(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			String username = map.get("empId") != null ? map.get("empId").toString() : "";
			Optional<Employee> empOpt = employeeRepossitory.findById(username);
			Employee employee = new Employee();
			Res res = new Res();
			if (empOpt.isPresent()) {
				employee = empOpt.get();
				if (employee != null) {
					employee.setEmpId(username);
					employee.setEmpName(map.get("empName") != null ? map.get("empName").toString() : "");
					employee.setEmpPhone(map.get("empPhone") != null ? map.get("empPhone").toString() : "");
					employee.setPositionId(map.get("empposition") != null ? map.get("empposition").toString() : "");
					employee.setDepartmentId(map.get("empdepartment") != null ? map.get("empdepartment").toString() : "");
					employeeRepossitory.save(employee);
					res.setEmpName(map.get("empName").toString());
					Optional<Position> posOpt = positionRepossitory.findById(map.get("empposition").toString());
					Position position = new Position();
					if(posOpt.isPresent()) {
						position = posOpt.get();
					}
					res.setPositionName(position.getPositionName());
					return res;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

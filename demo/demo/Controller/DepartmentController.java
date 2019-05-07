package com.example.demo.Controller;

import java.util.List;

import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Department;
import com.example.demo.Model.Req;
import com.example.demo.Repossitory.DepartmentRepossitory;

@RestController
public class DepartmentController {
	@Autowired
	private DepartmentRepossitory departmentRepossitory;
	
    @RequestMapping(value ="/findAllDepartment" , method = RequestMethod.POST)
    public List<Department> getAllDepartment(@RequestBody Req req){
    	try {
			Iterable<Department> iterable = departmentRepossitory.findAll();
			List<Department> deList = Lists.newArrayList(iterable);
			return deList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
    }
}

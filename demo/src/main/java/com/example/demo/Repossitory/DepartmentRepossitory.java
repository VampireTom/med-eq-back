package com.example.demo.Repossitory;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Model.Department;

public interface DepartmentRepossitory extends CrudRepository<Department, String> {
	

}

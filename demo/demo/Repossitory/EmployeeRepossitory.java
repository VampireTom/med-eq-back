package com.example.demo.Repossitory;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Model.Employee;

public interface EmployeeRepossitory extends CrudRepository<Employee, String>{

}

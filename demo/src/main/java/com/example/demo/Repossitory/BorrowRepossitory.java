package com.example.demo.Repossitory;

import org.springframework.data.repository.CrudRepository;

import com.example.demo.Model.Borrow;

public interface BorrowRepossitory extends CrudRepository<Borrow, String> {

}

package com.example.demo;

import java.util.Comparator;

import com.example.demo.Model.Type;

public class SortbyBooking implements Comparator<Type> {

	@Override
	public int compare(Type arg0, Type arg1) {
		return arg0.getTypeBooking().compareTo(arg1.getTypeBooking());
	}

}

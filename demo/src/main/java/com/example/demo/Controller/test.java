package com.example.demo.Controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class test {
	public static void main(String[] args) {
		String a = "20190511";
		String b = "20190512";
		System.out.println(a.compareTo(b));
		
		System.out.println(a.substring(0,4));
		System.out.println(a.substring(4,6));
		System.out.println(a.substring(6));
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		cal.set(Integer.valueOf(a.substring(0,4)), Integer.valueOf(a.substring(4,6))-1, Integer.valueOf(a.substring(6)));
		System.out.println(format.format(cal.getTime()));
		
	}
}

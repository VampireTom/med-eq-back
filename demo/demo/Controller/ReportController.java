package com.example.demo.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Employee;
import com.example.demo.Model.Position;
import com.example.demo.Repossitory.BookingRepossitory;
import com.example.demo.Repossitory.BorrowRepossitory;
import com.example.demo.Repossitory.DepartmentRepossitory;
import com.example.demo.Repossitory.EmployeeRepossitory;
import com.example.demo.Repossitory.EquipmentRepossitory;
import com.example.demo.Repossitory.PositionRepossitory;
import com.example.demo.Repossitory.TypeRepossitory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class ReportController {
	@Autowired
	private BookingRepossitory bookingRepossitory;
	@Autowired
	private BorrowRepossitory borrowRepossitory;
	@Autowired
	private DepartmentRepossitory departmentRepossitory;
	@Autowired
	private EmployeeRepossitory employeeRepossitory;
	@Autowired
	private EquipmentRepossitory equipmentRepossitory;
	@Autowired
	private PositionRepossitory positionRepossitory;
	@Autowired
	private TypeRepossitory typeRepossitory;

	
}

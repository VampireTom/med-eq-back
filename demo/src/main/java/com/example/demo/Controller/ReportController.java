package com.example.demo.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.SortbyBooking;
import com.example.demo.Model.Borrow;
import com.example.demo.Model.Department;
import com.example.demo.Model.Employee;
import com.example.demo.Model.Position;
import com.example.demo.Model.Req;
import com.example.demo.Model.Type;
import com.example.demo.Repossitory.BookingRepossitory;
import com.example.demo.Repossitory.BorrowRepossitory;
import com.example.demo.Repossitory.DepartmentRepossitory;
import com.example.demo.Repossitory.EmployeeRepossitory;
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
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
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
	private PositionRepossitory positionRepossitory;
	@Autowired
	private TypeRepossitory typeRepossitory;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reporttype", method = RequestMethod.POST)
	public String getReporttype(@RequestBody Req req) {
		Object object = req.getBody();
		Type type = new Type();
		Map<String, Object> map = (Map<String, Object>) object;
		System.out.println("map = " + map);
		Document doc = new Document();
		try {
			String username = map.get("username") != null ? map.get("username").toString() : "";
			BaseFont fo = BaseFont.createFont("THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf"));
			doc.open();

			Optional<Employee> employees = employeeRepossitory.findById(username);
			Employee employee = employees.isPresent() ? employees.get() : null;
			
			Iterable<Type> types = typeRepossitory.findAll();
			List<Type> typeList = Lists.newArrayList(types);
			
			Collections.sort(typeList, new SortbyBooking()); 
			type = typeList.get(typeList.size()-1);
			
			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy", new Locale("th", "TH"));
//
//			Calendar cal2 = Calendar.getInstance();
//			cal2.add(Calendar.DATE, Integer.valueOf(type.getBorrowing()));

			PdfPTable t1 = new PdfPTable(1);
			t1.setWidthPercentage(100); // Width 100%
			t1.setSpacingBefore(10f); // Space before table
			t1.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t1cw = { 1f };

			t1.setWidths(t1cw);

			PdfPCell t1c1 = new PdfPCell(new Paragraph("โรงพยาบาลราชวิถี ", new Font(fo, 14)));
			// t1c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c2 = new PdfPCell(new Paragraph("2 แขวงทุ่งพญาไท   เขตราชเทวี ", new Font(fo, 14)));
			// t1c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c3 = new PdfPCell(new Paragraph("กรุงเทพมหานคร  10400", new Font(fo, 14)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c4 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t1c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c5 = new PdfPCell(new Paragraph("เลขที่ ", new Font(fo, 14)));
			t1c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c6 = new PdfPCell(new Paragraph("วันที่ " + sdf1.format(cal.getTime()), new Font(fo, 14)));
			t1c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c6.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c7 = new PdfPCell(new Paragraph("รายงานอุปกรณ์ที่ไม่เพียงพอ", new Font(fo, 26)));
			t1c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c7.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c7.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c8 = new PdfPCell(new Paragraph("ประจำเดือน " + sdf1.format(cal.getTime()), new Font(fo, 14)));
			t1c8.setBorder(Rectangle.NO_BORDER);

			t1.addCell(t1c1);
			t1.addCell(t1c2);
			t1.addCell(t1c3);
			t1.addCell(t1c4);
			t1.addCell(t1c5);
			t1.addCell(t1c6);
			t1.addCell(t1c7);
			t1.addCell(t1c8);

			doc.add(t1);

			PdfPTable t2 = new PdfPTable(6);
			t2.setSpacingBefore(10f);
			t2.setSpacingAfter(10f);
			float[] t2cw = { 1f, 2f, 2f, 1f, 1f, 1f };

			t2.setWidths(t2cw);

			PdfPCell t2c1 = new PdfPCell(new Paragraph("ลำดับ", new Font(fo, 14)));
			t2c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c2 = new PdfPCell(new Paragraph("รายการอุปกรณ์", new Font(fo, 14)));
			t2c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c3 = new PdfPCell(new Paragraph("รหัสอุปกรณ์ทางการแพทย์", new Font(fo, 14)));
			t2c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c3.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c4 = new PdfPCell(new Paragraph("จำนวนคงคลัง", new Font(fo, 14)));
			t2c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c4.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c5 = new PdfPCell(new Paragraph("จำนวนที่ยืม", new Font(fo, 14)));
			t2c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c5.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c6 = new PdfPCell(new Paragraph("จำนวนที่จอง", new Font(fo, 14)));
			t2c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c6.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c7 = new PdfPCell(new Paragraph("1", new Font(fo, 14)));
			t2c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c7.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c8 = new PdfPCell(new Paragraph(type.getTypeName(), new Font(fo, 14)));
			t2c8.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c8.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c9 = new PdfPCell(new Paragraph(type.getTypeId(), new Font(fo, 14)));
			t2c9.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c9.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c10 = new PdfPCell(new Paragraph(type.getTypeTotal().toString(), new Font(fo, 14)));
			t2c10.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c10.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c11 = new PdfPCell(new Paragraph(type.getTypeBorrow().toString(), new Font(fo, 14)));
			t2c11.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c11.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c12 = new PdfPCell(new Paragraph(type.getTypeBooking().toString(), new Font(fo, 14)));
			t2c12.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c12.setVerticalAlignment(Element.ALIGN_MIDDLE);

			t2.addCell(t2c1);
			t2.addCell(t2c2);
			t2.addCell(t2c3);
			t2.addCell(t2c4);
			t2.addCell(t2c5);
			t2.addCell(t2c6);
			t2.addCell(t2c7);
			t2.addCell(t2c8);
			t2.addCell(t2c9);
			t2.addCell(t2c10);
			t2.addCell(t2c11);
			t2.addCell(t2c12);

			doc.add(t2);

			PdfPTable t3 = new PdfPTable(2);
			t3.setWidthPercentage(100); // Width 100%
			t3.setSpacingBefore(10f); // Space before table
			t3.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t3cw = { 1f, 1f };

			t3.setWidths(t3cw);

			PdfPCell t3c1 = new PdfPCell(new Paragraph(
					"ลงชื่อ...............................................................ผู้จัดทำ", new Font(fo, 16)));
			t3c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c1.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c2 = new PdfPCell(
					new Paragraph("ลงชื่อ...............................................................ผู้ตรวจสอบ",
							new Font(fo, 16)));
			t3c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c2.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c3 = new PdfPCell(new Paragraph(employee.getEmpName(), new Font(fo, 14)));
			t3c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c3.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c4 = new PdfPCell(new Paragraph("(_________________________)", new Font(fo, 14)));
			t3c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c4.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c5 = new PdfPCell(new Paragraph("วันที่ " + sdf1.format(cal.getTime()), new Font(fo, 14)));
			t3c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c6 = new PdfPCell(
					new Paragraph("วันที่.................../............../...................", new Font(fo, 14)));
			t3c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c6.setBorder(Rectangle.NO_BORDER);

			t3.addCell(t3c1);
			t3.addCell(t3c2);
			t3.addCell(t3c3);
			t3.addCell(t3c4);
			t3.addCell(t3c5);
			t3.addCell(t3c6);

			doc.add(t3);
			doc.close();
			writer.close();

			byte[] inputFile = Files.readAllBytes(Paths.get("test.pdf"));
			byte[] encodedBytes = Base64.getEncoder().encode(inputFile);
			String encodedString = new String(encodedBytes);
			return "{\"src\":\"" + encodedString + "\"}";
		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reportstatus", method = RequestMethod.POST)
	public String getReportstatus(@RequestBody Req req) {
		Object object = req.getBody();
		Map<String, Object> map = (Map<String, Object>) object;
		System.out.println("map = " + map);
		Document doc = new Document();
		try {
			BaseFont fo = BaseFont.createFont("THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf"));
			doc.open();
			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy", new Locale("th", "TH"));

			PdfPTable t1 = new PdfPTable(1);
			t1.setWidthPercentage(100); // Width 100%
			t1.setSpacingBefore(10f); // Space before table
			t1.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t1cw = { 1f };

			t1.setWidths(t1cw);

			PdfPCell t1c1 = new PdfPCell(new Paragraph("โรงพยาบาลราชวิถี ", new Font(fo, 14)));
			// t1c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c2 = new PdfPCell(new Paragraph("2 แขวงทุ่งพญาไท   เขตราชเทวี ", new Font(fo, 14)));
			// t1c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c3 = new PdfPCell(new Paragraph("กรุงเทพมหานคร  10400", new Font(fo, 14)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c4 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t1c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c5 = new PdfPCell(new Paragraph("เลขที่ ", new Font(fo, 14)));
			t1c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c6 = new PdfPCell(new Paragraph("วันที่ " + sdf1.format(cal.getTime()), new Font(fo, 14)));
			t1c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c6.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c7 = new PdfPCell(new Paragraph("รายงานอุปกรณ์ค้างส่ง", new Font(fo, 26)));
			t1c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c7.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c7.setBorder(Rectangle.NO_BORDER);

			t1.addCell(t1c1);
			t1.addCell(t1c2);
			t1.addCell(t1c3);
			t1.addCell(t1c4);
			t1.addCell(t1c5);
			t1.addCell(t1c6);
			t1.addCell(t1c7);

			doc.add(t1);

			PdfPTable t2 = new PdfPTable(6);
			t2.setSpacingBefore(10f);
			t2.setSpacingAfter(10f);
			float[] t2cw = { 1f, 2f, 2f, 1f, 2f, 2f };

			t2.setWidths(t2cw);

			PdfPCell t2c1 = new PdfPCell(new Paragraph("ลำดับ", new Font(fo, 14)));
			t2c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c2 = new PdfPCell(new Paragraph("ชื่อหน่วยงาน", new Font(fo, 14)));
			t2c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c3 = new PdfPCell(new Paragraph("รายการที่ยืม", new Font(fo, 14)));
			t2c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c3.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c4 = new PdfPCell(new Paragraph("เลขที่ยืม", new Font(fo, 14)));
			t2c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c4.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c5 = new PdfPCell(new Paragraph("วันยืม", new Font(fo, 14)));
			t2c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c5.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c6 = new PdfPCell(new Paragraph("วันครบกำหนดคืน", new Font(fo, 14)));
			t2c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c6.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c7 = new PdfPCell(new Paragraph("1", new Font(fo, 14)));
			t2c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c7.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c8 = new PdfPCell(new Paragraph("Department -> department_name", new Font(fo, 14)));
			t2c8.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c8.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c9 = new PdfPCell(new Paragraph("Type -> type_name", new Font(fo, 14)));
			t2c9.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c9.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c10 = new PdfPCell(new Paragraph("Borrow -> borrow_id", new Font(fo, 14)));
			t2c10.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c10.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c11 = new PdfPCell(new Paragraph("Borrow -> borrow_date", new Font(fo, 14)));
			t2c11.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c11.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c12 = new PdfPCell(new Paragraph("......", new Font(fo, 14)));
			t2c12.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c12.setVerticalAlignment(Element.ALIGN_MIDDLE);

			t2.addCell(t2c1);
			t2.addCell(t2c2);
			t2.addCell(t2c3);
			t2.addCell(t2c4);
			t2.addCell(t2c5);
			t2.addCell(t2c6);
			t2.addCell(t2c7);
			t2.addCell(t2c8);
			t2.addCell(t2c9);
			t2.addCell(t2c10);
			t2.addCell(t2c11);
			t2.addCell(t2c12);

			doc.add(t2);

			PdfPTable t3 = new PdfPTable(2);
			t3.setWidthPercentage(100); // Width 100%
			t3.setSpacingBefore(10f); // Space before table
			t3.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t3cw = { 1f, 1f };

			t3.setWidths(t3cw);

			PdfPCell t3c1 = new PdfPCell(new Paragraph(
					"ลงชื่อ...............................................................ผู้จัดทำ", new Font(fo, 16)));
			t3c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c1.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c2 = new PdfPCell(
					new Paragraph("ลงชื่อ...............................................................ผู้ตรวจสอบ",
							new Font(fo, 16)));
			t3c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c2.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c3 = new PdfPCell(new Paragraph("Employee - > emp_name", new Font(fo, 14)));
			t3c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c3.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c4 = new PdfPCell(new Paragraph("(_________________________)", new Font(fo, 14)));
			t3c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c4.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c5 = new PdfPCell(new Paragraph("วันที่ " + sdf1.format(cal.getTime()), new Font(fo, 14)));
			t3c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c6 = new PdfPCell(
					new Paragraph("วันที่.................../............../...................", new Font(fo, 14)));
			t3c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t3c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t3c6.setBorder(Rectangle.NO_BORDER);

			t3.addCell(t3c1);
			t3.addCell(t3c2);
			t3.addCell(t3c3);
			t3.addCell(t3c4);
			t3.addCell(t3c5);
			t3.addCell(t3c6);

			doc.add(t3);
			doc.close();
			writer.close();

			byte[] inputFile = Files.readAllBytes(Paths.get("test.pdf"));
			byte[] encodedBytes = Base64.getEncoder().encode(inputFile);
			String encodedString = new String(encodedBytes);
			return "{\"src\":\"" + encodedString + "\"}";
		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/reportalert", method = RequestMethod.POST)
	public String getReportalert(@RequestBody Req req) {
		Object object = req.getBody();
		Map<String, Object> map = (Map<String, Object>) object;
		System.out.println("map = " + map);
		Document doc = new Document();
		try {
			BaseFont fo = BaseFont.createFont("THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf"));
			doc.open();
			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy", new Locale("th", "TH"));

			PdfPTable t1 = new PdfPTable(1);
			t1.setWidthPercentage(100); // Width 100%
			t1.setSpacingBefore(10f); // Space before table
			t1.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t1cw = { 1f };

			t1.setWidths(t1cw);

			PdfPCell t1c1 = new PdfPCell(new Paragraph("โรงพยาบาลราชวิถี ", new Font(fo, 14)));
			// t1c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c2 = new PdfPCell(new Paragraph("2 แขวงทุ่งพญาไท   เขตราชเทวี ", new Font(fo, 14)));
			// t1c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c3 = new PdfPCell(new Paragraph("กรุงเทพมหานคร  10400", new Font(fo, 14)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c4 = new PdfPCell(new Paragraph(".", new Font(fo, 16, 0, BaseColor.WHITE)));
			t1c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c5 = new PdfPCell(new Paragraph("ใบแจ้งเตือนการจองอุปกรณ์ทางการแพทย์", new Font(fo, 26)));
			t1c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c6 = new PdfPCell(new Paragraph("เรียน  หัวหน้าหน่วยงาน", new Font(fo, 16)));
			t1c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c6.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c7 = new PdfPCell(new Paragraph("เรื่อง  กำหนดส่งคืนอุปกรณ์", new Font(fo, 16)));
			t1c7.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c7.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c8 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t1c8.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c9 = new PdfPCell(new Paragraph("เลขที่ใบยืม " + "Borrow -> borrow_id " + "ยืมโดยคุณ "
					+ "Employee - > emp_name " + "หน่วยงาน " + "Department -> department_name", new Font(fo, 16)));
			t1c9.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c9.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c10 = new PdfPCell(new Paragraph("อุปกรณ์การแพทย์ที่ท่านได้ทำการยืม โดยมี", new Font(fo, 16)));
			t1c10.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c10.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c11 = new PdfPCell(
					new Paragraph("Type -> type_name" + "จำนวน   1  Type -> typr_num", new Font(fo, 16)));
			t1c11.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c11.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c12 = new PdfPCell(new Paragraph(
					"ณ ตอนนี้ อุปกรณ์ที่ท่านยืมไปนั้น เลยกำหนดในการยืมแล้ว ขอให้ท่านนำอุปกรณ์มาคืน", new Font(fo, 16)));
			t1c12.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c12.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c13 = new PdfPCell(
					new Paragraph("หรือ มาทำการยืมใหม่ได้ที่หน่วยงาน อุปกรณ์การแพทย์ ค่ะ ", new Font(fo, 16)));
			t1c13.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c13.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c14 = new PdfPCell(new Paragraph("ขอบคุณมา ณ โอกาสนี้ ", new Font(fo, 16)));
			t1c14.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c14.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c15 = new PdfPCell(new Paragraph(".", new Font(fo, 16, 0, BaseColor.WHITE)));
			t1c15.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c16 = new PdfPCell(new Paragraph(".", new Font(fo, 16, 0, BaseColor.WHITE)));
			t1c16.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c17 = new PdfPCell(new Paragraph("(Employee - > emp_name) ", new Font(fo, 16)));
			t1c17.setVerticalAlignment(Element.ALIGN_LEFT);
			t1c17.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c18 = new PdfPCell(new Paragraph("Department-> department_name ", new Font(fo, 16)));
			t1c18.setVerticalAlignment(Element.ALIGN_LEFT);
			t1c18.setBorder(Rectangle.NO_BORDER);

			t1.addCell(t1c1);
			t1.addCell(t1c2);
			t1.addCell(t1c3);
			t1.addCell(t1c4);
			t1.addCell(t1c5);
			t1.addCell(t1c6);
			t1.addCell(t1c7);
			t1.addCell(t1c8);
			t1.addCell(t1c9);
			t1.addCell(t1c10);
			t1.addCell(t1c11);
			t1.addCell(t1c12);
			t1.addCell(t1c13);
			t1.addCell(t1c14);
			t1.addCell(t1c15);
			t1.addCell(t1c16);
			t1.addCell(t1c17);
			t1.addCell(t1c18);

			doc.add(t1);

			doc.close();
			writer.close();

			byte[] inputFile = Files.readAllBytes(Paths.get("test.pdf"));
			byte[] encodedBytes = Base64.getEncoder().encode(inputFile);
			String encodedString = new String(encodedBytes);
			return "{\"src\":\"" + encodedString + "\"}";
		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
}

package com.example.demo.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
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

import com.example.demo.Model.Borrow;
import com.example.demo.Model.Department;
import com.example.demo.Model.Employee;
import com.example.demo.Model.Position;
import com.example.demo.Model.Req;
import com.example.demo.Model.Res;
import com.example.demo.Model.Type;
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
import com.itextpdf.text.pdf.PdfWriter;

@RestController
public class BorrowController {
	@Autowired
	private BorrowRepossitory borrowRepossitory;
	@Autowired
	private TypeRepossitory typeRepossitory;
	@Autowired
	private EmployeeRepossitory employeeRepossitory;
	@Autowired
	private PositionRepossitory positionRepossitory;
	@Autowired
	private DepartmentRepossitory departmentRepossitory;

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saveborrow", method = RequestMethod.POST)
	public String getsaveBorrow(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			Borrow borrow = new Borrow();
			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			DateFormat sdf2 = new SimpleDateFormat("yy");
			if (map != null && map.size() > 0) {
				if (map.get("borrowId") != null && !"".equals(map.get("borrowId").toString())) {
					Optional<Borrow> borOpt = borrowRepossitory.findById(map.get("borrowId").toString());
					if (borOpt.isPresent()) {
						borrow = borOpt.get();
					}
				} else {
					Integer i = Integer.valueOf(sdf2.format(cal.getTime())) + 43;
					String s = i.toString();

					Iterable<Borrow> iterable = borrowRepossitory.findAll();
					List<Borrow> borrowList = Lists.newArrayList(iterable);

					Integer listSize = borrowList.size();
					listSize = listSize + 1;
					String listStr = listSize.toString();

					if (listStr.length() == 1) {
						listStr = "00" + listStr;
					} else if (listStr.length() == 2) {
						listStr = "0" + listStr;
					}
					borrow.setBorrowId(listStr + "/" + s);

					borrow.setBorrowDate(sdf1.format(cal.getTime()));
					borrow.setEmpId(map.get("userId") != null ? map.get("userId").toString() : "");
					borrow.setTypeId(map.get("typeId") != null ? map.get("typeId").toString() : "");
					borrow.setBorNum(map.get("borNum") != null ? Integer.valueOf(map.get("borNum").toString()) : 0); // borNum
				}
			}
			borrow.setBorrowDate(sdf1.format(cal.getTime()));
			borrow.setStatus("I");
			borrowRepossitory.save(borrow);

			Optional<Type> opType = typeRepossitory.findById(borrow.getTypeId());
			Type type = new Type();
			if (opType.isPresent()) {
				type = opType.get();
				if (type != null) {
					Integer borrowNum = type.getTypeBorrow() + borrow.getBorNum();
					type.setTypeBorrow(borrowNum);
					typeRepossitory.save(type);
				}
			}

			String pdf = getBorrowDoc(borrow);
			return pdf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savebooking", method = RequestMethod.POST)
	public String getsaveBooking(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			Borrow borrow = new Borrow();

			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			DateFormat sdf2 = new SimpleDateFormat("yy");
			Integer i = Integer.valueOf(sdf2.format(cal.getTime())) + 43;
			String s = i.toString();

			Iterable<Borrow> iterable = borrowRepossitory.findAll();
			List<Borrow> borrowList = Lists.newArrayList(iterable);

			Integer listSize = borrowList.size();
			listSize = listSize + 1;
			String listStr = listSize.toString();

			if (listStr.length() == 1) {
				listStr = "00" + listStr;
			} else if (listStr.length() == 2) {
				listStr = "0" + listStr;
			}

			borrow.setBorrowId(listStr + "/" + s);
			borrow.setBorrowDate(sdf1.format(cal.getTime()));
			borrow.setEmpId(map.get("userId") != null ? map.get("userId").toString() : "");
			borrow.setTypeId(map.get("typeId") != null ? map.get("typeId").toString() : "");
			borrow.setBorNum(map.get("bookNum") != null ? Integer.valueOf(map.get("bookNum").toString()) : 0); // borNum
			borrow.setStatus("W");
			borrowRepossitory.save(borrow);

			Optional<Type> opType = typeRepossitory.findById(borrow.getTypeId());
			Type type = new Type();
			if (opType.isPresent()) {
				type = opType.get();
				if (type != null) {
					Integer bookNum = type.getTypeBooking() + borrow.getBorNum();
					type.setTypeBooking(bookNum);
					typeRepossitory.save(type);
				}
			}

			String pdf = getBookingDoc(borrow);
			return pdf;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/saverevert", method = RequestMethod.POST)
	public String getsaveRevert(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			Borrow borrow = new Borrow();

			String borrowId = map.get("borrowId") != null ? map.get("borrowId").toString() : "";
			Optional<Borrow> borOpt = borrowRepossitory.findById(borrowId);
			if (borOpt.isPresent()) {
				borrow = borOpt.get();
				if (borrow != null) {
					Calendar cal = Calendar.getInstance();
					DateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
					borrow.setRevertDate(sdf1.format(cal.getTime()));
					borrow.setStatus("C");
					borrowRepossitory.save(borrow);

					Optional<Type> opType = typeRepossitory.findById(borrow.getTypeId());
					Type type = new Type();
					if (opType.isPresent()) {
						type = opType.get();
						if (type != null) {
							Integer borrowNum = type.getTypeBorrow() - borrow.getBorNum();
							type.setTypeBorrow(borrowNum);
							typeRepossitory.save(type);

							Iterable<Borrow> iterable = borrowRepossitory.findAll();
							List<Borrow> borrowList = Lists.newArrayList(iterable);

							for (Borrow b : borrowList) {
								if ("N".equalsIgnoreCase(b.getStatus())) {
									break;
								}
								Integer remain = type.getTypeTotal() - type.getTypeBorrow();
								Integer revate = type.getTypeRevert() - type.getTypeBorrow();
								if ("W".equalsIgnoreCase(b.getStatus()) && borrow.getTypeId().equals(type.getTypeId())
										&& (remain >= b.getBorNum()))

									if ("W".equalsIgnoreCase(b.getStatus())
											&& borrow.getTypeId().equals(type.getTypeId())
											&& (revate >= b.getBorRevert())) {

										b.setStatus("N");
										borrowRepossitory.save(b);
										break;
									}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getBorrowDoc(Borrow borrow) {
		Document doc = new Document();
		try {
			BaseFont fo = BaseFont.createFont("THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf"));
			doc.open();

			Optional<Employee> employees = employeeRepossitory.findById(borrow.getEmpId());
			Employee employee = employees.isPresent() ? employees.get() : null;

			Optional<Position> positions = positionRepossitory.findById(employee.getPositionId());
			Position position = positions.isPresent() ? positions.get() : null;

			Optional<Department> departments = departmentRepossitory.findById(employee.getDepartmentId());
			Department department = departments.isPresent() ? departments.get() : null;

			Optional<Type> types = typeRepossitory.findById(borrow.getTypeId());
			Type type = types.isPresent() ? types.get() : null;

			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy", new Locale("th", "TH"));

			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, Integer.valueOf(type.getBorrowing()));

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

			PdfPCell t1c5 = new PdfPCell(new Paragraph("ใบยืมอุปกรณ์ทางการแพทย์", new Font(fo, 26)));
			t1c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c6 = new PdfPCell(new Paragraph("ชื่อ " + employee.getEmpName(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c6.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c7 = new PdfPCell(new Paragraph("แผนก " + department.getDepartmentName(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c7.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c8 = new PdfPCell(new Paragraph("ตำแหน่ง " + position.getPositionName(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c8.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c9 = new PdfPCell(new Paragraph("เลขที่ใบยืม " + borrow.getBorrowId(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c9.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c9.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c10 = new PdfPCell(new Paragraph("วันที่ยืม " + sdf1.format(cal.getTime()), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c10.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c10.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c11 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t1c11.setBorder(Rectangle.NO_BORDER);

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

			doc.add(t1);

			PdfPTable t2 = new PdfPTable(5);
			t2.setSpacingBefore(10f);
			t2.setSpacingAfter(10f);
			float[] t2cw = { 1f, 2f, 1f, 1f, 2f };

			t2.setWidths(t2cw);

			PdfPCell t2c1 = new PdfPCell(new Paragraph("ลำดับ ", new Font(fo, 14)));
			t2c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c2 = new PdfPCell(new Paragraph("รายการ / Serial No", new Font(fo, 14)));
			t2c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c3 = new PdfPCell(new Paragraph("จำนวนที่ยืม", new Font(fo, 14)));
			t2c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c3.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c4 = new PdfPCell(new Paragraph("หน่วย", new Font(fo, 14)));
			t2c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c4.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c5 = new PdfPCell(new Paragraph("วันที่ต้องคืน", new Font(fo, 14)));
			t2c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c5.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c6 = new PdfPCell(new Paragraph("1", new Font(fo, 14)));
			t2c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c6.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c7 = new PdfPCell(
					new Paragraph(type.getTypeName() + " / " + type.getTypeId(), new Font(fo, 14)));
//			t2c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c7.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c8 = new PdfPCell(new Paragraph(borrow.getBorNum().toString(), new Font(fo, 14)));
			t2c8.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c8.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c9 = new PdfPCell(new Paragraph(type.getTypeNum(), new Font(fo, 14)));
			t2c9.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c9.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c10 = new PdfPCell(new Paragraph(sdf1.format(cal2.getTime()), new Font(fo, 14)));
//			t2c10.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c10.setVerticalAlignment(Element.ALIGN_MIDDLE);

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

			doc.add(t2);

			PdfPTable t3 = new PdfPTable(1);
			t3.setWidthPercentage(100); // Width 100%
			t3.setSpacingBefore(10f); // Space before table
			t3.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t3cw = { 1f };

			t3.setWidths(t3cw);

			PdfPCell t3c1 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t3c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t3c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c2 = new PdfPCell(
					new Paragraph("ได้รับตามจำนวนและรายการที่ยืมเรียบร้อยแล้ว ", new Font(fo, 14)));
//			t3c2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			t3c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t3c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c3 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t3c3.setBorder(Rectangle.NO_BORDER);

			t3.addCell(t3c1);
			t3.addCell(t3c2);
			t3.addCell(t3c3);

			doc.add(t3);

			PdfPTable t4 = new PdfPTable(2);
			t4.setWidthPercentage(100); // Width 100%
			t4.setSpacingBefore(10f); // Space before table
			t4.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t4cw = { 1f, 1f };

			t4.setWidths(t4cw);

			PdfPCell t4c1 = new PdfPCell(new Paragraph("ผู้ยืม", new Font(fo, 16)));
			t4c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c1.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c2 = new PdfPCell(new Paragraph("ผู้จ่ายอุปกรณ์", new Font(fo, 16)));
			t4c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c2.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c3 = new PdfPCell(new Paragraph("........................", new Font(fo, 14)));
			t4c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c3.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c4 = new PdfPCell(new Paragraph("........................", new Font(fo, 14)));
			t4c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c4.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c5 = new PdfPCell(new Paragraph(employee.getEmpName(), new Font(fo, 14)));
			t4c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c6 = new PdfPCell(new Paragraph("(_________________________)", new Font(fo, 14)));
			t4c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c6.setBorder(Rectangle.NO_BORDER);

			t4.addCell(t4c1);
			t4.addCell(t4c2);
			t4.addCell(t4c3);
			t4.addCell(t4c4);
			t4.addCell(t4c5);
			t4.addCell(t4c6);

			doc.add(t4);
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

	private String getBookingDoc(Borrow booking) {
		Document doc = new Document();
		try {
			BaseFont fo = BaseFont.createFont("THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf"));
			doc.open();

			Optional<Employee> employees = employeeRepossitory.findById(booking.getEmpId());
			Employee employee = employees.isPresent() ? employees.get() : null;

			Optional<Position> positions = positionRepossitory.findById(employee.getPositionId());
			Position position = positions.isPresent() ? positions.get() : null;

			Optional<Department> departments = departmentRepossitory.findById(employee.getDepartmentId());
			Department department = departments.isPresent() ? departments.get() : null;

			Optional<Type> types = typeRepossitory.findById(booking.getTypeId());
			Type type = types.isPresent() ? types.get() : null;

			Calendar cal = Calendar.getInstance();
			DateFormat sdf1 = new SimpleDateFormat("dd MMMM yyyy", new Locale("th", "TH"));

			Calendar cal2 = Calendar.getInstance();
			cal2.add(Calendar.DATE, Integer.valueOf(type.getBorrowing()));

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

			PdfPCell t1c5 = new PdfPCell(new Paragraph("ใบจองอุปกรณ์ทางการแพทย์", new Font(fo, 26)));
			t1c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t1c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c6 = new PdfPCell(new Paragraph("ชื่อ  " + employee.getEmpName(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c6.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c7 = new PdfPCell(new Paragraph("แผนก   " + department.getDepartmentName(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c7.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c8 = new PdfPCell(new Paragraph("ตำแหน่ง  " + position.getPositionName(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c8.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c8.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c9 = new PdfPCell(new Paragraph("เลขที่จอง  " + booking.getBorrowId(), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c9.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c9.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c10 = new PdfPCell(new Paragraph("วันที่จอง  " + sdf1.format(cal.getTime()), new Font(fo, 16)));
			// t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c10.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c10.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c11 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t1c11.setBorder(Rectangle.NO_BORDER);

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

			doc.add(t1);

			PdfPTable t2 = new PdfPTable(5);
			t2.setSpacingBefore(10f);
			t2.setSpacingAfter(10f);
			float[] t2cw = { 1f, 2f, 1f, 1f, 2f };

			t2.setWidths(t2cw);

			PdfPCell t2c1 = new PdfPCell(new Paragraph("ลำดับ ", new Font(fo, 14)));
			t2c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c1.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c2 = new PdfPCell(new Paragraph("รายการ/Serial No", new Font(fo, 14)));
			t2c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c2.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c3 = new PdfPCell(new Paragraph("จำนวนที่จอง", new Font(fo, 14)));
			t2c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c3.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c4 = new PdfPCell(new Paragraph("หน่วย", new Font(fo, 14)));
			t2c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c4.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c5 = new PdfPCell(new Paragraph("วันที่ต้องคืน", new Font(fo, 14)));
			t2c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c5.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c6 = new PdfPCell(new Paragraph("1", new Font(fo, 14)));
			t2c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c6.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c7 = new PdfPCell(
					new Paragraph(type.getTypeName() + " / " + type.getTypeId(), new Font(fo, 14)));
//			t2c7.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c7.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c8 = new PdfPCell(new Paragraph(booking.getBorNum().toString(), new Font(fo, 14)));
			t2c8.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c8.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c9 = new PdfPCell(new Paragraph(type.getTypeNum(), new Font(fo, 14)));
			t2c9.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c9.setVerticalAlignment(Element.ALIGN_MIDDLE);

			PdfPCell t2c10 = new PdfPCell(new Paragraph(sdf1.format(cal2.getTime()), new Font(fo, 14)));
//			t2c10.setHorizontalAlignment(Element.ALIGN_CENTER);
			t2c10.setVerticalAlignment(Element.ALIGN_MIDDLE);

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

			doc.add(t2);

			PdfPTable t3 = new PdfPTable(1);
			t3.setWidthPercentage(100); // Width 100%
			t3.setSpacingBefore(10f); // Space before table
			t3.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t3cw = { 1f };

			t3.setWidths(t3cw);

			PdfPCell t3c1 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t3c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t3c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c2 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
//			t3c2.setHorizontalAlignment(Element.ALIGN_MIDDLE);
			t3c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t3c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t3c3 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t3c3.setBorder(Rectangle.NO_BORDER);

			t3.addCell(t3c1);
			t3.addCell(t3c2);
			t3.addCell(t3c3);

			doc.add(t3);

			PdfPTable t4 = new PdfPTable(2);
			t4.setWidthPercentage(100); // Width 100%
			t4.setSpacingBefore(10f); // Space before table
			t4.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t4cw = { 1f, 1f };

			t4.setWidths(t4cw);

			PdfPCell t4c1 = new PdfPCell(new Paragraph("ผู้จอง", new Font(fo, 16)));
			t4c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c1.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c2 = new PdfPCell(new Paragraph("ผู้ตรวจสอบ", new Font(fo, 16)));
			t4c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c2.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c3 = new PdfPCell(new Paragraph("........................", new Font(fo, 14)));
			t4c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c3.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c4 = new PdfPCell(new Paragraph("........................", new Font(fo, 14)));
			t4c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c4.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c5 = new PdfPCell(new Paragraph(employee.getEmpName(), new Font(fo, 14)));
			t4c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c5.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c6 = new PdfPCell(new Paragraph("(_________________________)", new Font(fo, 14)));
			t4c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c6.setVerticalAlignment(Element.ALIGN_CENTER);
			t4c6.setBorder(Rectangle.NO_BORDER);

			t4.addCell(t4c1);
			t4.addCell(t4c2);
			t4.addCell(t4c3);
			t4.addCell(t4c4);
			t4.addCell(t4c5);
			t4.addCell(t4c6);

			doc.add(t4);
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
	@RequestMapping(value = "/getbooking", method = RequestMethod.POST)
	private List<Res> getBooking(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			String userId = map.get("username").toString();
			Iterable<Borrow> bowIte = borrowRepossitory.findAll();
			List<Borrow> borrows = Lists.newArrayList(bowIte);
			List<Res> list = new ArrayList<Res>();
			for (Borrow borrow : borrows) {
				Res res = new Res();
				if (("N".equals(borrow.getStatus()) || "W".equals(borrow.getStatus()))
						&& borrow.getEmpId().equals(userId)) {
					res.setBorrowId(borrow.getBorrowId());
					res.setBorNum(borrow.getBorNum());
					Optional<Type> tOpt = typeRepossitory.findById(borrow.getTypeId());
					Type type = new Type();
					if (tOpt.isPresent()) {
						type = tOpt.get();
					}
					res.setTypeName(type.getTypeName());
					res.setTypeRemain(type.getTypeTotal() - type.getTypeBorrow());
					res.setTypeNum(type.getTypeNum());
					res.setTypeId(type.getTypeId());
					list.add(res);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getrevert", method = RequestMethod.POST)
	private List<Res> getRevert(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			Iterable<Borrow> bowIte = borrowRepossitory.findAll();
			List<Borrow> borrows = Lists.newArrayList(bowIte);
			List<Res> list = new ArrayList<Res>();
			for (Borrow borrow : borrows) {
				Res res = new Res();
				if ("I".equals(borrow.getStatus())) {
					res.setBorrowId(borrow.getBorrowId());
					res.setBorNum(borrow.getBorNum());
					String borDate = borrow.getBorrowDate();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat sdf1 = new SimpleDateFormat("dd.MM.yyyy");
					Calendar cal = Calendar.getInstance();
					Calendar cal1 = Calendar.getInstance();
					cal.setTime(sdf.parse(borDate));
					cal1.setTime(sdf.parse(borDate));

					Optional<Type> tOpt = typeRepossitory.findById(borrow.getTypeId());
					Type type = new Type();
					if (tOpt.isPresent()) {
						type = tOpt.get();
						cal.add(Calendar.DATE, type.getBorrowing());
					}
					res.setReturnDate(sdf1.format(cal.getTime()));
					res.setBorrowDate(sdf1.format(cal1.getTime()));
					res.setTypeName(type.getTypeName());
					res.setTypeNum(type.getTypeNum());
					res.setTypeId(type.getTypeId());
					list.add(res);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

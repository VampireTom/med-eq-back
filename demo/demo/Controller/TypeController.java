package com.example.demo.Controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.glassfish.jersey.internal.guava.Lists;
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
import com.example.demo.Model.Type;
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
public class TypeController {
	@Autowired
	private TypeRepossitory typeRepossitory;

	@Autowired
	private PositionRepossitory positionRepossitory;

	@Autowired
	private EmployeeRepossitory employeeRepossitory;

	@RequestMapping(value = "/main", method = RequestMethod.POST)
	public List<Res> getMain(@RequestBody Req req) {
		try {
			Iterable<Type> iterable = typeRepossitory.findAll();
			List<Type> tyList = Lists.newArrayList(iterable);
			List<Res> resList = new ArrayList<Res>();
			for (Type type : tyList) {
				Res res = new Res();
				Integer borrowNo = type.getTypeBorrow();
				Integer totalNo = type.getTypeTotal();
				if (totalNo - borrowNo > 0) {
					res.setStatus("y");
				} else {
					res.setStatus("n");
				}
				res.setTypeRemain(totalNo - borrowNo);
				res.setTypeBooking(type.getTypeBooking());
				res.setTypeBorrow(type.getTypeBorrow());
				res.setTypeFile(type.getTypeFile());
				res.setTypeId(type.getTypeId());
				res.setTypeName(type.getTypeName());
				res.setTypeTotal(type.getTypeTotal());
				res.setTypeNum(type.getTypeNum());

				resList.add(res);
			}
			return resList;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/gettype", method = RequestMethod.POST)
	public Type getTypeById(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			String typeId = map.get("typeId") != null ? map.get("typeId").toString() : "";
			Optional<Type> optional = typeRepossitory.findById(typeId);
			Type type = new Type();
			if(optional.isPresent()) {
				type = optional.get();
			}
			return type;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/report1", method = RequestMethod.POST)
	private String getReport() {
		Document doc = new Document();
		try {
			BaseFont fo = BaseFont.createFont("THSarabunNew.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream("test.pdf"));
			doc.open();

			PdfPTable t1 = new PdfPTable(5);
			t1.setWidthPercentage(100); // Width 100%
			t1.setSpacingBefore(10f); // Space before table
			t1.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t1cw = { 1f, 1f, 1f, 1f, 1f };

			t1.setWidths(t1cw);

			PdfPCell t1c1 = new PdfPCell(new Paragraph("โรงพยาบาลราชวิถี ", new Font(fo, 14)));
//			t1c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c2 = new PdfPCell(new Paragraph("2 แขวงทุ่งพญาไท   เขตราชเทวี ", new Font(fo, 14)));
//			t1c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c3 = new PdfPCell(new Paragraph("กรุงเทพมหานคร  10400", new Font(fo, 14)));
//			t1c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c4 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t1c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t1c5 = new PdfPCell(new Paragraph("ใบยืม อุปกรณ์ทางการแพทย์", new Font(fo, 26)));
			t1c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t1c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t1c5.setBorder(Rectangle.NO_BORDER);

			t1.addCell(t1c1);
			t1.addCell(t1c2);
			t1.addCell(t1c3);
			t1.addCell(t1c4);
			t1.addCell(t1c5);

			doc.add(t1);

			PdfPTable t2 = new PdfPTable(8);
			t2.setWidthPercentage(100); // Width 100%
			t2.setSpacingBefore(10f); // Space before table
			t2.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t2cw = { 1f, 1f, 1f, 1f, 1f, 1f, 1f, 1f  };

			t2.setWidths(t2cw);

			PdfPCell t2c1 = new PdfPCell(new Paragraph("ชื่อ", new Font(fo, 14)));
			t2c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t2c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c2 = new PdfPCell(new Paragraph("เลขที่ใบยืม", new Font(fo, 14)));
			t2c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t2c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c3 = new PdfPCell(new Paragraph("นามสกุล", new Font(fo, 14)));
			t2c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t2c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c4 = new PdfPCell(new Paragraph("วันที่ยยืม", new Font(fo, 14)));
			t2c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c5 = new PdfPCell(new Paragraph("ตำแหน่ง", new Font(fo, 14)));
			t2c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t2c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c6 = new PdfPCell(new Paragraph("แผนก", new Font(fo, 14)));
			t2c6.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c7 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t2c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t2c7.setBorder(Rectangle.NO_BORDER);

			PdfPCell t2c8 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
			t2c8.setBorder(Rectangle.NO_BORDER);

			t2.addCell(t2c1);
			t2.addCell(t2c2);
			t2.addCell(t2c3);
			t2.addCell(t2c4);
			t2.addCell(t2c5);
			t2.addCell(t2c6);
			t2.addCell(t2c7);
			t2.addCell(t2c8);

			doc.add(t2);

//			Iterable<Position> iterable = positionRepossitory.findAll();
//
//			List<Position> list = Lists.newArrayList(iterable);
//
//			Optional<Employee> optional = employeeRepossitory.findById("22222");
//			Employee employee = new Employee();
//			if (optional.isPresent()) {
//				employee = optional.get();
//			}
//
//			String depId = employee.getDepartmentId();
//			String posId = employee.getPositionId();
//
//			PdfPTable t3 = new PdfPTable(2); // 2 คอลั่ม t3.setWidthPercentage(100);
//			t3.setSpacingBefore(10f);
//			t3.setSpacingAfter(10f);
//			float[] t3cw = { 1f, 2f, 1f, 1f, 2f };
//
//			t3.setWidths(t3cw);
//
//			PdfPCell t3c1 = new PdfPCell(new Paragraph("ลำดับ ", new Font(fo, 14)));
//			t3c1.setHorizontalAlignment(Element.ALIGN_CENTER);
//			t3c1.setVerticalAlignment(Element.ALIGN_MIDDLE); //
//			t2c1.setBorder(Rectangle.NO_BORDER);
//
//			PdfPCell t3c2 = new PdfPCell(new Paragraph("รายการ", new Font(fo, 14)));
//			t3c2.setHorizontalAlignment(Element.ALIGN_CENTER);
//			t3c2.setVerticalAlignment(Element.ALIGN_MIDDLE); //
//			t2c2.setBorder(Rectangle.NO_BORDER);
//
//			PdfPCell t3c3 = new PdfPCell(new Paragraph("จำนวนที่ยืม", new Font(fo, 14)));
//			t3c2.setHorizontalAlignment(Element.ALIGN_CENTER);
//			t3c2.setVerticalAlignment(Element.ALIGN_MIDDLE); //
//			t2c2.setBorder(Rectangle.NO_BORDER);
//
//			PdfPCell t3c4 = new PdfPCell(new Paragraph("หน่วย", new Font(fo, 14)));
//			t3c2.setHorizontalAlignment(Element.ALIGN_CENTER);
//			t3c2.setVerticalAlignment(Element.ALIGN_MIDDLE); //
//			t2c2.setBorder(Rectangle.NO_BORDER);
//
//			PdfPCell t3c5 = new PdfPCell(new Paragraph("วันที่ต้องคืน", new Font(fo, 14)));
//			t3c2.setHorizontalAlignment(Element.ALIGN_CENTER);
//			t3c2.setVerticalAlignment(Element.ALIGN_MIDDLE); //
//			t2c2.setBorder(Rectangle.NO_BORDER);
//
//			t3.addCell(t3c1);
//			t3.addCell(t3c2);
//			t3.addCell(t3c3);
//			t3.addCell(t3c4);
//			t3.addCell(t3c5);
//
//			for (Position position : list) {
//				PdfPCell t3c6 = new PdfPCell(new Paragraph(position.getPositionId(), new Font(fo, 14)));
//				t3c6.setHorizontalAlignment(Element.ALIGN_CENTER);
//				t3c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//				PdfPCell t3c7 = new PdfPCell(new Paragraph(position.getPositionName(), new Font(fo, 14)));
//				t3c7.setHorizontalAlignment(Element.ALIGN_CENTER);
//				t3c7.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//				t3.addCell(t3c6);
//				t3.addCell(t3c7);
//			}
//
//			doc.add(t3);
			 
			PdfPTable t4 = new PdfPTable(3);
			t4.setWidthPercentage(100); // Width 100%
			t4.setSpacingBefore(10f); // Space before table
			t4.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t4cw = { 1f, 1f, 1f  };

			t4.setWidths(t4cw);

			PdfPCell t4c1 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
		//	t1c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t4c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c2 = new PdfPCell(
					new Paragraph("ได้รับตามจำนวนและรายการที่ยืมเรียบร้อยแล้ว ", new Font(fo, 14)));
			//t4c2.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t4c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t4c3 = new PdfPCell(new Paragraph(".", new Font(fo, 14, 0, BaseColor.WHITE)));
	//		t4c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t4c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t4c3.setBorder(Rectangle.NO_BORDER);

			t4.addCell(t4c1);
			t4.addCell(t4c2);
			t4.addCell(t4c3);

			doc.add(t4);
			
			PdfPTable t5 = new PdfPTable(6);
			t5.setWidthPercentage(100); // Width 100%
			t5.setSpacingBefore(10f); // Space before table
			t5.setSpacingAfter(10f); // Space after table
			// Set Column widths
			float[] t5cw = { 1f, 1f, 1f, 1f, 1f, 1f  };

			t5.setWidths(t5cw);

			PdfPCell t5c1 = new PdfPCell(new Paragraph("ผู้ยืม", new Font(fo, 14)));
			t5c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t5c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t5c1.setBorder(Rectangle.NO_BORDER);

			PdfPCell t5c2 = new PdfPCell(new Paragraph("ผู้จ่ายอุปกรณ์", new Font(fo, 14)));
			t5c1.setHorizontalAlignment(Element.ALIGN_CENTER);
			t5c2.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t5c2.setBorder(Rectangle.NO_BORDER);

			PdfPCell t5c3 = new PdfPCell(new Paragraph(".....", new Font(fo, 14)));
			t5c3.setHorizontalAlignment(Element.ALIGN_CENTER);
			t5c3.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t5c3.setBorder(Rectangle.NO_BORDER);

			PdfPCell t5c4 = new PdfPCell(new Paragraph(".....", new Font(fo, 14)));
			t5c4.setHorizontalAlignment(Element.ALIGN_CENTER);
			t5c4.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t5c4.setBorder(Rectangle.NO_BORDER);

			PdfPCell t5c5 = new PdfPCell(new Paragraph("(                                        )", new Font(fo, 14)));
			t5c5.setHorizontalAlignment(Element.ALIGN_CENTER);
			t5c5.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t5c5.setBorder(Rectangle.NO_BORDER);

			PdfPCell t5c6 = new PdfPCell(new Paragraph("(                                        )", new Font(fo, 14)));
			t5c6.setHorizontalAlignment(Element.ALIGN_CENTER);
			t5c6.setVerticalAlignment(Element.ALIGN_MIDDLE);
			t5c6.setBorder(Rectangle.NO_BORDER);

			doc.add(t5);
			doc.close();
			writer.close();

		} catch (DocumentException de) {
			de.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

}

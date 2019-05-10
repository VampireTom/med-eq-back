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

import com.example.demo.Model.Borrow;
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
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
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
			if (optional.isPresent()) {
				type = optional.get();
			}
			return type;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/savetype", method = RequestMethod.POST)
	public String getsaveType(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);

			Type type = new Type();
			type.setTypeId(map.get("typeId") != null ? map.get("typeId").toString() : "");
			type.setTypeName(map.get("typeName") != null ? map.get("typeName").toString() : "");
			type.setTypeTotal(map.get("typeTotal") != null ? Integer.valueOf(map.get("typeTotal").toString()) : 0);
			type.setTypeNum(map.get("typeNum") != null ? map.get("typeNum").toString() : "");
			type.setBorrowing(map.get("borrowing") != null ? Integer.valueOf(map.get("borrowing").toString()) : 0);

			typeRepossitory.save(type);
			return "true";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edittype", method = RequestMethod.POST)
	public String geteditType(@RequestBody Req req) {
		try {
			Object object = req.getBody();
			Map<String, Object> map = (Map<String, Object>) object;
			System.out.println("map = " + map);
			Type type = new Type();
			String typeId = map.get("typeId") != null ? map.get("typeId").toString() : "";
			Integer typeTotal = map.get("typeTotal") != null ? Integer.valueOf(map.get("typeTotal").toString()) : 0;
			Optional<Type> types = typeRepossitory.findById(typeId);
			//Res res = new Res();
			if (types.isPresent()) {
				type = types.get();
				if (type != null) {
					Integer typeTotal2 = type.getTypeTotal();
					Integer typeTotal3 = typeTotal2 + typeTotal;
					type.setTypeId(map.get("typeId") != null ? map.get("typeId").toString() : "");
					type.setTypeTotal(typeTotal3);
				
				
					typeRepossitory.save(type);
					
			
				//	return "true";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/getalltype", method = RequestMethod.POST)
	public List<Type> getAllType(@RequestBody Req req) {
		try {
			Iterable<Type> iterable = typeRepossitory.findAll();
			List<Type> typeList = Lists.newArrayList(iterable);
			return typeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

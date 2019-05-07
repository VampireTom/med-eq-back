//
//package com.example.demo.Controller;
//
//import java.util.ArrayList;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import org.glassfish.jersey.internal.guava.Lists;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.phoenix.Phoenix.Model.Booking;
//import com.phoenix.Phoenix.Model.Member;
//import com.phoenix.Phoenix.Model.Req;
//import com.phoenix.Phoenix.Model.Res;
//import com.phoenix.Phoenix.Repossitory.BookingRepository;
//
//@RestController
//public class BookingController {
//
//	@Autowired
//	private BookingRepository bookingRepository;
//
//	@SuppressWarnings("unchecked")
//
//	@RequestMapping(value = "/searchbooking", method = RequestMethod.POST)
//	public Booking getBookingById(@RequestBody Req req) {
//
//		Object object = req.getBody();
//		Map<String, Object> map = (Map<String, Object>) object;
//		String bookingId = map.get("keyword").toString();
//		Optional<Booking> optional = bookingRepository.findById(bookingId);
//		Booking booking = new Booking();
//		try {
//			booking = optional.get();
//		} catch (Exception e) {
//		}
//		return booking;
//
//	}
//
//	@RequestMapping(value = "/searchadd", method = RequestMethod.POST)
//	public List<Booking> getBooking(@RequestBody Req req) {
//		String header = req.getHeader() != null ? req.getHeader() : "";
//		if ("admin".equals(header)) {
//			Iterable<Booking> iterable = bookingRepository.findAll();
//			List<Booking> myList = Lists.newArrayList(iterable);
//			List<Booking> resList = new ArrayList<Booking>();
//			try {
//				for (Booking booking : myList) {
//					if ("p".equalsIgnoreCase(booking.getBookingStatus())) {
//						resList.add(booking);
//					}
//				}
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return resList;
//		} else {
//			return null;
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//
//	@RequestMapping(value = "/addbooking", method = RequestMethod.POST)
//	public String booking(@RequestBody Req req) {
//		String header = req.getHeader() != null ? req.getHeader() : "";
//		if ("admin".equals(header)) {
//			System.out.println(req.getBody().toString());
//			Object object = req.getBody();
//			Map<String, Object> map = (Map<String, Object>) object;
//			Booking booking = new Booking();
//
//			booking.setBookingId(map.get("bookingId").toString());
//			booking.setCusName(map.get("cusName").toString());
//			booking.setBookingDate(map.get("bookingDate").toString());
//			booking.setBookingDetail(map.get("bookingDetail").toString());
//			booking.setBookingStatus(map.get("bookingStatus").toString());
//			booking.setProviceId(map.get("proviceId").toString());
//			booking.setCusEmail(map.get("cuseEmail").toString());
//
//			try {
//				bookingRepository.save(booking);
//				return "true";
//			} catch (Exception e) {
//				return "false";
//			}
//		} else {
//			return null;
//
//		}
//
//	}
//
//}

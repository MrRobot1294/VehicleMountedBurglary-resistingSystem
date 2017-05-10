package com.nj.cjy.safecar.main;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.metamodel.relational.Database;

import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.mappings.CarLocation;
import com.nj.cjy.safecar.server.TCPServer;
import com.nj.cjy.safecar.service.CarLocationService;
import com.nj.cjy.safecar.service.CarService;
import com.nj.cjy.safecar.service.UserService;

public class Main {

	public static void main(String[] args) {
		TCPServer tcpServer = (TCPServer)SpringContext.getSpringContext().getBean("TCPServer");
		tcpServer.tcpaccept();
		
//		UserService userService = (UserService) SpringContext.getSpringContext().getBean("UserServiceImpl");
//		try {
//			userService.userSignIn("aaa", "18351936058","123456","user", null);
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
//		try {
//			carService.carLogin("10000", "NO");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		carService.carLogin("10000", "NO");
//		try {
//			carService.carLogin("10001", "NO");
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		String[] a = new String[]{"10000","10001"}; 
//		UserService userService = (UserService) SpringContext.getSpringContext().getBean("UserServiceImpl");
//		
//		try {
//			userService.hasMoreCars("18351936058", a);
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			userService.hasLessCars("18351936058", a);
//		} catch (DatabaseException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		
//		CarLocationService carLocationService = (CarLocationService) SpringContext.getSpringContext().getBean("CarLocationServiceImpl");
//		
//		try {
//			List<CarLocation> carLocations = carLocationService.serachCarlocationBetween("10000", "2017:0:1:16:0", "2017:0:1:19:0", 1);
//			Iterator<CarLocation> iterator = carLocations.iterator();
//			while (iterator.hasNext()) {
//				CarLocation carLocation = (CarLocation) iterator.next();
//				System.out.println(carLocation.toString());
//			}
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		GregorianCalendar date = new GregorianCalendar(2017, 0, 1, 0, 0);
//		try {
//			System.out.println(carLocationService.serachNowCarlocation("10000").toString());
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		double latitude = +31.973901;
//        double longitude = +119.378666;
//        String lo = null;
//        String la = null;
//        GregorianCalendar date = new GregorianCalendar(2017, 0, 1, 0, 0);
//        for(int i = 1; i <= 9; i++) {
//        	lo = new Double(longitude).toString();
//        	la = new Double(latitude).toString();
//			try {
//				carLocationService.addCarLocation("10000", (Calendar)date, "NO", lo, la);
//			} catch (DatabaseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			date.add(Calendar.HOUR_OF_DAY, 1);
//			latitude += (0.01 * i);
//			longitude += (0.01 * i);
//        }
//        
//        for(int i = 1; i <= 9; i++) {
//        	lo = new Double(longitude).toString();
//        	la = new Double(latitude).toString();
//			try {
//				carLocationService.addCarLocation("10000", (Calendar)date, "YES", lo, la);
//			} catch (DatabaseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			date.add(Calendar.HOUR_OF_DAY, 1);
//			latitude += (0.01 * i);
//			longitude += (0.01 * i);
//        }
		
	}
}

package com.nj.cjy.safecar.thread.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;

import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.CarLocation;
import com.nj.cjy.safecar.mappings.User;
import com.nj.cjy.safecar.server.Link;
import com.nj.cjy.safecar.server.UserSocket;
import com.nj.cjy.safecar.service.CarLocationService;
import com.nj.cjy.safecar.service.CarService;
import com.nj.cjy.safecar.service.UserService;

public class SocketThread implements Runnable {

	private String uOrE;
	private String number;
	private UserSocket userSocket;

	public void setSocket(String uOre, String number, UserSocket userSocket) {
		uOrE = uOre;
		this.number = number;
		this.userSocket = userSocket;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		Link link = null;
		String string = null;
		switch (uOrE) {
		case "U": {
			link = userSocket.getUserLink();
			try {
				System.out.println("ccccccccccccccccccc");
				BufferedReader reader = link.getLinkReader();
				while ((string = reader.readLine()) != null) {
					System.out.println(string);
					System.out.println("aaaaaaaaaaaaaaaaa");
					switch (string.charAt(0)) {
					case 'N': {
						String[] strings = string.split(";");
						Car car;
						UserService userService = (UserService) SpringContext.getSpringContext()
								.getBean("UserServiceImpl");
						CarLocationService carLocationService = (CarLocationService) SpringContext.getSpringContext()
								.getBean("CarLocationServiceImpl");
						try {
							car = userService.userHasCar(strings[1], strings[2], strings[3]);
							CarLocation carLocation = carLocationService.serachNowCarlocation(car.getNumber());
							System.out.println(car.toString());

							System.out.println("N;" + car.getNumber() + ";" + carLocation.toString());
							link.getLinkWriter().println("N;" + car.getNumber() + ";" + carLocation.toString());
							link.getLinkWriter().flush();
						} catch (DatabaseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							link.getLinkWriter().println("NOW_CAR_LOCATION_ERROR");
							link.getLinkWriter().flush();
						}
					}
						break;
					case 'H': {
						String[] strings = string.split(";");
						Car car;
						UserService userService = (UserService) SpringContext.getSpringContext()
								.getBean("UserServiceImpl");
						CarLocationService carLocationService = (CarLocationService) SpringContext.getSpringContext()
								.getBean("CarLocationServiceImpl");
						try {
							car = userService.userHasCar(strings[1], strings[2], strings[3]);

							String result = "H;" + car.getNumber() + ";";

							Iterator<CarLocation> iterator = carLocationService.serachCarlocationBetween(
									car.getNumber(), strings[4], strings[5], new Integer(strings[6])).iterator();
							while (iterator.hasNext()) {
								CarLocation carLocation = (CarLocation) iterator.next();
								result += carLocation.toString();
							}
							System.out.println(result);
							link.getLinkWriter().println(result);
							link.getLinkWriter().flush();
						} catch (DatabaseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							link.getLinkWriter().println("HISTORY_CAR_LOCATION_ERROR");
							link.getLinkWriter().flush();
						}
					}
						break;
					case 'C': {
						String[] strings = string.split(";");
						UserService userService = (UserService) SpringContext.getSpringContext()
								.getBean("UserServiceImpl");
						CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
						Car car;
						User user;
						String[] cars = { strings[3] };
						try {
							car = carService.carIsBe(strings[3]);
							if (car.getHost() == null) {
								user = userService.hasMoreCars(strings[1], cars);
							} else {
								user = userService.hasLessCars(strings[1], cars);
							}
							link.getLinkWriter().println(user.toString().replaceFirst("U", "C"));
							link.getLinkWriter().flush();
						} catch (DatabaseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (e.getMessage().equals("无此车辆") || e.getMessage().equals("无此设备")) {
								link.getLinkWriter().println("CARCHANGE_ERROR_NOCAR");
								link.getLinkWriter().flush();
							} else if(e.getMessage().equals("该车已有主人")){
								link.getLinkWriter().println("CARCHANGE_ERROR_CARHASHOST");
								link.getLinkWriter().flush();
							}
						}
					}
						break;
					default:
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				System.out.println("用户" + userSocket.getTelephoneNumber() + "已下线");
				userSocket.setUserLink(null);
				userSocket.setCount(userSocket.getCount() - 1);
				link.linkDestroy();
				link = null;
			}
		}
			break;
		case "E": {
			link = userSocket.getCarSocket().get(number);
			try {
				BufferedReader reader = link.getLinkReader();
				while ((string = reader.readLine()) != null) {
					System.out.println(string);

					switch (string.charAt(0)) {
					case 'A': {
						CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
						CarLocationService carLocationService = (CarLocationService) SpringContext.getSpringContext()
								.getBean("CarLocationServiceImpl");
						Car car;
						try {
							car = carService.carIsBe(string.substring(1, 6));

							Double latitudeDouble = new Double(string.substring(6, 8))
									+ (new Double(string.substring(8, 10))
											+ new Double(string.substring(10, 15)) / 100000) / 60;
							if (string.charAt(15) == 'S') {
								latitudeDouble *= -1;
							}
							Double longitudeDouble = new Double(string.substring(16, 19))
									+ (new Double(string.substring(19, 21))
											+ new Double(string.substring(21, 26)) / 100000) / 60;
							if (string.charAt(26) == 'W') {
								longitudeDouble *= -1;
							}

							Calendar date = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
									Calendar.getInstance().get(Calendar.MONTH),
									Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
									Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
									Calendar.getInstance().get(Calendar.MINUTE) / 5 * 5);
							if (string.charAt(27) == '0') {
								carLocationService.addCarLocation(string.substring(1, 6), date, "NO",
										longitudeDouble.toString(), latitudeDouble.toString());
							} else {
								carLocationService.addCarLocation(string.substring(1, 6), date, "YES",
										longitudeDouble.toString(), latitudeDouble.toString());

							}
						} catch (DatabaseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						break;
					case 'B': {

					}
						break;
					default:
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			} finally {
				System.out.println("设备" + number + "连接已关闭");
				// if(userSocket.getCount() == 1) {
				//
				// }

				userSocket.setUserLink(null);
				userSocket.setCount(userSocket.getCount() - 1);
				link.linkDestroy();
				link = null;
			}
		}
			break;
		default:
			break;
		}
	}

}

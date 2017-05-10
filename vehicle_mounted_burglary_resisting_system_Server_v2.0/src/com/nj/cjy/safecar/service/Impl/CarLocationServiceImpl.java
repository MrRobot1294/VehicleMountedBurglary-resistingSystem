package com.nj.cjy.safecar.service.Impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import com.nj.cjy.safecar.dao.CarDao;
import com.nj.cjy.safecar.dao.CarLocationDao;
import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.CarLocation;
import com.nj.cjy.safecar.service.CarLocationService;
import com.nj.cjy.safecar.service.CarService;

public class CarLocationServiceImpl implements CarLocationService {

	@Override
	public void addCarLocation(String car, Calendar date, String state, String longitude, String latitude)
			throws DatabaseException {
		// TODO Auto-generated method stub
		CarLocationDao carLocationDao = (CarLocationDao) SpringContext.getSpringContext().getBean("CarLocationDaoImpl");

		CarLocation carLocation;

		CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
		Car car1 = carService.carIsBe(car);

		if ((carLocation = carLocationDao.findCarLocationByDate(car, date)) != null) {
			if(carLocation.getState().equals("YES") || state.equals("NO")) {
				throw new DatabaseException("记录已存在");
			} else {
				carLocationDao.carLocationRemove(carLocation);
			}
		}

		// CarDao carDao =
		// (CarDao)SpringContext.getSpringContext().getBean("CarDaoImpl");
		// Car car1;
		// if((car1 = carDao.findCarByNumber(number)))

		if (date == null || state == null || longitude == null || latitude == null) {
			throw new DatabaseException("位置信息等不能为空");
		}
		carLocation = (CarLocation) SpringContext.getSpringContext().getBean("CarLocation");
		carLocation.setCar(car1);
		carLocation.setLatitude(latitude);
		carLocation.setLongitude(longitude);
		carLocation.setState(state);
		carLocation.setDate(date);

		carLocationDao.carLocationAdd(carLocation);
	}

	@Override
	public CarLocation carLocationIsBe(String car, Calendar date) throws DatabaseException {
		// TODO Auto-generated method stub
		CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
		Car car1 = carService.carIsBe(car);

		CarLocationDao carLocationDao = (CarLocationDao) SpringContext.getSpringContext().getBean("CarLocationDaoImpl");
		CarLocation carLocation;
		if ((carLocation = carLocationDao.findCarLocationByDate(car, date)) == null) {
			throw new DatabaseException("无此记录");
		}

		return carLocation;
	}

	@Override
	public CarLocation serachNowCarlocation(String car) throws DatabaseException {
		// TODO Auto-generated method stub
		CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
		Car car1 = carService.carIsBe(car);

		CarLocationDao carLocationDao = (CarLocationDao) SpringContext.getSpringContext().getBean("CarLocationDaoImpl");
		CarLocation carLocation;

		if ((carLocation = carLocationDao.findCarLocationBefore(car, Calendar.getInstance())) == null) {
			throw new DatabaseException("无记录");
		}
		return carLocation;
	}

	@Override
	public List<CarLocation> serachCarlocationBetween(String car, String start, String end, int interval)
			throws DatabaseException {
		// TODO Auto-generated method stub
		String[] starts = start.split(":");
		String[] ends = end.split(":");
		
		CarService carService = (CarService) SpringContext.getSpringContext().getBean("CarServiceImpl");
		Car car1 = carService.carIsBe(car);
		
		Calendar startdate = new GregorianCalendar(new Integer(starts[0]), new Integer(starts[1]),
				new Integer(starts[2]), new Integer(starts[3]), new Integer(starts[4]));
		Calendar enddate = new GregorianCalendar(new Integer(ends[0]), new Integer(ends[1]), new Integer(ends[2]),
				new Integer(ends[3]), new Integer(ends[4]));

		List<Calendar> date = new ArrayList<Calendar>();
		date.add((Calendar)startdate.clone());
		
		switch (interval) {
		case 0: {
			while(startdate.compareTo(enddate) < 0) {
				startdate.add(Calendar.MINUTE, 15);
				Calendar temp = (Calendar) startdate.clone();
				date.add(temp);
			}
		}
			break;
		case 1: {
			while(startdate.compareTo(enddate) < 0) {
				startdate.add(Calendar.HOUR_OF_DAY, 1);
				Calendar temp = (Calendar) startdate.clone();
				date.add(temp);
			}
		}
			break;
		case 2: {
			while(startdate.compareTo(enddate) < 0) {
				startdate.add(Calendar.HOUR_OF_DAY, 12);
				Calendar temp = (Calendar) startdate.clone();
				date.add(temp);
			}
		}
			break;
		case 3: {
			while(startdate.compareTo(enddate) < 0) {
				startdate.add(Calendar.DAY_OF_MONTH, 1);
				Calendar temp = (Calendar) startdate.clone();
				date.add(temp);
			}
		}
			break;
		case 4: {
			while(startdate.compareTo(enddate) < 0) {
				startdate.add(Calendar.DAY_OF_MONTH, 7);
				Calendar temp = (Calendar) startdate.clone();
				date.add(temp);
			}
		}
			break;
		default:
			break;
		}
		CarLocationDao carLocationDao = (CarLocationDao) SpringContext.getSpringContext().getBean("CarLocationDaoImpl");
		List<CarLocation> carLocations = carLocationDao.findCarLocationIn(car, date);
		
		if(carLocations.isEmpty()) {
			throw new DatabaseException("无记录");
		}

		return carLocations;
	}

}

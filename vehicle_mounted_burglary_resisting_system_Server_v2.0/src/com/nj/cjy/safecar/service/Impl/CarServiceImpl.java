package com.nj.cjy.safecar.service.Impl;

import com.nj.cjy.safecar.dao.CarDao;
import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.service.CarService;

public class CarServiceImpl implements CarService{

	@Override
	public void carLogin(String number, String state) throws DatabaseException {
		// TODO Auto-generated method stub
		CarDao carDao = (CarDao) SpringContext.getSpringContext().getBean("CarDaoImpl");
		
		if(carDao.findCarByNumber(number) != null) {
			throw new DatabaseException("该设备已注册");
		}
		
		Car car = (Car) SpringContext.getSpringContext().getBean("Car");
		car.setNumber(number);
		car.setState(state);
		carDao.addCar(car);
	}

	@Override
	public Car carIsBe(String number) throws DatabaseException{
		// TODO Auto-generated method stub
		CarDao carDao = (CarDao) SpringContext.getSpringContext().getBean("CarDaoImpl");
		
		Car car = null;
		if((car = carDao.findCarByNumber(number)) == null) {
			throw new DatabaseException("无此设备");
		}
		return car;

	}

}

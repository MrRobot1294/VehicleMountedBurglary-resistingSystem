package com.nj.cjy.safecar.service.Impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.nj.cjy.safecar.dao.CarDao;
import com.nj.cjy.safecar.dao.UserDao;
import com.nj.cjy.safecar.dao.impl.UserDaoImpl;
import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.User;
import com.nj.cjy.safecar.service.UserService;

public class UserServiceImpl implements UserService{

	@Override
	public void userSignIn(String name, String telePhoneNumber, String password, String identity, String[] cars) throws DatabaseException {
		// TODO Auto-generated method stub
		UserDao userDao = (UserDao) SpringContext.getSpringContext().getBean("UserDaoImpl");
		
		if(userDao.findUserByTelephoneNumber(telePhoneNumber) != null) {
			throw new DatabaseException("���˻���ע��");
		}
		
		User user = (User) SpringContext.getSpringContext().getBean("User");
		user.setName(name);
		user.setTelePhoneNumber(telePhoneNumber);
		if(password == null || identity == null) {
			throw new DatabaseException("���û��������ݲ���Ϊ��");
		}
		user.setPassword(password);
		user.setIdentity(identity);
//		Set<Car> set = new HashSet<>();
//		Car car;
//		for(String carNumber:cars) {
//			car = SpringContext.getSpringContext().getBean(Car.class);
//			car.setNumber(carNumber);
//			car.set
//		}
		
		userDao.addUser(user);
	}

	@Override
	public User hasMoreCars(String telePhoneNumber, String[] cars) throws DatabaseException {
		// TODO Auto-generated method stub
		UserDao userDao = (UserDao)SpringContext.getSpringContext().getBean("UserDaoImpl");
		CarDao carDao = (CarDao) SpringContext.getSpringContext().getBean("CarDaoImpl");
		
		User user;
		if((user = userDao.findUserByTelephoneNumber(telePhoneNumber)) == null) {
			throw new DatabaseException("���˻�δע��");
		}
		
		Iterator<Car> iterator = user.getCars().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next().getNumber());
		}
		
		Set<Car> set = new HashSet<>();
		Car car;
		for(String carNumber:cars) {
			if((car = carDao.findCarByNumber(carNumber)) == null) {
				throw new DatabaseException("�޴˳���");
			}
			if(car.getHost() != null) {
				throw new DatabaseException("�ó���������");
			}
			car.setHost(user);
			user.getCars().add(car);
		}
		return user;
	}

	@Override
	public User hasLessCars(String telePhoneNumber, String[] cars) throws DatabaseException {
		// TODO Auto-generated method stub
		UserDao userDao = (UserDao)SpringContext.getSpringContext().getBean("UserDaoImpl");
		CarDao carDao = (CarDao) SpringContext.getSpringContext().getBean("CarDaoImpl");
		
		User user;
		if((user = userDao.findUserByTelephoneNumber(telePhoneNumber)) == null) {
			throw new DatabaseException("���˻�δע��");
		}
		
//		Car car;
//		Iterator<Car> iterator = user.getCars().iterator();
//		while (iterator.hasNext()) {
//			car = iterator.next();
//			for(String dcar:cars) {
//				if (car.getNumber() == dcar) {
////					user.getCars().remove(car);
//					car.setHost(null);
//				}
//			}
//		}
		
		Car car;
		for(String carNumber:cars) {
			if((car = carDao.findCarByNumber(carNumber)) == null) {
				throw new DatabaseException("�޴˳���");
			}
			if(user.equals(car.getHost()) == false) {
				throw new DatabaseException("���û��޴˳�");
			}
			car.setHost(null);
			user.getCars().remove(car);
		}
		return user;
	}

	@Override
	public User userIsBe(String telePhoneNumber, String password) throws DatabaseException {
		// TODO Auto-generated method stub
		UserDao userDao = (UserDao)SpringContext.getSpringContext().getBean("UserDaoImpl");
		User user;
		
		if((user = userDao.findUserByTelephoneNumber(telePhoneNumber)) == null) {
			throw new DatabaseException("���˻�δע��");
		}
		if(user.getPassword().equals(password) != true) {
			throw new DatabaseException("�������");
		}
		
		return user;
	}

	@Override
	public Car userHasCar(String telePhoneNumber, String password, String car) throws DatabaseException {
		// TODO Auto-generated method stub
		User user = userIsBe(telePhoneNumber, password);
		Car userCar;
		Iterator<Car> iterator = user.getCars().iterator();
		while (iterator.hasNext()) {
			userCar = (Car) iterator.next();
			if(userCar.getNumber().equals(car)) {
				return userCar;
			}
		}
		throw new DatabaseException("���û��޴˳���");
	}

	@Override
	public void userSignUp(String name, String telePhoneNumber, String password, String identity)
			throws DatabaseException {
		// TODO Auto-generated method stub
UserDao userDao = (UserDao) SpringContext.getSpringContext().getBean("UserDaoImpl");
		
		if(userDao.findUserByTelephoneNumber(telePhoneNumber) != null) {
			throw new DatabaseException("���˻���ע��");
		}
		
		User user = (User) SpringContext.getSpringContext().getBean("User");
		
		if(password == null || identity == null || telePhoneNumber == null || name == null) {
			throw new DatabaseException("���û��˺��������ݲ���Ϊ��");
		}
		user.setName(name);
		user.setTelePhoneNumber(telePhoneNumber);
		user.setPassword(password);
		user.setIdentity(identity);
		
		userDao.addUser(user);
	}

}

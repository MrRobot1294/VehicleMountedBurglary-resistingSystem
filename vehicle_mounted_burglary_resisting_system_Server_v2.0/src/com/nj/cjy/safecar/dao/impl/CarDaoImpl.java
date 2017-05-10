package com.nj.cjy.safecar.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.nj.cjy.safecar.dao.CarDao;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.User;

public class CarDaoImpl implements CarDao{

	public Session getSession() {		
		return SpringContext.getSpringContext().getBean(SessionFactory.class).getCurrentSession();
	}
	
	public void addCar(Car car) {
		// TODO Auto-generated method stub
		getSession().save(car);
	}

	@Override
	public Car findCarByNumber(String number) {
		// TODO Auto-generated method stub		
		String hql = "from Car c where c.number = ?";
		Query query = getSession().createQuery(hql).setString(0, number);
		
		return (Car)query.uniqueResult();
	}
}

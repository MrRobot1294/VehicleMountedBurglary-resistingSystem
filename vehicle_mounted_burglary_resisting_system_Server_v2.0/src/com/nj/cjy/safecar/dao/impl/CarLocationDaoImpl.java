package com.nj.cjy.safecar.dao.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.nj.cjy.safecar.dao.CarLocationDao;
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.CarLocation;

public class CarLocationDaoImpl implements CarLocationDao{

	public Session getSession() {		
		return SpringContext.getSpringContext().getBean(SessionFactory.class).getCurrentSession();
	}

	@Override
	public void carLocationAdd(CarLocation carLocation) {
		// TODO Auto-generated method stub
		getSession().save(carLocation);
	}

	@Override
	public CarLocation findCarLocationByDate(String car, Calendar date) {
		// TODO Auto-generated method stub

		String hql = "from CarLocation c where c.car.number = ? and date = ?";
		Query query = getSession().createQuery(hql).setString(0, car).setCalendar(1, date);
		
		return (CarLocation)query.uniqueResult();
	}

	@Override
	public CarLocation findCarLocationBefore(String car, Calendar before) {
		// TODO Auto-generated method stub
		String hql = "select c from CarLocation c where c.car.number = ? and date <= ? order by date desc";
		Query query = getSession().createQuery(hql).setString(0, car).setCalendar(1, before).setFirstResult(0).setMaxResults(1);
		
		return (CarLocation)query.uniqueResult();
	}

	@Override
	public List<CarLocation> findCarLocationIn(String car, List<Calendar> date) {
		// TODO Auto-generated method stub
		String hql = "select c from CarLocation c where c.car.number = :car and date in(:dates) order by date";
		Query query = getSession().createQuery(hql).setString("car", car).setParameterList("dates", date);
		return query.list();
	}

	@Override
	public void carLocationRemove(CarLocation carLocation) {
		// TODO Auto-generated method stub
		getSession().delete(carLocation);
	}
}

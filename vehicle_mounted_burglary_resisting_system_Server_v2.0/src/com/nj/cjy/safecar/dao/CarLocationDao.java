package com.nj.cjy.safecar.dao;

import java.util.Calendar;
import java.util.List;

import com.nj.cjy.safecar.mappings.CarLocation;

public interface CarLocationDao {

	public void carLocationAdd(CarLocation carLocation);
	
	public void carLocationRemove(CarLocation carLocation);
	
	public CarLocation findCarLocationByDate(String car, Calendar date);
	
	public CarLocation findCarLocationBefore(String car, Calendar before);
	
	public List<CarLocation> findCarLocationIn(String car,List<Calendar> date);
}

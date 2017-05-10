package com.nj.cjy.safecar.service;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.CarLocation;

public interface CarLocationService {

	public void addCarLocation(String car,Calendar date,String state,String longitude,String latitude) throws DatabaseException;
	
	public CarLocation carLocationIsBe(String car, Calendar date) throws DatabaseException;
	
	public CarLocation serachNowCarlocation(String car) throws DatabaseException;
	
	public List<CarLocation> serachCarlocationBetween(String car, String start, String end, int interval) throws DatabaseException;
}

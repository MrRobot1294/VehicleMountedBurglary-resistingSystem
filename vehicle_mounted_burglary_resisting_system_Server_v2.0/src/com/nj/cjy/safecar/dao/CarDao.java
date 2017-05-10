package com.nj.cjy.safecar.dao;

import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.User;

public interface CarDao {

	public void addCar(Car car);
	
	public Car findCarByNumber(String number);
}

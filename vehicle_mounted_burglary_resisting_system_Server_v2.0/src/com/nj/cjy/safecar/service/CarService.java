package com.nj.cjy.safecar.service;

import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.mappings.Car;

public interface CarService {

	public void carLogin(String number,String state) throws DatabaseException;
	
	public Car carIsBe(String number) throws DatabaseException;
}

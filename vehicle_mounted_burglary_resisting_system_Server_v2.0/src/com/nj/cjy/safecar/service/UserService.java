package com.nj.cjy.safecar.service;

import java.util.Set;

import com.nj.cjy.safecar.exception.DatabaseException;
import com.nj.cjy.safecar.mappings.Car;
import com.nj.cjy.safecar.mappings.User;

public interface UserService {

	public void userSignIn(String name, String telePhoneNumber, String password, String identity, String[] cars) throws DatabaseException;
	
	public void userSignUp(String name, String telePhoneNumber, String password, String identity) throws DatabaseException;
	
	public User hasMoreCars(String telePhoneNumber,String[] cars) throws DatabaseException;
	
	public User hasLessCars(String telePhoneNumber,String[] cars) throws DatabaseException;
	
	public User userIsBe(String telePhoneNumber,String password) throws DatabaseException;
	
	public Car userHasCar(String telePhoneNumber,String password,String car) throws DatabaseException;
}

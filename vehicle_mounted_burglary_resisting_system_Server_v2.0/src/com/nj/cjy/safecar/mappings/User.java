package com.nj.cjy.safecar.mappings;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class User {

	private int id;
	private String name;
	private String telePhoneNumber;
	private String password;
	private String identity;
	private Set<Car> cars = new HashSet<Car>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTelePhoneNumber() {
		return telePhoneNumber;
	}
	public void setTelePhoneNumber(String telePhoneNumber) {
		this.telePhoneNumber = telePhoneNumber;
	}
	public Set<Car> getCars() {
		return cars;
	}
	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getIdentity() {
		return identity;
	}
	public void setIdentity(String identity) {
		this.identity = identity;
	}
	public boolean equals(User user) {
		if (this == user)
			return true;
		if (user == null)
			return false;
		if (getClass() != user.getClass())
			return false;
//		if (id != user.id)
//			return false;
		if (!telePhoneNumber.equals(user.telePhoneNumber))
			return false;
		if (!password.equals(user.password))
			return false;
		if (!identity.equals(user.identity))
			return false;
		return true;
	}
	@Override
	public String toString() {
		String string = "";
		Car car;
		Iterator<Car> iterator = cars.iterator();
		while (iterator.hasNext()) {
			car = (Car) iterator.next();
			string = string + car.getNumber() + ":" + car.getState() + ";";	
		}
		return "U;" + telePhoneNumber + ";" + password + ";" + name + ";"
				+ identity + ";" + string;
	}
}

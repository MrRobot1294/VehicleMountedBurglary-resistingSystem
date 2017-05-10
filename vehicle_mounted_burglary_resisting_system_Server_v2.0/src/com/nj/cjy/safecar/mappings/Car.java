package com.nj.cjy.safecar.mappings;

import java.util.HashSet;
import java.util.Set;

public class Car {
	private int id;
	private String number;
	private String state;
	private User host;
	private Set<CarLocation> locations = new HashSet<CarLocation>();
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public User getHost() {
		return host;
	}
	public void setHost(User host) {
		this.host = host;
	}
	public Set<CarLocation> getLocations() {
		return locations;
	}
	public void setLocations(Set<CarLocation> locations) {
		this.locations = locations;
	}

	public boolean equals(Car car) {
		if (this == car)
			return true;
		if (car == null)
			return false;
		if (getClass() != car.getClass())
			return false;
//		if (id != car.getId())
//			return false;
		if (!number.equals(car.number))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Car [number=" + number + ", state=" + state + "]";
	}
}

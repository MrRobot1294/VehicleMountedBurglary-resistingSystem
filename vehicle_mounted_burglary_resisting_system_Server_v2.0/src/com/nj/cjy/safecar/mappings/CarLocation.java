package com.nj.cjy.safecar.mappings;

import java.util.Calendar;
import java.util.Date;

public class CarLocation {
	private int id;
	private Car car;
	private Calendar date;
	private String state;
	private String longitude;
	private String latitude;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public boolean equals(CarLocation carLocation) {
		if (this == carLocation)
			return true;
		if (carLocation == null)
			return false;
		if (getClass() != carLocation.getClass())
			return false;
		// if (id != carLocation.getId())
		// return false;
		if (!car.equals(car)) {
			return false;
		} else {
			if (!date.equals(carLocation.getDate())) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		// String string = date.toString() + ":" + state + ":" + longitude + ":"
		// + latitude;
		return state + ":" + date.get(Calendar.YEAR) + ":" + date.get(Calendar.MONTH) + ":" + date.get(Calendar.DAY_OF_MONTH)
				+ ":" + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + longitude
				+ ":" + latitude + ";";
	}
}

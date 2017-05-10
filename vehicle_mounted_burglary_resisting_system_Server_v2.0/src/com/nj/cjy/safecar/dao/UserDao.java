package com.nj.cjy.safecar.dao;

import com.nj.cjy.safecar.mappings.User;

public interface UserDao {

	public void addUser(User user);
	
	public User findUserByTelephoneNumber(String telephoneNumber);
}

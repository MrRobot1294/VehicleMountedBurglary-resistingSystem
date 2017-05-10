package com.nj.cjy.safecar.dao.impl;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.nj.cjy.safecar.dao.UserDao;	
import com.nj.cjy.safecar.main.SpringContext;
import com.nj.cjy.safecar.mappings.User;

public class UserDaoImpl implements UserDao{

	public Session getSession() {		
		return SpringContext.getSpringContext().getBean(SessionFactory.class).getCurrentSession();
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		getSession().save(user);
	}
	
	public User findUserByTelephoneNumber(String telephoneNumber) {
		String hql = "from User u where u.telePhoneNumber = ?";
		Query query = getSession().createQuery(hql).setString(0, telephoneNumber);
		
		return (User)query.uniqueResult();
	}
	
	
}

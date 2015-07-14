package net.laochu.design.dao;

import net.laochu.design.model.User;

public interface UserDao {
	public User findUserByUserName(String userName);
}

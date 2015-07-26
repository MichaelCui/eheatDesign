package net.laochu.design.serviceImpl;

import net.laochu.design.model.User;
import net.laochu.design.service.UserService;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	
//	@Autowired
//	private UserDao userDao;
	
	public User loginCheck(User user) {
		
//		return userDao.findUserByUserName("test");
		return null;
	}

}
	
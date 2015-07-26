package net.laochu.design.daoImpl;

import java.sql.ResultSet;
import java.sql.SQLException;

import net.laochu.design.dao.UserDao;
import net.laochu.design.model.User;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;


//@Repository
public class UserDaoImpl implements UserDao{

//	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Override
	public User findUserByUserName(String userName) {
		 String sqlStr = "select * from heatingwire";
	        final User user = new User();
	        jdbcTemplate.query(sqlStr, new RowCallbackHandler() {
	            @Override
	            public void processRow(ResultSet rs) throws SQLException {
	                user.setId(rs.getInt("idx"));
	            }
	        });
	        System.out.println(user.getId());
	        return user;
	}


}

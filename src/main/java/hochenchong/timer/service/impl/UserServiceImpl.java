package hochenchong.timer.service.impl;

import hochenchong.timer.domain.User;
import hochenchong.timer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(String userId) {
        List<User> users = jdbcTemplate.query("select * from user where user_id = ?", rs -> {
            List<User> list = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
                list.add(user);
            }
            return list;
        }, userId);
        return !ObjectUtils.isEmpty(users) ? users.get(0) : null;
    }
}

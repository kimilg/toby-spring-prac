package springbook.user.dao;

import springbook.user.domain.User;

import java.util.List;

/**
 * @author user
 */
public interface UserDao {
    List<User> getAll();
    User get(String id);
    void delete(String id);
    void add(User user);
    void deleteAll();
    int getCount();
    void update(User user);
}

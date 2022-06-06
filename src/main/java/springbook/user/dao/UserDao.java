package springbook.user.dao;

import springbook.user.domain.User;

import java.util.List;

/**
 * @author user
 */
public interface UserDao {
    public List<User> getAll();
    public User get(String id);
    public void delete(String id);
    public void add(User user);
    public void deleteAll();
    public int getCount();
}

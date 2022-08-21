package springbook.user.service;

import org.springframework.transaction.annotation.Transactional;
import springbook.user.domain.User;

import java.util.List;

/**
 * @author Ilgoo.Kim
 */
@Transactional
public interface UserService {
    void add(User user);
    void deleteAll();
    void delete(String id);
    void update(User user);
    void upgradeLevels();
    @Transactional(readOnly = true)
    User get(String id);
    @Transactional(readOnly = true)
    List<User> getAll();
}

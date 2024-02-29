package err.anas.chatyou.dao;

import err.anas.chatyou.dao.entities.User;
import java.util.List;

public interface UserDao extends Dao<User,Integer>{
    public List<User> searchUserByQuery(String query);
    public User findbyNameAndPassword(String username,String password);
}

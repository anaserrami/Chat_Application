package err.anas.chatyou.service;

import err.anas.chatyou.dao.entities.User;
import java.util.List;

public interface IUserService {

    public void addUser(User c);
    public void deleteUserBy(Integer id);
    public List<User> getAllUsers();
    public User getUserbyId(int id);
    public void updateUser(User c);
    public List<User> searchUserByQuery(String query);
    public User findUserbyNameAndPassword(String username,String password );

}
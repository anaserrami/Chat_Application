package err.anas.chatyou.service;

import err.anas.chatyou.dao.UserDao;
import err.anas.chatyou.dao.entities.User;
import java.util.List;

public class IServiceUserImpl implements IUserService {
    UserDao userDao;

    public IServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void addUser(User c) {
        userDao.save(c);
    }

    public User findUserbyNameAndPassword(String username,String password ){
        return userDao.findbyNameAndPassword(username,password);
    }
    public User getUserbyId(int id){
        return  userDao.getById(id);
    };

    @Override
    public void deleteUserBy(Integer id) {
        userDao.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    @Override
    public void updateUser(User c) {
        userDao.update(c);
    }

    @Override
    public List<User> searchUserByQuery(String query) {
        return  userDao.searchUserByQuery(query);
    }
}
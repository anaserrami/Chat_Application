package err.anas.chatyou.dao;

import err.anas.chatyou.dao.entities.User;
import java.util.List;

public class DaoTest {
    public static void main(String[] args) {
        UserDao userDao =new UserDaoImpl();
        List<User> User = userDao.searchUserByQuery("Y");
        for(User user : User){
            System.out.println("ID :"+ user.getIdUser()+" username:"+ user.getUsername());
        }
    }
}

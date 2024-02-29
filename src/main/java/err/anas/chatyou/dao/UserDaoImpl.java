package err.anas.chatyou.dao;

import err.anas.chatyou.dao.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public List<User> searchUserByQuery(String query) {
        Connection connection = DBSingleton.getConnection();
        List<User> users = new ArrayList<User>();
        try {
            PreparedStatement pstm=connection.prepareStatement("SELECT * FROM user WHERE username  like ? ");
            pstm.setString(1,"%"+query+"%");

            ResultSet rs=pstm.executeQuery();
            if(rs.next()) {
                User User =new User();
                User.setIdUser(rs.getInt("idUser"));
                User.setUsername(rs.getString("username"));
                users.add(User);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void save(User o) {
        Connection connection=DBSingleton.getConnection();
        try {
            PreparedStatement pstm= connection.prepareStatement("INSERT INTO user(username,password)"+"VALUES (?,?)");
            pstm.setString(1,o.getUsername());
            pstm.setString(2,o.getPassword());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        Connection connection=DBSingleton.getConnection();
        try {
            PreparedStatement pstm= connection.prepareStatement("DELETE FROM user WHERE"+" idUser=?");
            pstm.setInt(1,id);
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getById(Integer id) {
        Connection connection=DBSingleton.getConnection();
        User user =null;
        try {
            PreparedStatement pstm= connection.prepareStatement("SELECT * FROM user WHERE "+"idUser=?");
            pstm.setInt(1,id);
            ResultSet rs =pstm.executeQuery();
            if(rs.next()) {
                user =new User();
                user.setIdUser(rs.getInt("idUser"));
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        Connection connection=DBSingleton.getConnection();
        List<User> users =new ArrayList<>();
        try {
            PreparedStatement pstm= connection.prepareStatement("SELECT * FROM user ");
            ResultSet rs =pstm.executeQuery();
            while(rs.next()) {
                User user =new User();
                user.setIdUser(rs.getInt("idUser"));
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void update(User o) {
        Connection connection=DBSingleton.getConnection();
        try {
            PreparedStatement pstm= connection.prepareStatement("update username=?,password=? FROM user WHERE "+" idUser=?");

            pstm.setString(4,o.getPassword());
            pstm.setString(3,o.getUsername());
            pstm.setInt(5,o.getIdUser());

            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public  User findbyNameAndPassword(String username,String password){
        Connection connection=DBSingleton.getConnection();
        try {
            PreparedStatement pstm= connection.prepareStatement("SELECT * FROM user Where username=? AND password=? ");
            pstm.setString(1,username);
            pstm.setString(2,password);
            ResultSet rs =pstm.executeQuery();
            if(rs.next()){
                User user =new User();
                user.setIdUser(rs.getInt("idUser"));
                user.setPassword(rs.getString("password"));
                user.setUsername(rs.getString("username"));
                return user;
            }else{
                return  null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
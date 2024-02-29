package err.anas.chatyou.dao;

import err.anas.chatyou.dao.entities.Message;
import err.anas.chatyou.dao.entities.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    @Override
    public void save(Message o) {
        Connection connection=DBSingleton.getConnection();
        try {
            PreparedStatement pstm= connection.prepareStatement("INSERT INTO message(message,idSender,idReceiver)"+"VALUES (?,?,?)");
            pstm.setString(1,o.getMessage());
            pstm.setInt(2,o.getSender().getIdUser());
            pstm.setInt(3,o.getReceiver().getIdUser());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        Connection connection=DBSingleton.getConnection();
        try {
            PreparedStatement pstm= connection.prepareStatement("DELETE FROM message WHERE"+" idMsg=?");
            pstm.setInt(1,id);
            pstm.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message getById(Integer id) {
        Connection connection=DBSingleton.getConnection();
        Message  message =null;
        try {
            PreparedStatement pstm= connection.prepareStatement("SELECT * FROM message WHERE "+"idMsg=?");
            pstm.setInt(1,id);
            ResultSet rs =pstm.executeQuery();
            if(rs.next()) {
                message  =new Message();
                message.setIdMsg(rs.getInt("idMsg"));
                message.setMessage(rs.getString("message"));
                User sender=getUserLinkedWithMessage(rs.getInt("idSender"));
                message.setSender(sender);
                User receiver=getUserLinkedWithMessage(rs.getInt("idReceiver"));
                message.setReceiver(receiver);
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    @Override
    public List<Message> getAll() {
        Connection connection=DBSingleton.getConnection();
        List<Message> messages =new ArrayList<>();
        try {
            PreparedStatement pstm= connection.prepareStatement("SELECT * FROM message ");
            ResultSet rs =pstm.executeQuery();
            while(rs.next()) {
                Message message  =new Message();
                message.setIdMsg(rs.getInt("idMsg"));
                message.setMessage(rs.getString("message"));
                User sender=getUserLinkedWithMessage(rs.getInt("idSender"));
                message.setSender(sender);
                User receiver=getUserLinkedWithMessage(rs.getInt("idReceiver"));
                message.setReceiver(receiver);
                messages.add(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messages;
    }
    @Override
    public void update(Message o) {
        System.out.println("updating the messages");
    }


    public User getUserLinkedWithMessage(int idUser) throws SQLException {
        Connection connection=DBSingleton.getConnection();
        User user =null;
        PreparedStatement pstm1= connection.prepareStatement("SELECT * FROM user WHERE "+"idUser=?");
        pstm1.setInt(1,idUser);
        ResultSet rs1 =pstm1.executeQuery();
        if(rs1.next()) {
            user =new User();
            user.setIdUser(rs1.getInt("idUser"));
            user.setPassword(rs1.getString("password"));
            user.setUsername(rs1.getString("username"));
        }
        return  user;
    }

    @Override
    public List<Message> filterSenderAndReceiver(int idSender, int idReceiver) {
        Connection connection=DBSingleton.getConnection();
        List<Message> messages =new ArrayList<>();
        User user =null;
        PreparedStatement pstm1= null;
        try {
            System.out.println(idSender+" "+idReceiver);
            pstm1 = connection.prepareStatement("SELECT * FROM message WHERE "+" (idSender=?"+" AND  idReceiver=?) OR "+" (idSender=? AND "+" idReceiver=? )");
            pstm1.setInt(1,idSender);
            pstm1.setInt(2,idReceiver);
            pstm1.setInt(3,idReceiver);
            pstm1.setInt(4,idSender);
            ResultSet rs =pstm1.executeQuery();
            while(rs.next()) {
                Message message  =new Message();
                message.setIdMsg(rs.getInt("idMsg"));
                message.setMessage(rs.getString("message"));
                User sender=getUserLinkedWithMessage(rs.getInt("idSender"));
                message.setSender(sender);
                User receiver=getUserLinkedWithMessage(rs.getInt("idReceiver"));
                message.setReceiver(receiver);
                messages.add(message);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return messages;

    }
}
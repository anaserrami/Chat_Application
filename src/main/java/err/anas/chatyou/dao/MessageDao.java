package err.anas.chatyou.dao;

import err.anas.chatyou.dao.entities.Message;
import java.util.List;

public interface MessageDao extends Dao<Message,Integer>{
    public List<Message> filterSenderAndReceiver(int idSender, int idReceiver);
}

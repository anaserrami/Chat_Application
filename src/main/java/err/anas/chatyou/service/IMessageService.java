package err.anas.chatyou.service;

import err.anas.chatyou.dao.entities.Message;
import java.util.List;

public interface IMessageService {
    public void addMessage(Message c);
    public void deleteMessageBy(Integer id);
    public List<Message> getAllMessages();
    public void updateMessage(Message c);
}
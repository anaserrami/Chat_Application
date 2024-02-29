package err.anas.chatyou.server;

import err.anas.chatyou.dao.MessageDaoImpl;
import err.anas.chatyou.dao.UserDaoImpl;
import err.anas.chatyou.dao.entities.Message;
import err.anas.chatyou.dao.entities.User;
import err.anas.chatyou.service.IServiceMessageImpl;
import err.anas.chatyou.service.IServiceUserImpl;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientHandler implements  Runnable{
    public  static Map<Integer,ClientHandler> clientHandlers=new HashMap<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private IServiceMessageImpl iServiceMessage;
    private IServiceUserImpl iServiceUser;
    private  User user;
    public ClientHandler(Socket  socket){
        iServiceUser=new IServiceUserImpl(new UserDaoImpl());
        iServiceMessage=new IServiceMessageImpl(new MessageDaoImpl());
        try{
            this.socket=socket;
            this.bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
            this.bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())) ;
            int idUser=bufferedReader.read();
            System.out.println("user id :"+idUser);
            this.user=iServiceUser.getUserbyId(idUser);
            clientHandlers.put(idUser,this);
        }catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    @Override
    public void run() {
        while(socket.isConnected()){
            try{
                String messageFromClient=bufferedReader.readLine();

                //devide  between the two
                String []splits=messageFromClient.split(  "=>");

                //get received user
                int receiverUserId=Integer.parseInt(splits[0]);
                User receiverUser=iServiceUser.getUserbyId(receiverUserId);
                String messageFromSender=splits[1];
                System.out.println("message :"+messageFromSender);
                if(messageFromSender.equals("$$disconnect$$")){
                    disconnectClient();
                }else if(messageFromSender.equals("$$loadMessages$$")){
                    loadOldMessages(receiverUserId);
                } else if (messageFromSender.equals("$$LoadOnlineUsers$$")) {
                    loadOnlineUsers();
                } else if (messageFromSender.equals("$$$stop$$$")) {
                    sendMessage(this.user,this.user,"$$$stop$$$");
                } else{
                    //add message

                    Message msg=new Message(messageFromSender, user,receiverUser);
                    iServiceMessage.addMessage(msg);

                    sendMessage(this.user,receiverUser,messageFromSender);
                }

            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    // sendMessage need the  username Sender
    public  void sendMessage(User sender,User receiverUser,String messageToSend){


        try {
            ClientHandler clientHandler=clientHandlers.get(receiverUser.getIdUser());
            if(clientHandler!=null){
                clientHandler.bufferedWriter.write(sender.getUsername()+"=>"+messageToSend);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            }


        }catch (IOException e){

            closeEverything(socket,bufferedReader,bufferedWriter);
        }

    }
    //load allmessages
    public  void loadOldMessages(int receiverUserId)  {

        List<Message> msgs= iServiceMessage.getConversation(user.getIdUser(),receiverUserId);
        for(Message msg:msgs){

            sendMessage(msg.getSender(),user,msg.getMessage());
        }
    }
    public void loadOnlineUsers() {
        List<ClientHandler> onlineHandlers = new ArrayList<>(clientHandlers.values());
        StringBuilder usernamesBuilder = new StringBuilder();

        // Iterate over the values and concatenate usernames
        for (ClientHandler handler : onlineHandlers) {
            User onlineUser = handler.getUser();
            usernamesBuilder.append("%/0%").append(onlineUser.getUsername());
        }

        // The concatenated usernames string
        String concatenatedUsernames = usernamesBuilder.toString();
        sendMessage(user,user,concatenatedUsernames);
    }

    public  void disconnectClient(){
        clientHandlers.remove(user.getIdUser());
    }
    public  void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        disconnectClient();
        try{
            if(bufferedReader!=null){
                bufferedReader.close();
            }
            if(bufferedWriter!=null){
                bufferedWriter.close();
            }
            if(socket !=null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }
}
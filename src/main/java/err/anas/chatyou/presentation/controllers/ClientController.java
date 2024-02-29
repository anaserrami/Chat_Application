package err.anas.chatyou.presentation.controllers;

import err.anas.chatyou.dao.UserDaoImpl;
import err.anas.chatyou.dao.entities.User;
import err.anas.chatyou.server.ClientHandler;
import err.anas.chatyou.service.IServiceUserImpl;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
public class ClientController {
    private static Socket socket;
    private static BufferedWriter bufferedWriter;
    private  static PrintWriter pr;
    private static BufferedReader bufferedReader;
    private static User user;

    public ClientController(Socket socket, User user){
        try{
            ClientController.socket =socket;
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
            pr=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true) ;
            ClientController.user =user;

        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    public static void sendMessage(int userId,String message){
        String sendingMessage=userId+"=>"+message;
        if (!message.trim().isEmpty()) {
            sendMessagePrivate(sendingMessage);

        } else {
            System.out.println("Please enter a non-empty message.");
        }
    }
    public  static  void listenForMessage(BiFunction<String,String,Boolean> processMessage){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgReceived;
                while(socket.isConnected()){
                    try{
                        msgReceived=bufferedReader.readLine();
                        String [] splits=msgReceived.split("=>");

                        String usernameSender =splits[0];
                        String messageReceived = splits[1];
                        Boolean stopWhile=processMessage.apply(usernameSender,messageReceived);
                        if(stopWhile){

                            break;
                        }
                    }catch (IOException e){
                        closeEverything(socket,bufferedReader,bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public static void closeEverything(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
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
    public  static  List<String> loadOnlineUsers(){

        List<String> usernames=new ArrayList<>();
        String msgReceived= null;
        try {
            sendMessagePrivate("0=>$$LoadOnlineUsers$$");
            msgReceived = bufferedReader.readLine();

            String [] splits=msgReceived.split("=>");
            String usernameSender =splits[0];
            String messageReceived = splits[1];
            String [] table=messageReceived.split("%/0%");
            usernames.addAll(Arrays.asList(table));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return usernames;


    }
    private  static void sendMessagePrivate(String message){
        try {
            if (socket.isConnected()){

                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();

            }else{

                System.out.println("socket not connected");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    };

    public  static void  socketConnect(User authenticateduser){
        Socket socket= null;
        try {
            socket = new Socket("localhost",9090);
            ClientController.socket =socket;
            bufferedReader=new BufferedReader(new InputStreamReader(socket.getInputStream())) ;
            bufferedWriter=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())) ;
            ClientController.user =authenticateduser;
            bufferedWriter.write(authenticateduser.getIdUser());
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
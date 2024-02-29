package err.anas.chatyou.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
    private ServerSocket serverSocket;
    public  ChatServer(ServerSocket serverSocket){
        this.serverSocket=serverSocket;
    }
    public  void startServer(){
        try{
            while(!serverSocket.isClosed()){
                Socket socket=serverSocket.accept();
                System.out.println("A new client has connected!");

                ClientHandler clientHandler=new ClientHandler(socket);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        }catch (IOException e){
            closeServerSocket();
        }
    }
    public  void closeServerSocket(){
        try{
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket=new ServerSocket(9090);
        ChatServer server=new ChatServer(serverSocket);
        server.startServer();
    }
}
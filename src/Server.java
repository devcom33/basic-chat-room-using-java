import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private Socket clientSocket;
    // private PrintWriter out;
    // private BufferedReader in;
    private ArrayList<ConnectionHandler> connections;
    private ExecutorService pool;
    public Server(){
        connections=new ArrayList<>();
    }

    @Override
    public void run(){
        try{
            serverSocket = new ServerSocket(9999);
            pool=Executors.newCachedThreadPool();
            System.out.println("[+] Listening");
            while(true){
                clientSocket = serverSocket.accept();
                System.out.println("[+] Connected Successfully");
                ConnectionHandler handler=new ConnectionHandler(clientSocket);
                connections.add(handler);
                pool.execute(handler);
            }
        }catch(Exception e){
            // TODO : handle
        }
    }
    public void broadcast(String msg){
        for(ConnectionHandler client:connections){
            if(client != null) client.sendMessage(msg);
        }
    }


    public class ConnectionHandler implements Runnable{
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        private String nickname;

        public ConnectionHandler(Socket client){
            this.clientSocket=client;
        }

        public void run(){
            try{
                out = new PrintWriter(clientSocket.getOutputStream(),true);
                in =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out.println("please enter your nickname : ");
                nickname=in.readLine();
                System.out.println(nickname+" Connected!");
                broadcast(nickname+" joined the chat");
                String message;
                while((message = in.readLine()) != null){
                    if(message.startsWith("/rename ")){
                        String[] messageSplit = message.split(" ",2);
                        if(messageSplit.length==2){
                            out.println(nickname+" changed his name to "+messageSplit[1]);
                            System.out.println(nickname+" changed his name to "+messageSplit[1]);
                            nickname=messageSplit[1];
                            out.println("[*_*]Successfully changed nickname to "+nickname);
                        }else{
                            out.println("Unfortunately Your nickname hasn't been changed");
                        }
                    }else{
                        broadcast(nickname+": "+message);
                    }

                }

            }catch(Exception e){
                // TODO: handle
            }

        }
        public void sendMessage(String msg){
            // System.out.println(msg);
            try{
                // out = new PrintWriter(clientSocket.getOutputStream(),true);
                out.println(msg);

            }catch(Exception e){
                out.println("Hey Albatal");
            }
            

        }



    }




    // public void stop()throws Exception{
    //     in.close();
    //     out.close();
    //     clientSocket.close();
    //     serverSocket.close();
    // }


    public static void main(String[] args) throws Exception{
        Server server=new Server();
        server.run();

    }
}
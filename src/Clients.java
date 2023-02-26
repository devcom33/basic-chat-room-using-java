import java.net.*;
import java.io.*;

public class Clients implements Runnable{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    // public String nickname;

    @Override
    public void run(){
        try{
            clientSocket =new Socket("127.0.0.1", 9999);

            in =new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(),true);


            InputHandler handler=new InputHandler();
            Thread t=new Thread(handler);
            System.out.println("HHHH");
            t.start();
            // System.out.println("YYY");
            String inMessage;
            // System.out.println("JJJ");
            while((inMessage = in.readLine()) != null){
                System.out.println(inMessage);
            }

        }catch(Exception e){
            //TODO:handle
        }
    } 

    class InputHandler implements Runnable{
        @Override
        public void run() {
            try{
                while(true){ 
                    BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
                    // out = new PrintWriter(clientSocket.getOutputStream(),true);
                    String msg=in.readLine();
                    out.println(msg);
                }
            }catch(Exception e){
                //TODO:handle
            }
        }
    }
    public void stopConnection() throws Exception{
        in.close();
        out.close();
        clientSocket.close();
        
    }
    
    public static void main(String[] args) throws Exception {
        Clients client = new Clients();
        client.run();
    }
}
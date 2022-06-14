import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;
    // constructor
    public Server() {
       try {
        server = new ServerSocket(7777);
        System.out.println("server is ready to accept connection");
        socket = server.accept(); // server.accept accept the connection and socket stores the instances of that connection so that we can read inputstream

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

        startReading();
        startWriting();

       } catch (Exception e) {
        //TODO: handle exception
        e.printStackTrace();
       }
    }

    // we used threading because we have to read and write simultaneously in same program
    public void startReading() {
        // one thread will read and give input
        Runnable r1=()->{ //lamda expression
            System.out.println("Reader started");
            try {
                while (!socket.isClosed()) { // to read again and again
                        String message = br.readLine();
                        if (message.equals("exit")) {
                            System.out.println("Client has exited\n");
                            socket.close();
                            break;
                        }

                        System.out.println("Client : " + message);
                    

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(r1).start();
    }

    public void startWriting() {
        // other thread , it will take data from user and send to client
        Runnable r2 = ()-> { // thread implementation
            System.out.println("Writer started");
            try {
                while (!socket.isClosed()) {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                        String content = br1.readLine();
                        out.println(content);
                        out.flush();
                        if (content.equals("exit")) {
                            socket.close();
                            break;
                        }
                }
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        };

        new Thread(r2).start();
    }

    public static void main(String[] args) {
       System.out.println("Server Started");
       new Server(); 
    }
}

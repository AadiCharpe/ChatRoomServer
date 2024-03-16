import java.net.Socket;
import java.net.ServerSocket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Socket socket;
        try {
            ServerSocket serverSocket = new ServerSocket(8189);
            System.out.println("Server Started");
            while (true) {
                socket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(socket);
                serverThread.start();
            }
        } catch (Exception e) {}
    }
}

class ServerThread extends Thread {
    private Socket socket;
    private static ArrayList<String> messages = new ArrayList<>();
    private static ArrayList<ServerThread> clients = new ArrayList<>();
    public ServerThread(Socket s) {
        socket = s;
	clients.add(this);
    }
    public Socket getSocket() {return socket;}
    public void run() {
        String s;
        try {
            System.out.println("Connection Received by Client " + socket.toString());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("Hello! This is a chat room. Enter BYE to exit.");
	    printWriter.println(messages);
            while (true) {
                s = bufferedReader.readLine();
                System.out.println(s);
                if (s.equalsIgnoreCase("BYE")) break;
                messages.add(s);
		for(int i = 0; i < clients.size(); i++)
                	new PrintWriter(clients.get(i).getSocket().getOutputStream(), true).println(s);
            }
            printWriter.println("Connection Closed");
            socket.close();
            System.out.println("Connection " + socket.toString() + " Was Closed");
        } catch(Exception e) {}
    }
}

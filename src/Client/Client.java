package Client;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    List<String> tags=new ArrayList<>();
    List<String> users=new ArrayList<>();
    String nom;
    Socket socket;
    public Client(String nom,int port)
    {
        this.nom=nom;
        try {
            socket=new Socket("localhost",port);
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void sendRequest(Request request) throws IOException {
        if (!socket.isClosed())
            request.send(socket.getOutputStream());
    }
    public Socket getSocket() {
        return socket;
    }
    public void startChat() throws IOException {

        sendRequest(new PublishRequest(nom,"#dimaraja"));
        sendRequest(new GetTags());
        sendRequest(new GetUsers());
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Please enter your name");
        Scanner scanner=new Scanner(System.in);
        String name= scanner.nextLine();
        Client client=new Client(name,8000);
        ServerHandler serverHandler=new ServerHandler(client);
        Thread thread =new Thread(serverHandler);
        thread.start();
        Userinterface.Start(client,serverHandler);
    }
}

package Client;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
        sendRequest(new InitializeRequest(nom,""));
        sendRequest(new PublishRequest(nom,"#dimaraja"));
        System.out.println("sending tags request");
        sendRequest(new GetTags());
        sendRequest(new GetUsers());
    }

    public static void main(String[] args) throws IOException {
        Client client=new Client("taha",8000);
        Thread thread =new Thread(new ServerHandler(client));
        thread.start();
        client.startChat();
    }
}

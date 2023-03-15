package Client;
import java.io.IOException;
import java.net.Socket;

public class Client {
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
        sendRequest(new PublishRequest(nom,"Hello #mama "+nom));
        sendRequest(new SubscribeTagRequest("mama"));
        sendRequest(new PublishRequest(nom,"Hello #mama "+nom));
    }

    public static void main(String[] args) throws IOException {
        Client client=new Client("taha",8000);
        Thread thread =new Thread(new ServerHandler(client));
        thread.start();
        client.startChat();
        Client client1=new Client("saad",8000);
        Thread thread1 =new Thread(new ServerHandler(client1));
        thread1.start();
        client1.startChat();
    }
}

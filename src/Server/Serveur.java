package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Serveur implements Runnable{
    List<Message> messages=new ArrayList<>();
    List<Tag> tags=new ArrayList<>();
    List<Author> authors=new ArrayList<>();
    ServerSocket server;
    List<ClientHandler> clients=new ArrayList<>();

    public Author getAuthor(String ToSearchauthor) {
        for (Author author:authors)
        {
            if (ToSearchauthor.equals(author.name))
            {
                return author;
            }
        }
        return null;
    }
    public Tag getTag(String ToSearchtag) {
        for (Tag tag:tags)
        {
            if (ToSearchtag.equals(tag.tag))
            {
                return tag;
            }
        }
        return null;
    }

    public Serveur(int port)
    {
        try {
            server=new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            System.out.println("-------------------Server Started-------------------");
            while (true)
            {
                Socket client= server.accept();
                ClientHandler clientHandler=new ClientHandler(this,client);
                Thread thread=new Thread(clientHandler);
                thread.start();
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args) {
        Thread thread=new Thread(new Serveur(8000));
        thread.start();
    }
}

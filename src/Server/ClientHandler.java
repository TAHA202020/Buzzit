package Server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    Serveur server;
    Author author;
    private final Socket client;
    public ClientHandler(Serveur server,Socket client)
    {
        server.clients.add(this);
        this.server=server;
        this.client=client;

    }
    public void sendMessage(String header,String body)
    {
        try {
            client.getOutputStream().write((header+"\r\n"+body+"\r\n").getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());;
        }
    }
    public void handleInitialize(Author author) throws Exception
    {
        int count=0;
        for (ClientHandler cl: server.clients)
        {
            if (cl.author.name.equals(author.name))
            {
                count=count+1;
            }
        }
        if (count>1)
        {
            sendMessage("CLOSE","Name already in use please change it");
            System.out.println("Name: "+author.name+" already in use please change it");
            client.getOutputStream().close();
            client.getInputStream().close();
            client.close();
        }
        else {
            server.authors.add(author);
            System.out.println(author.name + " connected");
        }
    }
    public void handlePublish(Message message)
    {
        System.out.println("handling publish");
        server.messages.add(message);
        for (Tag messagetags:message.getTag())
        {
            Tag tag=server.getTag(messagetags.tag);
            if (tag!=null)
            {
                tag.notifySubscribers(message.getMessage());
            }else
            {
                server.tags.add(messagetags);
                System.out.println("added tag");
            }
        }
        author.notifySubscribers(message.getMessage());
        System.out.println("tags:" +server.tags);
    }

    public void HandleRequest(String header, String body)throws Exception
    {
        String[] headers=header.split(" ");
        switch (headers[0])
        {
            case "Initialize":
                author=new Author(headers[1].split("@")[1]);
                handleInitialize(author);
                break;
            case "PUBLISH" :
                handlePublish(new Message(body));
                break;
            case "SUBSCRIBE":
                String tagauthor;
                if (headers[1].contains("#"))
                {
                    tagauthor=headers[1].split("#")[1];
                    Tag tag=server.getTag(tagauthor);
                    System.out.println(tag);
                    if (tag!=null)
                    {
                        tag.addSubscribers(this);
                    }
                }
                else
                {
                    tagauthor=headers[1].split("@")[1];
                    Author author =server.getAuthor(tagauthor);
                    if (author!=null)
                    {
                        author.addSubscriber(this);
                    }
                }
                break;
        }
    }
    @Override
    public void run() {
        try {
            BufferedReader inputStream=new BufferedReader(new InputStreamReader(client.getInputStream()));
            while (true) {
                String header = inputStream.readLine();
                String body = inputStream.readLine();
                HandleRequest(header,body);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            server.clients.remove(this);
        }
    }
}

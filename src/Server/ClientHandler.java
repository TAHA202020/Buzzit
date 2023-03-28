package Server;

import java.io.*;
import java.net.Socket;
import java.util.List;

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
    public void handleInitialize(String name) throws Exception
    {
        boolean exists=false;
        for (ClientHandler cl: server.clients)
        {
            if (cl.author!=null)
                if (cl.author.name.equals(name))
                {
                    exists=true;
                }
        }
        if (exists)
        {
            sendMessage("CLOSE","Name already in use please change it");
            System.out.println("Name: "+name+" already in use please change it");
            client.getOutputStream().close();
            client.getInputStream().close();
            client.close();
        }
        else {
            for (Author author1: server.authors)
                if (author1.name.equals(name))
                    this.author=author1;
            if (this.author==null)
            {
                this.author=new Author(name);
                server.authors.add(author);
            }
            System.out.println(author.name + " connected");
        }
    }
    public void handlePublish(Message message)
    {
        server.messages.add(message);
        List<Tag> tags=message.getTag();
        for (Tag messagetags:tags)
        {
            Tag tag=server.getTag(messagetags.tag);
            if (tag!=null)
            {
                tag.notifySubscribers(message.getMessage());
            }else
            {
                server.tags.add(messagetags);
            }
        }
        author.notifySubscribers(message.getMessage());
        sendMessage("MESSAGE", message.getMessage());
    }

    public void HandleRequest(String header, String body)throws Exception
    {
        String[] headers=header.split(" ");
        switch (headers[0])
        {
            case "Initialize":
                String name=headers[1].split("@")[1];
                handleInitialize(name);
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
                    System.out.println("Tag does not Exist");
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
            case "GETTAGS":
                System.out.println("getting tags");
                String toSend="";
                for(Tag tag: server.tags)
                    toSend=toSend+" "+tag.tag;
                sendMessage("RESPONSE",toSend);
                break;
            case "GETUSERS":
                System.out.println("getting users");
                String Users="";
                for(Author author1: server.authors)
                    Users=Users+" "+author1.name;
                sendMessage("RESPONSE",Users);
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

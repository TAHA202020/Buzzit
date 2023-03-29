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
            System.out.println(e.getMessage());
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
            sendMessage("Initialize","ERROR");
            System.out.println("Name: "+name+" already in use please change it");
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
            author.setCl(this);
            sendMessage("Initialize","OK");
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
        author.addMessage(message);
        author.notifySubscribers(message.getMessage());
        sendMessage("MESSAGE", "OK");
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
                handlePublish(new Message(body,server.messages.size()+1,this.author));
                break;
            case "SUBSCRIBE":
                String tagauthor;
                if (headers[1].contains("#"))
                {
                    tagauthor=headers[1].split("#")[1];
                    Tag tag=server.getTag(tagauthor);
                    if (tag!=null)
                    {
                        tag.addSubscribers(author);
                        sendMessage("REPONSE","OK");
                    }
                    else
                    {
                        sendMessage("REPONSE","ERROR");
                    }

                }
                else
                {
                    tagauthor=headers[1].split("@")[1];
                    Author author =server.getAuthor(tagauthor);
                    if (author!=null)
                    {
                        author.addSubscriber(this.author);
                        sendMessage("REPONSE","OK");
                    }
                    else
                    {
                        sendMessage("REPONSE","ERROR");
                    }
                }
                break;
            case "UNSUBSCRIBE":
                if (headers[1].contains("#"))
                {
                    tagauthor=headers[1].split("#")[1];
                    Tag tag=server.getTag(tagauthor);
                    if (tag!=null)
                    {
                        tag.removeSubscriber(author);
                        sendMessage("REPONSE","OK");
                    }
                    else
                    {
                        sendMessage("REPONSE","ERROR");
                    }

                }
                else
                {
                    tagauthor=headers[1].split("@")[1];
                    Author author =server.getAuthor(tagauthor);
                    if (author!=null)
                    {
                        author.removeSubscriber(this.author);
                        sendMessage("REPONSE","OK");
                    }
                    else
                    {
                        sendMessage("REPONSE","ERROR");
                    }
                }

            case "GETTAGS":
                System.out.println("getting tags");
                String toSend="";
                int i=1;
                for(Tag tag: server.tags)
                    toSend=toSend+i+"-"+tag.tag+" ";
                sendMessage("RESPONSE",toSend);
                break;
            case "GETUSERS":
                System.out.println("getting users");
                String Users="";
                i=1;
                for(Author author1: server.authors) {

                    if (!author1.name.equals(author.name))
                        Users = Users+i+"-"+author1.name+" ";
                }
                sendMessage("GETUSERS",Users);
                break;
            case "RCV_IDS":
                System.out.println(header);
                String author=null;
                String tag=null;
                String id=null;
                String limit=null;
                for (String param:headers)
                {
                    if (param.startsWith("author"))
                        author=param.split("@")[1];
                    if (param.startsWith("tag"))
                        tag=param.split("#")[1];
                    if (param.startsWith("since_id"))
                        id=param.split(":")[1];
                    if (param.startsWith("limit"))
                        limit=param.split(":")[1];
                }
                String res="";
                int max;
                int id_start=0;
                if (limit==null)
                    max=5;
                else
                    max = Integer.parseInt(limit);
                System.out.println(limit);
                if (id!=null)
                {
                    id_start=Integer.parseInt(id)-1;
                }
                if (author!=null)
                {
                    Author auth=server.getAuthor(author);
                    System.out.println(auth.messages);
                    for (Message message:auth.messages)
                    {
                        if (tag!=null)
                        {
                            if (message.containsTag(tag)&& message.id>=id_start) {
                                res = res +message.id + " ";
                                max--;
                            }
                        }
                        else
                            if (message.id>=id_start) {
                                res = res +message.id+ " ";
                                max--;
                            }

                        if (max==0)
                            break;
                    }
                }
                else
                    for (i=id_start;i<server.messages.size();i++)
                    {
                        System.out.println(server.messages);
                        if (tag!=null)
                        {
                            if (server.messages.get(i).containsTag(tag)&& server.messages.get(i).id>=id_start){
                                res = res + server.messages.get(i).id+ " ";
                                max--;
                            }
                        }
                        else
                        if (server.messages.get(i).id>=id_start) {
                            res = res +server.messages.get(i).id+" ";
                            max--;
                        }
                        if (max==0)
                            break;
                    }
                    sendMessage("RCV_IDS",res);
                    break;
            case "RCV_MSG":
                int idtoget=Integer.parseInt(headers[1].split(":")[1]);
                sendMessage("RCV_IDS",idtoget+":"+server.messages.get(idtoget-1).getMessage());
                break;
            case "REPLY":
                author=headers[1].split("@")[1];
                id=headers[2].split(":")[1];
                idtoget=Integer.parseInt(id);
                server.messages.add(new Message(body,server.messages.size()+1,this.author));
                String response=author+" Responded to "+server.messages.get(idtoget-1).getMessage()+"--------->"+body;
                server.messages.get(idtoget-1).author.cl.sendMessage("REPLY",response);
                sendMessage("RESPONSE","Response Sent");
                break;
            case "REPUBLISH":
                id=headers[2].split(":")[1];
                Message msg=server.messages.get(Integer.parseInt(id)-1);
                this.author.notifySubscribers(this.author.name+" REPUBLISHED: "+msg.getMessage());
                sendMessage("REPUBLISH","Republished:"+msg.getMessage());
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

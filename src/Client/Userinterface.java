package Client;

import java.util.Scanner;

public class Userinterface {
    static String blue="\033[34m";static String reset = "\u001B[0m";

    public static int getUSerinputint()
    {
        Scanner scanner=new Scanner(System.in);
        String line= scanner.nextLine();
        return Integer.parseInt(line);
    }
    public static String getUSerinput()
    {
        Scanner scanner=new Scanner(System.in);
        String line= scanner.nextLine();
        return line;
    }
    public static void Initialize(Client client, ServerHandler serverHandler) throws Exception
    {
        client.sendRequest(new InitializeRequest(client.nom,""));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        if(res.equals("OK"))
        {
            System.out.println(blue+"connection established"+reset);
            return;
        }
        serverHandler.setLastRepsonsenull();
        System.out.println("Name: "+client.nom+" already in use please change it");
        Scanner scanner = new Scanner(System.in);
        String name= scanner.nextLine();
        client.setNom(name);
        Initialize(client,serverHandler);
    }
    public static void ServerSaid(String s){
        System.out.println(blue+s+reset);
    }
    public static void Publish(Client client, ServerHandler serverHandler,String body) throws Exception
    {
        client.sendRequest(new PublishRequest(client.nom,body));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        if(res.equals("OK"))
        {
            ServerSaid("Message Published : "+body);
        }
        else
        {
            System.out.println("something went Wrong");
        }
    }
    public static void SubscribeUser(Client client, ServerHandler serverHandler) throws Exception
    {
        client.sendRequest(new GetUsers());
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        if (res.isEmpty())
        {
            ServerSaid("No other Users");
            return;
        }
        ServerSaid("ALL Users:"+"\n"+res+"\n");
        System.out.println("Please Type the user you want to subscribe to:");
        String name=getUSerinput();
        client.sendRequest(new SubscibeUserRequest(name));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        res=serverHandler.lastRepsonse;
        if(res.equals("OK"))
        {
            ServerSaid(name+" Added");
            client.users.add(name);
        }
        else
        {
            ServerSaid("Error has Occured ");
        }
    }public static void SubscribeTag(Client client, ServerHandler serverHandler) throws Exception
    {
        client.sendRequest(new GetTags());
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        if (res.isEmpty())
        {
            ServerSaid("No other Tags");
            return;
        }
        ServerSaid("ALL Tags:"+"\n"+res+"\n");
        System.out.println("Please Type the Tag you want to subscribe to:");
        String tag=getUSerinput();
        client.sendRequest(new SubscribeTagRequest(tag));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        res=serverHandler.lastRepsonse;
        if(res.equals("OK"))
        {
            ServerSaid(tag+" Added");
            client.tags.add(tag);
        }
        else
        {
            ServerSaid("Error has Occured ");
        }
    }
    public static void Rcv_Ids(Client client, ServerHandler serverHandler,String author,String tag,String id,String limit) throws Exception
    {
        client.sendRequest(new ReceiveIds(author,tag,id,limit));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        System.out.println(res);
        if (res.isEmpty())
            return;
        String[] ids=res.split(" ");
        for (String idd:ids)
        {
            RCV_Message(client,serverHandler,Integer.parseInt(idd));
        }

    }
    public static void Unsibscribetag(Client client,ServerHandler serverHandler,String tag)throws Exception{
        client.sendRequest(new UnsubscibeTagRequest(tag));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        if(res.equals("OK"))
        {
            ServerSaid(tag+" Removed");
            client.tags.remove(tag);
        }
        else
        {
            ServerSaid("Error has Occured ");
        }
    }
    public static void UnsibscribeUser(Client client,ServerHandler serverHandler,String username)throws Exception{
        client.sendRequest(new UnsubscribeUserRequest(username));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        if(res.equals("OK"))
        {
            ServerSaid(username+" Removed");
            client.tags.remove(username);
        }
        else
        {
            ServerSaid("Error has Occured ");
        }
    }
    public static void RCV_Message(Client client, ServerHandler serverHandler,int id) throws Exception
    {
        //change classe
        client.sendRequest(new ReceiveMessage(id));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        ServerSaid("messages");
        ServerSaid(res);
    }
    public static void Reply_MEssage(Client client, ServerHandler serverHandler,int id,String message) throws Exception
    {
        client.sendRequest(new ReplyRequest(client.nom,id,message));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        ServerSaid(res);
    }
    public static void Republish(Client client,ServerHandler serverHandler,String id)throws Exception{
        client.sendRequest(new RepublishRequest(client.nom,id));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        ServerSaid(res);
    }
    public static void Start(Client client,ServerHandler serverHandler) throws Exception
    {
        Initialize(client,serverHandler);
        serverHandler.setLastRepsonsenull();
        while (true)
        {
            System.out.println("Choose your next move");
            System.out.println("1-publish a message");
            System.out.println("2-subscribe to users");
            System.out.println("3-getMessages");
            System.out.println("4-subscribe to Tag");
            System.out.println("5-unsubscribe Tag");
            System.out.println("6-Reply to message");
            System.out.println("7-Unsubscribe user");
            System.out.println("8-Republish a message");
            int response=getUSerinputint();
            switch (response)
            {
                case 1:
                    System.out.println("type your message");
                    String body=getUSerinput();
                    Publish(client,serverHandler,body);
                    break;
                case 2:
                    System.out.println("getting users");
                    SubscribeUser(client,serverHandler);
                    break;
                case 3:
                    System.out.println("getting messages");
                    System.out.println("AUthor name");
                    String author=getUSerinput();
                    if (author.isEmpty())
                        author=null;
                    System.out.println("give tag");
                    String tag=getUSerinput();
                    if (tag.isEmpty())
                        tag=null;
                    System.out.println("give id");
                    String id=getUSerinput();
                    if (id.isEmpty())
                        id=null;
                    System.out.println("give limit");
                    String limit =getUSerinput();
                    if (limit.isEmpty())
                        limit=null;
                    Rcv_Ids(client,serverHandler,author,tag,id,limit);
                    break;
                case 4:
                    System.out.println("getting Tags");
                    SubscribeTag(client,serverHandler);
                    break;
                case 5:
                    System.out.println("getting tags you subbed to");
                    System.out.println(client.tags);
                    System.out.println("select tags you want to unsub from");
                    tag=getUSerinput();
                    Unsibscribetag(client,serverHandler,tag);
                    System.out.println(client.tags);
                    break;
                case 6:
                    System.out.println("Message id to reply to");
                    int idd=getUSerinputint();
                    System.out.println("Message to say");
                    String message=getUSerinput();
                    Reply_MEssage(client,serverHandler,idd,message);
                    break;
                case 7:
                    System.out.println("getting users you subbed to");
                    System.out.println(client.users);
                    System.out.println("select user you want to unsub from");
                    String user=getUSerinput();
                    UnsibscribeUser(client,serverHandler,user);
                    System.out.println(client.tags);
                    break;
                case 8:
                    System.out.println("Type id to republish");
                    id=getUSerinput();
                    Republish(client,serverHandler,id);
                    break;


            }

        }
    }
}

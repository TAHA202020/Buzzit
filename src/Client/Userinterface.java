package Client;

import java.util.Scanner;

public class Userinterface {
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
            System.out.println("connection established");
            return;
        }
        serverHandler.setLastRepsonsenull();
        System.out.println("Please enter another name");
        Scanner scanner = new Scanner(System.in);
        String name= scanner.nextLine();
        client.setNom(name);
        Initialize(client,serverHandler);
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
            System.out.println("Message Published : "+body);
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
            System.out.println("No other Users");
            return;
        }
        System.out.print("ALL Users:"+"\n"+res+"\n");
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
            System.out.println("Added");
        }
        else
        {
            System.out.println("Error has Occured ");
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
    }
    public static void RCV_Message(Client client, ServerHandler serverHandler,String id)
    {
        //change classe
        client.sendRequest(new ReceiveIds(author,tag,id,limit));
        synchronized (serverHandler)
        {
            serverHandler.wait();
        }
        String res=serverHandler.lastRepsonse;
        System.out.println(res);
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
                    Rcv_Ids(client,serverHandler,null,null,null,null);
                    break;

            }

        }
    }
}

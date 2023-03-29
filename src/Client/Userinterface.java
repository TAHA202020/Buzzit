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
    public static void Start(Client client,ServerHandler serverHandler) throws Exception
    {
        Initialize(client,serverHandler);
        serverHandler.setLastRepsonsenull();
        while (true)
        {
            System.out.println("Choose your next move");
            System.out.println("1-publish a message");
            int response=getUSerinputint();
            switch (response)
            {
                case 1:
                    System.out.println("type your message");
                    String body=getUSerinput();
                    client.sendRequest(new PublishRequest(client.nom,body));
                    while (serverHandler.getLastRepsonse()==null)
                    {}
                    System.out.println("Server: "+ serverHandler.getLastRepsonse());
                    serverHandler.setLastRepsonsenull();
                    break;

            }

        }
    }
}

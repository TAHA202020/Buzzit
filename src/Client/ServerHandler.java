package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServerHandler implements Runnable{

    Client client;
    String lastRepsonse=null;
    public ServerHandler(Client client){
            this.client=client;
    }

    public void setLastRepsonsenull() {
        this.lastRepsonse = null;
    }

    public String getLastRepsonse() {
        return lastRepsonse;
    }

    void HandleRequest(String header, String body)throws Exception
    {
        switch (header)
        {
            case "CLOSE":
                client.getSocket().close();
                System.out.println(body);
                break;
            case "Initialize":
                lastRepsonse=body;
                break;

        }
        System.out.println(lastRepsonse);
        synchronized (this)
        {
            notify();
        }
    }
    @Override
    public void run() {
        try {
            BufferedReader inputStream=new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
            while (true) {
                String header = inputStream.readLine();
                String body = inputStream.readLine();
                HandleRequest(header,body);
            }
        }catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        }

}

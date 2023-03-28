package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler implements Runnable{

    Client client;
    public ServerHandler(Client client){
            this.client=client;
    }
    void HandleRequest(String header, String body)
    {

        if (header.equals("CLOSE")) {
            try {
                client.getSocket().close();
                System.out.println(body);
            } catch (Exception e) {
                System.out.println("Socket Couldn't close");;
            }
        }
        else System.out.println(body);

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

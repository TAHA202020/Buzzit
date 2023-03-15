package Client;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Request {
    String header;
    String body;
    public Request(String header,String body)
    {
        this.header=header;
        this.body=body;
    }
    public void send(OutputStream outputStream) {
        String Tosend=header+"\r\n"+body+"\r\n";
        try {
            outputStream.write(Tosend.getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

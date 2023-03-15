package Client;

import java.io.IOException;
import java.io.OutputStream;

public class PublishRequest extends Request{
    public PublishRequest(String username,String body)
    {
        super("PUBLISH author:@"+username,body);
    }
    @Override
    public void send(OutputStream outputStream) {
        String Tosend=header+"\r\n"+body+"\r\n";
        try {
            outputStream.write(Tosend.getBytes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}

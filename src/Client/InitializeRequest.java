package Client;

import java.io.OutputStream;

public class InitializeRequest extends Request{
    public InitializeRequest(String header,String body)
    {
        super("Initialize :@"+header,body);
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

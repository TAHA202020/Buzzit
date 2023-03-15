package Client;

import java.io.OutputStream;

public class SubscribeTagRequest extends Request{
    public SubscribeTagRequest(String tag)
    {
        super("SUBSCRIBE tag:#"+tag,"");
    }
}

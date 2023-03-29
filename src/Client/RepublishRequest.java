package Client;

public class RepublishRequest extends Request{

    public RepublishRequest( String author,String id) {
        super("REPUBLISH author:@"+author+" msg_ig:"+id, "");
    }
}
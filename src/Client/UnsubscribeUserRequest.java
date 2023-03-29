package Client;

public class UnsubscribeUserRequest extends Request{
    public UnsubscribeUserRequest(String name) {
        super("UNSUBSCRIBE author:@"+name,"");
    }
}

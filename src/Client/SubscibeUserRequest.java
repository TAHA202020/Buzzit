package Client;

public class SubscibeUserRequest extends Request{
    public SubscibeUserRequest(String author)
    {
        super("SUBSCRIBE author:@"+author,"");
    }
}

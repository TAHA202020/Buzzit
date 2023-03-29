package Client;

public class ReplyRequest extends Request{

    public ReplyRequest(String username,int id, String body) {
        super("REPLY author:@"+username+" reply_to_id:"+id, body);
    }
}

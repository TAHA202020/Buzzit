package Client;

public class ReceiveMessage extends Request{
    public ReceiveMessage(int id) {
        super("RCV_MSG msg_id:"+id, "");
    }
}

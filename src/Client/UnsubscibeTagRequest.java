package Client;

public class UnsubscibeTagRequest extends Request{
    public UnsubscibeTagRequest(String tag) {
        super("UNSUBSCRIBE tag:#"+tag, "");
    }
}

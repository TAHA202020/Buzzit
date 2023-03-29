package Client;

public class ReceiveIds extends Request{
    public ReceiveIds(String author,String tag,String id,String limit) {
        super("RCV_IDS", "");
        if (author!=null)
            header=header+" author:@"+author;
        if (tag!=null)
            header=header+" tag:#"+tag;
        if (id!=null)
            header=header+" since_id:"+id;
        if (limit!=null)
            header=header+" limit:"+limit;
        else
            header=header+" limit:"+5;
    }
}

package Server;

import Client.Client;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    List<Author> subscribers=new ArrayList<>();
    String tag="";
    public Tag(String tag)
    {
        this.tag=tag;
    }
    public boolean equals(Tag tag)
    {
        return this.tag.equals(tag.tag);
    }
    public void notifySubscribers(String message)
    {
        for (Author cl:subscribers)
        {
            cl.cl.sendMessage("NOTIFY tag:#"+tag,message);
        }
    }

    public void addSubscribers(Author subscriber) {
        this.subscribers.add(subscriber);
    }
    public void removeSubscriber(Author sub){this.subscribers.remove(this);}
}

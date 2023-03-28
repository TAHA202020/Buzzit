package Server;

import Client.Client;

import java.util.ArrayList;
import java.util.List;

public class Tag {
    List<ClientHandler> subscribers=new ArrayList<>();
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
        for (ClientHandler cl:subscribers)
        {
            cl.sendMessage("NOTIFY tag:#"+tag,message);
        }
    }

    public void addSubscribers(ClientHandler subscriber) {
        this.subscribers.add(subscriber);
    }
}

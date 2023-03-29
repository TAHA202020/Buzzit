package Server;

import java.util.ArrayList;
import java.util.List;

public class Author {
    ClientHandler cl;
    String name;
    List<Message> messages=new ArrayList<>();
    List<Author> subsribers=new ArrayList<>();
    public Author(String name)
    {
        this.name=name;
    }

    public void setCl(ClientHandler cl) {
        this.cl = cl;
    }

    public void addSubscriber(Author client)
    {
        subsribers.add(client);
    }
    public void removeSubscriber(Author sub){subsribers.remove(sub);}
    public void addMessage(Message message)
    {
        messages.add(message);
    }
    public void notifySubscribers(String message)
    {
        for (Author subscriber:subsribers)
        {
            subscriber.cl.sendMessage("NOTIFY",name+" said :"+message);
        }
    }

    public boolean equals(Author author) {
        return name.equals(author.name);
    }
}

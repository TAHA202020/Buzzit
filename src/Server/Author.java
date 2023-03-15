package Server;

import java.util.ArrayList;
import java.util.List;

public class Author {
    String name;
    List<Message> messages=new ArrayList<>();
    List<ClientHandler> subsribers=new ArrayList<>();
    public Author(String name)
    {
        this.name=name;
    }
    public void addSubscriber(ClientHandler client)
    {
        subsribers.add(client);
    }
    public void addMessage(Message message)
    {
        messages.add(message);
    }
    public void notifySubscribers(String message)
    {
        for (ClientHandler subscriber:subsribers)
        {
            subscriber.sendMessage("NOTIFY",message);
        }
    }

    public boolean equals(Author author) {
        return name.equals(author.name);
    }
}

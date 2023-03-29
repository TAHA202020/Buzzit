package Server;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private List<Tag> tags=new ArrayList<>();
    Author author;
    int id;
    private final String message;

    public Message(String message,int id,Author author) {
        this.author=author;
        this.id=id;
        this.message = message;
        ParseTags();
    }
    public String getMessage() {
        return message;
    }


    private void ParseTags() {
        String[] words=message.split(" ");
        for (String word:words)
        {
            if (word.contains("#"))
            {
                if (word.split("#").length>1)
                    tags.add(new Tag(word.split("#")[1]));
            }
        }
    }
    public boolean containsTag(String hashtag)
    {
        for (Tag tag:tags)
            if (tag.tag.equals(hashtag))
                return true;
        return false;
    }
    public List<Tag> getTag() {
        return tags;
    }
}

package Server;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private List<Tag> tags=new ArrayList<>();
    private final String message;

    public Message(String message) {
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

    public List<Tag> getTag() {
        return tags;
    }
}

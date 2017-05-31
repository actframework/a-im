package chatroom.model;

import act.data.annotation.Data;
import act.util.SimpleBean;

/**
 * A chat message
 */
@Data
public class Message implements SimpleBean {

    public String text;

    public String room;

}

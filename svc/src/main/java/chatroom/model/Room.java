package chatroom.model;

import act.cli.Command;
import act.cli.Optional;
import act.cli.Required;
import act.controller.annotation.UrlContext;
import act.db.morphia.MorphiaAdaptiveRecord;
import act.db.morphia.MorphiaDao;
import act.validation.NotBlank;
import org.mongodb.morphia.annotations.Entity;
import org.osgl.aaa.AAA;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.util.E;
import org.osgl.util.S;

import static chatroom.security.AppPrivileges.PRIV_ADMIN;

/**
 * A chat room
 */
@SuppressWarnings("unused")
@Entity("room")
public class Room extends MorphiaAdaptiveRecord<Room> {

    public static final String MAIN = "__def__";

    public String name;
    public String desc;

    private Room(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public static boolean isMain(String roomName) {
        return S.eq(MAIN, roomName);
    }

    @UrlContext("/admin/rooms")
    public static class Dao extends MorphiaDao<Room> {

        @Command(name = "room.create", help = "create new chat room")
        @PostAction
        public Room create(
                @Required("specify the room name") @NotBlank String name,
                @Optional("specify the room description") String desc
        ) {
            AAA.requirePrivilege(PRIV_ADMIN);
            E.illegalArgumentIf(countBy("name", name) > 0, "Room already exists: %s", name);
            Room room = new Room(name, desc);
            return save(room);
        }

    }


}

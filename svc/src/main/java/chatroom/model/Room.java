package chatroom.model;

import act.app.ActionContext;
import act.cli.Command;
import act.cli.Optional;
import act.cli.Required;
import act.controller.annotation.UrlContext;
import act.db.morphia.MorphiaAdaptiveRecord;
import act.db.morphia.MorphiaDao;
import act.util.Stateless;
import act.validation.NotBlank;
import chatroom.util.LangUtil;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Indexed;
import org.osgl.$;
import org.osgl.aaa.AAA;
import org.osgl.mvc.annotation.PostAction;
import org.osgl.util.C;
import org.osgl.util.E;
import org.osgl.util.S;

import java.util.List;

import static chatroom.security.AppPrivileges.PRIV_ADMIN;

/**
 * A chat room
 */
@SuppressWarnings("unused")
@Entity("room")
public class Room extends MorphiaAdaptiveRecord<Room> {

    public static final String MAIN = "__def__";

    @Indexed(unique = true)
    public String name;
    public String desc;
    public boolean isChinese;

    public Room(String name, String desc) {
        this.name = $.notNull(name);
        this.desc = S.blank(desc) ? name : desc;
        this.isChinese = hasChinese(name) || hasChinese(desc);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Room) {
            Room that = (Room) obj;
            return S.eq(name, that.name);
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }

    private boolean hasChinese(String s) {
        return s.codePoints().anyMatch(codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN);
    }

    public static boolean isMain(String roomName) {
        return S.eq(MAIN, roomName);
    }


    @UrlContext("/admin/rooms")
    @Stateless
    public static class Dao extends MorphiaDao<Room> {

        public List<Room> findByLanuage(ActionContext context) {
            boolean isChinese = LangUtil.isZh(context);
            return C.list(findBy("isChinese", isChinese));
        }

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

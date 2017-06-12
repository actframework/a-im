package chatroom;

import act.Act;
import act.aaa.LoginUser;
import act.app.ActionContext;
import act.controller.annotation.TemplateContext;
import act.job.OnAppStart;
import act.util.Global;
import act.util.Output;
import chatroom.model.Room;
import chatroom.model.User;
import chatroom.util.LangUtil;
import chatroom.util.NameGenerator;
import org.osgl.aaa.NoAuthentication;
import org.osgl.mvc.annotation.Before;
import org.osgl.mvc.annotation.GetAction;
import org.osgl.util.IO;
import org.osgl.util.S;

import javax.inject.Inject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static act.controller.Controller.Util.render;

@SuppressWarnings("unused")
@TemplateContext("/")
public class AppEntry {

    @LoginUser
    @Output
    User me;

    @Inject
    NameGenerator nameGenerator;

    @Before
    @NoAuthentication
    @Global
    public void generateNickName(ActionContext context) {
        String nickname = context.session("nickname");
        if (null == nickname) {
            nickname = null == me ? nameGenerator.random(context) : me.nickname;
            context.session("nickname", nickname);
        }
        boolean isChinese = LangUtil.isZh(context);
        context.renderArg("zh", isChinese);
    }

    @GetAction
    @NoAuthentication
    public void index(ActionContext context) {
        String room = Room.MAIN;
        render(room, room);
    }

    @GetAction("/rooms")
    @NoAuthentication
    public void rooms(Room.Dao dao, ActionContext context) {
        List<Room> rooms = dao.findByLanuage(context);
        render(rooms);
    }

    @OnAppStart
    public static void generateDefaultRooms(Room.Dao dao) {
        if (0 < dao.count()) {
            return;
        }
        dao.save(loadDefaultRooms());
    }

    private static List<Room> loadDefaultRooms() {
        InputStream inputStream = AppEntry.class.getResourceAsStream("/rooms.txt");
        List<String> lines = IO.readLines(inputStream);
        List<Room> rooms = new ArrayList<>(lines.size());
        for (String line : lines) {
            if (line.startsWith("#") || S.isBlank(line)) {
                continue;
            }
            int pos = line.indexOf('=');
            String name = line.substring(0, pos);
            String desc = line.substring(pos + 1, line.length());
            rooms.add(new Room(name.trim(), desc.trim()));
        }
        return rooms;
    }

    public static void main(String[] args) throws Exception {
        Act.start("A-Chat");
    }

}

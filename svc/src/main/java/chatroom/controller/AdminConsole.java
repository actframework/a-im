package chatroom.controller;

import act.cli.Command;
import chatroom.AppEntry;
import chatroom.model.Room;

public class AdminConsole {

    @Command(name = "room.reset", help = "reset to default rooms")
    public void resetRooms(Room.Dao dao) {
        dao.drop();
        AppEntry.generateDefaultRooms(dao);
    }

}

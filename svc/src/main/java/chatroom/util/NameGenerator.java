package chatroom.util;

import act.app.ActionContext;
import org.osgl.util.IO;

import javax.inject.Singleton;
import java.util.List;
import java.util.Random;

@Singleton
public class NameGenerator {

    public String random(ActionContext context) {
        List<String> names = names(LangUtil.isZh(context));
        Random random = new Random();
        int id = random.nextInt(names.size());
        return names.get(id);
    }

    private List<String> names(boolean zh) {
        String resourceName = zh ? "/names-zh.txt" : "/names.txt";
        return IO.readLines(NameGenerator.class.getResource(resourceName));
    }

}

package chatroom.util;

import act.app.ActionContext;

public class LangUtil {

    public static boolean isZh(ActionContext context) {
        return "zh".equals(lang(context));
    }

    public static String lang(ActionContext context) {
        return context.locale(true).getLanguage();
    }

}

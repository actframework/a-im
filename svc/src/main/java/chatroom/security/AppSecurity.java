package chatroom.security;

import act.aaa.ActAAAService;
import chatroom.model.User;
import org.osgl.aaa.Principal;
import org.osgl.util.C;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * Plugin the aaa security framework
 */
@Singleton
public class AppSecurity extends ActAAAService.Base<User> implements AppPrivileges {

    private ConcurrentMap<String, User> userCache = new ConcurrentHashMap<>();

    public AppSecurity() {
        System.out.println("");
    }

    @Override
    protected final String username(User user) {
        return user.email;
    }

    @Override
    protected final Set<String> permissionsOf(User user) {
        return C.set();
    }

    @Override
    protected Integer privilegeOf(User user) {
        return user.privilege;
    }

    @Override
    protected Set<String> rolesOf(User user) {
        return super.rolesOf(user);
    }

    @Override
    protected final boolean verifyPassword(User user, char[] chars) {
        return user.verifyPassword(new String(chars));
    }

    @Override
    protected void setPrincipalProperties(Principal principal, User user) {
        userCache.put(principal.getName(), user);
    }

    public User getUserFromPrincipal(Principal principal) {
        return userCache.get(principal.getName());
    }

    /**
     * Returns permissions granted to every logged in user
     * @return the set of common permissions
     */
    public static Set<String> newCommonPermissions() {
        Set<String> perms = new HashSet<>();
        return perms;
    }

}

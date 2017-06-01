package chatroom.model;

import act.Act;
import act.db.morphia.MorphiaAdaptiveRecord;
import act.db.morphia.MorphiaDao;
import act.social.SocialProfile;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity("user")
public class User extends MorphiaAdaptiveRecord<User> {

    public String email;
    public String screenname;
    private String password;
    public int privilege;
    public Map<String, SocialProfile> socialProfiles = new HashMap<>();
    public Set<String> rooms = new HashSet<>();

    public User(SocialProfile socialProfile) {
        email = socialProfile.getEmail();
        screenname = socialProfile.getDisplayName();
    }

    /**
     * Set an new password on the user account.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = Act.crypto().passwordHash(password);
    }

    /**
     * Verify a password against the user's password
     * @param password the password
     * @return `true` if password verified or `false` otherwise
     */
    public boolean verifyPassword(String password) {
        return Act.crypto().verifyPassword(password, this.password);
    }

    /**
     * Link a social profile to this user
     * @param profile a social profile
     * @return true if not already linked otherwise false
     */
    public boolean linkSocialProfile(SocialProfile profile) {
        String provider = profile.getId().getProvider();
        if (!socialProfiles.containsKey(provider)) {
            socialProfiles.put(provider, profile);
            return true;
        }
        return false;
    }

    public static class Dao extends MorphiaDao<User> {

        /**
         * Fine a user by either ID or email
         * @param idOrEmail the string to find the user
         * @return the user found or `null` if not found
         */
        public User findByIdOrEmail(String idOrEmail) {
            if (ObjectId.isValid(idOrEmail)) {
                return findById(idOrEmail);
            }
            return findOneBy("email", idOrEmail);
        }

        /**
         * Link a social profile to a user
         * @param user the user
         * @param profile the social profile
         * @return the user with social profile linked
         */
        public User linkSocialProfile(User user, SocialProfile profile) {
            return user.linkSocialProfile(profile) ? save(user) : user;
        }

        /**
         * Find a user with a matching email and password
         * @param email an email
         * @param password a password
         * @return a user if the email and password match, else null
         */
        public User authenticate(String email, String password) {
            User user = findOneBy("email", email.toLowerCase());
            if (null == user) {
                return null;
            }

            return user.verifyPassword(password) ? user : null;
        }

        /**
         * Join a user into a chat roomName
         * @param user the user
         * @param roomName the name of the room to join
         * @return the user that has joined the chat roomName
         */
        public User joinRoom(User user, String roomName) {
            user.rooms.add(roomName);
            return save(user);
        }

        /**
         * Quit a user from a chat room
         * @param user the user
         * @param roomName the name of the room from which the user to quit
         * @return the user with room quit
         */
        public User quitRoom(User user, String roomName) {
            user.rooms.remove(roomName);
            return save(user);
        }

    }


}

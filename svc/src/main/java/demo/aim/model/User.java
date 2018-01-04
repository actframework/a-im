package demo.aim.model;

import act.Act;
import act.cli.Required;
import act.crypto.AppCrypto;
import act.db.morphia.MorphiaAdaptiveRecord;
import act.db.morphia.MorphiaDao;
import act.util.Stateless;
import org.mongodb.morphia.annotations.Entity;
import org.osgl.util.S;

import javax.inject.Inject;

@Entity("user")
public class User extends MorphiaAdaptiveRecord<User> {

    @Required
    public String email;
    public String firstName;
    public String lastName;
    private String password;

    public boolean isActivated() {
        return S.isEmpty(password);
    }

    public boolean verifyPassword(char[] password) {
        return Act.crypto().verifyPassword(password, this.password);
    }

    @Stateless
    public static class Dao extends MorphiaDao<User> {

        @Inject
        private AppCrypto crypto;

        public User findByEmail(String email) {
            return findOneBy("email", email);
        }

        public User authenticate(String email, char[] password) {
            User user = findByEmail(email);
            return null != user ? crypto.verifyPassword(password, user.password) ? user : null : null;
        }

        public void activate(User user, char[] password) {
            user.password = crypto.passwordHash(password);
            save(user, "password", user.password);
        }

        public boolean exists(String email) {
            return countBy("email", email) > 0;
        }
    }

}

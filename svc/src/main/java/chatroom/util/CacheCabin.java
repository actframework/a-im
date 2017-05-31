package chatroom.util;

import act.Act;
import act.util.SingletonBase;
import org.bson.types.ObjectId;
import org.osgl.cache.CacheService;
import org.osgl.util.E;

import javax.inject.Inject;

/**
 * A `CacheCabin` can be used to put any type of object inside and then
 * retrieve it before time expired
 */
@SuppressWarnings("unused")
public class CacheCabin extends SingletonBase {

    private CacheService cache;

    private int expiration;

    @Inject
    public CacheCabin(CacheService cache) {
        this(60 * 60, cache);
    }

    public CacheCabin(int expiration, CacheService cache) {
        E.illegalArgumentIf(expiration < 0);
        this.expiration = expiration;
        this.cache = cache;
    }

    public String deposit(Object value) {
        String key = new ObjectId().toHexString();
        cache.put(key, value, expiration);
        return key;
    }

    public <T> T take(String key) {
        T value = cache.get(key);
        cache.evict(key);
        return value;
    }

    public CacheCabin get(int expiration) {
        return new CacheCabin(expiration, Act.cache());
    }
}

package demo.aim.model;

import act.db.morphia.MorphiaAdaptiveRecord;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Property;

@Entity("channel")
public class Channel extends MorphiaAdaptiveRecord<Channel> {

    public String name;

    @Property("desc")
    public String description;

}

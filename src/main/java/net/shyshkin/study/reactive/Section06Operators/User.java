package net.shyshkin.study.reactive.Section06Operators;

import lombok.Data;
import net.shyshkin.study.reactive.courseutil.Util;

@Data
public class User {

    private int userId;
    private String name;

    public User(int userId) {
        this.userId = userId;
        name = Util.FAKER.name().firstName();
    }
}

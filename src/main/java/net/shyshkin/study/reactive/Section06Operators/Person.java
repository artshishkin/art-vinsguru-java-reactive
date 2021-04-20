package net.shyshkin.study.reactive.Section06Operators;

import lombok.Data;
import net.shyshkin.study.reactive.courseutil.Util;

@Data
public class Person {

    private String name;
    private int age;

    public Person() {
        this.age = Util.FAKER.random().nextInt(1, 30);
        this.name = Util.FAKER.name().firstName();
    }
}

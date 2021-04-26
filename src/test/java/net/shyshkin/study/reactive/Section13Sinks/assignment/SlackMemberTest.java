package net.shyshkin.study.reactive.Section13Sinks.assignment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class SlackMemberTest {

    @Test
    void receives() {
        //given
        SlackMember art = new SlackMember("art");

        //when - then
        art.receives("hello");
    }

    @Test
    void says() {
        //given
        SlackMember kate = new SlackMember("kate");
        kate.setMessageConsumer(message -> log.debug("kate says: {}", message));

        //when - then
        kate.says("hello everyone");
        kate.says("I am kate");
    }
}
package net.shyshkin.study.reactive.Section13Sinks.assignment;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;

@Slf4j
class SlackRoomTest {

    @Test
    void demo() {
        //given
        SlackRoom slackRoom = new SlackRoom("room01");
        SlackMember art = new SlackMember("art");
        SlackMember kate = new SlackMember("kate");
        slackRoom.joinRoom(art);
        slackRoom.joinRoom(kate);

        //when
        art.says("hello");
        art.says("I am Art");
        art.says("Welcome");

        //then
        Util.sleep(1);
        kate.says("HI");

        Util.sleep(1);
        SlackMember arina = new SlackMember("arina");
        slackRoom.joinRoom(arina);
        arina.says("bye");
    }
}
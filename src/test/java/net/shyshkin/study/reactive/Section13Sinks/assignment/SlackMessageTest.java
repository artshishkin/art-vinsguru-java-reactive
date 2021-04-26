package net.shyshkin.study.reactive.Section13Sinks.assignment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class SlackMessageTest {

    @Test
    void builder_test() {
        //given
        String sender = "art";
        String receiver = "kate";
        String message = "hello";

        //when
        SlackMessage slackMessage = SlackMessage.builder()
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .build();

        //then
        assertAll(
                () -> assertEquals(sender, slackMessage.getSender()),
                () -> assertEquals(receiver, slackMessage.getReceiver()),
                () -> assertEquals(message, slackMessage.getMessage()),
                () -> assertNotNull(slackMessage.getDate()),
                () -> assertNotNull(slackMessage.getId()),
                () -> assertTrue(slackMessage.toString().contains("][" + sender + " -> " + receiver + "] " + message))
        );
        log.debug("{}", slackMessage);
    }
}
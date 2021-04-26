package net.shyshkin.study.reactive.Section13Sinks.assignment;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Slf4j
public class SlackRoom {

    private String name;
    private final Sinks.Many<SlackMessage> sink;
    private final Flux<SlackMessage> flux;

    public SlackRoom(String name) {
        this.name = name;
        sink = Sinks.many().replay().all();
        flux = sink.asFlux();
    }

    public void joinRoom(SlackMember member) {
        log.debug("{} ----------------joined---------------- {}", member.getName(), this.name);
        subscribe(member);
        member.setMessageConsumer(msg -> this.postMessage(msg, member));
    }

    private void postMessage(String msg, SlackMember member) {

        SlackMessage slackMessage = SlackMessage.builder()
                .message(msg)
                .sender(member.getName())
                .build();

        sink.tryEmitNext(slackMessage);

    }

    private void subscribe(SlackMember member) {
        flux
                .doOnNext(msg -> msg.setReceiver(member.getName()))
                .map(SlackMessage::toString)
                .subscribe(member::receives);
    }
}

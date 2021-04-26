package net.shyshkin.study.reactive.Section13Sinks.assignment;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class SlackMember {

    @Getter
    private final String name;

    @Setter(AccessLevel.PACKAGE)
    private Consumer<String> messageConsumer;

    public void says(String message) {
        messageConsumer.accept(message);
    }

    public void receives(String message) {
        log.debug("{}", message);
    }

}

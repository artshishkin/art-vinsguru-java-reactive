package net.shyshkin.study.reactive.Section13Sinks.assignment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SlackMessage {

    private static final String FORMAT = "[%s][%s -> %s] %s";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String message;
    private String sender;
    private String receiver;

    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();

    @Override
    public String toString() {
        return String.format(FORMAT,
                date.format(DATE_TIME_FORMATTER),
                sender,
                receiver,
                message);
    }
}

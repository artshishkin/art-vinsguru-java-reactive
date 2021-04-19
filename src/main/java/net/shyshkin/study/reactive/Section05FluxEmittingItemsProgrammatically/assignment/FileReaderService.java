package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically.assignment;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Slf4j
public class FileReaderService {

    public static Flux<String> readFile(Path filePath) {

        return Flux.generate(
                openReader(filePath),
                read(),
                closeReader());
    }

    private static Consumer<BufferedReader> closeReader() {
        return bufferedReader -> {
            try {
                bufferedReader.close();
                log.debug("---Reader Closed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private static BiFunction<BufferedReader, SynchronousSink<String>, BufferedReader> read() {
        return (br, sink) -> {
            try {
                String line = br.readLine();

                if (line == null)
                    sink.complete();
                else
                    sink.next(line);

            } catch (IOException e) {
                sink.error(e);
            }
            return br;
        };
    }

    private static Callable<BufferedReader> openReader(Path filePath) {
        return () -> Files.newBufferedReader(filePath);
    }
}

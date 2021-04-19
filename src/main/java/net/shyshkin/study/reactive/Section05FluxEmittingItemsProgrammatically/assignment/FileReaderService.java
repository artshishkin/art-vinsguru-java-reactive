package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically.assignment;

import reactor.core.publisher.Flux;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileReaderService {

    public static Flux<String> readFile(Path filePath) {

        return Flux.generate(
                () -> Files.newBufferedReader(filePath),
                (bufferedReader, synchronousSink) -> {
                    try {
                        String line = bufferedReader.readLine();

                        if (line == null)
                            synchronousSink.complete();
                        else
                            synchronousSink.next(line);

                    } catch (IOException e) {
                        synchronousSink.error(e);
                    }
                    return bufferedReader;
                },
                bufferedReader -> {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}

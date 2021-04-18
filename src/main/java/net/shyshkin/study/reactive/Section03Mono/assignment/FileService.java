package net.shyshkin.study.reactive.Section03Mono.assignment;

import reactor.core.publisher.Mono;

import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    private static final Path parentPath;

    static {
        String projectDir = System.getProperty("user.dir");
        parentPath = Path.of(projectDir).resolve("src/main/resources/assignment/Section03Mono");
    }

    public static Mono<String> read(String fileName) {
        Path fileAbsolutePath = parentPath.resolve(fileName);
        return Mono.fromCallable(() -> Files.readString(fileAbsolutePath));
    }

    public static Mono<Void> write(String fileName, String content) {
        Path fileAbsolutePath = parentPath.resolve(fileName);
        return Mono.fromCallable(() -> Files.writeString(fileAbsolutePath, content)).then();
    }

    public static Mono<Void> delete(String fileName) {
        return Mono.fromCallable(
                () -> {
                    Files.delete(parentPath.resolve(fileName));
                    return "OK";
                }
        )
                .then();
    }

    public static boolean fileExist(String fileName) {
        return Files.exists(parentPath.resolve(fileName));
    }
}

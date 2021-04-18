package net.shyshkin.study.reactive.Section03Mono.assignment;

import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class VinsguruFileService {
    private static final Path parentPath;

    static {
        String projectDir = System.getProperty("user.dir");
        parentPath = Path.of(projectDir).resolve("src/main/resources/assignment/Section03Mono");
    }

    public static Mono<String> read(String fileName) {
        return Mono.fromSupplier(() -> readFile(fileName));
    }

    public static Mono<Void> write(String fileName, String content) {
        return Mono.fromRunnable(() -> writeFile(fileName, content));
    }

    public static Mono<Void> delete(String fileName) {
        return Mono.fromRunnable(() -> deleteFile(fileName));
    }

    public static boolean fileExist(String fileName) {
        return Files.exists(parentPath.resolve(fileName));
    }


    private static String readFile(String fileName) {
        Path fileAbsolutePath = parentPath.resolve(fileName);
        try {
            return Files.readString(fileAbsolutePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void writeFile(String fileName, String content) {
        Path fileAbsolutePath = parentPath.resolve(fileName);
        try {
            Files.writeString(fileAbsolutePath, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void deleteFile(String fileName) {
        Path fileAbsolutePath = parentPath.resolve(fileName);
        try {
            Files.delete(fileAbsolutePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

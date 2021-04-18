package net.shyshkin.study.reactive.Section03Mono.assignment;

import lombok.extern.slf4j.Slf4j;
import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class VinsguruFileServiceTest {

    @ParameterizedTest
    @MethodSource
    void read_success(String filename, String content) {

        //when
        Mono<String> mono = VinsguruFileService.read(filename)
                .doOnNext(System.out::println);

        //then
        StepVerifier.create(mono)
                .expectNext(content)
                .verifyComplete();
    }

    private static Stream<Arguments> read_success() {
        return Stream.of(
                Arguments.of("one_line.txt", "Just one line"),
                Arguments.of("many_lines.txt", "line 1\r\nline 2\r\nline 3")
        );
    }

    @Test
    void read_fileNotFound() {

        //given
        String filename = "absent_file.txt";
        //when
        Mono<String> mono = VinsguruFileService.read(filename)
                .doOnNext(System.out::println)
                .doOnError(Util.onError);

        //then
        StepVerifier.create(mono)
                .verifyErrorSatisfies(ex -> assertAll(
                        () -> assertEquals(NoSuchFileException.class, ex.getCause().getClass()),
                        () -> assertTrue(ex.getMessage().contains(filename))
                ));
    }

    @Test
    void write_success() {
        //given
        String filename = "file_to_write.txt";
        String content = "write success: " + LocalDateTime.now();

        //when
        Mono<Void> mono = VinsguruFileService.write(filename, content)
                .doOnNext(System.out::println);

        //then
        StepVerifier.create(mono)
                .verifyComplete();

        Mono<String> result = VinsguruFileService.read(filename)
                .doOnNext(Util.onNext);

        StepVerifier.create(result)
                .expectNext(content)
                .verifyComplete();
    }

    @Test
    void delete_success() {
        //given
        String filename = "file_to_delete.txt";
        String content = "delete file content. created: " + LocalDateTime.now();

        Mono<Void> mono = VinsguruFileService.write(filename, content)
                .doOnNext(System.out::println);

        StepVerifier.create(mono)
                .verifyComplete();
        assertTrue(VinsguruFileService.fileExist(filename));

        //when
        Mono<Void> deleteMono = VinsguruFileService.delete(filename);

        //then
        StepVerifier.create(deleteMono)
                .verifyComplete();

        assertFalse(VinsguruFileService.fileExist(filename));
    }

    @Test
    void delete_fileNotFound() {

        //given
        String filename = "absent_file.txt";

        //when
        Mono<Void> mono = VinsguruFileService.delete(filename)
                .doOnError(Util.onError);

        //then
        StepVerifier.create(mono)
                .verifyErrorSatisfies(ex -> assertAll(
                        () -> assertEquals(NoSuchFileException.class, ex.getCause().getClass()),
                        () -> assertTrue(ex.getMessage().contains(filename))
                ));
    }

}
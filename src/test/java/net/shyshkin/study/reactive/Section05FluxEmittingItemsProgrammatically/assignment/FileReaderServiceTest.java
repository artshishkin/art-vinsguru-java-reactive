package net.shyshkin.study.reactive.Section05FluxEmittingItemsProgrammatically.assignment;

import net.shyshkin.study.reactive.courseutil.Util;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileReaderServiceTest {

    @Test
    void read_successAll() {

        //given
        Path path = Path.of("src/main/resources/assignment/Section04FluxGenerate/many_lines.txt");

        //when
        Flux<String> flux = FileReaderService.readFile(path);

        //then
        flux.subscribe(Util.subscriber());

        StepVerifier
                .create(flux)
                .expectNext("line 1")
                .expectNext("line 2")
                .expectNext("line 3")
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    void read_successPart() {

        //given
        Path path = Path.of("src/main/resources/assignment/Section04FluxGenerate/many_lines.txt");

        //when
        Flux<String> flux = FileReaderService.readFile(path);

        //then
        StepVerifier
                .create(flux.log().take(3))
                .expectNext("line 1")
                .expectNext("line 2")
                .expectNext("line 3")
                .verifyComplete();
    }

    @Test
    void read_fileAbsent() {

        //given
        Path path = Path.of("src/main/resources/assignment/Section04FluxGenerate/file_absent.txt");

        //when
        Flux<String> flux = FileReaderService.readFile(path);

        //then
        flux.subscribe(Util.subscriber());

        StepVerifier
                .create(flux)
                .verifyErrorSatisfies(ex -> assertAll(
                        () -> assertEquals(NoSuchFileException.class, ex.getClass()),
                        () -> assertTrue(ex.getMessage().contains(path.toString()))
                ));
    }

    @Test
    void read_error() {

        //given
        Path path = Path.of("src/main/resources/assignment/Section04FluxGenerate/many_lines.txt");

        //when
        Flux<String> flux = FileReaderService
                .readFile(path)
                .map(s -> {
                    if (Util.FAKER.random().nextInt(1, 10) > 6)
                        throw new RuntimeException("Fake Exception for Testing");
                    return s;
                });

        //then
        flux.subscribe(Util.subscriber());
//        StepVerifier
//                .create(flux)
//                .verifyErrorSatisfies(ex -> assertAll(
//                        () -> assertEquals(RuntimeException.class, ex.getClass()),
//                        () -> assertEquals("Fake Exception for Testing", ex.getMessage())
//                ));
    }
}
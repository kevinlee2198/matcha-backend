package com.wilton.matcha.common.util;

import static com.wilton.matcha.common.util.FunctionalUtil.invokeSafely;
import static com.wilton.matcha.common.util.FunctionalUtil.safeConsumer;
import static com.wilton.matcha.common.util.FunctionalUtil.safeFunction;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class FunctionUtilTest {
    private static final boolean DONT_THROW_EXCEPTION = false;
    private static final boolean THROW_EXCEPTION = true;

    @Test
    public void checkedExceptionsAreConvertedToRuntimeExceptions() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> invokeSafely(this::methodThatThrows))
                .withCauseInstanceOf(Exception.class);
    }

    @Test
    public void ioExceptionsAreConvertedToUncheckedIoExceptions() {
        assertThatExceptionOfType(UncheckedIOException.class)
                .isThrownBy(() -> invokeSafely(this::methodThatThrowsIOException))
                .withCauseInstanceOf(IOException.class);
    }

    @Test
    public void runtimeExceptionsAreNotWrapped() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> invokeSafely(this::methodWithCheckedSignatureThatThrowsRuntimeException))
                .withNoCause();
    }

    @Test
    public void canUseConsumerThatThrowsCheckedExceptionInLambda() {
        safeConsumer(this::consumerMethodWithChecked).accept(DONT_THROW_EXCEPTION);
    }

    @Test
    public void exceptionsForConsumersAreConverted() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> safeConsumer(this::consumerMethodWithChecked).accept(THROW_EXCEPTION))
                .withCauseExactlyInstanceOf(Exception.class);
    }

    @Test
    public void canUseFunctionThatThrowsCheckedExceptionInLambda() {
        Optional<String> result = Stream.of(DONT_THROW_EXCEPTION)
                .map(safeFunction(this::functionMethodWithChecked))
                .findFirst();
        assertThat(result).isPresent().contains("Hello");
    }

    @Test
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void exceptionsForFunctionsAreConverted() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> Stream.of(THROW_EXCEPTION)
                        .map(safeFunction(this::functionMethodWithChecked))
                        .findFirst())
                .withCauseExactlyInstanceOf(Exception.class);
    }

    @Test
    public void interruptedExceptionShouldSetInterruptedOnTheThread() {
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> invokeSafely(this::methodThatThrowsInterruptedException))
                .withCauseInstanceOf(InterruptedException.class);

        assertThat(Thread.currentThread().isInterrupted()).isTrue();

        // Clear interrupt flag
        Thread.interrupted();
    }

    private String methodThatThrows() throws Exception {
        throw new Exception("Throwing an exception");
    }

    private String methodThatThrowsIOException() throws IOException {
        throw new IOException("Throwing an IOException");
    }

    private String methodWithCheckedSignatureThatThrowsRuntimeException() {
        throw new RuntimeException("Throwing RuntimeException");
    }

    private String methodThatThrowsInterruptedException() throws InterruptedException {
        throw new InterruptedException();
    }

    private void consumerMethodWithChecked(Boolean shouldThrow) throws Exception {
        if (shouldThrow) {
            throw new Exception("I should throw");
        }
    }

    private String functionMethodWithChecked(Boolean shouldThrow) throws Exception {
        if (shouldThrow) {
            throw new Exception("I should throw");
        }
        return "Hello";
    }
}

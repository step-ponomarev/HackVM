package edu.nand2tetris.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.nand2tetris.Constants;
import edu.nand2tetris.Resources;


public final class ParserTest {
    @Test
    public void testEmptyFile() throws IOException {
        final Path file = Resources.RESOURCES_DIR.resolve("Empty.vm");
        Files.createFile(file);

        try (Parser parser = new Parser((file))) {
            parser.advance();

            Assertions.assertFalse(parser.hasMoreLines());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void testOneCommand() throws IOException {
        final Path file = Resources.RESOURCES_DIR.resolve("OneCommand.vm");
        Files.createFile(file);

        Files.writeString(file, "push local 1");
        try (Parser parser = new Parser((file))) {
            parser.advance();

            Assertions.assertFalse(parser.hasMoreLines());
        } finally {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void testPushPop() throws IOException {
        final Path file = Resources.RESOURCES_DIR.resolve("PushPop.vm");
        Files.createFile(file);

        final String[] segments = {"argument", "local", "static", "constant", "this", "that", "pointer", "temp"};
        final String pushPop = """
                push %s %d
                pop %s %d
                """;

        final StringBuilder byteCode = new StringBuilder("");
        for (String seg : segments) {
            for (int i = 0; i < Constants.MAX_DECIMAL_VALUE; i++) {
                byteCode.append(
                        pushPop.formatted(seg, i, seg, i)
                );
            }
        }
        Files.writeString(file, byteCode.toString());

        try (Parser parser = new Parser((file))) {
            for (String seg : segments) {
                for (int i = 0; i < Constants.MAX_DECIMAL_VALUE; i++) {
                    parser.advance();
                    Assertions.assertEquals(CommandType.C_PUSH, parser.commandType());
                    Assertions.assertEquals(seg, parser.arg1());
                    Assertions.assertEquals(i, parser.arg2());

                    parser.advance();
                    Assertions.assertEquals(CommandType.C_POP, parser.commandType());
                    Assertions.assertEquals(seg, parser.arg1());
                    Assertions.assertEquals(i, parser.arg2());
                }
            }

            Assertions.assertFalse(parser.hasMoreLines());
        } finally {
            Files.deleteIfExists(file);
        }
    }
}

package edu.nand2tetris.code.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import edu.nand2tetris.CommandType;
import edu.nand2tetris.Resources;
import edu.nand2tetris.Segment;

public final class CodeWriterTest {
    private static final Set<CommandType> PUSH_POP_COMMAND_TYPES = new HashSet<>() {{
        add(CommandType.C_POP);
        add(CommandType.C_PUSH);
    }};

    @Test
    public void testPushPopIllegalCommands() {
        for (CommandType type : CommandType.values()) {
            if (PUSH_POP_COMMAND_TYPES.contains(type)) {
                continue;
            }

            Assertions.assertThrows(IllegalStateException.class, () -> CodeWriter.handlePushPop(type, Segment.LOCAL, 0));
            Assertions.assertThrows(IllegalStateException.class, () -> CodeWriter.generateConstantPushOrPopCode(type, 0));
        }

        for (CommandType type : PUSH_POP_COMMAND_TYPES) {
            CodeWriter.handlePushPop(type, Segment.LOCAL, 0);
            CodeWriter.generateConstantPushOrPopCode(type, 0);
        }
    }

    @Test
    public void testWritePushPopIllegalCommands() throws IOException {
        final Path path = Resources.RESOURCES_DIR.resolve("mock");
        Files.createFile(path);

        try (CodeWriter codeWriter = new CodeWriter(path)) {
            for (CommandType type : CommandType.values()) {
                if (PUSH_POP_COMMAND_TYPES.contains(type)) {
                    continue;
                }

                Assertions.assertThrows(IllegalStateException.class, () -> codeWriter.writePushPop(type, Segment.LOCAL, 0));
            }

            for (CommandType type : PUSH_POP_COMMAND_TYPES) {
                codeWriter.writePushPop(type, Segment.LOCAL, 0);
            }
        } finally {
            Files.deleteIfExists(path);
        }
    }
}

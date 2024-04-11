package edu.nand2tetris.code.writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
    private static final String PUSH_TEMPLATE = "push %s %d";
    private static final String POP_TEMPLATE = "pop %s %d";
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

    @Test
    public void testSimpleSegmentsPush() throws IOException {
        testSimpleSegmentsPushPop(CommandType.C_PUSH);
    }

    @Test
    public void testSimpleSegmentsPop() throws IOException {
        testSimpleSegmentsPushPop(CommandType.C_POP);
    }

    private static void testSimpleSegmentsPushPop(CommandType commandType) throws IOException {
        if (commandType != CommandType.C_PUSH && commandType != CommandType.C_POP) {
            throw new IllegalArgumentException("Unsupported command type: " + commandType);
        }

        final String prefix = commandType == CommandType.C_PUSH ? "push" : "pop";
        final Path cmpDir = Resources.RESOURCES_DIR.resolve("%s/cmp".formatted(prefix));
        final Path testDir = Resources.RESOURCES_DIR.resolve("%s/test".formatted(prefix));
        Files.createDirectory(testDir);
        try {
            final Path vmFile = Resources.RESOURCES_DIR.resolve("%s/Push.vm".formatted(prefix));
            final String pushAsmFileNameTemplate = prefix + "_%s.asm";
            final int testIndex = 0;

            final Segment[] segments = {Segment.LOCAL, Segment.CONSTANT, Segment.ARGUMENT};
            for (Segment segment : segments) {
                Files.createFile(vmFile);

                String register = segment.getRegister(testIndex);
                if (register == null) {
                    register = "constant";
                }

                final String asmFileName = pushAsmFileNameTemplate.formatted(register);
                final Path testFile = testDir.resolve(asmFileName);
                Files.createFile(testFile);

                final BufferedWriter bufferedWriter = Files.newBufferedWriter(vmFile);
                bufferedWriter.write(
                        commandType == CommandType.C_PUSH
                                ? PUSH_TEMPLATE.formatted(register, testIndex)
                                : POP_TEMPLATE.formatted(register, testIndex)
                );
                bufferedWriter.close();

                try (final CodeWriter codeWriter = new CodeWriter(testFile);
                     final BufferedReader vmReader = Files.newBufferedReader(vmFile);
                ) {
                    codeWriter.writePushPop(CommandType.parse(vmReader.readLine()), segment, testIndex);
                } finally {
                    Files.deleteIfExists(vmFile);
                }

                final Path cmpFile = cmpDir.resolve(asmFileName);
                Assertions.assertEquals(Files.size(cmpFile), Files.size(testFile));

                try (final BufferedReader cmpFileReader = Files.newBufferedReader(cmpFile);
                     final BufferedReader testFileReader = Files.newBufferedReader(testFile);
                ) {
                    String cmpLine;
                    String testLine;
                    while ((cmpLine = cmpFileReader.readLine()) != null && (testLine = testFileReader.readLine()) != null) {
                        Assertions.assertEquals(cmpLine, testLine);
                    }
                } finally {
                    Files.deleteIfExists(testFile);
                }
            }
        } finally {
            Files.deleteIfExists(testDir);
        }
    }
}

package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import edu.nand2tetris.code.writer.CodeWriter;

public class VMTranslator {
    private static final String VM_SUFFIX = ".vm";

    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 arg, but got: " + args.length);
        }

        final Path vmFileOrDir = Paths.get(args[0]);
        if (Files.notExists(vmFileOrDir)) {
            throw new IllegalStateException("File does not exist: " + vmFileOrDir);
        }

        final boolean dir = !vmFileOrDir.getFileName().toString().endsWith(VM_SUFFIX);
        if (dir && !Files.isDirectory(vmFileOrDir)) {
            throw new IllegalArgumentException(vmFileOrDir + " is not directory");
        }

        final Path outFile = Paths.get(args[1]);
        try {
            Files.deleteIfExists(outFile);
            Files.createFile(outFile);
        } catch (IOException e) {
            throw new RuntimeException("Compilation is failed ", e);
        }

        try (CodeWriter codeWriter = new CodeWriter(outFile)) {
            if (!dir) {
                try (Parser parser = new Parser(vmFileOrDir)) {
                    handleParser(parser, codeWriter);
                }
                return;
            }

            final List<Path> files = Files.walk(vmFileOrDir).toList();
            final List<Path> invalidFiles = files.stream().filter(e -> !e.getFileName().endsWith(VM_SUFFIX)).toList();
            if (!invalidFiles.isEmpty()) {
                final StringBuilder invalidFilesStr = new StringBuilder();
                for (Path invalidFile : invalidFiles) {
                    invalidFilesStr.append(invalidFile).append(", ");
                }

                throw new IllegalStateException("Invalid files: " + invalidFilesStr);
            }

            for (Path file : files) {
                try (Parser parser = new Parser(file)) {
                    handleParser(parser, codeWriter);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Translation is failed ", e);
        }
    }

    private static void handleParser(Parser parser, CodeWriter codeWriter) throws IOException {
        while (parser.hasMoreLines()) {
            parser.advance();

            final CommandType commandType = parser.commandType();
            switch (commandType) {
                case C_PUSH, C_POP -> codeWriter.writePushPop(commandType, Segment.parse(parser.arg1()), parser.arg2());
                case C_ARITHMETIC -> codeWriter.writeArithmetic(parser.arg1());
                default -> throw new IllegalStateException("Unsupported command type: " + commandType);
            }
        }
    }
}
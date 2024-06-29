package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.nand2tetris.code.writer.CodeWriter;

public class VMTranslator {
    private static final String VM_SUFFIX = ".vm";

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            throw new IllegalArgumentException("Expected 1 arg, but got: " + args.length);
        }

        final Path vmFileOrDir = Paths.get(args[0]);
        if (Files.notExists(vmFileOrDir)) {
            throw new IllegalStateException("File does not exist: " + vmFileOrDir);
        }

        final boolean dir = !vmFileOrDir.getFileName().toString().endsWith(VM_SUFFIX);
        if (dir && !Files.isDirectory(vmFileOrDir)) {
            throw new IllegalArgumentException(vmFileOrDir + " is not directory");
        }

        if (dir) {
            handleDir(vmFileOrDir);
        } else {
            final Path outFile = args.length > 1
                    ? Paths.get(args[1])
                    : vmFileOrDir.getParent().resolve(vmFileOrDir.getFileName().toString().replace("vm", "asm"));

            handleSingleFile(vmFileOrDir, prepareOutFile(outFile));
        }
    }

    private static Path prepareOutFile(Path outFile) {
        if (Files.isDirectory(outFile)) {
            throw new IllegalStateException("Path is dir: " + outFile);
        }

        try {
            Files.deleteIfExists(outFile);
            Files.createFile(outFile);
        } catch (IOException e) {
            throw new RuntimeException("Compilation is failed ", e);
        }

        return outFile;
    }

    private static void handleSingleFile(Path srcFile, Path outFile) throws IOException {
        if (Files.isDirectory(srcFile)) {
            throw new IllegalStateException("Path is dir: " + srcFile);
        }

        if (Files.notExists(srcFile)) {
            throw new IllegalStateException("Source file is not exist: " + srcFile);
        }

        if (Files.notExists(outFile)) {
            throw new IllegalStateException("Out file is not exist: " + outFile);
        }

        try (CodeWriter codeWriter = new CodeWriter(outFile)) {
            try (Parser parser = new Parser(srcFile)) {
                handleParser(parser, codeWriter);
            }
        }
    }

    private static void handleDir(Path srcDir) {
        if (!Files.isDirectory(srcDir)) {
            throw new IllegalArgumentException("Path is not srcDir: " + srcDir);
        }

        throw new UnsupportedOperationException("Not implemented yet");

//        try (CodeWriter codeWriter = new CodeWriter(outFile)) {
//            if (!srcDir) {
//                try (Parser parser = new Parser(vmFileOrDir)) {
//                    handleParser(parser, codeWriter);
//                }
//                return;
//            }
//
//            final List<Path> files = Files.walk(vmFileOrDir).toList();
//            final List<Path> invalidFiles = files.stream().filter(e -> !e.getFileName().endsWith(VM_SUFFIX)).toList();
//            if (!invalidFiles.isEmpty()) {
//                final StringBuilder invalidFilesStr = new StringBuilder();
//                for (Path invalidFile : invalidFiles) {
//                    invalidFilesStr.append(invalidFile).append(", ");
//                }
//
//                throw new IllegalStateException("Invalid files: " + invalidFilesStr);
//            }
//
//            for (Path file : files) {
//                try (Parser parser = new Parser(file)) {
//                    handleParser(parser, codeWriter);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Translation is failed ", e);
//        }
    }

    private static void handleParser(Parser parser, CodeWriter codeWriter) throws IOException {
        while (parser.hasMoreLines()) {
            parser.advance();

            final CommandType commandType = parser.commandType();
            switch (commandType) {
                case C_PUSH, C_POP -> codeWriter.writePushPop(commandType, Segment.parse(parser.arg1()), parser.arg2());
                case C_ARITHMETIC -> codeWriter.writeArithmetic(parser.arg1());
                case C_LABEL -> codeWriter.writeLabel(parser.arg1());
                default -> throw new IllegalStateException("Unsupported command type: " + commandType);
            }
        }
    }
}
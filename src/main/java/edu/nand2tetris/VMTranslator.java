package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import edu.nand2tetris.code.writer.CodeWriter;

public class VMTranslator {
    private static final String VM_SUFFIX = ".vm";

    public static void main(String[] args) throws IOException {
        final Path vmFileOrDir = Paths.get(args[0]);
        if (Files.notExists(vmFileOrDir)) {
            throw new IllegalStateException("File does not exist: " + vmFileOrDir);
        }

        final boolean dir = !vmFileOrDir.getFileName().toString().endsWith(VM_SUFFIX);
        final Path outFile = prepareOutFile(
                args.length > 1
                        ? Paths.get(args[1])
                        : vmFileOrDir.getParent().resolve(vmFileOrDir.getFileName().toString().replace("vm", "asm"))
        );

        if (dir) {
            handleDir(vmFileOrDir, outFile);
        } else {
            handleSingleFile(vmFileOrDir, outFile);
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
            codeWriter.setFileName(getVmFileName(srcFile));

            try (Parser parser = new Parser(srcFile)) {
                handleParser(parser, codeWriter);
            }
        }
    }

    private static void handleDir(Path srcDir, Path outFile) {
        if (!Files.isDirectory(srcDir)) {
            throw new IllegalArgumentException("Path is not srcDir: " + srcDir);
        }

        try (CodeWriter codeWriter = new CodeWriter(outFile)) {
            codeWriter.initCode();

            final List<Path> files = Files.walk(srcDir).toList().stream().filter(e -> e.getFileName().toString().endsWith(VM_SUFFIX)).toList();
            for (Path srcFile : files) {
                codeWriter.setFileName(getVmFileName(srcFile));
                try (Parser parser = new Parser(srcFile)) {
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
                case C_LABEL -> codeWriter.writeLabel(parser.arg1());
                case C_GOTO -> codeWriter.writeGoto(parser.arg1());
                case C_IF -> codeWriter.writeIf(parser.arg1());
                case C_FUNCTION -> codeWriter.writeFunction(parser.arg1(), parser.arg2());
                case C_RETURN -> codeWriter.writeReturn();
                case C_CALL -> codeWriter.writeCall(parser.arg1(), parser.arg2());
                default -> throw new IllegalStateException("Unsupported command type: " + commandType);
            }
        }
    }

    private static String getVmFileName(Path srcFile) {
        String fileName = srcFile.getFileName().toString();
        return fileName.substring(0, fileName.length() - 3);
    }
}
package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.nand2tetris.code.writer.CodeWriter;

public class VMTranslator {
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Expected 2 arg, but got: " + args.length);
        }

        final Path vmFile = Paths.get(args[0]);
        if (Files.notExists(vmFile)) {
            throw new IllegalStateException("File does not exist: " + vmFile);
        }

        final Path outFile = Paths.get(args[1]);
        try {
            Files.deleteIfExists(outFile);
            Files.createFile(outFile);
        } catch (IOException e) {
            throw new RuntimeException("Compilation is failed ", e);
        }

        try (Parser parser = new Parser(vmFile);
             CodeWriter codeWriter = new CodeWriter(outFile)
        ) {
            codeWriter.init();
            while (parser.hasMoreLines()) {
                parser.advance();

                final CommandType commandType = parser.commandType();
                switch (commandType) {
                    case C_PUSH, C_POP ->
                            codeWriter.writePushPop(commandType, Segment.parse(parser.arg1()), parser.arg2());
                    case C_ARITHMETIC -> codeWriter.writeArithmetic(parser.arg1());
                    default -> throw new IllegalStateException("Unsupported command type: " + commandType);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Translation is failed ", e);
        }
    }
}
package edu.nand2tetris;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Parser implements Closeable {
    private final BufferedReader fileReader;

    private boolean eof = false;

    private String currInstruction;

    private CommandType commandType;

    private String arg1;

    private Integer arg2;

    public Parser(Path file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("Path cannot be null!");
        }

        if (Files.notExists(file)) {
            throw new IllegalStateException("Path " + file + " does not exist!");
        }

        this.fileReader = Files.newBufferedReader(file);
    }

    public boolean hasMoreLines() {
        return !eof;
    }

    public void advance() {
        if (eof) {
            throw new IllegalStateException("End of file");
        }

        String command;
        try {
            command = readNextInstruction();

            final boolean firstTime = currInstruction == null;
            if (firstTime) {
                currInstruction = readNextInstruction();
            } else {
                final String nextInstruction = command;
                command = currInstruction;
                currInstruction = nextInstruction;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (eof && command == null) {
            return;
        }

        if (command == null) {
            throw new IllegalStateException("Command is null");
        }

        commandType = CommandType.parse(command);
        arg1 = null;
        arg2 = null;

        switch (commandType) {
            case C_ARITHMETIC -> handleArithmetic(command);
            case C_PUSH, C_POP -> handlePushPop(command);
            case C_LABEL, C_IF, C_GOTO -> handleLabel(command);
            default -> throw new UnsupportedOperationException("Unsupported operation " + commandType);
        }
    }

    private void handleArithmetic(String command) {
        this.arg1 = command;
    }

    private void handleLabel(String command) {
        final String[] split = command.split("\s+");
        this.arg1 = split[1];
    }

    private void handlePushPop(String command) {
        final String[] split = command.split("\s");
        this.arg1 = split[1];
        this.arg2 = Integer.parseInt(split[2]);

        if (this.arg2 < 0 || this.arg2 > Constants.MAX_DECIMAL_VALUE) {
            throw new IllegalStateException("Value out of range [" + 0 + ", " + this.arg2 + "]");
        }
    }

    private String readNextInstruction() throws IOException {
        String currentLine;
        while ((currentLine = fileReader.readLine()) != null) {
            currentLine = currentLine.trim();
            if (currentLine.isEmpty() || TokenPatterns.COMMENT.matcher(currentLine).matches()) {
                continue;
            }

            return currentLine;
        }

        eof = true;

        return null;
    }

    public CommandType commandType() {
        return commandType;
    }

    public String arg1() {
        return arg1;
    }

    public int arg2() {
        if (arg2 == null) {
            throw new IllegalStateException("Unsupported method for current command type: " + commandType);
        }

        return arg2;
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }
}

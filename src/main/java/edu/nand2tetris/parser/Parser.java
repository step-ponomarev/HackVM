package edu.nand2tetris.parser;

import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Parser implements Closeable {
    private final Reader fileReader;
    private CommandType commandType;

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
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void advance() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public CommandType commandType() {
        return commandType;
    }

    public String arg1() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public int arg2() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void close() throws IOException {
        fileReader.close();
    }
}

package edu.nand2tetris.parser;

import java.io.Closeable;
import java.io.IOException;

public final class Parser implements Closeable {
    public boolean hasMoreLines() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void advance() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public CommandType commandType() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public String arg1() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public int arg2() {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}

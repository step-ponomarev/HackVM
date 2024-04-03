package edu.nand2tetris;

import java.io.Closeable;
import java.io.IOException;

import edu.nand2tetris.parser.CommandType;

public final class CodeWriter implements Closeable {
    public void writeArithmetic(String command) {
        throw new UnsupportedOperationException("Unsupported operation");
    }
    
    public void writePushPop(CommandType commandType, String segment, int index) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}

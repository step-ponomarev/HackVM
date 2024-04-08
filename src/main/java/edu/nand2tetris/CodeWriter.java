package edu.nand2tetris;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CodeWriter implements Closeable {
    private static final String READ_SEGMENT_BASE_ADDRESS_TEMPLATE = """
            @%s
            D=M
            """;
    private static final String PUSH_VALUE_ON_STACK_TEMPLATE = """
            @SP
            M=M+1
                        
            A=M
            M=%s
            """;

    private static final String POP_VALUE_FROM_STACK_TEMPLATE = """
            @SP
            D=M
            M=M-1
                        
            A=D
            D=M
                        
            A=%s
            M=D
            """;
    private final BufferedWriter writer;

    public CodeWriter(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path);
    }

    public void writeArithmetic(String command) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    public void writePushPop(CommandType commandType, String segment, int index) {
        checkPushOrPopCommand(commandType);

        final StringBuilder asm = new StringBuilder();
        final Segment segmentType = Segment.parse(segment);
        if (segmentType == Segment.CONSTANT) {

        }

        final String register = segmentToRegister(segmentType, index);

        final String readSegmentBaseAddressCode = READ_SEGMENT_BASE_ADDRESS_TEMPLATE.formatted(register);

        // смапить сегмент в регистр
        // прочитать адресс сегмента
        // ветвление по PUSH/POP
        // сохранить/положить на стек запись из/в сегмент

        //A=STACK_ADDRESS

    }

    static String generateConstantPushOrPopCode(CommandType commandType, int index) {
        checkPushOrPopCommand(commandType);

        return commandType == CommandType.C_PUSH ? PUSH_VALUE_ON_STACK_TEMPLATE.formatted(index) : POP_VALUE_FROM_STACK_TEMPLATE.formatted(index);
    }

    static String segmentToRegister(Segment segmentType, int index) {
        if (segmentType == Segment.POINTER && (index != 0 && index != 1)) {
            throw new IllegalStateException("Unsupported index %d for pointer segment".formatted(index));
        }

        return switch (segmentType) {
            case LOCAL -> Constants.LOCAL_REGISTER;
            case ARGUMENT -> Constants.ARGUMENT_REGISTER;
            case POINTER ->
                    index == 0 ? Constants.POINTER_THIS_REGISTER : Constants.POINTER_THAT_REGISTER; // todo: check illegal index
            case CONSTANT -> null;
            case TEMP, STATIC -> throw new UnsupportedOperationException("Unsupported segment: " + segmentType);
        };
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    private static void checkPushOrPopCommand(CommandType commandType) {
        if (!isPushOrPopCommand(commandType)) {
            throw new IllegalStateException("Unsupported command: " + commandType);
        }
    }

    private static boolean isPushOrPopCommand(CommandType commandType) {
        return commandType == CommandType.C_POP || commandType == CommandType.C_PUSH;
    }
}

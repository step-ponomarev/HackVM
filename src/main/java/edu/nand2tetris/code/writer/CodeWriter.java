package edu.nand2tetris.code.writer;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import edu.nand2tetris.CommandType;
import edu.nand2tetris.Constants;
import edu.nand2tetris.Segment;

public final class CodeWriter implements Closeable {
    private static final String READ_SEGMENT_BASE_ADDRESS_TEMPLATE = """
            @%s
            D=M
            """;
    
    //TODO: заменить на темплейты со значениями, а не косвенно через D
    private static final String PUSH_FROM_D_TEMPLATE = """
            @SP
            M=M+1
            A=M
            M=D
            """;
    //TODO: Тут баг, мы не помещаем значение в сегмент т.к. оверайдим Д
    private static final String POP_INTO_D_TEMPLATE = """
            @SP
            M=M-1
            A=D
            D=M
            A=D
            M=D
            """;

    private final BufferedWriter writer;

    public CodeWriter(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path);
    }

    public void writeArithmetic(String command) {
        final CommandType commandType = CommandType.parse(command);
        if (commandType != CommandType.C_ARITHMETIC) {
            throw new IllegalStateException("Illegal command type: " + commandType + " command " + command);
        }
        
        
    }

    public void writePushPop(CommandType commandType, Segment segment, int index) throws IOException {
        checkPushOrPopCommand(commandType);
        writer.write(
                handlePushPop(commandType, segment, index)
        );
    }

    static String handlePushPop(CommandType commandType, Segment segment, int index) {
        checkPushOrPopCommand(commandType);
        final StringBuilder asm = new StringBuilder();
        if (segment == Segment.CONSTANT) {
            return asm.append(generateConstantPushOrPopCode(commandType, index)).toString();
        }

        asm.append(
                READ_SEGMENT_BASE_ADDRESS_TEMPLATE.formatted(segmentToRegister(segment, index))
        );
        asm.append(
                commandType == CommandType.C_PUSH
                        ? PUSH_FROM_D_TEMPLATE
                        : POP_INTO_D_TEMPLATE
        );

        return asm.toString();
    }

    static String generateConstantPushOrPopCode(CommandType commandType, int index) {
        checkPushOrPopCommand(commandType);

        return commandType == CommandType.C_PUSH ? PUSH_FROM_D_TEMPLATE.formatted(index) : POP_INTO_D_TEMPLATE.formatted(index);
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
        writer.close();
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

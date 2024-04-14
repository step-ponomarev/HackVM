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
    private static final String INIT_CODE = """
            @256
            D=A
            @SP
            M=D
            """;
    private static final String READ_SEGMENT_BASE_ADDRESS_TEMPLATE = """
            @%s
            D=M
            """;
    private static final String PUSH_FROM_D_TEMPLATE = """
            @SP
            M=M+1
            A=M
            M=D
            """;
    private static final String POP_INTO_D_TEMPLATE = """
            @ADDR
            M=D
            @SP
            D=M
            M=M-1
            A=D
            D=M
            @ADDR
            A=M
            M=D
            """;

    private static final String POP_INTO_ARG1_TEMPLATE = POP_INTO_D_TEMPLATE + """
            @ARG1
            M=D
            """;

    private static final String POP_INTO_ARG2_TEMPLATE = POP_INTO_D_TEMPLATE + """
            @ARG2
            M=D
            """;

    private static final String ADD_TEMPLATE = POP_INTO_ARG1_TEMPLATE + POP_INTO_ARG2_TEMPLATE + """
            @ARG1
            D=M
            @ARG2
            A=M
            D=A+D
            """ + PUSH_FROM_D_TEMPLATE;

    private static final String SUB_TEMPLATE = POP_INTO_ARG1_TEMPLATE + POP_INTO_ARG2_TEMPLATE + """
            @ARG1
            D=M
            @ARG2
            A=M
            D=D-A
            """ + PUSH_FROM_D_TEMPLATE;

    private static final String NEG_TEMPLATE = POP_INTO_ARG1_TEMPLATE + """
            @ARG1
            D=M
            D=0-D
            """ + PUSH_FROM_D_TEMPLATE;

    private final BufferedWriter writer;

    public CodeWriter(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path);
    }

    public void init() throws IOException {
        this.writer.write(INIT_CODE);
    }

    public void writeArithmetic(String command) throws IOException {
        final CommandType commandType = CommandType.parse(command);
        if (commandType != CommandType.C_ARITHMETIC) {
            throw new IllegalStateException("Illegal command type: " + commandType + " command " + command);
        }

        switch (command) {
            case "add" -> writer.write(ADD_TEMPLATE);
            case "neg" -> writer.write(NEG_TEMPLATE);
            case "sub" -> writer.write(SUB_TEMPLATE);
            default -> throw new IllegalStateException("Unsupported command " + command);
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
            asm.append("""
                    @%d
                    D=A
                    @cnst
                    M=D
                    D=A
                    """.formatted(index));
        } else if (segment == Segment.THIS) {
            asm.append(
                    """
                    @0
                    D=A
                    """
            );
        } else if (segment == Segment.THAT) {
            asm.append(
                    """
                    @1
                    D=A          
                    """
            );
        } else if (segment == Segment.TEMP) {
            asm.append(
                    """
                    @%d
                    D=A
                    """.formatted(Constants.TEMP_BASE + index)
            );
        }else {
            asm.append(
                    READ_SEGMENT_BASE_ADDRESS_TEMPLATE.formatted(segmentToRegister(segment, index))
            );
        }

        asm.append(
                commandType == CommandType.C_PUSH
                        ? PUSH_FROM_D_TEMPLATE
                        : POP_INTO_D_TEMPLATE
        );

        return asm.toString();
    }

    static String generateConstantPushOrPopCode(CommandType commandType, int index) {
        checkPushOrPopCommand(commandType);

        return commandType == CommandType.C_PUSH ? PUSH_FROM_D_TEMPLATE : POP_INTO_D_TEMPLATE;
    }

    static String segmentToRegister(Segment segmentType, int index) {
        if (segmentType == Segment.THIS && (index != 0 && index != 1)) {
            throw new IllegalStateException("Unsupported index %d for pointer segment".formatted(index));
        }

        return switch (segmentType) {
            case LOCAL -> Constants.LOCAL_REGISTER;
            case ARGUMENT -> Constants.ARGUMENT_REGISTER;
            case CONSTANT -> null;
            default -> throw new UnsupportedOperationException("Unsupported segment: " + segmentType);
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

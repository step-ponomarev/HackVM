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
    private final BufferedWriter writer;

    public CodeWriter(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path);
    }

    public void writeArithmetic(String command) throws IOException {
        final CommandType commandType = CommandType.parse(command);
        if (commandType != CommandType.C_ARITHMETIC) {
            throw new IllegalStateException("Illegal command type: " + commandType + " command " + command);
        }

        switch (command) {
            case "add" -> writer.write(AsmTemplate.ADD_TEMPLATE);
            case "neg" -> writer.write(AsmTemplate.NEG_TEMPLATE);
            case "sub" -> writer.write(AsmTemplate.SUB_TEMPLATE);
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
            asm.append(AsmTemplate.LOAD_D_TEMPLATE.formatted(Integer.toString(index)));
        } else if (segment == Segment.TEMP) {
            asm.append(AsmTemplate.LOAD_D_TEMPLATE.formatted(Byte.toString(Constants.TEMP_BASE)));
        } else if (segment == Segment.POINTER) {
            asm.append(AsmTemplate.LOAD_D_TEMPLATE.formatted(index == 0 ? Constants.THIS_REGISTER : Constants.THAT_REGISTER));
            index = 0;
        } else if (segment == Segment.STATIC) {
            asm.append(AsmTemplate.LOAD_D_TEMPLATE.formatted("FileName." + index));
        } else {
            asm.append(
                    AsmTemplate.READ_SEGMENT_BASE_ADDRESS_TEMPLATE.formatted(
                            segmentToRegister(segment)
                    )
            );
        }

        if (commandType == CommandType.C_POP) {
            asm.append(AsmTemplate.ADD_TO_D.formatted(index));
        }

        // IF not constant - address in D, read value in D from segment
        if (commandType == CommandType.C_PUSH && segment != Segment.CONSTANT) {
            asm.append(AsmTemplate.LOAD_FROM_D_ADDRESS_AND_INDEX.formatted(index));
        }

        asm.append(
                commandType == CommandType.C_PUSH
                        ? AsmTemplate.PUSH_FROM_D_TEMPLATE
                        : AsmTemplate.POP_INTO_D_TEMPLATE
        );

        return asm.toString();
    }

    static String segmentToRegister(Segment segmentType) {
        return switch (segmentType) {
            case LOCAL -> Constants.LOCAL_REGISTER;
            case ARGUMENT -> Constants.ARGUMENT_REGISTER;
            case THIS -> Constants.THIS_REGISTER;
            case THAT -> Constants.THAT_REGISTER;
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

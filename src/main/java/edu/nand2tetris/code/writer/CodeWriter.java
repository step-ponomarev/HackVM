package edu.nand2tetris.code.writer;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import edu.nand2tetris.CommandType;
import edu.nand2tetris.Constants;
import edu.nand2tetris.Segment;

public final class CodeWriter implements Closeable {
    private static final String[] COMPARISON_COMMANDS = {
            "eq",
            "gt",
            "lt"
    };
    private final Map<String, Integer> labelCommandToIndex = new HashMap<>();
    private final BufferedWriter writer;

    private final StringBuilder code = new StringBuilder();
    private StringBuilder currentFunction = new StringBuilder();
    private String fileName;

    private final Map<String, Integer> functionNameToCallCount = new HashMap<>();

    public CodeWriter(Path path) throws IOException {
        this.writer = Files.newBufferedWriter(path);

        for (String command : COMPARISON_COMMANDS) {
            labelCommandToIndex.put(command, 1);
        }
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void writeArithmetic(String command) {
        final CommandType commandType = CommandType.parse(command);
        if (commandType != CommandType.C_ARITHMETIC) {
            throw new IllegalStateException("Illegal command type: " + commandType + " command " + command);
        }

        int index = -1;
        if (labelCommandToIndex.containsKey(command)) {
            index = labelCommandToIndex.get(command);

            labelCommandToIndex.put(command, index + 1);
        }

        switch (command) {
            case "add" -> write(AsmTemplate.ADD_TEMPLATE);
            case "neg" -> write(AsmTemplate.NEG_TEMPLATE);
            case "sub" -> write(AsmTemplate.SUB_TEMPLATE);
            case "eq" -> write(AsmTemplate.EQ_TEMPLATE.formatted(index, index, index, index));
            case "lt" -> write(AsmTemplate.LT_TEMPLATE.formatted(index, index, index, index));
            case "gt" -> write(AsmTemplate.GT_TEMPLATE.formatted(index, index, index, index));
            case "and" -> write(AsmTemplate.AND_TEMPLATE);
            case "or" -> write(AsmTemplate.OR_TEMPLATE);
            case "not" -> write(AsmTemplate.NOT_TEMPLATE);
            default -> throw new IllegalStateException("Unsupported command " + command);
        }
    }

    public void writePushPop(CommandType commandType, Segment segment, int index) throws IOException {
        checkPushOrPopCommand(commandType);
        write(
                handlePushPop(commandType, segment, index)
        );
    }

    public void writeLabel(String label) {
        write(AsmTemplate.LABEL_TEMPLATE.formatted(label));
    }

    public void writeGoto(String label) {
        write(AsmTemplate.GOTO_TEMPLATE.formatted(label));
    }

    public void writeIf(String label) {
        write(
                AsmTemplate.IF_TEMPLATE.formatted(label)
        );
    }

    public void writeFunction(String functionName, int nArgs) {
        final StringBuilder function = new StringBuilder();
        function.append(AsmTemplate.LABEL_TEMPLATE.formatted(functionName));
        for (int i = 0; i < nArgs; i++) {
            function.append(handlePushPop(CommandType.C_PUSH, Segment.LOCAL, i));
        }

        currentFunction.append(function);
    }

    public void writeCall(String fnName, int argCount) {
        final Integer count = functionNameToCallCount.getOrDefault(fnName, 0);
        final String returnLabelName = fnName + "$ret." + count;
        functionNameToCallCount.put(fnName, count + 1);

        write(
                AsmTemplate.PUSH_STACK_FRAME_TEMPLATE.formatted(returnLabelName, argCount, fnName, returnLabelName)
        );
    }

    public void writeReturn() {
        currentFunction.append(
                AsmTemplate.POP_STACK_FRAME_TEMPLATE
        );

        code.insert(0, currentFunction);
        currentFunction = new StringBuilder();
    }

    private String handlePushPop(CommandType commandType, Segment segment, int index) {
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
            asm.append(AsmTemplate.LOAD_D_TEMPLATE.formatted(this.fileName + "." + index));
        } else {
            asm.append(
                    AsmTemplate.READ_SEGMENT_BASE_ADDRESS_TEMPLATE.formatted(
                            segmentToRegister(segment)
                    )
            );
        }

        if (commandType == CommandType.C_POP) {
            asm.append(AsmTemplate.ADD_TO_D_TEMPLATE.formatted(index));
        }

        // IF not constant - address in D, read value in D from segment
        if (commandType == CommandType.C_PUSH && segment != Segment.CONSTANT) {
            asm.append(AsmTemplate.LOAD_FROM_D_ADDRESS_AND_INDEX_TEMPLATE.formatted(index));
        }

        asm.append(
                commandType == CommandType.C_PUSH
                        ? AsmTemplate.PUSH_FROM_D_TEMPLATE
                        : AsmTemplate.POP_INTO_D_AND_SAVE_ADDRESS_TEMPLATE
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
        //TODO: не нрав
        writer.write(
                code.toString()
        );
        writer.close();
    }

    private void write(String code) {
        if (!currentFunction.isEmpty()) {
            currentFunction.append(code);
        } else {
            this.code.append(code);
        }
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

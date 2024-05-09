package edu.nand2tetris.code.writer;

final class AsmTemplate {
    static final String READ_SEGMENT_BASE_ADDRESS_TEMPLATE = """
            @%s
            D=M
            """;
    static final String PUSH_FROM_D_TEMPLATE = """
            @SP
            A=M
            M=D
            @SP
            M=M+1
            """;
    static final String POP_INTO_D_TEMPLATE = """
            @ADDR_SAVE
            M=D
            @SP
            M=M-1
            A=M
            D=M
            @ADDR_SAVE
            A=M
            M=D
            """;

    static final String POP_INTO_ARG1_TEMPLATE = POP_INTO_D_TEMPLATE + """
            @ARG1
            M=D
            """;

    static final String POP_INTO_ARG2_TEMPLATE = POP_INTO_D_TEMPLATE + """
            @ARG2
            M=D
            """;

    static final String ADD_TEMPLATE = POP_INTO_ARG1_TEMPLATE + POP_INTO_ARG2_TEMPLATE + """
            @ARG1
            D=M
            @ARG2
            A=M
            D=A+D
            """ + PUSH_FROM_D_TEMPLATE;

    static final String SUB_TEMPLATE = POP_INTO_ARG1_TEMPLATE + POP_INTO_ARG2_TEMPLATE + """
            @ARG2
            D=M
            @ARG1
            A=M
            D=D-A
            """ + PUSH_FROM_D_TEMPLATE;

    static final String NEG_TEMPLATE = POP_INTO_ARG1_TEMPLATE + """
            @ARG1
            D=M
            D=0-D
            """ + PUSH_FROM_D_TEMPLATE;

    static final String LOAD_D_TEMPLATE = """
            @%s
            D=A
            """;

    static final String ADD_TO_D = """
            @%d
            D=D+A
            """;

    static final String LOAD_FROM_D_ADDRESS_AND_INDEX = """
            @%d
            A=D+A
            D=M
            """;
    
    private AsmTemplate() {}
}

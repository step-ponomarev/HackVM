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
    
      static final String JUST_POP_INTO_D_TEMPLATE = """
            @SP
            M=M-1
            A=M
            D=M
            """;
    static final String POP_INTO_D_AND_SAVE_ADDRESS_TEMPLATE = """
            @ADDR_SAVE
            M=D
            """ + 
            JUST_POP_INTO_D_TEMPLATE + 
            """
            @ADDR_SAVE
            A=M
            M=D
            """;

    static final String POP_INTO_ARG1_TEMPLATE = JUST_POP_INTO_D_TEMPLATE + """
            @ARG1
            M=D
            """;

    static final String POP_INTO_ARG2_TEMPLATE = JUST_POP_INTO_D_TEMPLATE + """
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
    static final String NEG_TEMPLATE = JUST_POP_INTO_D_TEMPLATE + """
            D=-D
            """ + PUSH_FROM_D_TEMPLATE;

    static final String LOAD_D_TEMPLATE = """
            @%s
            D=A
            """;

    static final String ADD_TO_D_TEMPLATE = """
            @%s
            D=D+A
            """;

    static final String LOAD_FROM_D_ADDRESS_AND_INDEX_TEMPLATE = """
            @%d
            A=D+A
            D=M
            """;

    static final String EQ_TEMPLATE = SUB_TEMPLATE + JUST_POP_INTO_D_TEMPLATE + """         
            @JEQ_LABEL_%d
            D;JEQ
            D=0
            @JEQ_END_%d
            0;JMP
            (JEQ_LABEL_%d)
            D=-1
            (JEQ_END_%d)
            """ + PUSH_FROM_D_TEMPLATE;

    static final String LT_TEMPLATE = SUB_TEMPLATE + JUST_POP_INTO_D_TEMPLATE + """
            @JLT_LABEL_%d
            D;JLT
            D=0
            @JLT_END_%d
            0;JMP
            (JLT_LABEL_%d)
            D=-1
            (JLT_END_%d)
            """ + PUSH_FROM_D_TEMPLATE;

    static final String GT_TEMPLATE = SUB_TEMPLATE + JUST_POP_INTO_D_TEMPLATE + """
            @JGT_LABEL_%d
            D;JGT
            D=0
            @JGT_END_%d
            0;JMP
            (JGT_LABEL_%d)
            D=-1
            (JGT_END_%d)
            """ + PUSH_FROM_D_TEMPLATE;

    static final String AND_TEMPLATE = POP_INTO_ARG1_TEMPLATE + POP_INTO_ARG2_TEMPLATE + """
            @ARG2
            D=M
            @ARG1
            A=M
            D=D&A
            """ + PUSH_FROM_D_TEMPLATE;

    static final String OR_TEMPLATE = POP_INTO_ARG1_TEMPLATE + POP_INTO_ARG2_TEMPLATE + """
            @ARG2
            D=M
            @ARG1
            A=M
            D=D|A
            """ + PUSH_FROM_D_TEMPLATE;

    static final String NOT_TEMPLATE = JUST_POP_INTO_D_TEMPLATE + """
            D=!D
            """ + PUSH_FROM_D_TEMPLATE;

    static final String LABEL_TEMPLATE = """
            (%s)
            """;
    
    static final String GOTO_TEMPLATE = """
            @%s
            0;JMP
            """;
    
    static final String IF_TEMPLATE = JUST_POP_INTO_D_TEMPLATE + """
            @%s
            D;JNE
            """;

    private static final String INIT_SEGMENT_ADDRESS_FROM_FRAME_INDEX_TEMPLATE = """
            @%d
            D=A
            @frame
            A=M-D
            D=M
            @%s
            M=D
            """;
    
    private static final String PUSH_SEGMENT_ADDRESS = """
            @%s
            D=M
            """ + PUSH_FROM_D_TEMPLATE;
    
    static final String PUSH_STACK_FRAME_TEMPLATE = """
            @%s
            D=A
            """ + PUSH_FROM_D_TEMPLATE
            + PUSH_SEGMENT_ADDRESS.formatted("LCL")
            + PUSH_SEGMENT_ADDRESS.formatted("ARG")
            + PUSH_SEGMENT_ADDRESS.formatted("THIS")
            + PUSH_SEGMENT_ADDRESS.formatted("THAT")
            + """

            @SP
            D=M
            
            @5
            D=D-A
            
            @%d
            D=D-A
            
            @ARG
            M=D
            
            @SP
            D=M
            
            @LCL
            M=D
            
            @%s
            0;JMP
            (%s)
            """;
    
    static final String POP_STACK_FRAME_TEMPLATE = 
            // save LCL address into tmp variable frame
            """
            @LCL
            D=M
            @frame
            M=D
            """
            //save return address into tmp variable returnAddr        
            + """
            @5
            D=A
            @frame
            A=M-D
            D=M
            @returnAddr
            M=D
            """ + JUST_POP_INTO_D_TEMPLATE 

            // save return value for caller
            + """
            @ARG
            A=M
            M=D
            """
            // save stack pointer for caller                  
            + """
            @ARG
            D=M+1
            @SP
            M=D
            """ + INIT_SEGMENT_ADDRESS_FROM_FRAME_INDEX_TEMPLATE.formatted(1, "THAT") // restore segments for caller from stack frame
            + INIT_SEGMENT_ADDRESS_FROM_FRAME_INDEX_TEMPLATE.formatted(2, "THIS") 
            + INIT_SEGMENT_ADDRESS_FROM_FRAME_INDEX_TEMPLATE.formatted(3, "ARG") 
            + INIT_SEGMENT_ADDRESS_FROM_FRAME_INDEX_TEMPLATE.formatted(4, "LCL")
            
            // go to instruction after function call      
            + """
            @returnAddr
            A=M
            0;JMP
            """;

    private AsmTemplate() {}
}

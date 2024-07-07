package edu.nand2tetris;

import java.util.regex.Pattern;

public final class TokenPatterns {
    public static final Pattern ARGUMENT_SEGMENT_PATTERN = Pattern.compile("^argument$");
    public static final Pattern LOCAL_SEGMENT_PATTERN = Pattern.compile("^local$");
    public static final Pattern STATIC_SEGMENT_PATTERN = Pattern.compile("^static$");
    public static final Pattern CONSTANT_SEGMENT_PATTERN = Pattern.compile("^constant$");
    public static final Pattern THIS_SEGMENT_PATTERN = Pattern.compile("^this$");
    public static final Pattern THAT_SEGMENT_PATTERN = Pattern.compile("^that$");
    public static final Pattern TEMP_SEGMENT_PATTERN = Pattern.compile("^temp$");

    private static Pattern SYMBOL = Pattern.compile("^[a-zA-Z_.$:][a-zA-Z_.$:0-9]*$");

    public static final Pattern LABEL_COMMAND = Pattern.compile("^label\\s+" + "(" + SYMBOL.pattern().substring(1, SYMBOL.pattern().length() - 1) + ")$");
    public static final Pattern IF_COMMAND = Pattern.compile("^if-goto\\s+" + "(" + SYMBOL.pattern().substring(1, SYMBOL.pattern().length() - 1) + ")$");
    
    public static final Pattern GOTO = Pattern.compile("^goto\\s+" + "(" + SYMBOL.pattern().substring(1, SYMBOL.pattern().length() - 1) + ")$");
    
    public static final Pattern FUNCTION_COMMAND = Pattern.compile("^function\\s+" + "(" + SYMBOL.pattern().substring(1, SYMBOL.pattern().length() - 1) + ")" + "\\s+\\d+$");
    
    public static final Pattern RETURN_COMMAND = Pattern.compile("^return$");
    public static final Pattern CALL_COMMAND = Pattern.compile("^call\\s+" + "(" + SYMBOL.pattern().substring(1, SYMBOL.pattern().length() - 1) + ")$");
    

    public static final Pattern POINTER_SEGMENT_PATTERN = Pattern.compile("^pointer$");

    static final Pattern SEGMENT_PATTERN = Pattern.compile("^(%s|%s|%s|%s|%s|%s|%s|%s)$".formatted(
                    ARGUMENT_SEGMENT_PATTERN.pattern().substring(1, ARGUMENT_SEGMENT_PATTERN.pattern().length() - 1),
                    LOCAL_SEGMENT_PATTERN.pattern().substring(1, LOCAL_SEGMENT_PATTERN.pattern().length() - 1),
                    STATIC_SEGMENT_PATTERN.pattern().substring(1, STATIC_SEGMENT_PATTERN.pattern().length() - 1),
                    CONSTANT_SEGMENT_PATTERN.pattern().substring(1, CONSTANT_SEGMENT_PATTERN.pattern().length() - 1),
                    THIS_SEGMENT_PATTERN.pattern().substring(1, THIS_SEGMENT_PATTERN.pattern().length() - 1),
                    THAT_SEGMENT_PATTERN.pattern().substring(1, THAT_SEGMENT_PATTERN.pattern().length() - 1),
                    TEMP_SEGMENT_PATTERN.pattern().substring(1, TEMP_SEGMENT_PATTERN.pattern().length() - 1),
                    POINTER_SEGMENT_PATTERN.pattern().substring(1, POINTER_SEGMENT_PATTERN.pattern().length() - 1)
            )
    );

    private static final Pattern ADD_COMMAND = Pattern.compile("^add$");

    private static final Pattern SUB_COMMAND_PATTERN = Pattern.compile("^sub$");

    private static final Pattern NEG_COMMAND_PATTERN = Pattern.compile("^neg$");

    private static final Pattern EQ_COMMAND_PATTERN = Pattern.compile("^eq$");

    private static final Pattern GT_COMMAND_PATTERN = Pattern.compile("^gt$");

    private static final Pattern LT_COMMAND_PATTERN = Pattern.compile("^lt$");
    private static final Pattern AND_COMMAND_PATTERN = Pattern.compile("^and$");

    private static final Pattern OR_COMMAND_PATTERN = Pattern.compile("^or$");

    private static final Pattern NOT_COMMAND_PATTERN = Pattern.compile("^not$");

    static final Pattern ARITHMETIC_LOGICAL_COMMAND_PATTERN = Pattern.compile("^(%s|%s|%s|%s|%s|%s|%s|%s|%s)$".formatted(
            ADD_COMMAND.pattern().substring(1, ADD_COMMAND.pattern().length() - 1),
            SUB_COMMAND_PATTERN.pattern().substring(1, SUB_COMMAND_PATTERN.pattern().length() - 1),
            NEG_COMMAND_PATTERN.pattern().substring(1, NEG_COMMAND_PATTERN.pattern().length() - 1),
            AND_COMMAND_PATTERN.pattern().substring(1, AND_COMMAND_PATTERN.pattern().length() - 1),
            OR_COMMAND_PATTERN.pattern().substring(1, OR_COMMAND_PATTERN.pattern().length() - 1),
            NOT_COMMAND_PATTERN.pattern().substring(1, NOT_COMMAND_PATTERN.pattern().length() - 1),
            EQ_COMMAND_PATTERN.pattern().substring(1, EQ_COMMAND_PATTERN.pattern().length() - 1),
            GT_COMMAND_PATTERN.pattern().substring(1, GT_COMMAND_PATTERN.pattern().length() - 1),
            LT_COMMAND_PATTERN.pattern().substring(1, LT_COMMAND_PATTERN.pattern().length() - 1)
    ));

    static final Pattern PUSH_COMMAND_PATTERN = Pattern.compile("^push %s \\d+$".formatted(
                    SEGMENT_PATTERN.pattern().substring(1, SEGMENT_PATTERN.pattern().length() - 1)
            )
    );

    static final Pattern POP_COMMAND_PATTERN = Pattern.compile("^pop %s \\d+$".formatted(
                    SEGMENT_PATTERN.pattern().substring(1, SEGMENT_PATTERN.pattern().length() - 1)
            )
    );

    static final Pattern COMMENT = Pattern.compile("^\\/{2}.*$");

    private TokenPatterns() {}
}

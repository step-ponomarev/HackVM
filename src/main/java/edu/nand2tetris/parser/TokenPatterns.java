package edu.nand2tetris.parser;

import java.util.regex.Pattern;

public final class TokenPatterns {
    private static final Pattern ARGUMENT_SEGMENT_PATTERN = Pattern.compile("^argument$");
    private static final Pattern LOCAL_SEGMENT_PATTERN = Pattern.compile("^local$");
    private static final Pattern STATIC_SEGMENT_PATTERN = Pattern.compile("^static$");
    private static final Pattern CONSTANT_SEGMENT_PATTERN = Pattern.compile("^constant$");

    private static final Pattern THIS_SEGMENT_PATTERN = Pattern.compile("^this$");

    private static final Pattern THAT_SEGMENT_PATTERN = Pattern.compile("^that$");
    private static final Pattern POINTER_SEGMENT_PATTERN = Pattern.compile("^pointer$");
    private static final Pattern TEMP_SEGMENT_PATTERN = Pattern.compile("^temp$");

    static final Pattern SEGMENT_PATTERN = Pattern.compile("^(%s|%s|%s|%s|%s|%s|%s|%s)$".formatted(
                    ARGUMENT_SEGMENT_PATTERN.pattern().substring(1, ARGUMENT_SEGMENT_PATTERN.pattern().length() - 1),
                    LOCAL_SEGMENT_PATTERN.pattern().substring(1, LOCAL_SEGMENT_PATTERN.pattern().length() - 1),
                    STATIC_SEGMENT_PATTERN.pattern().substring(1, STATIC_SEGMENT_PATTERN.pattern().length() - 1),
                    CONSTANT_SEGMENT_PATTERN.pattern().substring(1, CONSTANT_SEGMENT_PATTERN.pattern().length() - 1),
                    THIS_SEGMENT_PATTERN.pattern().substring(1, THIS_SEGMENT_PATTERN.pattern().length() - 1),
                    THAT_SEGMENT_PATTERN.pattern().substring(1, THAT_SEGMENT_PATTERN.pattern().length() - 1),
                    POINTER_SEGMENT_PATTERN.pattern().substring(1, POINTER_SEGMENT_PATTERN.pattern().length() - 1),
                    TEMP_SEGMENT_PATTERN.pattern().substring(1, TEMP_SEGMENT_PATTERN.pattern().length() - 1)
            )
    );

    private static final Pattern ADD_COMMAND = Pattern.compile("^add$");

    private static final Pattern SUB_COMMAND_PATTERN = Pattern.compile("^sub$");

    private static final Pattern NEG_COMMAND_PATTERN = Pattern.compile("^neg$");

    static final Pattern ARITHMETIC_COMMAND_PATTERN = Pattern.compile("^(%s|%s|%s)".formatted(
            ADD_COMMAND.pattern().substring(1, ADD_COMMAND.pattern().length() - 1),
            SUB_COMMAND_PATTERN.pattern().substring(1, SUB_COMMAND_PATTERN.pattern().length() - 1),
            NEG_COMMAND_PATTERN.pattern().substring(1, NEG_COMMAND_PATTERN.pattern().length() - 1)
    ));

    private static final Pattern EQ_COMMAND_PATTERN = Pattern.compile("^eq$");

    private static final Pattern GT_COMMAND_PATTERN = Pattern.compile("^gt$");

    private static final Pattern LT_COMMAND_PATTERN = Pattern.compile("^lt$");

    static final Pattern COMPARISON_COMMAND_PATTERN = Pattern.compile("^(%s|%s|%s)".formatted(
            EQ_COMMAND_PATTERN.pattern().substring(1, EQ_COMMAND_PATTERN.pattern().length() - 1),
            GT_COMMAND_PATTERN.pattern().substring(1, GT_COMMAND_PATTERN.pattern().length() - 1),
            LT_COMMAND_PATTERN.pattern().substring(1, LT_COMMAND_PATTERN.pattern().length() - 1)
    ));

    private static final Pattern AND_COMMAND_PATTERN = Pattern.compile("^and$");

    private static final Pattern OR_COMMAND_PATTERN = Pattern.compile("^or$");

    private static final Pattern NOT_COMMAND_PATTERN = Pattern.compile("^not$");

    static final Pattern LOGICAL_COMMAND_PATTERN = Pattern.compile("^(%s|%s|%s)".formatted(
            AND_COMMAND_PATTERN.pattern().substring(1, AND_COMMAND_PATTERN.pattern().length() - 1),
            OR_COMMAND_PATTERN.pattern().substring(1, OR_COMMAND_PATTERN.pattern().length() - 1),
            NOT_COMMAND_PATTERN.pattern().substring(1, NOT_COMMAND_PATTERN.pattern().length() - 1)
    ));

    static final Pattern PUSH_COMMAND_PATTERN = Pattern.compile("^push (%s) \\d+$".formatted(
                    SEGMENT_PATTERN.pattern().substring(1, SEGMENT_PATTERN.pattern().length() - 1)
            )
    );

    static final Pattern POP_COMMAND_PATTERN = Pattern.compile("^pop (%s) \\d+$".formatted(
                    SEGMENT_PATTERN.pattern().substring(1, SEGMENT_PATTERN.pattern().length() - 1)
            )
    );

    private TokenPatterns() {
    }
}

package edu.nand2tetris;

import java.util.regex.Pattern;

public enum CommandType {
    C_ARITHMETIC(TokenPatterns.ARITHMETIC_COMMAND_PATTERN),
    C_PUSH(TokenPatterns.PUSH_COMMAND_PATTERN),
    C_POP(TokenPatterns.POP_COMMAND_PATTERN),
    C_LABEL(null),
    C_GOTO(null),
    C_IF(null),
    C_FUNCTION(null),
    C_RETURN(null),
    C_CALL(null);

    private Pattern commandPattern;

    CommandType(Pattern commandPattern) {
        this.commandPattern = commandPattern;
    }

    public static CommandType parse(String command) {
        for (CommandType ct : CommandType.values()) {
            if (ct.commandPattern == null) {
                continue;
            }

            if (ct.commandPattern.matcher(command).matches()) {
                return ct;
            }
        }

        throw new IllegalArgumentException("Unsupported command: " + command);
    }
}

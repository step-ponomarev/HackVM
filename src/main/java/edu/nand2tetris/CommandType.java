package edu.nand2tetris;

import java.util.regex.Pattern;

public enum CommandType {
    C_ARITHMETIC(TokenPatterns.ARITHMETIC_LOGICAL_COMMAND_PATTERN),
    C_PUSH(TokenPatterns.PUSH_COMMAND_PATTERN),
    C_POP(TokenPatterns.POP_COMMAND_PATTERN),
    C_LABEL(TokenPatterns.LABEL_COMMAND),
    C_GOTO(TokenPatterns.GOTO),
    C_IF(TokenPatterns.IF_COMMAND),
    C_FUNCTION(TokenPatterns.FUNCTION_COMMAND),
    C_RETURN(TokenPatterns.RETURN_COMMAND),
    C_CALL(TokenPatterns.CALL_COMMAND);

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

package edu.nand2tetris;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class TokenPatternsTest {
    private static final String[] SEGMENTS = {
            "argument",
            "local",
            "static",
            "constant",
            "this",
            "that",
            "pointer",
            "temp"
    };

    private static final String[] ARITHMETIC_COMMANDS = {
            "add",
            "sub",
            "neg"
    };

    private static final String[] COMPARISON_COMMANDS = {
            "eq",
            "gt",
            "lt"
    };

    private static final String[] LOGICAL_COMMANDS = {
            "and",
            "or",
            "not"
    };

    @Test
    public void testSegmentPattern() {
        testValuesByPattern(SEGMENTS, TokenPatterns.SEGMENT_PATTERN);
    }

    @Test
    public void testPushPattern() {
        final String pushTemplate = "push %s %d";
        for (String seg : SEGMENTS) {
            for (int i = 0; i < 199; i++) {
                Assertions.assertTrue(TokenPatterns.PUSH_COMMAND_PATTERN.matcher(pushTemplate.formatted(seg, i)).matches());
            }
        }
    }

    @Test
    public void testPopPattern() {
        final String pushTemplate = "pop %s %d";
        for (String seg : SEGMENTS) {
            for (int i = 0; i < 199; i++) {
                Assertions.assertTrue(TokenPatterns.POP_COMMAND_PATTERN.matcher(pushTemplate.formatted(seg, i)).matches());
            }
        }
    }

    @Test
    public void testArithmeticCommandsPattern() {
        testValuesByPattern(ARITHMETIC_COMMANDS, TokenPatterns.ARITHMETIC_COMMAND_PATTERN);
    }

    @Test
    public void testComparisonCommandsPattern() {
        testValuesByPattern(COMPARISON_COMMANDS, TokenPatterns.COMPARISON_COMMAND_PATTERN);
    }

    @Test
    public void testLogicalCommandsPattern() {
        testValuesByPattern(LOGICAL_COMMANDS, TokenPatterns.LOGICAL_COMMAND_PATTERN);
    }

    private static void testValuesByPattern(String[] values, Pattern pattern) {
        for (String value : values) {
            Assertions.assertTrue(pattern.matcher(value).matches());
        }
    }
}

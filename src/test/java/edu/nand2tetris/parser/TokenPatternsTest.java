package edu.nand2tetris.parser;

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

    @Test
    public void testSegmentPattern() {
        for (String seg : SEGMENTS) {
            Assertions.assertTrue(TokenPatterns.SEGMENT_PATTERN.matcher(seg).matches());
        }
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
}

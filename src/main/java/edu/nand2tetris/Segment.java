package edu.nand2tetris;

import java.util.regex.Pattern;

public enum Segment {
    ARGUMENT(TokenPatterns.ARGUMENT_SEGMENT_PATTERN),
    LOCAL(TokenPatterns.LOCAL_SEGMENT_PATTERN),
    STATIC(TokenPatterns.STATIC_SEGMENT_PATTERN),
    CONSTANT(TokenPatterns.CONSTANT_SEGMENT_PATTERN),
    POINTER(TokenPatterns.POINTER_SEGMENT_PATTERN),
    TEMP(TokenPatterns.TEMP_SEGMENT_PATTERN);

    private final Pattern pattern;
    private final String register;

    Segment(Pattern pattern) {
        this(pattern, null);
    }

    Segment(Pattern pattern, String register) {
        this.pattern = pattern;
        this.register = register;
    }

    public static Segment parse(String seg) {
        for (Segment segment : Segment.values()) {
            if (segment.pattern.matcher(seg).matches()) {
                return segment;
            }
        }

        throw new IllegalArgumentException("Unsupported segment: " + seg);
    }
}

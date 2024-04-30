package edu.nand2tetris;

import java.util.regex.Pattern;

public enum Segment {
    ARGUMENT(TokenPatterns.ARGUMENT_SEGMENT_PATTERN, "argument"),
    LOCAL(TokenPatterns.LOCAL_SEGMENT_PATTERN, "local"),
    STATIC(TokenPatterns.STATIC_SEGMENT_PATTERN, "static"),
    CONSTANT(TokenPatterns.CONSTANT_SEGMENT_PATTERN, null),
    //depends of index 0==this 1==that
    THIS(TokenPatterns.THIS_SEGMENT_PATTERN, null),
    THAT(TokenPatterns.THAT_SEGMENT_PATTERN, null),
    TEMP(TokenPatterns.TEMP_SEGMENT_PATTERN, "temp"),
    POINTER(TokenPatterns.POINTER_SEGMENT_PATTERN, null);

    private final Pattern pattern;
    private final String register;

    Segment(Pattern pattern, String register) {
        this.pattern = pattern;
        this.register = register;
    }

    public String getRegister(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("Index cannot be < 0");
        }

        return register;
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

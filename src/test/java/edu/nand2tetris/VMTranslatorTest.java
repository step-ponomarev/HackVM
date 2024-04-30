package edu.nand2tetris;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

public final class VMTranslatorTest {
    @Test
    public void basicTest() {
        final Path src = Resources.RESOURCES_DIR.resolve("translator/SimpleAdd.vm");
        final Path out = Resources.RESOURCES_DIR.resolve("translator/SimpleAdd.asm");
        VMTranslator.main(new String[]{src.toString(), out.toString()});
    }
}

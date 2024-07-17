package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import nand2tetris.test.lib.ASMTester;
import nand2tetris.test.lib.ASMTranslationException;

public final class VMTranslatorTest {
    private final static Pattern VM_FILE_NAME = Pattern.compile("^\\S+.vm$");

    @Test
    public void testMemoryAccessBasicTest() throws IOException, ASMTranslationException {
        final Path basicTestDir = Resources.RESOURCES_DIR.resolve("MemoryAccess").resolve("BasicTest");
        final Path file = getVmFiles(basicTestDir).next();

        VMTranslator.main(new String[]{file.toAbsolutePath().toString(), basicTestDir.resolve("BasicTest.asm").toString()});
        ASMTester.executeTestScript(
                basicTestDir.resolve("BasicTest.tst")
        );
    }

    @Test
    public void testMemoryAccessPointerTest() throws IOException, ASMTranslationException {
        final Path basicTestDir = Resources.RESOURCES_DIR.resolve("MemoryAccess").resolve("PointerTest");
        final Path file = getVmFiles(basicTestDir).next();

        VMTranslator.main(new String[]{file.toAbsolutePath().toString(), basicTestDir.resolve("PointerTest.asm").toString()});
        ASMTester.executeTestScript(
                basicTestDir.resolve("PointerTest.tst")
        );
    }

    @Test
    public void testMemoryAccessStaticTest() throws IOException, ASMTranslationException {
        final Path basicTestDir = Resources.RESOURCES_DIR.resolve("MemoryAccess").resolve("StaticTest");
        final Path file = getVmFiles(basicTestDir).next();

        VMTranslator.main(new String[]{file.toAbsolutePath().toString(), basicTestDir.resolve("StaticTest.asm").toString()});
        ASMTester.executeTestScript(
                basicTestDir.resolve("StaticTest.tst")
        );
    }

    @Test
    public void testStackArithmeticSimpleAdd() throws IOException, ASMTranslationException {
        final Path basicTestDir = Resources.RESOURCES_DIR.resolve("StackArithmetic").resolve("SimpleAdd");
        final Path file = getVmFiles(basicTestDir).next();

        VMTranslator.main(new String[]{file.toAbsolutePath().toString(), basicTestDir.resolve("SimpleAdd.asm").toString()});
        ASMTester.executeTestScript(
                basicTestDir.resolve("SimpleAdd.tst")
        );
    }

    @Test
    public void testStackArithmeticStackTest() throws IOException, ASMTranslationException {
        final Path basicTestDir = Resources.RESOURCES_DIR.resolve("StackArithmetic").resolve("StackTest");
        final Path file = getVmFiles(basicTestDir).next();

        VMTranslator.main(new String[]{file.toAbsolutePath().toString(), basicTestDir.resolve("StackTest.asm").toString()});
        ASMTester.executeTestScript(
                basicTestDir.resolve("StackTest.tst")
        );
    }
    
    @Test
    public void testFunctionCallsFibonacciElement() throws IOException, ASMTranslationException {
        final Path basicTestDir = Resources.RESOURCES_DIR.resolve("FunctionCalls").resolve("FibonacciElement");

        VMTranslator.main(new String[]{basicTestDir.toAbsolutePath().toString(), basicTestDir.resolve("FibonacciElement.asm").toString()});
        ASMTester.executeTestScript(
                basicTestDir.resolve("FibonacciElement.tst")
        );
    }

    public static Iterator<Path> getVmFiles(Path path) throws IOException {
        return Files.list(path)
                .filter(f -> VM_FILE_NAME.matcher(f.getFileName().toString()).matches())
                .iterator();
    }
}

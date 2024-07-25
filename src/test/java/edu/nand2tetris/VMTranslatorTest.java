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

    private void runTestWithFile(Path dir, String asmFileName, String tstFileName) throws IOException, ASMTranslationException {
        final Path file = getVmFiles(dir).next();
        VMTranslator.main(new String[]{file.toAbsolutePath().toString(), dir.resolve(asmFileName).toString()});
        ASMTester.executeTestScript(dir.resolve(tstFileName));
    }

    private void runTestWithDir(Path dir, String asmFileName, String tstFileName) throws IOException, ASMTranslationException {
        VMTranslator.main(new String[]{dir.toAbsolutePath().toString(), dir.resolve(asmFileName).toString()});
        ASMTester.executeTestScript(dir.resolve(tstFileName));
    }

    @Test
    public void testMemoryAccessBasicTest() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("MemoryAccess").resolve("BasicTest");
        runTestWithFile(dir, "BasicTest.asm", "BasicTest.tst");
    }

    @Test
    public void testMemoryAccessPointerTest() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("MemoryAccess").resolve("PointerTest");
        runTestWithFile(dir, "PointerTest.asm", "PointerTest.tst");
    }

    @Test
    public void testMemoryAccessStaticTest() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("MemoryAccess").resolve("StaticTest");
        runTestWithFile(dir, "StaticTest.asm", "StaticTest.tst");
    }

    @Test
    public void testStackArithmeticSimpleAdd() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("StackArithmetic").resolve("SimpleAdd");
        runTestWithFile(dir, "SimpleAdd.asm", "SimpleAdd.tst");
    }

    @Test
    public void testStackArithmeticStackTest() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("StackArithmetic").resolve("StackTest");
        runTestWithFile(dir, "StackTest.asm", "StackTest.tst");
    }

    @Test
    public void testProgramFlowBasicLoop() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("ProgramFlow").resolve("BasicLoop");
        runTestWithFile(dir, "BasicLoop.asm", "BasicLoop.tst");
    }

    @Test
    public void testProgramFlowFibonacciSeries() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("ProgramFlow").resolve("FibonacciSeries");
        runTestWithFile(dir, "FibonacciSeries.asm", "FibonacciSeries.tst");
    }

    @Test
    public void testFunctionCallsFibonacciElement() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("FunctionCalls").resolve("FibonacciElement");
        runTestWithDir(dir, "FibonacciElement.asm", "FibonacciElement.tst");
    }

    @Test
    public void testFunctionCallsSimpleFunction() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("FunctionCalls").resolve("SimpleFunction");
        runTestWithFile(dir, "SimpleFunction.asm", "SimpleFunction.tst");
    }

    @Test
    public void testFunctionCallsStaticsTest() throws IOException, ASMTranslationException {
        final Path dir = Resources.RESOURCES_DIR.resolve("FunctionCalls").resolve("StaticsTest");
        runTestWithDir(dir, "StaticsTest.asm", "StaticsTest.tst");
    }


    public static Iterator<Path> getVmFiles(Path path) throws IOException {
        return Files.list(path)
                .filter(f -> VM_FILE_NAME.matcher(f.getFileName().toString()).matches())
                .iterator();
    }
}

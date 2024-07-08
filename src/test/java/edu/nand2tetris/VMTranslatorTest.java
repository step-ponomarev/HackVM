package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class VMTranslatorTest {
    private final static Pattern VM_FILE_NAME = Pattern.compile("^\\S+.vm$");

    @Test
    public void basicTest() throws IOException {
        final Path translatorPath = Resources.RESOURCES_DIR.resolve("basic");
        final Iterator<Path> vmFiles = getVmFiles(translatorPath);
        testVmFiles(translatorPath, vmFiles);
    }

    @Test
    public void gotoTest() throws IOException {
        final Path translatorPath = Resources.RESOURCES_DIR.resolve("goto");
        final Iterator<Path> vmFiles = getVmFiles(translatorPath);
        testVmFiles(translatorPath, vmFiles);
    }

    @Test
    public void testSimpleFunction() throws IOException {
        final Path translatorPath = Resources.RESOURCES_DIR.resolve("function");
        final Iterator<Path> vmFiles = getVmFiles(translatorPath);
        testVmFiles(translatorPath, vmFiles);
    }
    
    @Test
    public void testFibonacciElement() throws IOException {
        final Path translatorPath = Resources.RESOURCES_DIR.resolve("function").resolve("FibonacciElement");
        final Iterator<Path> vmFiles = getVmFiles(translatorPath);
        testVmFiles(translatorPath, vmFiles);
    }
    
    @Test
    public void testStatic() throws IOException {
        final Path translatorPath = Resources.RESOURCES_DIR.resolve("function").resolve("StaticsTest");
        final Path testPath = translatorPath.resolve("test");
        Files.createDirectory(testPath);

        final Path outFile = testPath.resolve("StaticsTest.asm");
        try {
            VMTranslator.main(new String[]{translatorPath.toAbsolutePath().toString(), outFile.toAbsolutePath().toString()});
        } finally {
//            Files.delete(outFile);
//            Files.delete(testPath);
        }
    }

    public static Iterator<Path> getVmFiles(Path path) throws IOException {
        return Files.list(path)
                .filter(f -> VM_FILE_NAME.matcher(f.getFileName().toString()).matches())
                .iterator();
    }

    public static void testVmFiles(Path srcDir, Iterator<Path> files) throws IOException {
        final Path testPath = srcDir.resolve("test");
        final Path cmpPath = srcDir.resolve("cmp");
        Files.createDirectory(testPath);

        try {
            while (files.hasNext()) {
                final Path vmFile = files.next();
                final String outFileName = vmFile.getFileName().toString().replace("vm", "asm");
                final Path outFile = testPath.resolve(outFileName);

                try {
                    VMTranslator.main(new String[]{vmFile.toAbsolutePath().toString(), outFile.toAbsolutePath().toString()});
                    Assertions.assertEquals(Files.readString(cmpPath.resolve(outFileName)), Files.readString(outFile));
                } finally {
                    Files.deleteIfExists(outFile);
                }
            }
        } finally {
            Files.deleteIfExists(testPath);
        }
    }
}

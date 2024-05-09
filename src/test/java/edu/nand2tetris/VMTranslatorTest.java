package edu.nand2tetris;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class VMTranslatorTest {
    private final static Pattern VM_FILE_NAME = Pattern.compile("^[A-Za-z]+.vm$");

    @Test
    public void basicTest() throws IOException {
        final Path translatorPath = Resources.RESOURCES_DIR.resolve("translator");
        final List<Path> vmFiles = Files.list(translatorPath)
                .filter(f -> VM_FILE_NAME.matcher(f.getFileName().toString()).matches())
                .toList();

        final Path testPath = translatorPath.resolve("test");
        final Path cmpPath = translatorPath.resolve("cmp");
        Files.createDirectory(testPath);

        try {
            for (Path vmFile : vmFiles) {
                final String outFileName = vmFile.getFileName().toString().replace("vm", "asm");
                final Path outFile = cmpPath.resolve(outFileName);
                VMTranslator.main(new String[]{vmFile.toAbsolutePath().toString(), outFile.toAbsolutePath().toString()});

                Assertions.assertEquals(Files.readString(cmpPath.resolve(outFileName)), Files.readString(outFile));
                Files.deleteIfExists(outFile);
            }
        } finally {
            Files.deleteIfExists(testPath);
        }
    }
}

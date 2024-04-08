package edu.nand2tetris;

public class VMTranslator {
    //TODO:
    //Сначала нужно инициализировать сегменты памяти
    
    private static final String INIT_CODE = """
            // init stack adress
            @256
            D=A
            @SP
            M=D
            """;
    
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }
}
(SimpleFunction.SimpleFunction.test)
@LCL
D=M
@0
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@1
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
M=M-1
A=M
D=M
@ARG1
M=D
@SP
M=M-1
A=M
D=M
@ARG2
M=D
@ARG1
D=M
@ARG2
A=M
D=A+D
@SP
A=M
M=D
@SP
M=M+1
@SP
M=M-1
A=M
D=M
D=!D
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@0
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
M=M-1
A=M
D=M
@ARG1
M=D
@SP
M=M-1
A=M
D=M
@ARG2
M=D
@ARG1
D=M
@ARG2
A=M
D=A+D
@SP
A=M
M=D
@SP
M=M+1
@ARG
D=M
@1
A=D+A
D=M
@SP
A=M
M=D
@SP
M=M+1
@SP
M=M-1
A=M
D=M
@ARG1
M=D
@SP
M=M-1
A=M
D=M
@ARG2
M=D
@ARG2
D=M
@ARG1
A=M
D=D-A
@SP
A=M
M=D
@SP
M=M+1
@LCL
D=M
@frame
M=D
@5
D=A
@frame
A=M-D
D=M
@returnAddr
M=D
@SP
M=M-1
A=M
D=M
@ARG
A=M
M=D
@ARG
D=M+1
@SP
M=D
@1
D=A
@frame
A=M-D
D=M
@THAT
M=D
@2
D=A
@frame
A=M-D
D=M
@THIS
M=D
@3
D=A
@frame
A=M-D
D=M
@ARG
M=D
@4
D=A
@frame
A=M-D
D=M
@LCL
M=D
@returnAddr
A=M
0;JMP

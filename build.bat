javac ./src/main/*.java
jar -cfv BrainfuckingIDE.jar ./src/main/*.class 
del ./src/main/*.class 
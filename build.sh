#compiles the IDE as .jar
javac ./src/main/*.java
jar -cfv BrainfuckingIDE.jar ./src/main/*.class 
rm ./src/main/*.class 
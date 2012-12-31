#compiles the IDE as .jar
cd ./src
javac ./main/*.java
jar cef main.BrainfuckingIDE BrainfuckingIDE.jar ./main/*.class 
mv BrainfuckingIDE.jar ..
cd ./main/
rm *.class
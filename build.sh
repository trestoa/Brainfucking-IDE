#compiles the IDE as .jar
cd ./src
javac ./main/*.java ./ui/*.java ./ui/extras/*.java
jar cef main.BrainfuckingIDE BrainfuckingIDE.jar ./main/*.class ./ui/*.class ./ui/extras/*.class
mv BrainfuckingIDE.jar ..
rm ./main/*.class 
rm ./ui/*.class
rm ./ui/extras/*.class
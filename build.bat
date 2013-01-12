cd ./src
javac ./main/*.java ./ui/*.java ./ui/extras/*.java
jar cef main.BrainfuckingIDE BrainfuckingIDE.jar ./main/*.class ./ui/*.class ./ui/extras/*.class
move BrainfuckingIDE.jar ..
del ./main/*.class 
del ./ui/*.class
del ./ui/extras/*.class
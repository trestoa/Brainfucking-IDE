Brainfucking_IDE
================

Brainfucking IDE an IDE/Interpreter for Brainfuck

What is the Brainfucking IDE?
-----------------------------

The Brainfucking IDE is an Integrated Development Environment and Interpreter for the programming language Brainfuck.

And what is Brainfuck?
----------------------

Brainfuck is an minimalistic, esoteric programm language. Is´s Turing complete and only uses eight characters:

* +
* -
* >
* <
* [
* ]
* .
* ,

For a detailed information and tutorial about Brainfuck visit [the Wikipedia entry about Brainfuck](http://en.wikipedia.org/wiki/Brainfuck "Brainfuck") or 
download the project with the samples distributed with the IDE.

Usage 
------
'java -cp bin main.BrainfuckingIDE [-d] [FILENAME]'

You can use it as IDE with the GUI or as Interpreter with the Shell/Commandline.    
For the GUI just start the start.bat for windows or the start.sh on unix. 
If you prefer the Commandline/Shell and want to execute one Brainfuck file type this:    
'java -cp bin main.BrainfuckingIDE [-d] [FILENAME]' -d Is for printing debug information. So if you interpret the file "nestingsloops.b" it should return something like [this](http://pastie.org/5568182# "nestingloops.b debuged").
You cat test it with the samples distributed with the IDE and maybe understand Brainfuck a little bit :-).
With no arguments ('java -cp bin main.BrainfuckingIDE) the GUI will start automatically.

Requirements
------------

For running the IDE only the Java Runtime Environment(JRE) is required. You don´n need to recompile it.(Written in javva!!!!)

Where can I get it?
-------------------

If you´ve got Git with this:
'git clone https://github.com/kleiinnn/Brainfucking-IDE.git'
You don´t want to install Git? Download the IDE as tarball or zip or visit the [Github page](https://github.com/kleiinnn/Brainfucking-IDE, "Github page of Brainfucking IDE")


License
-------

The Brainfucking IDE ist licensed under the [MIT License](http://klein-server.at/license/ "MIT-License").

Links
-----

* [Wikipedia entry about Brainfuck](http://en.wikipedia.org/wiki/Brainfuck "Brainfuck")
* [Esolangs entry about Brainfuck] (http://esolangs.org/wiki/Brainfuck "Brainfuck")
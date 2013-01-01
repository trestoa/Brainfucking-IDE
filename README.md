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
`java -jar BrainfuckingIDE.jar [-d] [FILENAME]`

You can use it as IDE with the GUI or as Interpreter with the shell/commandline.  

If you just want to use the IDE/Interpreter and don´t want to read/change source please download the final_1.0.0.zip file, extract it and execute the start.bat/start.sh or execute the jar file.

Else:   
1. First execute build.bat/build.sh. This little script will build the jar file.
2. Now you should see an jar file named BrainfuckingIDE.jar
3. Execute the start.sh/start.bat file or execute the jar file.

For the GUI just start the start.bat for windows or the start.sh on unix. 
If you prefer the Commandline/Shell and just want to execute one Brainfuck file type this:    
`java -cp bin main.BrainfuckingIDE [-d] [FILENAME]` -d Is for printing debug information. So if you interpret the file "nestingsloops.b" it should return something like [this](http://pastie.org/5568182# "nestingloops.b debuged").
You can test it with the samples distributed with the IDE and maybe understand Brainfuck a little bit :-).
With no arguments (`java -cp bin main.BrainfuckingIDE`) the GUI will start automatically.

Where can I get it?
-------------------

If you´ve got Git with this:
`git clone https://github.com/kleiinnn/Brainfucking-IDE.git`
You don´t want to install Git? Download the IDE as [zip](http://github.com/kleiinnn/Brainfucking-IDE/archive/master.zip "BrainfuckingIDE Repo as zip file") or visit the [Github page](https://github.com/kleiinnn/Brainfucking-IDE "Github page of Brainfucking IDE")

For the just-use version download [this](http://klein-server.at/files/BrainfuckingIDE/final_1.0.0.tar.bz2 "just version of BrainfuckingIDE")
Requirements
------------

For the just-use version only the Java Runtime Environment(java7!!!) is required. If you download the [repo](https://github.com/kleiinnn/Brainfucking-IDE "Github repo of Brainfucking IDE") from github you´ll need the JDK(at least 1.7.*) and the JDK binary folder must be added to the Path. 

License
-------

The Brainfucking IDE ist licensed under the [MIT License](http://klein-server.at/license/ "MIT-License").

Contact
-------

If you any questions please write me an e-mail:

[me[MASTERSPACE]klein-server.at](mailto:me@klein-server.at "me[MASTERSPACE]klein-server.at")
Reference
-----

* [Wikipedia entry about Brainfuck](http://en.wikipedia.org/wiki/Brainfuck "Brainfuck")
* [Esolangs entry about Brainfuck](http://esolangs.org/wiki/Brainfuck "Brainfuck")
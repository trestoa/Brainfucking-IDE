package main;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Brainfucking IDE v1.0.0
 * 
 * Description: IDE/Interpreter for Brainfuck
 * 
 * License: 
 * 
 * Copyright (c) 2012 Markus Klein
 *
 * Permission is hereby granted, free of charge, to any person 
 * obtaining a copy of this software and associated documentation 
 * files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class BrainfuckingIDE{
	private static BrainfuckingIDE main;
	private short[] memory;
	private short memPointer;
	private String code;
	private int codePos = 0;
	private boolean ui = false;
	private boolean consoleDebugging = false;
	private Scanner buffer;
	private UI UI;
	
	public static void main(String[] args){
		System.out.println("starting...");
		main = new BrainfuckingIDE(args);
	}
	
	public BrainfuckingIDE(String... args){
		String fileName = null;
		if(args.length == 1){
			fileName = args[0];
		}
		else if(args.length == 2){
			consoleDebugging = true;
			fileName = args[1];
		}
		if(fileName == null ){
			ui = true;
		}
		memory = new short[0xFFFF];
		System.out.println("creating memory...success");
		memPointer = 0;	
		System.out.println("creating memorypointer...success");
		if(ui){
			UI = new UI();
			UI.setInterpreter(this);
		}
		else{
			buffer = new Scanner(System.in);
			code = readCode(fileName);
			prepareInterpret();
		}	
	}
	
	public String readCode(String fileName){
		System.out.print("reading code...");
		try{
			Path path = Paths.get(fileName);
			List<String> sl =  Files.readAllLines(path, StandardCharsets.UTF_8);
			Iterator<String> it = sl.iterator();
			String s = "";
			while (it.hasNext()){
				s += it.next();
				s += "\n";
			}
			System.out.println("success");
			return s;
		}
		catch(IOException e){
			System.out.println("error");
			System.out.println("IOException: " + e.toString());
			System.exit(0);
			return null;
		}
	}
	
	public String readCode(File file){
		System.out.print("reading code...");
		try{
			Path path = file.toPath();
			List<String> sl =  Files.readAllLines(path, StandardCharsets.UTF_8);
			Iterator<String> it = sl.iterator();
			String s = "";
			while (it.hasNext()){
				s += it.next();
				s += "\n";
			}
			System.out.println("success");
			return s;
		}
		catch(IOException e){
			System.out.println("error");
			System.out.println("IOException: " + e.toString());
			System.exit(0);
			return null;
		}
	}
	
	public void prepareInterpret(){
		System.out.println("prepare for interpreting...");
		if(ui){
			code = UI.getCode();
		}
		String newCode = "";
		if(code == null){
			JOptionPane.showMessageDialog(null, "An Error occured - code == null",null ,JOptionPane.ERROR_MESSAGE);
			doCleanup();
			return;
		}
		if(code.isEmpty()){
			JOptionPane.showMessageDialog(null, "An Error occured - code == emtpy",null ,JOptionPane.ERROR_MESSAGE);
			doCleanup();
			return;
		}
		System.out.print("compiling code...");
		for(int i = 0; i < code.length(); i++){
			if(code.charAt(i) == '<' || code.charAt(i) == '>' || code.charAt(i) == '+' || code.charAt(i) == '-' || code.charAt(i) == ','  || code.charAt(i) == '.' || code.charAt(i) == '[' || code.charAt(i) == ']'){
				newCode += code.charAt(i);
			}
		}
		code = newCode;
		if(code.isEmpty()){
			JOptionPane.showMessageDialog(null, "An Error occured - The compiled Code is empty!!", null, JOptionPane.ERROR_MESSAGE);
			doCleanup();
			return;
		}
		System.out.println("success");
		System.out.println("interpreting...");
		if(!ui){
			System.out.println("\n\n");
		}
		if(consoleDebugging){
			System.out.println("code == {" + code + "}");
		}
		interpret();
	}
	
	public void interpret(){
		while(codePos < code.length()){
			if(consoleDebugging){
				System.out.println("Character: " + code.charAt(codePos));
			}
			switch(code.charAt(codePos)){
			case '+':
				memory[memPointer]++;
				if(consoleDebugging){
					System.out.println("Action: incrementing the memory@memory pointer");
				}
				break;
			case '-':
				if(consoleDebugging){
					System.out.println("Action: decrementing the memory@memory pointer");
				}
				memory[memPointer]--;
				break;
			case '>':
				if(consoleDebugging){
					System.out.println("Action: incrementing the memory pointer");
				}
				memPointer++;
				break;
			case '<':
				if(consoleDebugging){
					System.out.println("Action: decrementing the memory pointer");
				}
				memPointer--;
				break;
			case '.':
				if(ui){
					UI.print((char)memory[memPointer]);
				}
				else{
					if(consoleDebugging){
						System.out.println("Action: printing the memory@memory pointer as char\nOutput: " + (char) memory[memPointer]);
					}
					else{
						System.out.print((char) memory[memPointer]);
					}
				}
				break;
			case ',':
				if(ui){
					UI.prepareForInput();
					codePos++;
					return;
				}
				else{
					if(consoleDebugging){
						System.out.println("Action: reading next token byte");
					}
					buffer = new Scanner(System.in);
					String input = buffer.next();
					if(input.endsWith("n")){
						try{
							memory[memPointer] = Short.parseShort(input.substring(0, input.length()-1));
						}
						catch(NumberFormatException e){
							System.out.println("An error occured - can not convert your input to a number!");
							doCleanup();
							return;
						}
					}
					else{
						memory[memPointer] = (short) input.charAt(0);
					}	
				}
				break;
			case '[':
				if(consoleDebugging){
					System.out.println("Action: enter loop");
				}
				if(memory[memPointer] == 0){
					if(consoleDebugging){
						System.out.println("memory@memory pointer == 0, jump forward after end of the loop");
					}
					codePos++;
					int looplvl = 0;
					boolean loopEndFound = false;
					while(!loopEndFound){
						if(consoleDebugging){
							System.out.println("\tlooplvl == " + looplvl);
						}
						if(codePos == code.length()+1){
							if(ui){
								JOptionPane.showMessageDialog(null, "An Error occured - no loopend found", null, JOptionPane.ERROR_MESSAGE);
								doCleanup();
								return;
							}
							else{
								System.out.println("error: no loopend found");
								buffer.close();
								System.exit(0);
							}
						}
						if(code.charAt(codePos) == '['){
							looplvl++;
						}
						if(code.charAt(codePos) == ']'){
							if(looplvl == 0){
								loopEndFound = true;
								codePos--;
							}
							else{
								looplvl--;
							}
						}
						codePos++;
					}
				}
				else if(consoleDebugging){
					System.out.println("memory@memory pointer != 0, enter loop");
				}
				break;
			case ']':
				if(consoleDebugging){
					System.out.println("Action: leave loop");
				}
				if(memory[memPointer] != 0){
					if(consoleDebugging){
						System.out.println("memory@memory pointer != 0, jumping back to loopstart");
					}
					codePos--;
					int looplvl = 0;
					boolean loopEndFound = false;
					while(loopEndFound == false){
						if(consoleDebugging){
							System.out.println("\tlooplvl == " + looplvl);
						}
						if(codePos == 0){
							if(ui){
								JOptionPane.showMessageDialog(null, "An Error occured - no loopstart found", null, JOptionPane.ERROR_MESSAGE);
								doCleanup();
								return;
							}
							else{
								System.out.println("error: no loopstart found");
								buffer.close();
								System.exit(0);
							}
						}
						if(code.charAt(codePos) == ']'){
							looplvl++;
						}
						if(code.charAt(codePos) == '['){
							if(looplvl == 0){
								loopEndFound = true;
							}
							else{
								looplvl--;
							}
						}
						codePos--;
					}
				}
				else if(consoleDebugging){		
					System.out.println("memory@memory pointer == 0, go forward");
				}
				break;
			}
			if(consoleDebugging){
				System.out.println("code position = " + codePos);
				System.out.println("memory pointer = " + memPointer);
				System.out.println("memory @ memory pointer = " + memory[memPointer]);
			}
			codePos++;
		}
		if(consoleDebugging){
			System.out.println("Nothing more to do.\nsuccess.");
		}
		if(codePos == code.length()){
			doCleanup();
		}
	}
	
	public void doCleanup(){
		if(!ui){
			System.out.println("\n\n");
		}
		System.out.print("doing cleanup...");
		code = null;
		codePos = 0;
		memPointer = 0;
		memory = new short[0xFFFF];
		System.out.println("success");
	}		
	
	public void continueRunning(short res){
		memory[memPointer] = res;
		interpret();
	}
}
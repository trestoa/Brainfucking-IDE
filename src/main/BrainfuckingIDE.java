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

import ui.*;

/**
 * Brainfucking IDE v1.1.0
 *  
 * IDE/Interpreter for Brainfuck
 * Copyright (C) 2013  Markus Klein
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
				if(memPointer == 0xFFFF){
					memPointer = 0;
				}
				
				else{
					memPointer++;
				}
				break;
			case '<':
				if(consoleDebugging){
					System.out.println("Action: decrementing the memory pointer");
				}
				if(memPointer == 0){
					memPointer = (short) 0xFFFF;
				}
				else{
					memPointer--;
				}
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
								return;
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
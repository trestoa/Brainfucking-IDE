package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

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

public class UI implements ActionListener{	
	
	public MainFrame mainFrame;
	public Terminal terminal;
	private BrainfuckingIDE bF = null;
	public static final String RUN = "runInterpreting";
	public static final String STOP = "stopInterpreting";
	public static final String NEWFILE = "newFile";
	public static final String OPENFILE = "openFile";
	public static final String SAVEFILE = "save";
	public static final String SAVEFILEAS = "saveAs";
	public static final String EXIT = "exit";
	public static final String OPENPREFS = "openPrefs";
	public static final String EMPTYFILE = "Untitled Brainfuck File";
	public static final String INPUT = "input";
	
	public String fileName = EMPTYFILE;	
	public JFileChooser fileChooser;
	public File currentFile = null;
	public boolean fileIsSaved = false;
	private String lastSavedCode = null;
		
	
	public UI(){
		mainFrame = new MainFrame("Brainfucking IDE");
		mainFrame.setActionListener(this);
		mainFrame.setVisible(true);
		terminal = new Terminal("Brinfucking Terminal");
		terminal.setActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if(command == RUN){
			if(bF != null){
				terminal.setVisible(true);
				bF.prepareInterpret();
			}
		}
		if(command == NEWFILE){
			setLastSaveState();
			if(fileIsSaved){
				newFile();
			}
			else{
				int returnVal = JOptionPane.showConfirmDialog(mainFrame, "Your current file is not saved. Would you like to save it?");
				if(returnVal == JOptionPane.YES_OPTION){
					saveFile();
					newFile();
				}
				if(returnVal == JOptionPane.NO_OPTION){
					newFile();
				}
			}
		}
		if(command == OPENFILE){
			setLastSaveState();
			fileChooser = new JFileChooser();
			FileNameExtensionFilter extFilter = new FileNameExtensionFilter("Brainfuck Files", "b", "bf");
			fileChooser.setFileFilter(extFilter);
			int returnValFileChooser = 0;
			if(fileIsSaved){
				returnValFileChooser = fileChooser.showOpenDialog(mainFrame);
			}
			else{
				int returnVal = JOptionPane.showConfirmDialog(mainFrame, "Your current file is not saved. Would you like to save it?");
				if(returnVal == JOptionPane.YES_OPTION){
					saveFile();
					returnValFileChooser = fileChooser.showOpenDialog(mainFrame);
				}
				if(returnVal == JOptionPane.NO_OPTION){
					returnValFileChooser = fileChooser.showOpenDialog(mainFrame);
				}
			}
			if(returnValFileChooser == JFileChooser.APPROVE_OPTION){
				fileName = fileChooser.getSelectedFile().getName();
				mainFrame.setNewFileName(fileName);
				currentFile = fileChooser.getSelectedFile();
				mainFrame.setCode(bF.readCode(currentFile));
			}
		}
		if(command == SAVEFILE){
			saveFile();
		}
		if(command == SAVEFILEAS){
			saveFileAs();
		}
		if(command == EXIT){
			exit();
		}
		if(command == INPUT){
			terminal.setInputable(false);	
			bF.continueRunning(terminal.getInput());
		}
		if(command == STOP){
			stopInterpreting();
			terminal.setInputable(false);
			terminal.setVisible(false);
		}
	}
	
	public void stopInterpreting(){
		bF.doCleanup();
	}
	
	private void setLastSaveState(){
		if(lastSavedCode == null && mainFrame.getCode().isEmpty()){
			fileIsSaved = true;
		}
		else if(lastSavedCode == null && !mainFrame.getCode().isEmpty()){
			fileIsSaved = false;
		}
		else{
			if(lastSavedCode.contentEquals(mainFrame.getCode()) || mainFrame.getCode().isEmpty()){
				fileIsSaved = true;
			}
			else{
				fileIsSaved = false;
			}
		}
	}
	
	private void newFile(){
		mainFrame.setCode("");
		fileName = EMPTYFILE;
		mainFrame.setNewFileName(fileName);
		fileIsSaved = false;
		lastSavedCode = null;
		currentFile = null;
	}
	
	private void saveFile(){
		if(currentFile == null){
			saveFileAs();
		}
		else{
			try{
				writeFile(mainFrame.getCode(), currentFile);
				lastSavedCode = mainFrame.getCode();
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(mainFrame, "An Error occured, the programm stops automaticaly", null, JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
	
	private void saveFileAs(){
		fileChooser = new JFileChooser();
		FileNameExtensionFilter extFilter = new FileNameExtensionFilter("Brainfuck Files [*.b, *.bf]", "b", "bf");
		fileChooser.setFileFilter(extFilter);
		int returnVal = fileChooser.showOpenDialog(mainFrame);
		if(returnVal == JFileChooser.APPROVE_OPTION){
			try{
				currentFile = fileChooser.getSelectedFile();
				writeFile(mainFrame.getCode(), currentFile);
				lastSavedCode = mainFrame.getCode();
				fileName = fileChooser.getSelectedFile().getName();
				mainFrame.setNewFileName(fileName);
			}
			catch(IOException e){
				JOptionPane.showMessageDialog(mainFrame, "An Error occured, the programm stops automaticaly", null, JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}
	
	public void writeFile(String text, File file) throws IOException{
		Path path = null;
		try{
			path = file.toPath();
			if(!file.exists()){
				Files.createFile(path);
			}
			Files.write(path, text.getBytes());
		}
		catch(IOException e){
			throw e;
		}
	}
	
	public void writeFile(String text, String fileName){
		Path path = null;
		try{
			path = Paths.get(fileName);
			Files.createFile(path);
			Files.write(path, text.getBytes());
		}
		catch(FileAlreadyExistsException e){
			System.out.print("file named already exists!");
		}
		catch(IOException e){
			System.out.print("createFile error!");
		}
	}
	
	public void setInterpreter(BrainfuckingIDE b){
		bF = b;
	}		
	
	public void exit(){
		setLastSaveState();
		if(fileIsSaved){
			System.exit(0);
		}
		else{
			int returnVal = JOptionPane.showConfirmDialog(mainFrame, "Your current file is not saved. Would you like to save it?");
			if(returnVal == JOptionPane.YES_OPTION){
				saveFile();
				System.exit(0);
			}
			if(returnVal == JOptionPane.CANCEL_OPTION){}
			else{
				System.exit(0);
			}
		}
	}
	
	public String getCode(){
		return mainFrame.getCode();
	}

	public void print(char c){
		terminal.setText(c + "");
	}
	
	public void prepareForInput(){
		terminal.setInputable(true);
	}
}

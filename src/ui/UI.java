package ui;

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

import ui.extras.TextGenerator;

import main.BrainfuckingIDE;

/**
 * Brainfucking IDE v1.2.0
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
	public static final String OPENTEXTGENERATOR = "openTextGenerator";
	public static final String DEBUG = "debug";
	public static final String STEPFORWARD = "steoForward";
	
	public String fileName = EMPTYFILE;	
	public JFileChooser fileChooser;
	public File currentFile = null;
	public boolean fileIsSaved = false;
	private String lastSavedCode = null;
	private TextGenerator gen;
		
	
	public UI(){
		mainFrame = new MainFrame("Brainfucking IDE");
		mainFrame.setActionListener(this);
		mainFrame.setVisible(true);
		terminal = new Terminal("Brainfucking Terminal");
		terminal.setActionListener(this);
		gen = new TextGenerator();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command){
		case RUN:
			if(bF != null){
				terminal.setVisible(true);
				bF.prepareInterpret(false);
			}
			break;
		case DEBUG:
			if(bF != null){
				terminal.setVisible(true);
				bF.prepareInterpret(true);	
			}
			break;
		case STEPFORWARD:
			if(bF != null){
				bF.interpret();
			}
			break;
		case NEWFILE:
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
			break;
		case OPENFILE:
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
			break;
		case SAVEFILE:
			saveFile();
			break;
		case SAVEFILEAS:
			saveFileAs();
			break;
		case EXIT:
			exit();
			break;
		case INPUT: 
			terminal.setInputable(false);	
			short input = terminal.getInput();
			if(input == -1){
				JOptionPane.showMessageDialog(null, "Wrong input, please try again!");
				prepareForInput();
				return;
			}
			bF.continueRunning(terminal.getInput());
			break;
		case STOP:
			bF.doCleanup(true);
			terminal.setInputable(false);
			terminal.setVisible(false);
			break;
		case OPENTEXTGENERATOR:
			gen.setVisible(true);
		}
	}
	
	public void stopInterpreting(){
		terminal.setInputable(false);
		terminal.setVisible(false);
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
	
	public void setDebugPos(int debugPos){
		mainFrame.setDebugPos(debugPos);
	}
	
	public void clearDebugging(){
		mainFrame.clearDebugging();
	}
	
	public void resetTerminal(){
		terminal.reset();
	}
}

package ui.extras;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ui.FrameListener;

/**
 * Brainfucking IDE v1.3.0
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

public class TextGenerator extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JPanel textPanel;
	private JScrollPane textPane;
	private JTextArea textArea;
	private JPanel controlPanel;
	private JButton generateButton;
	private JButton aboutButton;
	private JScrollPane codePane;
	private JTextArea codeArea;
	private FrameListener frameListener;
	private AboutFrame about;
	
	public TextGenerator(){
		about = new AboutFrame();
		frameListener = new FrameListener();
		this.setTitle("Brainfucking IDE Text Generator");
		this.setSize(400, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addComponentListener(frameListener);
		this.addWindowStateListener(frameListener);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		this.add(mainPanel);
		
		codeArea = new JTextArea();
		codeArea.setEditable(false);
		codePane = new JScrollPane(codeArea);
		codePane.setBorder(BorderFactory.createTitledBorder("Brainfuck Code"));
		codePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codePane.setVisible(false);
		mainPanel.add(codePane, BorderLayout.PAGE_END);
		
		textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		textPanel.setBorder(BorderFactory.createTitledBorder("Type some text here: "));
		mainPanel.add(textPanel, BorderLayout.LINE_START);
		
		textArea = new JTextArea();
		textArea.setEditable(true);
		textPane = new JScrollPane(textArea);
		textPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textPane.setBorder(BorderFactory.createEmptyBorder());
		textPane.setPreferredSize(new Dimension(this.getWidth() - 35, this.getHeight() - 100));
		textPanel.add(textPane);
		
		controlPanel = new JPanel();
		controlPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		textPanel.add(controlPanel);
		
		generateButton = new JButton("Generate Code");
		generateButton.addActionListener(this);
		generateButton.setActionCommand("generate");
		controlPanel.add(generateButton);
		
		aboutButton = new JButton("About this Tool");
		aboutButton.addActionListener(this);
		aboutButton.setActionCommand("openAbout");
		controlPanel.add(aboutButton);
	}
	
	@Override
	public void setSize(Dimension d){
		super.setSize(d);
		if(codePane.isVisible()){
			textPane.setPreferredSize(new Dimension(this.getWidth() - 35, this.getHeight() / 2));
			codePane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / 2));
		}
		else{
			textPane.setPreferredSize(new Dimension(this.getWidth() - 35, this.getHeight() - 100));
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "generate"){
			codeArea.setText(generateBrainfuckCode(textArea.getText()));
			if(!codePane.isVisible()){
				codePane.setVisible(true);
				this.setSize(new Dimension(this.getWidth(), this.getHeight() + 100));
			}
		}
		if(e.getActionCommand() == "openAbout"){
			about.setVisible(true);
		}
	}
	
	private String generateBrainfuckCode(String s){
		short [] text = new short[s.length()];
		short currentMemory = 0;
		String code = "";
		for(int i = 0; i < s.length(); i++){
			text[i] = (short) s.charAt(i);
			currentMemory += text[i];
		}
		int i = 0;
		currentMemory = (short) (currentMemory / text.length);
		for( ; i <= currentMemory - 10; i += 10){
			code += "+";
		}
		if(i != 0){
			code += "[->++++++++++<]>";
		}
		for( ; i < currentMemory; i++){
				code += "+";
		}
		code += "\n";
		i = 0;
		for( ; i < text.length; i++){
			int differenceToCurrentMemory = Math.abs(text[i] - currentMemory);
			int j = 0;
			for( ; j*5 <= differenceToCurrentMemory - 5; j ++){
				if(j == 0){
					code += "<";
				}
				code += "+";
			}
			if(j != 0){
				if(text[i] < currentMemory){
					code += "[->-----<]>";
					currentMemory -= 5*j;
				}
				else{
					code += "[->+++++<]>";
					currentMemory += 5*j;
				}
				j = j*5;
			}
			for( ; j < differenceToCurrentMemory; j++){
				if(text[i] < currentMemory){
					code += "-";
					currentMemory--;
				}
				else{
					code += "+";
					currentMemory++;
				}
			}
			code += ".\n";
		}
		return code;
	}
	
	private class AboutFrame extends JFrame{
		
		private JTextArea textArea;
		
		public AboutFrame(){
			super("Brainfucking IDE Text Generator");
			this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			this.setResizable(false);
			this.setSize(300, 150);			textArea = new JTextArea();
			textArea.setText("The Text Generator helps you to\n" +
					"output text in Brainfuck. Type your text into\n" +
					"the textarea annd click \"Generate Code\". Now\n" +
					"copy die Brainfuck code into your file.");
			textArea.setEditable(false);
			textArea.setBorder(BorderFactory.createTitledBorder("What is the Text Generator?"));
			this.add(textArea);
		}
		
	}
}
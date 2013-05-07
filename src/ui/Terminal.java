package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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

public class Terminal extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JPanel mainPanel;
	private JTextArea mainTextArea;
	private JScrollPane mainTextPane;
	private JPanel inputPanel;
	private JLabel textInputLabel;
	private JTextField textInputArea;
	private JLabel numberInputLabel;
	private JTextField numberInputArea;
	private JButton inputButton;
	private FrameListener frameListener;
	private JMenuBar menuBar;
	private JMenu windowMenu;
	private JMenuItem menuItem;
	
	public Terminal(String title){
		super(title);
		frameListener = new FrameListener();
		this.setSize(400, 400);
		this.setMaximumSize(new Dimension(300, 300));
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.addComponentListener(frameListener);
		this.addWindowStateListener(frameListener);
		this.addWindowListener(frameListener);
		
		mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(Color.WHITE);
		this.add(mainPanel);
		
		mainTextArea = new JTextArea();
		mainTextArea.setEditable(false);
		mainTextPane = new JScrollPane(mainTextArea);
		mainTextPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		mainTextPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(mainTextPane, BorderLayout.NORTH);
		
		inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.setBorder(BorderFactory.createTitledBorder("Input:"));
		inputPanel.setPreferredSize(new Dimension(this.getWidth(), 50));
		mainPanel.add(inputPanel, BorderLayout.SOUTH);
		
		textInputLabel = new JLabel("for Character: ");
		inputPanel.add(textInputLabel);
		
		textInputArea = new JTextField(20);
		inputPanel.add(textInputArea);
		
		numberInputLabel = new JLabel("or for Number: ");
		inputPanel.add(numberInputLabel);
		
		numberInputArea = new JTextField(20);
		inputPanel.add(numberInputArea);
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		windowMenu = new JMenu("Window");
		menuBar.add(windowMenu);
		menuItem = new JMenuItem("clear terminal");
		menuItem.addActionListener(this);
		menuItem.setActionCommand("clear");
		windowMenu.add(menuItem);
	}
	
	public void setActionListener(UI l){
		frameListener.setUI(l);
		inputButton = new JButton("ok");
		inputButton.addActionListener(l);
		inputButton.setActionCommand(Byte.toString(ui.UI.INPUT));
		inputButton.setEnabled(false);
		inputPanel.add(inputButton);
	}
	
	@Override
	public void setSize(Dimension d){
		super.setSize(d);
		mainTextPane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 107));
	}
	
	public void setText(String s){
		mainTextArea.setText(mainTextArea.getText() + s);
	}
	
	public void setInputable(boolean b){
		inputButton.setEnabled(b);
	}
	public short getInput(){
		if(!textInputArea.getText().isEmpty()){
			short res = (short)textInputArea.getText().charAt(0);
			textInputArea.setText("");
			return res;
		}
		else if(!numberInputArea.getText().isEmpty()){
			short res;
			try{
				res = Short.parseShort(numberInputArea.getText());
				if(res < 0){
					return -1;
				}
			}
			catch(NumberFormatException x){
				res = -1;
			}
			numberInputArea.setText("");
			return res;
		}
		else{
			return -1;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "clear"){
			mainTextArea.setText("");
		}		
	}
	
	public void reset(){
		this.setVisible(false);
		mainTextArea.setText("");
	}
}

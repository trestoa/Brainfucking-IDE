package main;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

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
		inputButton.setActionCommand(main.UI.INPUT);
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
		else{
			short res;
			try{
				res = Short.parseShort(numberInputArea.getText());
			}
			catch(NumberFormatException x){
				JOptionPane.showMessageDialog(null, "Error - your entered number was no number!!");
				res = 0;
			}
			numberInputArea.setText("");
			return res;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand() == "clear"){
			mainTextArea.setText("");
		}		
	}
}

package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

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

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JPanel iconPanel;
	private JButton runButton;
	private JButton stopButton;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem menuItem;
	private JMenu windowMenu;
	private JTextArea codeTextArea;
	private JScrollPane codePane;
	private TitledBorder codePaneBorder;
	private FrameListener frameListener;
	
	public MainFrame(String title){
		super(title);
		this.setTitle(title);
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameListener = new FrameListener();
		this.addComponentListener(frameListener);
		this.addWindowStateListener(frameListener);
		this.addWindowListener(frameListener);
		
		mainPanel = new JPanel();
		BorderLayout mainLayoutManager = new BorderLayout();
		mainLayoutManager.setVgap(5);
		mainPanel.setLayout(mainLayoutManager);
		mainPanel.setBackground(Color.WHITE);
		this.add(mainPanel);
		
		iconPanel = new JPanel();
		iconPanel.setLayout(new BoxLayout(iconPanel, BoxLayout.X_AXIS));
		iconPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		mainPanel.add(iconPanel, BorderLayout.NORTH);
		
		codeTextArea = new JTextArea();
		codePane = new JScrollPane(codeTextArea);
		codePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codePaneBorder = BorderFactory.createTitledBorder(main.UI.EMPTYFILE);
		codePane.setBorder(codePaneBorder);
		Dimension size = new Dimension();
		size.height = this.getHeight() - 100;
		codePane.setPreferredSize(size);
		mainPanel.add(codePane, BorderLayout.SOUTH);
	}
	
	public void setActionListener(UI l){
		frameListener.setUI(l);
		JSeparator separator = new JSeparator(JSeparator.VERTICAL);
		Dimension size = new Dimension(
			separator.getPreferredSize().width,
			separator.getMaximumSize().height);
		separator.setMaximumSize(size);
		
		runButton = new JButton(new ImageIcon("img/run.png"));
		runButton.setMaximumSize(new Dimension(26, 26));
		runButton.setToolTipText("run");
		runButton.setActionCommand(main.UI.RUN);
		runButton.addActionListener(l);
		iconPanel.add(runButton);
		
		iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		iconPanel.add(separator);
		iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		stopButton = new JButton(new ImageIcon("img/stop.png"));
		stopButton.setMaximumSize(new Dimension(26, 26));
		stopButton.setActionCommand(main.UI.STOP);
		stopButton.addActionListener(l);
		iconPanel.add(stopButton);
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		menuItem = new JMenuItem("New Brainfuck File...");
		menuItem.setActionCommand(main.UI.NEWFILE);
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Open Brainfuck File...");
		menuItem.setActionCommand(main.UI.OPENFILE);
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		fileMenu.addSeparator();
		menuItem = new JMenuItem("Save...");
		menuItem.setActionCommand(main.UI.SAVEFILE);
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Save as...");
		menuItem.setActionCommand(main.UI.SAVEFILEAS);
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		fileMenu.addSeparator();
		menuItem = new JMenuItem("Exit...");
		menuItem.setActionCommand(main.UI.EXIT);
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		
		/* the Preferences are not implemented jet! 
		windowMenu = new JMenu("Window");
		menuBar.add(windowMenu);
		menuItem = new JMenuItem("Preferences...");
		windowMenu.add(menuItem);
		stopButton.addActionListener(l);
		stopButton.setActionCommand(main.UI.OPENPREFS);*/
	}
	
	public void setNewFileName(String fileName){
		codePaneBorder = BorderFactory.createTitledBorder(fileName);
		codePane.setBorder(codePaneBorder);
	}
	
	public void setCode(String text){
		codeTextArea.setText(text);
	}
	
	public String getCode(){
		return codeTextArea.getText();
	}
	
	@Override
	public void setSize(Dimension d){
		super.setSize(d);
		codePane.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() - 105));
	}
}
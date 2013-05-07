package ui;

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
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

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

public class MainFrame extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainPanel;
	private JPanel iconPanel;
	private JButton runButton;
	private JButton debugButton;
	private JButton stopButton;
	private JButton stepButton;
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenuItem menuItem;
	private JMenu extraMenu;
	private JTextPane codeTextArea;
	private JScrollPane codePane;
	private TitledBorder codePaneBorder;
	private FrameListener frameListener;
	private final DefaultStyledDocument doc;
	private StyleContext sc;
	private final Style mainStyle;
	private final Style debugCurrent;
	private int lastDebugPos = 0;
	
	public MainFrame(String title){
		super(title);
		this.setTitle(title);
		this.setSize(1000, 600);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frameListener = new FrameListener();
		this.addComponentListener(frameListener);
		this.addWindowStateListener(frameListener);
		this.addWindowListener(frameListener);
		
		sc = new StyleContext();
		doc = new DefaultStyledDocument(sc);
		
		Style defaultStyle = sc.getStyle(StyleContext.DEFAULT_STYLE);
	    mainStyle = sc.addStyle("MainStyle", defaultStyle);
	    StyleConstants.setFontFamily(mainStyle, "monospaced");
	    StyleConstants.setFontSize(mainStyle, 12);

	    debugCurrent = sc.addStyle("ConstantWidth", null);
	    StyleConstants.setFontFamily(debugCurrent, "monospaced");
	    StyleConstants.setForeground(debugCurrent, Color.green);
	    StyleConstants.setBackground(debugCurrent, Color.BLACK);

	    try {
	      SwingUtilities.invokeAndWait(new Runnable() {
	        public void run() {
	          try {
	            doc.setLogicalStyle(0, mainStyle);
	            doc.insertString(0, "", null);
	          } catch (BadLocationException e) {
	          }
	        }
	      });
	    } catch (Exception e) {
	      System.out.println("Exception when constructing document: " + e);
	      System.exit(1);
	    }

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
		
		codeTextArea = new JTextPane(doc);
		codePane = new JScrollPane(codeTextArea);
		codePane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		codePaneBorder = BorderFactory.createTitledBorder(ui.UI.EMPTYFILE);
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
		runButton.setActionCommand(Byte.toString(ui.UI.RUN));
		runButton.addActionListener(l);
		iconPanel.add(runButton);
		
		iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		stopButton = new JButton(new ImageIcon("img/stop.png"));
		stopButton.setMaximumSize(new Dimension(26, 26));
		stopButton.setActionCommand(Byte.toString(ui.UI.STOP));
		stopButton.addActionListener(l);
		stopButton.setToolTipText("stop");
		iconPanel.add(stopButton);
		
		iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		iconPanel.add(separator);
		iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		debugButton = new JButton(new ImageIcon("img/debug.png"));
		debugButton.setMaximumSize(new Dimension(26, 26));
		debugButton.setActionCommand(Byte.toString(ui.UI.DEBUG));
		debugButton.setToolTipText("debug");
		debugButton.addActionListener(l);
		iconPanel.add(debugButton);
		
		iconPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		
		stepButton = new JButton(new ImageIcon("img/step.png"));
		stepButton.setMaximumSize(new Dimension(26, 26));
		stepButton.setActionCommand(Byte.toString(ui.UI.STEPFORWARD));
		stepButton.setToolTipText("step forward");
		stepButton.addActionListener(l);
		iconPanel.add(stepButton);
		
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		menuItem = new JMenuItem("New Brainfuck File...");
		menuItem.setActionCommand(Byte.toString(ui.UI.NEWFILE));
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Open Brainfuck File...");
		menuItem.setActionCommand(Byte.toString(ui.UI.OPENFILE));
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		fileMenu.addSeparator();
		menuItem = new JMenuItem("Save...");
		menuItem.setActionCommand(Byte.toString(ui.UI.SAVEFILE));
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		menuItem = new JMenuItem("Save as...");
		menuItem.setActionCommand(Byte.toString(ui.UI.SAVEFILEAS));
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		fileMenu.addSeparator();
		menuItem = new JMenuItem("Exit...");
		menuItem.setActionCommand(Byte.toString(ui.UI.EXIT));
		menuItem.addActionListener(l);
		fileMenu.add(menuItem);
		
		extraMenu = new JMenu("Extras");
		menuBar.add(extraMenu);
		menuItem = new JMenuItem("TextGenerator");
		menuItem.addActionListener(l);
		menuItem.setActionCommand(Byte.toString(ui.UI.OPENTEXTGENERATOR));
		extraMenu.add(menuItem);
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
	
	public void setDebugPos(int debugPos){
		doc.setCharacterAttributes(getRealPos(debugPos), 1, debugCurrent, false);
		if(debugPos != 0){
			doc.setCharacterAttributes(getRealPos(lastDebugPos), 1, mainStyle, true);
		}
		lastDebugPos = debugPos;
	}
	
	private int getRealPos(int compiledPos){
		String code = codeTextArea.getText();
		int realPos = 0;
		int comPos = 0;
		for(;;){
			if(comPos == compiledPos && (code.charAt(realPos) == '<' || code.charAt(realPos) == '>' || code.charAt(realPos) == '+' || code.charAt(realPos) == '-' || code.charAt(realPos) == ','  || code.charAt(realPos) == '.' || code.charAt(realPos) == '[' || code.charAt(realPos) == ']')){
				return realPos;
			}
			if(code.charAt(realPos) == '<' || code.charAt(realPos) == '>' || code.charAt(realPos) == '+' || code.charAt(realPos) == '-' || code.charAt(realPos) == ','  || code.charAt(realPos) == '.' || code.charAt(realPos) == '[' || code.charAt(realPos) == ']'){
				comPos++;
			}
			realPos++;
		}
				
	}
	
	public void clearDebugging(){
		String code = codeTextArea.getText();
		doc.setCharacterAttributes(0, code.length(), mainStyle, true);
	}
}
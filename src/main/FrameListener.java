package main;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

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

public class FrameListener implements ComponentListener, WindowStateListener, WindowListener {
	private UI UI;
	
	public void setUI(UI ui){
		UI = ui;
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		
	}

	@Override
	public void componentResized(ComponentEvent e) {
		//Hello! :D
		if(e.getComponent().getClass() == MainFrame.class){
			e.getComponent().setSize(new Dimension(e.getComponent().getSize()));
		}
		if(e.getComponent().getClass() == Terminal.class){
			e.getComponent().setSize(new Dimension(e.getComponent().getSize()));
		}
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		if(e.getComponent().getClass() == MainFrame.class){
			e.getComponent().setSize(new Dimension(e.getComponent().getSize()));
		}
		if(e.getComponent().getClass() == Terminal.class){
			e.getComponent().setSize(new Dimension(e.getComponent().getSize()));
		}
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		if(e.getComponent().getClass() == MainFrame.class){
			UI.exit();
		}
		if(e.getComponent().getClass() == Terminal.class){
			UI.stopInterpreting();
		}
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

}

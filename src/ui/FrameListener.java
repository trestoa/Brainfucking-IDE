package ui;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

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
		e.getComponent().setSize(new Dimension(e.getComponent().getSize()));
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		
	}

	@Override
	public void windowStateChanged(WindowEvent e) {
		e.getComponent().setSize(new Dimension(e.getComponent().getSize()));		
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

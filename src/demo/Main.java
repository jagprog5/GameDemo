package demo;

import javax.swing.SwingUtilities;

/*
 * John Giorshev
 * November 14th, 2018.
 * 
 * The primary purpose of this project is to familiarize myself with GitHub.
 * The secondary purpose is to refresh myself on the light-weight game framework I build several years ago.
 * 
 * The project is a demo of various simple game mechanics and particle effects.
 */
public class Main {
	public static void main(String[] args) {
		// As per best-practices for swing, GUI is started in initial thread.
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new GUI();
			}
		});
	}
}

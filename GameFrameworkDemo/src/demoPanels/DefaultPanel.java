package demoPanels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DefaultPanel extends JPanel {
	public DefaultPanel() {
		setLayout(new BorderLayout());
		JLabel message = new JLabel("Select a demo to begin.", SwingConstants.CENTER);
		message.setFont(new Font("sans-serif", Font.PLAIN, 50));
		message.setForeground(Color.LIGHT_GRAY);
		add(message);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
	}
}

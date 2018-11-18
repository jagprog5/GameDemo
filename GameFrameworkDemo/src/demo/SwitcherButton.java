package demo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * SwitcherButton is a fake JRadioButton.
 * When an instance of SwitcherButton is pressed, it iterates through all components in its parent,
 * and enables all SwitcherButton that are not this instance, and disables this instance.
 * The ActionListener is then called normally.
 * @author John Giorshev
 */
public class SwitcherButton extends JButton {
	public SwitcherButton(String txt, ActionListener al) {
		super(txt);
		addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				//This acts like a radio button setup.
				for (Component c : getParent().getComponents()) {
					if (c instanceof SwitcherButton) {
						c.setEnabled(!((SwitcherButton) c).equals(getInstance()));
					}
				}
				al.actionPerformed(ae);
			}
		});
	}
	
	//required to get instance reference inside ActionListener
	private SwitcherButton getInstance() {
		return this;
	}
}
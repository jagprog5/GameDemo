package util;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.SwingUtilities;

/**
 * GamePanel is an extension of AnimationPanel that adds functionality for key
 * and mouse input.
 * 
 * Edit: November 17th, 2018 Fixed a bug where strange behavior would occur for
 * charsDown and keyCodesDown when mashing shift and caps-lock. Fixed a bug
 * where num-lock would cause sticking keys.
 * 
 * @author John Giorshev
 */
public abstract class GamePanel extends AnimationPanel
		implements KeyListener, MouseListener, MouseWheelListener, MouseMotionListener, FocusListener {
	private boolean keyReleased;

	// Character lists should only contain unique elements.
	// They also don't need any particular order.
	private HashSet<Character> charsDown;
	private HashSet<Integer> keyCodesDown;
	private boolean lastCapsState = false;

	private int lastMouseX;
	private int lastMouseY;
	private boolean leftMousePressed;
	private boolean leftMouseJustPressed;
	private boolean leftMouseReleased;

	private boolean rightMousePressed;
	private boolean rightMouseJustPressed;
	private boolean rightMouseReleased;

	private int mouseWheelChange;

	public GamePanel() {
		super();
		addKeyListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
		addMouseMotionListener(this);
		addFocusListener(this);
		setFocusable(true);

		// charsDown is a list of all simple characters that are currently being
		// pressed.
		// w, W, etc.
		// Simple characters are all character that have a lower-case or upper-case
		// equivalent on the keyboard
		charsDown = new HashSet<Character>();

		// keyCodesDown is a list of all non-simple key-codes that are currently
		// being pressed.
		// This is used for arrow keys, etc.
		// Simple key codes will no appear in this.
		keyCodesDown = new HashSet<Integer>();
	}
	
	/**
	 * @return True if any key is being pressed
	 */
	public boolean keyPressed() {
		return keyCodesDown.size() > 0 || charsDown.size() > 0;
	}

	/**
	 * @return HashSet containing all "simple characters" that are currently being
	 *         pressed down on the keyboard.<br>
	 *         Simple characters are all character that have a lower-case or
	 *         upper-case equivalent on the keyboard.<br>
	 *         Examples: 'w', 'W', '2', '@', '`', '='<br>
	 *         If the array is always blank, make sure this component has focus.
	 * @see #keyCodesDown()
	 */
	public HashSet<Character> charsDown() {
		return charsDown;
	}

	/**
	 * @return HashSet of key codes for all non-simple characters that are currently
	 *         being pressed down on the keyboard<br>
	 *         Examples: Arrow keys, backspace, ctrl, alt.<br>
	 *         Example: keyCodesDown.contains(KeyEvent.VK_UP)
	 *         
	 * @see #charsDown()
	 */
	public HashSet<Integer> keyCodesDown() {
		return keyCodesDown;
	}

	/**
	 * @param c
	 *            Character to check.
	 * @return True if the character is currently being pressed.
	 * @see #keyCodesDownContains(int)
	 */
	public boolean charsDownContains(char c) {
		return charsDown.contains(c);
	}

	/**
	 * @param c
	 *            Characters to check.
	 * @return True if any of the characters are currently being pressed. Note that
	 *         this function will only work for simple characters.
	 * @see #keyCodesDownContains(int[])
	 */
	public boolean charsDownContains(Character[] c) {
		for (char ch : c) {
			if (charsDownContains(ch)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param i
	 *            KeyCode to check.
	 * @return True if the key for the KeyCode is being pressed.
	 * @see #charsDownContains(char)
	 */
	public boolean keyCodesDownContains(int i) {
		return keyCodesDown.contains(i);
	}

	/**
	 * @param i
	 *            KeyCodes to check.
	 * @return True if any of the KeyCodes corresponding keys are being pressed.
	 * @see #charsDownContains(char[])
	 */
	public boolean keyCodesDownContains(int[] is) {
		for (int i : is) {
			if (keyCodesDownContains(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return If any key was released since the previous frame.
	 */
	public boolean keyReleased() {
		return keyReleased;
	}

	// Updating mouse xy values.

	/**
	 * @return The X value of the mouse position.
	 */
	public int getMouseX() {
		return lastMouseX;
	}

	/**
	 * @return The Y value of the mouse position in the last mouse event received by
	 *         this panel.
	 */
	public int getMouseY() {
		return lastMouseY;
	}
	
	/**
	 * @return If mouse button 1 is currently being pressed.
	 */
	public boolean leftMousePressed() {
		return leftMousePressed;
	}

	/**
	 * @return If mouse button 1 was released since the previous frame.
	 */
	public boolean leftMouseReleased() {
		return leftMouseReleased;
	}

	/**
	 * @return If mouse button 1 was not pressed, then pressed since the previous
	 *         frame.
	 */
	public boolean leftMouseJustPressed() {
		return leftMouseJustPressed;
	}

	/**
	 * @return If mouse button 3 is currently being pressed.
	 */
	public boolean rightMousePressed() {
		return rightMousePressed;
	}

	/**
	 * @return If mouse button 3 was released since the previous frame.
	 */
	public boolean rightMouseReleased() {
		return rightMouseReleased;
	}

	/**
	 * @return If mouse button 3 was not pressed, then pressed since the previous
	 *         frame.
	 */
	public boolean rightMouseJustPressed() {
		return rightMouseJustPressed;
	}
	
	/**
	 * @return The change in mouseWheel since the last frame.<br>
	 * This value is generally -1, 0, or 1.
	 */
	public int getMouseWheelChange() {
		return mouseWheelChange;
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		super.actionPerformed(ae);

		// repaint can act weird, invokeLater makes sure that the full paint
		// cycle is finished before resetting a few values.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Left/right just-pressed will only be true for a frame at a time.
				if (leftMouseJustPressed) {
					leftMouseJustPressed = false;
				}
				if (rightMouseJustPressed) {
					rightMouseJustPressed = false;
				}
				if (leftMouseReleased) {
					leftMouseReleased = false;
				}
				if (rightMouseReleased) {
					rightMouseReleased = false;
				}
				mouseWheelChange = 0;
				keyReleased = false;
			}
		});
	}

	// KeyListener stuff

	@Override
	public void keyPressed(KeyEvent ke) {
		// Shift key needs to be processed before modifyCapsIfNeeded,
		// since modifyCapsIfNeeded is based on the state of the shift key
		if (ke.getKeyCode() == KeyEvent.VK_SHIFT)
			keyCodesDown.add(ke.getKeyCode());
		modifyCapsIfNeeded();

		Character c = ke.getKeyChar();

		// exclude from charsDown all characters that aren't simple.
		// Note: HashSet can't have duplicates. No need to check if character already
		// contained
		if (Character.isLetter(c) || LNAKE.contains(c) || CNAKE.contains(c)) {
			charsDown.add(c);
		} else {
			keyCodesDown.add(ke.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		keyReleased = true;
		Character c = ke.getKeyChar();
		if (capsUsed()) {
			c = toUpperCase(ke.getKeyChar());
		} else {
			c = toLowerCase(ke.getKeyChar());
		}
		// If element does not exist in list, then array is not changed.
		charsDown.remove(c);

		// Stopping NUMPAD from sticking keys. e.g. Shift + NUM1 will give !, but won't
		// release properly.
		// second part of if statement is a workaround from shift key acting strange
		// with num-lock keys
		if (ke.getKeyLocation() == KeyEvent.KEY_LOCATION_NUMPAD && CNKE.contains(ke.getKeyCode())) {
			c = LNKE.get(CNKE.indexOf(ke.getKeyCode()));
			Character newChar = toUpperCase(c);
			charsDown.remove(c);
			if (newChar != c) {
				charsDown.remove(newChar);
			} else {
				charsDown.remove(toLowerCase(c));
			}

		}

		// Note, remove is based on Object (in this case wrapper type), not int
		// (primitive) index.
		keyCodesDown.remove(ke.getKeyCode());
		modifyCapsIfNeeded();
	}

	/**
	 * variable name: capital non-alphabetical keyboard equivalent -> CNAKE note:
	 * indexes of CNAKE and LNAKE are related on keyboard
	 * 
	 * @see #LNAKE
	 */
	private static final ArrayList<Character> CNAKE = new ArrayList<Character>() {
		{
			Character[] arr = new Character[] { '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{',
					'}', '|', ':', '"', '<', '>', '?' };
			addAll(Arrays.asList(arr));
		}
	};

	/**
	 * variable name: lower-case non-alphabetical keyboard equivalent -> LNAKE note:
	 * indexes of CNAKE and LNAKE are related on keyboard
	 * 
	 * @see #CNAKE
	 */
	private static final ArrayList<Character> LNAKE = new ArrayList<Character>() {
		{
			Character[] arr = new Character[] { '`', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '-', '=', '[',
					']', '\\', ';', '\'', ',', '.', '/' };
			addAll(Arrays.asList(arr));
		}
	};

	/**
	 * variable name: capital num-pad keyboard equivalent -> CNKE note: indexes of
	 * CNKE and LNKE are related on keyboard
	 * 
	 * @see #LNKE
	 */
	private static final ArrayList<Integer> CNKE = new ArrayList<Integer>() {
		{
			Integer[] arr = new Integer[] { 35, 40, 34, 37, 12, 39, 36, 38, 33, 155, 127, 107, 109, 106, 111 };
			addAll(Arrays.asList(arr));
		}
	};

	/**
	 * variable name: lower-case num-pad keyboard equivalent -> CNKE note: indexes
	 * of CNKE and LNKE are related on keyboard
	 * 
	 * @see #CNKE
	 */
	private static final ArrayList<Character> LNKE = new ArrayList<Character>() {
		{
			Character[] arr = new Character[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '+', '-', '*',
					'/' };
			addAll(Arrays.asList(arr));
		}
	};

	/**
	 * @return If capital letters should be outputted by the keyboard. Based off of
	 *         if the caps lock is down and if the shift key is pressed. This will
	 *         not function if this instance is paused, since the shift key state is
	 *         updated with the game-loop.
	 */
	public boolean capsUsed() {
		return Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK) != keyCodesDown
				.contains(KeyEvent.VK_SHIFT);
	}

	// Assumes character is already lower-case if no upper-case equivalent exists.
	private static Character toLowerCase(Character c) {
		if (Character.isUpperCase(c)) {
			return Character.toLowerCase(c);
		}
		if (CNAKE.contains(c)) {
			return LNAKE.get(CNAKE.indexOf(c));
		}
		return c;
	}

	// Assumes character is already upper-case if no lower-case equivalent exists.
	private static Character toUpperCase(Character c) {
		if (Character.isLowerCase(c)) {
			return Character.toUpperCase(c);
		}
		if (LNAKE.contains(c)) {
			return CNAKE.get(LNAKE.indexOf(c));
		}
		return c;
	}

	// To simplify and reduce bugs,
	// keyCodesDown and charsDown will be modified to stay consistent with the
	// keyboard shift state
	// Prevents sticking keys from messing with caps lock and shift, etc.
	private void modifyCapsIfNeeded() {
		boolean capsNeeded = capsUsed();
		if (lastCapsState != capsNeeded) {
			lastCapsState = capsNeeded;
			// Requires iterating through charsDown while adding and removing elements.
			// Separate list with order is required to prevent concurrent modification
			for (Character c : new ArrayList<Character>(charsDown)) {
				if (capsNeeded) {
					Character newChar = toUpperCase(c);
					if (newChar != c) {
						charsDown.remove(c);
						charsDown.add(newChar);
					}
				} else {
					Character newChar = toLowerCase(c);
					if (newChar != c) {
						charsDown.remove(c);
						charsDown.add(newChar);
					}
				}
			}
		}
	}

	private void storePoint(MouseEvent me) {
		lastMouseX = me.getX();
		lastMouseY = me.getY();
	}

	@Override
	public void mouseEntered(MouseEvent me) {
	}

	@Override
	public void mouseExited(MouseEvent me) {
	}

	@Override
	public void mouseDragged(MouseEvent me) {
		storePoint(me);
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		storePoint(me);
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			leftMousePressed = true;
			leftMouseJustPressed = true;
		}
		if (me.getButton() == MouseEvent.BUTTON3) {
			rightMousePressed = true;
			rightMouseJustPressed = true;
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (me.getButton() == MouseEvent.BUTTON1) {
			leftMousePressed = false;
			leftMouseJustPressed = false;
			leftMouseReleased = true;
		}
		if (me.getButton() == MouseEvent.BUTTON3) {
			rightMousePressed = false;
			rightMouseJustPressed = false;
			rightMouseReleased = true;
		}
	}

	// MouseWheelListener stuff

	@Override
	public void mouseWheelMoved(MouseWheelEvent mwe) {
		mouseWheelChange = mwe.getWheelRotation();
	}

	@Override
	public void mouseClicked(MouseEvent me) {

	}

	@Override
	public void keyTyped(KeyEvent ke) {

	}

	//If focus is gained or lost, clear keys. Prevents sticking when leaving panel.
	@Override
	public void focusGained(FocusEvent e) {
		charsDown.clear();
		keyCodesDown.clear();
	}

	@Override
	public void focusLost(FocusEvent e) {
		charsDown.clear();
		keyCodesDown.clear();
	}

}
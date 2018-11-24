[![Demo Video](https://github.com/olta8/GameFrameworkDemo/blob/master/src/Assets/DemoThumbnail.png)](http://www.youtube.com/watch?v=X63V_5YDLC0 "Click to watch the demo")

<!--- Above file links to demo video. Displayed image is contained within the project itself. --->

# Game Framework Demo

This is the first project I've used git with. It was built in Eclipse with JDK 8 and uses the stock Java Graphics and Java Swing Libraries. The program is a GUI which outlines some of the features of my simple game framework.

The GUI gives a simple example boss fight, graphics outlining user input, and two other simple demos. The framework handles game-loops and user input (mouse and keyboard). One of the purposes of the framework is to simplify the mouse and keyboard listeners. For an example, if you press ‘w’, ‘e’, then shift, normally you would have to work with three different key events: the two lowercase letters, and KeyCode 16. Instead, the framework provides exactly what is being inputted in the keyboard at all times,  regardless of how you mess with the shift, caps-lock, and any other keys on the keyboard or numpad. If the key characters being pressed in the example are retrieved, you would get capital ‘W’ and ‘E’.

As for mouse input, the framework provides easy functions like getting the scroll wheel delta, mouse position, and whether or not the left or right mouse button has been pressed, just pressed (pressed since last frame), or released. The game-loop can be started, paused, and delay set. Each frame, the function ‘updateMechanics()’ is followed by ‘draw(Graphics2D g2d)’ and is called in the game instance. It is non-interpolating, based on a simple Swing timer.

-John Giorshev

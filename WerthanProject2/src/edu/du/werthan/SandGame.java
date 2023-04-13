package edu.du.werthan;
import java.util.Scanner;
import edu.du.dudraw.DUDraw;

/////////////////////////////////////////////////////////
//   When the program is executed, the user will be    //
//	 prompted to enter their desired canvas size. 	   //
//	 Once both coordinates are entered, the canvas	   //
//   will be drawn. Press H to see a list of all of    //
//   the controls and options. Be sure to see how each //
//   element interacts with one another. Try to find   //
//   the hidden element...Enjoy!!                      //
/////////////////////////////////////////////////////////

public class SandGame {

	public static void main(String[] args) {
		final int F = 70; // initializing the key code for F
		final int S = 83; // initializing the key code for S
		final int E = 69; // initializing the key code for E
		final int W = 87; // initializing the key code for W
		final int L = 76; // initializing the key code for L
		final int R = 82; // initializing the key code for R
		final int One = 49; // initializing the key code for 1
		final int Two = 50; // initializing the key code for 2
		final int Three = 51; // initializing the key code for 3
		final int H = 72; // initializing the key code for H
		int canvasX = 600; // initializes the canvasX size to a default value of 600
		int canvasY = 600; // initializes the canvasY size to a default value of 600
		Scanner myScanner = new Scanner(System.in); // creates a scanner object
	    System.out.println("Enter your desired canvas width:"); // prompts the user for a canvas width
	    canvasX = Integer.parseInt(myScanner.nextLine()); // stores the user's input into canvasX
	    System.out.println("Enter your desired canvas height:"); // prompts the user for a canvas height
	    canvasY = Integer.parseInt(myScanner.nextLine()); // stores the user's input into canvasY
		SandWorld sandworld1 = new SandWorld(canvasX, canvasY); // creates a new SandWorld object and passes in the user specified sizes
		myScanner.close(); // closes the scanner
		DUDraw.enableDoubleBuffering(); // enables double buffering for smoother animations
		int tool = 1; // sets the tool value to 1 (sand tool)
		while(true) { // infinite loop
			DUDraw.clear(); // clears the canvas
			if(DUDraw.isKeyPressed(F)) { // will execute if F is pressed
				tool = 2; // sets the tool value to 2 (floor tool)
			}
			if(DUDraw.isKeyPressed(S)) { // will execute if S is pressed
				tool = 1; // sets the tool value to 1 (sand tool)
			}
			if(DUDraw.isKeyPressed(E)) { // will execute if E is pressed
				tool = 0; // sets the tool value to 0 (empty tool)
			}
			if(DUDraw.isKeyPressed(W)) { // will execute if W is pressed
				tool = 3; // sets the tool value to 3 (water tool)
			}
			if(DUDraw.isKeyPressed(L)) { // will execute if L is pressed
				tool = 4; // sets the tool value to 4 (lava tool)
			}
			if(DUDraw.isKeyPressed(R)) { // will execute if R is pressed
				sandworld1.resetCanvas(); // function call to reset the canvas
			}
			if(DUDraw.isKeyPressed(One)) { // will execute if 1 is pressed
				sandworld1.setPenSize(1); // function call to set the penSize to 1
			}
			if(DUDraw.isKeyPressed(Two)) { // will execute if 2 is pressed
				sandworld1.setPenSize(1.1);  // function call to set the penSize to 1.1 
			}
			if(DUDraw.isKeyPressed(Three)) { // will execute if 3 is pressed
				sandworld1.setPenSize(1.2); // function call to set the penSize to 1.2
			}
			if(DUDraw.isKeyPressed(H)) { // will execute if H is pressed
				sandworld1.toggleHelp(); // function call to set the value of isHelp
			}
			if(DUDraw.isMousePressed()) { // will execute if the mouse is pressed
				sandworld1.placeParticle((int) (DUDraw.mouseX()), (int) (DUDraw.mouseY()), tool); // places a new particle based on the value of tool at the location of the mouse 
			}
			sandworld1.draw(); // function call to draw all particles
			sandworld1.step(); // function call to update all particles	
			DUDraw.pause(10); // buffer delay for 10mS 
		}
	}

}

package edu.du.werthan;
import java.awt.Color;

import edu.du.dudraw.DUDraw;

public class SandWorld {

	private int[][] pixelState; // initialize the 2D pixel array 
	public static final int EMPTY = 0; // initialize empty pixel state
	public static final int SAND = 1; // initialize sand pixel state
	public static final int FLOOR = 2; // initialize floor pixel state
	public static final int WATER = 3; // initialize water pixel state
	public static final int LAVA = 4; // initialize lava pixel state
	public static final int ROCK = 5; // initialize rock pixel state
	public static final int sidesEmpty = 0; // initialize empty water border state
	public static final int leftEmpty = 1; // initialize left empty water border state
	public static final int rightEmpty = 2; // initialize right empty water border state
	public static final int sidesOccupied = 3; // initialize not empty water border state
	public static final int waterLavaBelow = 4; // initialize lava below water border state
	public static final int waterLavaRight = 5; // initialize lava right of water border state
	public static final int waterLavaLeft = 6; // initialize lava left of water border state
	public static final int lavaEmpty = 0; // initialize empty lava border state
	public static final int lavaLeft = 1; // initialize left empty lava border state
	public static final int lavaRight = 2; // initialize right empty lava border state
	public static final int lavaOccupied = 3; // initialize not empty lava border state
	public static final int lavaWaterLeft = 4; // initialize water left of lava border state
	public static final int lavaWaterRight = 5; // initialize water right of lava border state
	public static final int lavaWaterBelow = 6; // initialize water below lava border state
	public static final int sandEmpty = 0; // initialize empty sand border state
	public static final int sandLeft = 1; // initialize left empty sand border state
	public static final int sandRight = 2; // initialize right empty sand border state
	public static final int sandStop = 3; // initialize not empty sand border state
	private double penSize = 1; // initialize the pen size
	private int myRow; // initialize the canvas width
	private int myCol; // initialize the canvas height
	private int mode = SAND; // initializes the tool mode
	private boolean isHelp = false; // initializes the help page condition
	private String toolName; // initializes the display name for the tool
	private Color sand = new Color(	194, 178, 128); // initializes the sand color
	private Color water = new Color(116,204,244); // initializes the water color
	private Color lava = new Color(237, 41, 57); // initializes the lava color
	private Color rock = new Color(101,83,83); // initializes the rock color
	private int waterBorderState = sidesEmpty; // initializes the water border state to empty
	private int lavaBorderState = lavaEmpty; // initializes the lava border state to empty
	private int sandBorderState = sandEmpty; // initializes the sand border state to empty
	
	// Sand World Constructor //
	public SandWorld(int row, int col) {
		DUDraw.setCanvasSize(row, col); // sets the canvas size to the user specified values
		DUDraw.setXscale(0, row); // sets the scale of the x axis from 0 to the user specified value
		DUDraw.setYscale(0, col); // sets the scale of the y axis from 0 to the user specified value
		myRow = row; // sets the private variable for width of the canvas to the user specified value
		myCol = col; // sets the private variable for height of the canvas to the user specified value
		pixelState = new int[row][col]; // sets the size of pixel state to the size of the canvas
		initializeParticles(); // function call to set the state of the initial pixels on the canvas
	}
	
	// Place a pixel of a certain element based on the current tool and mouse position //
	public boolean placeParticle(int row, int col, int newType) {
		if(row > 0 && 
			row < myRow - 1 && 
			col > 0 && 
			col < myCol - 1) { // will execute if the current pixel is within the bounds of the canvas
			if(pixelState[row][col] == 0) {	// ensures that the pixel is empty
				if(newType == 1) { // will execute if the tool is SAND
					newSandParticle(row, col); // function call to place a new sand pixel
				} else if (newType == 2) { // will execute if the pixel is FLOOR
					newFloorParticle(row, col); // function call to place a new floor pixel
				} else if (newType == 3) { // will execute if the pixel is WATER
					newWaterParticle(row, col); // function call to place a new water pixel
				} else if (newType == 4) { // will execute if the pixel is LAVA
					newLavaParticle(row, col); // function call to place a new lava pixel
				} else if (newType == 5) { // will execute if the pixel is ROCK
					newRockParticle(row, col); // function call to place a new rock pixel
				} else { // will execute if the pixel is EMPTY
					newEmptyParticle(row, col); // function call to place a new empty pixel
				}
				mode = newType; // sets the mode to the current tool and/or element type
				return true; // returns true if a pixel is placed on the canvas
			}else { // will execute if the current pixel is not empty
				return false; // returns false if the current pixel is empty
			}
		} else { // will execute if the pixel is not within the bounds of the canvas
			return false; // returns false if the pixel is outside of the bounds of the canvas
		}
	}
	
	// Update the state of each pixel // 
	public void step() {
		for(int i = 0; i < pixelState.length; i++) {
			for (int j = 0; j < pixelState[i].length; j++) { // accesses each element of pixelState
				pixelWaterBorderState(i, j); // function call to update waterBorderState
				pixelLavaBorderState(i, j); // function call to update lavaBorderState
				pixelSandBorderState(i, j); // function call to update sandBorderState
				updateSandPixels(i, j); // function call to update each sand pixel
				updateWaterPixels(i, j); // function call to update each water pixel
				updateLavaPixels(i, j); // function call to update each lava pixel
			}
		}
	}

	// Draw each pixel in pixelState //
	public void draw() {
		if(isHelp) { // will execute if isHelp is true
			drawHelp(); // function call to draw the help page
		} else { // will execute if isHelp is false
			DUDraw.clear(); // clears the canvas
			DUDraw.setPenColor(); // sets the pen color to black
			displayToolName(); // function call to draw the current tool name
			for (int i = 0; i < pixelState.length; i++) { 
				for (int j = 0; j < pixelState[i].length; j++) { // accesses each element in pixelState
					if(pixelState[i][j] == SAND) { // will execute if the current pixel is sand
						drawSandParticle(i, j); // function call to draw a sand pixel
					}else if (pixelState[i][j] == FLOOR) { // will execute if the current pixel is floor
						drawFloorParticle(i, j); // function call to draw a floor pixel
					}else if (pixelState[i][j] == WATER) { // will execute if the current pixel is water
						drawWaterParticle(i, j); // function call to draw a water pixel
					}else if (pixelState[i][j] == LAVA) { // will execute if the current pixel is lava
						drawLavaParticle(i, j); // function call to draw a lava pixel
					} else if (pixelState[i][j] == ROCK) { // will execute if the current pixel is rock
						drawRockParticle(i, j); // function call to draw a rock pixel
					}
				}
			}
		}
		DUDraw.show(); // copies the drawing from the off screen canvas to the on screen one
	}
	
	// Initializes the particles in the initial canvas view //
	public void initializeParticles() {
		for(int i = 0; i < pixelState.length; i++) {
			for(int j = 0; j < pixelState[i].length; j++) { // accesses each element in pixelState
				if(j < 6) { // will execute if the current pixel is in the bottom 5 rows of the canvas
					pixelState[i][j] = FLOOR; // sets the current pixel to floor
				}else { // will execute if the current pixel is not in the bottom 5 rows of the canvas
					pixelState[i][j] = EMPTY; // sets the current pixel to empty				 	
				}
			}
		}
	}
	
	// Updates each element in pixelState that is sand //
	public void updateSandPixels(int i, int j) {
		if(pixelState[i][j] == SAND && j != 0 && 
			pixelState[i][j - 1] == EMPTY) { // will execute if the current pixel is sand, the pixel below is empty, and the current pixel is above the bottom of the canvas
			pixelState[i][j] = EMPTY; // sets the current pixel to empty
			pixelState[i][j - 1] = SAND; // sets the pixel below the current pixel to sand
		} 
		else if (pixelState[i][j] == SAND && 
				i > 0 && 
				i < myRow - 1 &&  
				j > 0 && 
				(pixelState[i][j - 1] != EMPTY || 
				pixelState[i][j - 1] != FLOOR)) { // will execute if the current pixel is above the bottom of the canvas, in between the left and right sides of the canvas, and the pixel below is not empty or not floor
			if(sandBorderState == sandEmpty) { // will execute if the current sand pixel has an empty pixel to the bottom right and bottom left
				if(Math.random() < 0.17) { // will execute if Math.random() produces a value below 0.17
					pixelState[i][j] = EMPTY; // sets the current pixel to empty
					pixelState[i - 1][j - 1] = SAND; // sets the pixel on the the bottom left of the current pixel to sand
				}else { // will execute if Math.random() produces a value above 0.17 
					pixelState[i][j] = EMPTY; // sets the current pixel to empty
					pixelState[i + 1][j - 1] = SAND; // sets the pixel on the the bottom right of the current pixel to sand
				}
			} 
			else if (sandBorderState == sandLeft) { // will execute if the current pixel has an empty pixel to the bottom left and a not empty pixel to the bottom right
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i - 1][j - 1] = SAND; // sets the pixel on the the bottom left of the current pixel to sand
			} else if (sandBorderState == sandRight) { // will execute if the current pixel has an empty pixel to the bottom right and a not empty pixel to the bottom left
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i + 1][j - 1] = SAND; // sets the pixel on the the bottom right of the current pixel to sand
			}  else if (sandBorderState == sandStop){ // will execute if the current pixel does not have an empty pixel to the bottom right and not an empty pixel to the bottom left
				pixelState[i][j] = SAND; // sets the current pixel to sand
			}
		}
	}
	
	// Updates each element in pixelState that is water //
	public void updateWaterPixels(int i, int j) {
		if(pixelState[i][j] == WATER && 
			j !=0 && 
			pixelState[i][j - 1] == EMPTY) { // will execute if the current pixel is water, the current pixel is above the bottom of the canvas, and the pixel below is empty
			pixelState[i][j] = EMPTY; // sets the current pixel to empty
			pixelState[i][j - 1] = WATER; // sets the pixel below to water
		}else if (pixelState[i][j] == WATER && 
				i > 0 && 
				i < myRow - 1 &&
				j != 0 &&
				pixelState[i][j - 1] != EMPTY) { // will execute if the current pixel is water, the current pixel is between the left and right sides of the canvas, the current pixel is above the bottom of the canvas, and the pixel below if not empty
			if(waterBorderState == sidesEmpty) { // will execute if the current pixel has empty pixels to both the left and right side
				if(Math.random() < 0.19) { // will execute if Math.random() produces a value below 0.19
					pixelState[i][j] = EMPTY; // sets the current pixel to empty
					pixelState[i - 1][j] = WATER; // sets the pixel to the left to water
				}else { // will execute if Math.random() produces a value above 0.19
					pixelState[i][j] = EMPTY; // sets the current pixel to empty
					pixelState[i + 1][j] = WATER; // sets the pixel to the right to water
				}
			} else if(waterBorderState == leftEmpty) { // will execute if the current pixel has an empty pixel to the left, but not to the right
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i - 1][j] = WATER; // sets the pixel to the left to water
			} else if(waterBorderState == rightEmpty) { // will execute if the current pixel has an empty pixel to the right, but not to the left
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i + 1][j] = WATER; // sets the pixel to the right to water
			} else if(pixelState[i][j - 1] == FLOOR){ // will execute if the pixel below is floor
				pixelState[i][j] = WATER; // sets the current pixel to water
			} else if (waterBorderState == waterLavaBelow) { // will execute if the current pixel has a lava pixel below
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i][j - 1] = ROCK; // sets the pixel below to rock
			}
		}
	}
	
	// Updates each element in pixelState that is lava //
	// Updates each element in pixelState that is lava //
	public void updateLavaPixels(int i, int j){
		if(pixelState[i][j] == LAVA &&
				(pixelState[i][j - 1] == SAND || 
				pixelState[i][j - 1] == EMPTY)) { // will execute if the current pixel is lava and the pixel below is sand or empty
			pixelState[i][j] = EMPTY; // sets the current pixel to empty
			pixelState[i][j - 1] = LAVA; // sets the pixel below to lava
		}else if (pixelState[i][j] == LAVA &&
				i > 0 &&
				i < myRow - 1 &&
				(pixelState[i][j - 1] != SAND &&
				pixelState[i][j - 1] != EMPTY)) { // will execute if the current pixel is lava and is between the left and right sides of the canvas, and if the pixel below is not sand and is not empty
			if(lavaBorderState == lavaEmpty) { // will execute if either side of the current pixel is empty or sand
				if(Math.random() < 0.25) { // will execute if Math.random() produces a value below 0.25
					pixelState[i][j] = EMPTY; // sets the current pixel to empty
					pixelState[i - 1][j] = LAVA; // sets the pixel to the left to lava
				}else { // will execute if Math.random() produces a value above 0.25
					pixelState[i][j] = EMPTY; // sets the current pixel to empty
					pixelState[i + 1][j] = LAVA; // sets the pixel to the right to lava
				}
			} else if(lavaBorderState == lavaLeft) { // will execute if the current pixel has an empty or sand pixel to the left and not an empty or sand pixel to the right
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i - 1][j] = LAVA; // sets the pixel to the left to lava
			} else if(lavaBorderState == lavaRight) { // will execute if the current pixel has an empty or sand pixel to the right and not an empty or sand pixel to the left
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i + 1][j] = LAVA; // sets the current pixel to lava
			} else if(lavaBorderState == lavaOccupied){ // will execute if the current pixel does not have either a sand or empty pixel to the left or right
				pixelState[i][j] = LAVA; // sets the current pixel to lava
			} else if(lavaBorderState == lavaWaterLeft) { // will execute if the current pixel has a water pixel to the left
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i - 1][j] = ROCK; // sets the pixel to the left to rock
			} else if(lavaBorderState == lavaWaterRight) { // will execute if the current pixel has a water pixel to the right
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i + 1][j] = ROCK; // sets the pixel to the right to rock
			} else if(lavaBorderState == lavaWaterBelow) { // will execute if the current pixel has a water pixel below
				pixelState[i][j] = EMPTY; // sets the current pixel to empty
				pixelState[i][j - 1] = ROCK; // sets the pixel below to rock
			} else if(pixelState[i][j - 1] == FLOOR ||
					pixelState[i][j - 1] == ROCK) { // will execute if the pixel below is floor or rock
				pixelState[i][j] = LAVA; // sets the current pixel to lava
			}
		}
	}
	
	// Draws the help view //
	// Draws the help view //
	public void drawHelp() {
		DUDraw.clear(); // clears the current canvas
		DUDraw.setPenColor(); // sets the pen color to black
		DUDraw.text(myRow * 0.5, myCol * 0.75, "Help: H (Toggle)"); // text for help button
		DUDraw.text(myRow * 0.5, myCol * 0.7, "Sand: S"); // text for sand button
		DUDraw.text(myRow * 0.5, myCol * 0.65, "Floor: F"); // text for sand button
		DUDraw.text(myRow * 0.5, myCol * 0.6, "Water: W"); // text for water button
		DUDraw.text(myRow * 0.5, myCol * 0.55, "Lava: L"); // text for lava button
		DUDraw.text(myRow * 0.5, myCol * 0.5, "Reset: R"); // text for reset button
		DUDraw.text(myRow * 0.5, myCol * 0.45, "Pen Size (Small): 1"); // text for small penSize button
		DUDraw.text(myRow * 0.5, myCol * 0.4, "Pen Size (Medium): 2"); // text for medium penSize button
		DUDraw.text(myRow * 0.5, myCol * 0.35, "Pen Size (Large): 3"); // text for large penSize button
	}

	// Draws a sand particle // 
	// Draws each element in pixelState that is sand //
	public void drawSandParticle(int row, int col) {
		DUDraw.setPenColor(sand); // set pen color to sand
		DUDraw.filledCircle(row, col, 0.25*penSize); // draw a filled circle at the location of the sand particle
	}
	
	// Draws a water particle // 
	// Draws each element in pixelState that is water //
	public void drawWaterParticle(int row, int col) {
		DUDraw.setPenColor(water); // sets the pen color to water
		DUDraw.filledCircle(row, col, 5*penSize); // draws a filled circle at the location of the water pixel
	}
	
	// Draws a floor particle //
	// Draws each element in pixelState that is floor //
	public void drawFloorParticle(int row, int col) {
		DUDraw.setPenColor(); // sets the pen color to black
		DUDraw.filledRectangle(row, col, 1*penSize, 1*penSize); // draws a filled rectangle at the location of the floor pixel
	}
	
	// Draws a lava particle //
	// Draws each element in pixelState that is lava //
	public void drawLavaParticle(int row, int col) {
		DUDraw.setPenColor(lava); // sets the pen color to lava
		DUDraw.filledCircle(row, col, 4*penSize); // draws a filled circle at the location of the lava pixel
	}
	
	// Draws a rock particle // 
	// Draws each element in pixelState that is rock //
	public void drawRockParticle(int row, int col) {
		DUDraw.setPenColor(rock); // sets the pen color to rock
		DUDraw.filledSquare(row, col, 8*penSize); // draws a filled square at the location of the pixel
	}

	// Sets the state of a new sand particle //
	// Sets the state of a new sand pixel //
	public void newSandParticle(int row, int col) {
		for(int s = -5; s < 5; s++) {
			for(int c = -5; c < 5; c++) { // 10x10 nested for loop for random positions of new sand particles
				if(Math.random() < 0.5 &&
						row >= 5 &&
						row < pixelState.length-5 &&
						col >= 5 &&
						col < pixelState[row].length - 5) { // will execute if Math.random() produces a value below 0.5, and the current pixel is within the bounds of the canvas
					pixelState[row + s][col + c] = SAND; // sets the pixel state of random pixels within a 10x10 box of the current pixel to sand
				}else { // will execute if Math.random() produces a value above 0.5 and the current pixel is not within the bounds of the canvas
					pixelState[row][col] = SAND; // sets the current pixel to sand
				}
			}
		}
	}
	
	// Sets the state of a new rock particle //
	// Sets the state of a new rock pixel //
	public void newRockParticle(int row, int col) {
		for(int s = -5; s < 5; s++) {
			for(int c = -5; c < 5; c++) { // 10x10 nested for loop for random positions of new rock particles
				if(row >= 5 &&
						row < pixelState.length-5 &&
						col >= 5 &&
						col < pixelState[row].length - 5) { // will execute if Math.random() produces a value below 0.5, and the current pixel is within the bounds of the canvas
					pixelState[row + s][col + c] = ROCK; // sets the pixel state of random pixels within a 10x10 box of the current pixel to rock
				}else { // will execute if Math.random() produces a value above 0.5 and the current pixel is not within the bounds of the canvas
					pixelState[row][col] = ROCK; // sets the current pixel to rock
				}
			}
		}
	}
	
	// Sets the state of a new water particle //
	// Sets the state of a new water pixel //
	public void newWaterParticle(int row, int col) {
		pixelState[row][col] = WATER; // sets the current pixel to water
	}

	// Sets the state of a new empty particle //
	// Sets the state of a new empty pixel //
	public void newEmptyParticle(int row, int col) {
		pixelState[row][col] = EMPTY; // sets the current pixel to empty
	}

	// Sets the state of a new floor particle //
	// Sets the state of a new floor pixel //
	public void newFloorParticle(int row, int col) {
		for(int s = -5; s < 5; s++) {
			for(int c = -5; c < 5; c++) { // 10x10 nested for loop for random positions of new floor particles
				if(row >= 5 &&
						row < pixelState.length-5 &&
						col >= 5 &&
						col < pixelState[row].length - 5) { // will execute if Math.random() produces a value below 0.5, and the current pixel is within the bounds of the canvas
					pixelState[row + s][col + c] = FLOOR; // sets the pixel state of random pixels within a 10x10 box of the current pixel to floor
				}else { // will execute if Math.random() produces a value above 0.5 and the current pixel is not within the bounds of the canvas
					pixelState[row][col] = FLOOR; // sets the current pixel to floor
				}
			}
		}
	}

	// Sets the state of a new lava particle //
	// Sets the state of a new lava pixel //
	public void newLavaParticle(int row, int col) {
		pixelState[row][col] = LAVA; // sets the current pixel to lava
	}
	
	// Sets the toolName based on the current mode //
	// Sets the toolName based on the current mode // 
	public void displayToolName() {
		if(mode == EMPTY) { // will execute if the current mode is empty
			toolName = "Empty"; // sets the tool name to empty
		}else if(mode == SAND) { // will execute if the current mode is sand
			toolName = "Sand"; // sets the tool name to sand
		}else if(mode == FLOOR) { // will execute if the current mode is floor
			toolName = "Floor"; // sets the tool name to floor
		}else if(mode == WATER) { // will execute if the current mode is water
			toolName = "Water"; // sets the tool name to water
		}else if(mode == LAVA) { // will execute if the current mode is lava
			toolName = "Lava"; // sets the tool name to lava
		}
		DUDraw.setPenColor(); // sets the pen color to black
		DUDraw.text(myRow * 0.0833, myCol * 0.85, toolName); // draws the tool name
		DUDraw.text(myRow * 0.141, myCol * 0.9, "Press H for help"); // draws the information for help
	}
	
	// Updates the sandBorderState //
	// Updates the sandBorderState //
	public void pixelSandBorderState(int i, int j) {
		if (i > 0 &&
				i < myRow - 1 &&
				pixelState[i][j] == SAND &&
				(pixelState[i][j - 1] != FLOOR ||
				pixelState[i][j - 1] != EMPTY)) { // will execute if the current pixel is sand, is between the left and right sides of the canvas, and the pixel below is not floor or empty
			if(pixelState[i - 1][j - 1] == EMPTY &&
					pixelState[i + 1][j - 1] == EMPTY) { // will execute if both the pixel to the bottom right and left is empty
				sandBorderState = sandEmpty; // sets the sandBorderState to sandEmpty
			} else if(pixelState[i - 1][j - 1] == EMPTY &&
					pixelState[i + 1][j - 1] != EMPTY) { // will execute if the pixel to the bottom left is empty and the bottom right is not empty
				sandBorderState = sandLeft; // sets the sandBorderState to sandLeft
			} else if(pixelState[i - 1][j - 1] != EMPTY &&
					pixelState[i + 1][j - 1] == EMPTY) { // will execute if the pixel to the bottom right is empty and the bottom left is not empty
				sandBorderState = sandRight; // sets the sandBorderState to sandRight
			} else if(pixelState[i][j - 1] == FLOOR) { // will execute if the pixel below is floor
				sandBorderState = sandStop; // sets the sandBorderState to sandStop
			}
		}
	}
	
	// Updates the waterBorderState //
	// Updates the waterBorderState //
	public void pixelWaterBorderState(int i, int j) {
		if(i > 0 &&
				i < myRow - 1 &&
				pixelState[i][j] == WATER) { // will execute if the current pixel is water and is between the left and right sides of the canvas
			if(pixelState[i - 1][j] == EMPTY &&
					pixelState[i + 1][j] == EMPTY) { // will execute if both the pixel to the left and right are empty
				// both sides are empty
				waterBorderState = sidesEmpty; // sets waterBorderState to sidesEmpty
			}else if(pixelState[i - 1][j] == EMPTY &&
					pixelState[i + 1][j] != EMPTY){ // will execute if the pixel to the left is empty but the pixel to the right is not empty
				// left side is empty
				waterBorderState = leftEmpty; // sets waterBorderState to leftEmpty
			}else if(pixelState[i - 1][j] != EMPTY &&
					pixelState[i + 1][j] == EMPTY){ // will execute if the pixel to the right is empty but the pixel to the left is not empty
				// right side is empty
				waterBorderState = rightEmpty; // sets waterBorderState to rightEmpty
			}else if(pixelState[i - 1][j] != EMPTY &&
					pixelState[i + 1][j] != EMPTY){ // will execute if both the pixel to the left and right are not empty
				// neither side is empty
				waterBorderState = sidesOccupied; // sets waterBorderState to sidesOccupied
			} else if(pixelState[i][j - 1] == LAVA) { // will execute if the pixel below is lava
				waterBorderState = waterLavaBelow; // sets waterBorderState to waterLavaBelow
			}
		}
	}
	
	// Updates the lavaBorderState //
	// Updates the lavaBorderState //
	public void pixelLavaBorderState(int i, int j) {
		if(i > 0 &&
				i < myRow - 1 &&
				pixelState[i][j] == LAVA) { // will execute if the current pixel is lava and is between the left and right sides of the canvas
			if((pixelState[i - 1][j] == SAND ||
				pixelState[i - 1][j] == EMPTY) &&
				(pixelState[i + 1][j] == SAND ||
				pixelState[i + 1][j] == EMPTY)){ // will execute if both the pixel to the left and right are sand or empty 
				// lava both sides are empty 
				lavaBorderState = lavaEmpty; // sets the lavaBorderState to lavaEmpty
			}else if((pixelState[i - 1][j] == SAND ||
					pixelState[i - 1][j] == EMPTY) &&
					(pixelState[i + 1][j] != SAND &&
					pixelState[i + 1][j] != WATER &&
					pixelState[i + 1][j] != EMPTY)){ // will execute if the pixel to the left is either empty or sand and the pixel to the right is not sand water or empty
				// lava left side is empty
				lavaBorderState = lavaLeft; // sets the lavaBorderState to lavaLeft
			}else if((pixelState[i - 1][j] != SAND &&
					pixelState[i - 1][j] != WATER &&
					pixelState[i - 1][j] != EMPTY) && 
					(pixelState[i + 1][j] == SAND ||
					pixelState[i + 1][j] == EMPTY)){ // will execute if the pixel to the left is not sand, water, or empty and the pixel to the right is sand or empty
				// lava right side is empty
				lavaBorderState = lavaRight; // sets the lavaBorderState to lavaRight
			}else if((pixelState[i - 1][j] != SAND &&
					pixelState[i - 1][j] != WATER &&
					pixelState[i - 1][j] != EMPTY) && 
					(pixelState[i + 1][j] != SAND &&
					pixelState[i + 1][j] != WATER &&
					pixelState[i + 1][j] != EMPTY)){ // will execute if both the pixel to the left and right are not sand, water, or empty
				// lava neither side is empty
				lavaBorderState = lavaOccupied; // sets the lavaBorderState to lavaPccupied
			}else if(pixelState[i - 1][j] == WATER) { // will execute if the pixel to the left is water
				// water on the left and cannot go right
				lavaBorderState = lavaWaterLeft; // sets the lavaBorderState to lavaWaterLeft
			}else if(pixelState[i + 1][j] == WATER) { // will execute if the pixel to the right is water
				// water on the right and cannot go left
				lavaBorderState = lavaWaterRight; // sets the lavaBorderState to lavaWaterRight
			}else if(pixelState[i][j - 1] == WATER) { // will execute if the pixel below is water
				// water below
				lavaBorderState = lavaWaterBelow; // sets the lavaBorderState to lavaWaterBelow
			}
		}
	}
	
	// Resets the canvas //
	// Clears the canvas and resets the world // 
	public void resetCanvas() {
		initializeParticles(); // function call to set each pixel back to the original state
	}
	
	// Sets the penSize //
	// Changes the penSize based on the user's input //
	public void setPenSize(double size) {
		penSize = size; // sets the penSize based on the user's input
	}
	
	// Toggles isHelp //
	// Changes isHelp to either true or false depending on its current value //
	public void toggleHelp() {
		DUDraw.pause(200); // buffer so that the variable has time to set before the next step() is called
		isHelp = !isHelp; // sets isHelp to the opposite value
	}
}

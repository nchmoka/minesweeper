package mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Mines{
	// A list to save all placed mines
	private List<PositionState> minesOnGrid = new ArrayList<>();
	private PositionState[][] posState;
	private boolean showAll;
	private int height, width, numMines;
	// The class constructor
	public Mines(int height, int width, int numMines) {
		
		posState = new PositionState[height][width];
		this.height = height; this.width = width;
		
		// Check if there are more mines than cells
		// If this is the case, fill the grid with mines and leave one clear cell
		if ((height * width) <= numMines) this.numMines = (height * width) - 1;
		else this.numMines = numMines;
		
		// Initialize the grid
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) { posState[i][j] = new PositionState(); }
		
		// Randomly place mines on the grid
		int x, y;
		Random rand = new Random();
		for (int i = 0; i < this.numMines; i++) {
			// Get random coordinates using the height and width parameters
			x = rand.nextInt(height);
			y = rand.nextInt(width);
			// Try to place a mine on the grid, if unable for some reason	//
			// reduce i and repeat the iteration 							//
			if (!(addMine(x, y)))
				i--;
		}
	}
	
	// A class to represent the state of a single cell on the grid
	private class PositionState{
		// If a mine is present in a cell
		private boolean isMine;
		// If the cell is flagged
		private boolean isFlag;
		// If the cell is open
		private boolean isOpen;
		// Number of mines around the cell
		private int minesAround;
	}
	
	
	// A simple method to check boundaries
	private boolean inBounds(int i, int j) {
		if (i < 0 || i >= height) return false;
		if (j < 0 || j >= width) return false;
		return true;
	}
	
	// A method for adding a mine on the grid
	public boolean addMine(int i, int j) {
		// Check if the coords are out of bounds or if there's a mine already
		if (!(inBounds(i, j))) return false;
		if (posState[i][j].isMine) return false;
		// Add a mine to this cell, then go around the cell and	//
		// increase the minesAround variable around this cell	//
		posState[i][j].isMine = true;
		posState[i][j].minesAround = 0;
		minesOnGrid.add(posState[i][j]);
		for (int x = i - 1; x <= i + 1; x++)
			for (int y = j - 1; y <= j + 1; y++) {
				if (inBounds(x, y) && !(x == i && y == j))
					posState[x][y].minesAround++;
			}
		return true;
	}
	
	// A method to open a cell, then opens all valid adjacent cells
	public boolean open(int i, int j) {
		if (!(inBounds(i, j))) return false;
		// Can't open a flagged cell
		if (posState[i][j].isFlag) return false;
		// Can't open a cell that is already open
		if (posState[i][j].isOpen) return false;
		// If a cell containing a mine is opened, lose the game
		if (posState[i][j].isMine) {
			//posState[i][j].isOpen = true;
			//setShowAll(true);
			return false;
		}

		posState[i][j].isOpen = true;
		// If possible, open all adjacent cells
		if (posState[i][j].minesAround == 0) {
			open(i - 1, j - 1);
			open(i - 1, j);
			open(i - 1, j + 1);
			open(i, j - 1);
			open(i, j + 1);
			open(i + 1, j - 1);
			open(i + 1, j);
			open(i + 1, j + 1);
		}
		return true;
	}
	
	// A method to toggle a flag on a cell
	public void toggleFlag(int x, int y) {
		if (posState[x][y].isFlag)
			posState[x][y].isFlag = false;
		else
			posState[x][y].isFlag = true;
	}
	
	// A method to check if all the clear cells are open
	public boolean isDone() {
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++) {
				if (!posState[i][j].isMine && !posState[i][j].isOpen) return false;
			}
		return true;
	}
	
	// Returns a string representation of a cell
	public String get(int i, int j) {
		PositionState cell = posState[i][j];
		// If showAll is set to false
		if (!showAll) {
			if (cell.isOpen && cell.isMine) return "X";
			if (!cell.isOpen && cell.isFlag) return "F";
			if (cell.isOpen && cell.minesAround > 0) return cell.minesAround + "";
			if (cell.isOpen && cell.minesAround == 0) return " ";
			return ".";
		}
		else {
			if (cell.isMine) return "X";
			if (cell.isFlag) return "F";
			if (cell.minesAround > 0) return cell.minesAround + "";
			if (cell.minesAround == 0) return " ";
		}
		return "";
	}
	
	// Switches the boolean state of showAll
	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}
	
	
	// Prints the grid
	public String toString() {
		StringBuilder mazeScheme = new StringBuilder();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				mazeScheme.append(get(i, j));
			}
			mazeScheme.append('\n');
		}
		return mazeScheme.toString();	
	}
	// Returns true if the cell contains a mine
	public boolean getMineState(int x, int y) {
		return posState[x][y].isMine;
	}
}


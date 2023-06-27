package mines;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
// This class controls the user GUI
public class Controller {
	private Stage stage;
	
	MinesFX minesFx = new MinesFX();

	@FXML
	private GridPane base;
    @FXML
    private StackPane field;

    @FXML
    private TextArea heightInput;

    @FXML
    private Label heightLabel;

    @FXML
    private TextArea minesInput;

    @FXML
    private Label minesLabel;

    @FXML
    private Button resetButton;

    @FXML
    private TextArea widthInput;

    @FXML
    private Label widthLabel;

    @FXML
    void ResetGrid(ActionEvent event) {
    	// Clear the previous grid and set a new one
    	field.getChildren().clear();
    	setGrid();
    	base.autosize();
    	stage.sizeToScene();
    }
    
    StackPane getField() {
    	return field;
    }
    // Get width
    int getWidth() {
    	int width = Integer.parseInt(widthInput.getText());
    	return width;
    }
    // Get height
    int getHeight() {
    	int height = Integer.parseInt(heightInput.getText());
    	return height;
    }
    // Get the number of mines
    int getMines() {
    	int mines = Integer.parseInt(minesInput.getText());
    	return mines;
    }
    
    // Returns an image view of a given image and it's location
    ImageView setImage(String imageName, int fitHeight) {
		Image image = new Image("file:src/assets/" + imageName);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(fitHeight);
        imageView.setPreserveRatio(true);
        return imageView;
    }
    
    
    void setGrid() {
    	// Create the mine field
    	Mines mineField = new Mines(getHeight(), getWidth(), getMines());
    	GridPane mineGrid = new GridPane();
    	mineGrid.setPadding(new Insets(10));
    	for (int i = 0; i < getHeight(); i++)
			for (int j = 0; j < getWidth(); j++) {
				Button cell = new GridCell(i, j);
				cell.setOpacity(0.8);
				// For each cell on the grid, add an OnMouseClick event	//
				// to handle the game logic.							//
				cell.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
				    	GridCell cell = (GridCell)event.getSource();
				    	int x = cell.getX();
				    	int y = cell.getY();
				    	
				    	// Get the status of each cell on the grid
				    	String cellStatus = mineField.get(x, y);
				    	
				    	// Left mouse click - opens cell	============================================//
				    	if (event.getButton() == MouseButton.PRIMARY) {	
				    		//Open the cell and get it's status
				    		mineField.open(x, y);
				    		cellStatus = mineField.get(x, y);
				    		
			    			// Incase the user stepped on a mine, disable the whole grid	
			    			if (mineField.getMineState(x, y)) {	
			    				mineField.setShowAll(true);
		        				ImageView lose = setImage("gameover.png", 200);
		        				field.getChildren().addAll(lose);
		        				mineGrid.setDisable(true);		
			    			}
			    			
				    		// Each left click, update all cells on screen
				        	for (int i = 0; i < getHeight(); i++)
				        		for (int j = 0; j < getWidth(); j++) {
				        			Button button = (Button)mineGrid.getChildren().get(i * getWidth() + j);
				        			// Control the look and behaviour of opened cells
				        			if (mineField.get(i, j) != "." && mineField.get(i, j) != "F") {
				        				if (mineField.get(i, j) == "X") {
				        					// If the cell contains a bomd, place a bomb picture
							                button.setGraphic(setImage("bomb.png", 19));
						            		button.setText(null);	
				        				}
				        				else {
				        					// Get the string of each cell
				        					button.setText(mineField.get(i, j));
				        				}
				        				button.setOpacity(0.65);
				        				button.setDisable(true);
				        			}
				        		}
				        	
				    	// Right mouse click - sets a flag	============================================//
			            } else if (event.getButton() == MouseButton.SECONDARY) {
			            	if (cellStatus == "." || cellStatus == "F") {
			            		mineField.toggleFlag(x, y);
				            	if (cellStatus == "F") {
				            		cell.setGraphic(null);
				            		cell.setText(".");
				            	}
				            	else {	
				            		// Set a picture of a flag on the cell
					                cell.setGraphic(setImage("flag.png", 18));
				            		cell.setText(null);
				            	}
			            	}
			            }
				    	//==============================================================================//
				    	// Check if the user won
				    	if (cellStatus != "X" && mineField.isDone()) {
	        				ImageView win = setImage("youwin.png", 100);
	        				field.getChildren().addAll(win);
	        				mineGrid.setDisable(true);
				    	}
				    	cellStatus = mineField.get(x, y);
				    }
				});
				// Add the cells to the grid
				mineGrid.add(cell, j, i);
				
			}
    	
		mineGrid.setAlignment(Pos.CENTER);
		// Add grid to the Stack Pane
		field.getChildren().addAll(mineGrid);
		field.autosize();
    }
    
    // A private class of a single cell on the visual grid
    private class GridCell extends Button{
    	private int x, y;
    	Font font = Font.font("Verdana", FontWeight.BOLD, null, 20);
		public GridCell(int x, int y) {
			this.setMinWidth(45);
			this.setMinHeight(45);
			this.setText(".");
			this.setFont(font);
			this.setStyle("-fx-background-radius: 0;"
					+ "-fx-text-fill: #1c2841;");
			this.x = x;
			this.y = y;
			
		}
		// Return x position
		public int getX() {
			return x;
		}
		// Return y position
		public int getY() {
			return y;
		}
    }
    // Handle the stage
    public void setStage(Stage stage, GridPane root) {
    	// Set a scene background
		BackgroundImage myBI= new BackgroundImage(new Image("file:src/assets/bg.gif", root.getWidth(), root.getHeight(), true, true),
		        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		root.setBackground(new Background(myBI));
    	this.stage = stage;
    	this.base = root;
    }
}

package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GraphicTile {
	
	private final int TILE_SIZE = 20;
	int x; //Location values
	int y;
	
	String roverName = "";
	
	Color colorTerrain = Color.RED;
	Color colorScience = Color.BLUE;
	Color colorString = Color.BLACK;
	boolean hasTerrain = false;
	boolean hasScience = false;
	boolean hasString = false;
	
	
	public GraphicTile(int x, int y){ 
		
		this.x = x;
		this.y = y;
	}
	
	public GraphicTile(int x, int y, Color terColor, Color sciColor){ 
		this.x = x;
		this.y = y;
		this.colorTerrain = terColor;
		this.colorScience = sciColor;
	}
	
	public void drawTile(Graphics g){	
		if(hasTerrain){
			g.setColor(colorTerrain);
			g.fillRect(x *TILE_SIZE, y *TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}
		if(hasScience){
			g.setColor(colorScience);
			g.fillOval(x*TILE_SIZE +11, y*TILE_SIZE +11 , 7, 7);
		}			
		if(hasString){
			g.setColor(colorString);
			Font font = new Font("Arial", Font.BOLD, 12);
			g.setFont(font);
			g.drawString(roverName , 4 *TILE_SIZE +2, 9 *TILE_SIZE -10);
		}			
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
//	public Color getColor() {
//		return color;
//	}
//	public void setColor(Color color) {
//		this.color = color;
//	}

	public String getRoverName() {
		return roverName;
	}

	public void setRoverName(String roverName) {
		this.roverName = roverName;
	}

	public Color getColorTerrain() {
		return colorTerrain;
	}

	public Color getColorScience() {
		return colorScience;
	}

	public void setColorTerrain(Color colorTerrain) {
		this.colorTerrain = colorTerrain;
	}

	public void setColorScience(Color colorScience) {
		this.colorScience = colorScience;
	}

	public boolean isHasTerrain() {
		return hasTerrain;
	}

	public boolean isHasScience() {
		return hasScience;
	}

	public void setHasTerrain(boolean hasTerrain) {
		this.hasTerrain = hasTerrain;
	}

	public void setHasScience(boolean hasScience) {
		this.hasScience = hasScience;
	}
	
}

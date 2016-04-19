package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GraphicTile {
	
	private final int TILE_SIZE = 20;
	int x; //Location values
	int y;
	
	Color colorTerrain = Color.RED;
	Color colorScience = Color.BLUE;
	Color colorString = Color.BLACK;
	boolean hasTerrain = true;
	boolean hasScience = true;
	boolean hasString = true;
	
	Graphics graphic;
	
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
			g.  fillRect(x *TILE_SIZE, y *TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}
		if(hasScience){
			g.setColor(colorScience);
			g.fillOval(x*TILE_SIZE +11, y*TILE_SIZE +11 , 7, 7);
		}			
		if(hasString){
			g.setColor(colorString);
			Font font = new Font("Arial", Font.BOLD, 12);
			g.setFont(font);
			g.drawString("15" , 4 *TILE_SIZE +2, 9 *TILE_SIZE -10);
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
	public Graphics getGraphic() {
		return graphic;
	}
	public void setGraphic(Graphics graphic) {
		this.graphic = graphic;
	}
	
}

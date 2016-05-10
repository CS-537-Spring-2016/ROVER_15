package common;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import enums.Science;
import enums.Terrain;

public class GraphicTile {
	
	private final int TILE_SIZE = 20;
	int x; //Location values
	int y;
	
	int teamNum = 0;
	
	String roverName = "";
	
	Terrain terrain = Terrain.SOIL;
	Science science = Science.NONE;
	
	Color colorTerrain = Color.WHITE;
	Color colorScience = Color.WHITE;
	Color colorString = Color.BLACK;
	
	boolean hasTerrain = false;
	boolean hasScience = false;
	boolean hasRover = false;
	
	
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
			switch (this.terrain) {
	            case NONE:  colorTerrain = Color.BLACK;
	                break;
	            case SOIL:  colorTerrain = Color.WHITE;
                	break;
	            case ROCK:  colorTerrain = Color.BLUE;
                	break;         
	            case SAND:  colorTerrain = Color.ORANGE;
                	break;
	            case GRAVEL:  colorTerrain = Color.LIGHT_GRAY;
                	break;
	            default: colorTerrain = Color.BLACK;
	            	break;
			}
			
			g.setColor(colorTerrain);
			g.fillRect(x *TILE_SIZE, y *TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}
		
		if(hasScience){
			switch (this.science) {
            case NONE:  colorScience = Color.WHITE;
						//g.setColor(colorScience);
						//g.fillOval(x*TILE_SIZE +11, y*TILE_SIZE +11 , 7, 7);
				break;
            case ORGANIC:  colorScience = Color.GREEN;
            			//draw a filled circle
						g.setColor(colorScience);
						g.fillOval(x*TILE_SIZE +11, y*TILE_SIZE +11 , 7, 7);
						//draw a black circle outline
						g.setColor(Color.BLACK);
						g.drawOval(x*TILE_SIZE +11, y*TILE_SIZE +11 , 7, 7);
            	break;
            case MINERAL:  colorScience = Color.CYAN;
            			//draw a filled triangle
            			g.setColor(colorScience);		
						g.fillPolygon(new int[]{x*TILE_SIZE +14, x*TILE_SIZE +18, x*TILE_SIZE +10},
										new int[]{y*TILE_SIZE +11, y*TILE_SIZE +18, y*TILE_SIZE +18}, 3);
						//draw a black triangle outline
            			g.setColor(Color.BLACK);		
						g.drawPolygon(new int[]{x*TILE_SIZE +14, x*TILE_SIZE +18, x*TILE_SIZE +10},
										new int[]{y*TILE_SIZE +11, y*TILE_SIZE +18, y*TILE_SIZE +18}, 3);
            	break;
            case RADIOACTIVE:  colorScience = Color.RED;
            			//draw a filled upside down triangle			
            			g.setColor(colorScience);
						g.fillPolygon(new int[]{x*TILE_SIZE +14, x*TILE_SIZE +18, x*TILE_SIZE +10},
								new int[]{y*TILE_SIZE +18, y*TILE_SIZE +11, y*TILE_SIZE +11}, 3);
						//draw a black upside down triangle	outline		
            			g.setColor(Color.BLACK);
						g.drawPolygon(new int[]{x*TILE_SIZE +14, x*TILE_SIZE +18, x*TILE_SIZE +10},
								new int[]{y*TILE_SIZE +18, y*TILE_SIZE +11, y*TILE_SIZE +11}, 3);
            	break;
            case CRYSTAL:  colorScience = Color.YELLOW;
            
            g.setColor(colorScience);
					//draw a filled diamond
					g.setColor(colorScience);
					g.fillPolygon(new int[]{x*TILE_SIZE +14, x*TILE_SIZE +18, x*TILE_SIZE +14, x*TILE_SIZE +10 },
							new int[]{y*TILE_SIZE +10, y*TILE_SIZE +14, y*TILE_SIZE +18, y*TILE_SIZE +14}, 4);
					//draw a black diamond outline
					g.setColor(Color.BLACK);
					g.drawPolygon(new int[]{x*TILE_SIZE +14, x*TILE_SIZE +18, x*TILE_SIZE +14, x*TILE_SIZE +10 },
							new int[]{y*TILE_SIZE +10, y*TILE_SIZE +14, y*TILE_SIZE +18, y*TILE_SIZE +14}, 4);
            	break;
				
				default:
					break;
			}
		}			
		if(hasRover){
			Color fillColor = Color.WHITE;
			Color outlineColor = Color.WHITE;
			Color textColor = Color.WHITE;
			Font font = new Font("Arial", Font.BOLD, 12);
			g.setFont(font);
			g.setColor(fillColor);
			
			setTeamNumber();
			if(teamNum == 0){
				fillColor = Color.MAGENTA;
				outlineColor = Color.BLACK;
				textColor = Color.YELLOW;
			} else if(teamNum == 1){
				fillColor = new Color(50, 50, 255); //slightly lighter blue
				outlineColor = Color.BLACK;
				textColor = Color.WHITE;
				
			} else if(teamNum == 2){
				fillColor = Color.GREEN;
				outlineColor = Color.BLACK;
				textColor = Color.BLACK;
			}
			
			g.setColor(fillColor);
			g.fillOval(x *TILE_SIZE +2, y *TILE_SIZE +1, 14, 14);
			
			g.setColor(textColor);
			g.drawString(roverName , x *TILE_SIZE +3, y *TILE_SIZE +13);
			
			g.setColor(outlineColor);
			g.drawOval(x *TILE_SIZE +2, y *TILE_SIZE +1, 14, 14);
		}			
	}
	


	public void setRoverName(String roverName) {
		this.roverName = roverName;
		this.hasRover = true;
	}

	public void setTerrain(Terrain ter){
		this.terrain = ter;
		this.hasTerrain = true;
	}
	
	public void setScience(Science sci){
		this.science = sci;
		this.hasScience = true;
	}

	private void setTeamNumber(){
		int tnum = 0;
		
		if(roverName.equals("")){
			this.teamNum = 0;
		} else if(roverName.equals("01") || roverName.equals("02") || roverName.equals("03") 
				|| roverName.equals("04") || roverName.equals("05") || roverName.equals("06") 
				|| roverName.equals("07") || roverName.equals("08") || roverName.equals("09")){
			this.teamNum = 1;
		} else if(roverName.equals("10") || roverName.equals("11") || roverName.equals("12") 
				|| roverName.equals("13") || roverName.equals("14") || roverName.equals("15") 
				|| roverName.equals("16") || roverName.equals("17") || roverName.equals("18")){
			this.teamNum = 2;
		}
		
	}
}

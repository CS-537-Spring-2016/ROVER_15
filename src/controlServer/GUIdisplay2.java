package controlServer;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

import common.GraphicTile;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;

// Thanks to this posting for the seed this was constructed from:
// http://stackoverflow.com/questions/30204521/thread-output-to-gui-text-field

public class GUIdisplay2 extends JPanel implements MyGUIAppendable2 {
	private final int TILE_SIZE = 20;
	
	private JTextArea area;
	private int width ;
	private int height;
	private int pixelWidth;
	private int pixelHeight;
	private List<Point> fillCells;
	private List<GraphicTile> graphicTiles;

	public GUIdisplay2() {
		//area = new JTextArea(55, 110);  //height x width
		//JScrollPane scrollPane = new JScrollPane(area);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		//DefaultCaret caret = (DefaultCaret)area.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		//add(scrollPane);
	}

	public GUIdisplay2(int width, int height) {	
		
		this.width = width ;
		this.height = height ;
		this.pixelWidth = (this.width*TILE_SIZE);
		this.pixelHeight = (this.height*TILE_SIZE);
		fillCells = new ArrayList<>();
		graphicTiles = new ArrayList<>();
		
		//area = new JTextArea(height +5, (width * 2)+2);
		//this.width = width ;
		//this.height = height;
		//JScrollPane scrollPane = new JScrollPane(area);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//add(scrollPane);
	}

	@Override
	public void append(String text) {
		area.append(text);
	}

	@Override
	public void clearDisplay() {
		area.setText("");
	}
	
	@Override
	public void setText(String text) {
		area.setText(text);
	}
	
	public void fillCell(int x, int y) {
        fillCells.add(new Point(x, y));
        repaint();
    }
	
	// For testing purpose only 
	private void fillRover(){
		fillCell(0,0);
		fillCell(1,1);
		fillCell(1,2);
	}
	
	private void fillGraphicTile(){
		graphicTiles.add(new GraphicTile(5, 5, Color.ORANGE, Color.GREEN));
		graphicTiles.add(new GraphicTile(7, 3));
		graphicTiles.add(new GraphicTile(4, 8));
	}
	
	
	@Override
    protected void paintComponent(Graphics g) {
		// get the idea from: 
		// http://stackoverflow.com/questions/15870608/creating-a-draw-rectangle-filled-with-black-color-function-in-java-for-a-grid
		
		// Draw all the grid by 10 pixel 
        super.paintComponent(g);
        
        
//        fillRover(); // only for testing purpose 
//        
//        // will modify in the future. 
//        for (Point fillCell : fillCells) {
//            int cellX = (fillCell.x * 10);
//            int cellY = (fillCell.y * 10);
//            g.setColor(Color.RED);
//            //g.fillRect(cellX, cellY, 10, 10);
//            g.fillOval(cellX, cellY, 10, 10);
//        }
        
        fillGraphicTile();
        
        for(GraphicTile graphicTile : graphicTiles){
        	graphicTile.drawTile(g);
        }
        
        g.setColor(Color.BLACK);
        // This draws the rectangle start at point (10,10) 
        g.drawRect(0, 0, pixelWidth, pixelHeight);
        for (int i = 0; i <= pixelWidth; i += TILE_SIZE) {
            g.drawLine(i, 0, i, pixelHeight);
        }

        for (int i = 0; i <= pixelHeight; i += TILE_SIZE) {
            g.drawLine(0, i, pixelWidth, i);
        }
        
        /*for (Point fillCell : fillCells) {
            int cellX = 10 + (fillCell.x * 10);
            int cellY = 10 + (fillCell.y * 10);
            g.setColor(Color.RED);
            g.fillRect(cellX, cellY, 10, 10);
        }
        g.setColor(Color.BLACK);
        g.drawRect(10, 10, 800, 500);
        for (int i = 10; i <= 800; i += 10) {
            g.drawLine(i, 10, i, 510);
        }
        for (int i = 10; i <= 500; i += 10) {
            g.drawLine(10, i, 810, i);
        }*/
    }
	
	@Override
    public Dimension getPreferredSize() {
        return new Dimension(this.pixelWidth+50, this.pixelHeight+50);
    }

	static void createAndShowGui(MyGUIWorker2 myWorker, GUIdisplay2 mainPanel) {

		// add a Prop Change listener here to listen for
		// DONE state then call get() on myWorker
		myWorker.execute();

		JFrame frame = new JFrame("GUIdisplay");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(mainPanel);
		frame.setSize(1000, 1000);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
	}
}

// #############################################################################################

class MyGUIWorker2 extends SwingWorker<Void, String> {
	private MyGUIAppendable2 myAppendable;
	private String msg;
	private RoverLocations roverLoc;
	private ScienceLocations sciloc;
	
	public MyGUIWorker2(MyGUIAppendable2 myAppendable) {
		this.myAppendable = myAppendable;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// not sure what to do with this one
		return null;
	}

	public void printOut(String msg) {
		this.msg = msg;
		try {
			doInBackground();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearDisplay() {
		myAppendable.clearDisplay();
	}
	
//	public void displayRovers(RoverLocations roverLoc){
//		myAppendable.clearDisplay();
//		String[] roverPrint = new String[(50 * 50)];
//		for(int j=0; j<50; j++){
//			for(int i=0; i<50; i++){
//				Coord tcor = new Coord(i, j);
//				if(roverLoc.containsCoord(tcor)){
//
//					String rNum = roverLoc.getName(tcor).toString();
//
//					myAppendable.append(rNum.substring(6));
//				} else {
//					myAppendable.append("  ");
//				}
//			}
//			myAppendable.append("\n");
//		}
//	}
	
	public void displayGraphicMap(RoverLocations roverLoc, ScienceLocations sciloc, PlanetMap planetMap){
		int mWidth = planetMap.getWidth();
		int mHeight = planetMap.getHeight();
		/*Graphics g = new Graphics ();
		
		//StringBuilder roverPrint = new StringBuilder();
		for(int j=0; j<mHeight; j++){
			for(int i=0; i<mWidth; i++){
				//scan through the map - left to right, top to bottom
				Coord tcor = new Coord(i, j);
				// first check for a rover and add to map if found
				if(roverLoc.containsCoord(tcor)){
					String rNum = roverLoc.getName(tcor).toString();
					roverPrint.append("|" + rNum.substring(6));
				
				// then check if there is a terrain feature (if not SOIL then display terrain)
				} else if(planetMap.getTile(tcor).getTerrain() != Terrain.SOIL){
					roverPrint.append("|");
					roverPrint.append(planetMap.getTile(tcor).getTerrain().getTerString());
					if(sciloc.checkLocation(tcor)) {
						roverPrint.append(sciloc.scanLocation(tcor).getSciString());
					} else {
						roverPrint.append("_");
					}
				
				} else if(planetMap.getTile(tcor).getTerrain() == Terrain.SOIL){
					roverPrint.append("|_");
					if(sciloc.checkLocation(tcor)) {
						roverPrint.append(sciloc.scanLocation(tcor).getSciString());
					} else {
						roverPrint.append("_");
					}
					
				} else {
					
					roverPrint.append("|__");
				}
			}
			roverPrint.append("|\n");
		}
		myAppendable.clearDisplay();
		myAppendable.setText(roverPrint.toString());*/
	}
	
	public void displayFullMap(RoverLocations roverLoc, ScienceLocations sciloc, PlanetMap planetMap){
		displayGraphicMap(roverLoc,sciloc,planetMap);
		/*int mWidth = planetMap.getWidth();
		int mHeight = planetMap.getHeight();
		
		//StringBuilder roverPrint = new StringBuilder();
		for(int j=0; j<mHeight; j++){
			for(int i=0; i<mWidth; i++){
				//scan through the map - left to right, top to bottom
				Coord tcor = new Coord(i, j);
				// first check for a rover and add to map if found
				if(roverLoc.containsCoord(tcor)){
					String rNum = roverLoc.getName(tcor).toString();
					roverPrint.append("|" + rNum.substring(6));
				
				// then check if there is a terrain feature (if not SOIL then display terrain)
				} else if(planetMap.getTile(tcor).getTerrain() != Terrain.SOIL){
					roverPrint.append("|");
					roverPrint.append(planetMap.getTile(tcor).getTerrain().getTerString());
					if(sciloc.checkLocation(tcor)) {
						roverPrint.append(sciloc.scanLocation(tcor).getSciString());
					} else {
						roverPrint.append("_");
					}
				
				} else if(planetMap.getTile(tcor).getTerrain() == Terrain.SOIL){
					roverPrint.append("|_");
					if(sciloc.checkLocation(tcor)) {
						roverPrint.append(sciloc.scanLocation(tcor).getSciString());
					} else {
						roverPrint.append("_");
					}
					
				} else {
					
					roverPrint.append("|__");
				}
			}
			roverPrint.append("|\n");
		}
		myAppendable.clearDisplay();
		myAppendable.setText(roverPrint.toString());*/
	}
	
//	public void displayActivity(RoverLocations roverLoc, ScienceLocations sciloc){
//		this.roverLoc = roverLoc;
//		this.sciloc = sciloc;
//		
//		try {
//			doInBackground();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		myAppendable.clearDisplay();
////		//String[] locPrint = new String[(61 * 50)];
////		ArrayList<String> locPrint = new ArrayList<String>();
////		for(int j=0; j<50; j++){
////			for(int i=0; i<50; i++){
////				Coord tcor = new Coord(i, j);
////				if(roverLoc.containsCoord(tcor)){
////					String rNum = roverLoc.getName(tcor).toString();
////					//myAppendable.append(rNum.substring(6));
////					locPrint.add("|" + rNum.substring(6));
////				} else if(sciloc.scanLocation(tcor).getSciString().equals(Science.NONE.toString())) {
////					//myAppendable.append((sciloc.scanLocation(tcor).getSciString()) + " ");
////					locPrint.add("|" + (sciloc.scanLocation(tcor).getSciString()) + " ");
////				} else {
////					//myAppendable.append("__");
////					locPrint.add("|__");
////				}
////			}
////			//myAppendable.append("|\n");
////			locPrint.add("|\n");
////		}
////		String[] locPrintArray = locPrint.toArray(new String[0]);
////		publish(locPrintArray);
//	}

	@Override
	protected void process(List<String> chunks) {
		System.out.println("GUI: MyGUIWorker process has been called");

		for (String text : chunks) {
			myAppendable.append(text);
		}
	}
}

interface MyGUIAppendable2 {
	public void append(String text);
	public void setText(String text);
	public void clearDisplay();
}
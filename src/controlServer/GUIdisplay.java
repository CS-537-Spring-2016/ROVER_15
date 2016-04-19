package controlServer;


import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

import common.Coord;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import enums.Science;
import enums.Terrain;

// Thanks to this posting for the seed this was constructed from:
// http://stackoverflow.com/questions/30204521/thread-output-to-gui-text-field

public class GUIdisplay extends JPanel implements MyGUIAppendable {
	private JTextArea area;

	public GUIdisplay() {
		area = new JTextArea(55, 110);  //height x width
		JScrollPane scrollPane = new JScrollPane(area);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		//DefaultCaret caret = (DefaultCaret)area.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		add(scrollPane);
	}

	public GUIdisplay(int width, int height) {	
		area = new JTextArea(height +5, (width * 2)+2);
		JScrollPane scrollPane = new JScrollPane(area);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(scrollPane);
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

	static void createAndShowGui(MyGUIWorker myWorker, GUIdisplay mainPanel) {

		// add a Prop Change listener here to listen for
		// DONE state then call get() on myWorker
		myWorker.execute();

		JFrame frame = new JFrame("GUIdisplay");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setContentPane(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}
}

// #############################################################################################

class MyGUIWorker extends SwingWorker<Void, String> {
	private MyGUIAppendable myAppendable;
	private String msg;
	private RoverLocations roverLoc;
	private ScienceLocations sciloc;
	
	public MyGUIWorker(MyGUIAppendable myAppendable) {
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
	
	public void displayFullMap(RoverLocations roverLoc, ScienceLocations sciloc, PlanetMap planetMap){
		int mWidth = planetMap.getWidth();
		int mHeight = planetMap.getHeight();
		
		StringBuilder roverPrint = new StringBuilder();
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
		myAppendable.setText(roverPrint.toString());
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

interface MyGUIAppendable {
	public void append(String text);
	public void setText(String text);
	public void clearDisplay();
}
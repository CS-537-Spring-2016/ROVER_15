package supportTools;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import enums.RoverName;
import enums.Science;
import enums.Terrain;

/**
 * 
 * @author rkjc
 * This class can be run independently or statically to convert a set of files generated from a PaintMap to a SwarmServer map
 * Reads 3 files:
 * 
 * 1.) information file with map size, start/target locations, rover assignment list, list of associated file names
 * 2.) coordinates with terrain colors xyz from gdal tif -> xyz
 * 3.) coordinates with science colors xyz from gdal tif -> xyz
 * 		science location map will contain terrain colors that will be ignored 
 * 		includes rover marker color. Rover color will be parsed as a range, last 2 digits of color value is rover number
 * 
 *
 */
public class PaintMapUtility {
	
	private RoverLocations roverLocations;
	private ScienceLocations scienceLocations;
	private String mapName;
	private String terrainMapFileName;
	private String scienceMapFileName;
	private String inputLine;
	private int mapWidth;
	private int mapHeight;
	private PlanetMap planetMap;


	public void parseInputFromMaintMapFiles(String fileName) throws IOException {
		this.roverLocations = new RoverLocations();
		this.scienceLocations = new ScienceLocations();

		String terrainMapFileName;
		String scienceMapFileName;
		String inputLine;
		
		FileReader inputPaintMapInfo = new FileReader(fileName);
		BufferedReader bufRead_mapInfo = new BufferedReader(inputPaintMapInfo);
		

		
		
		
		String myLine = null;

		// line 1 - map name
		this.mapName = bufRead_mapInfo.readLine();
		System.out.println("MapInit: " + this.mapName);

		// line 2 - map width and height
		Coord mapSize = extractCoord(bufRead_mapInfo.readLine());
		this.mapWidth = mapSize.xpos;
		this.mapHeight = mapSize.ypos;
		
		// line 3 - start position (x, y) coordinate
		Coord startPos = extractCoord(bufRead_mapInfo.readLine());

		// line 4 - target position x coordinate
		Coord targetPos = extractCoord(bufRead_mapInfo.readLine());
		
		// line 5 - read in terrain map file name
		terrainMapFileName = bufRead_mapInfo.readLine();
			
		// line 6 - read in science map file name
		scienceMapFileName = bufRead_mapInfo.readLine();
		
		FileReader inputPaintMapTerrain = new FileReader(terrainMapFileName);
		BufferedReader bufRead_mapTerrain = new BufferedReader(inputPaintMapTerrain);
		
		FileReader inputPaintMapScience = new FileReader(scienceMapFileName);
		BufferedReader bufRead_mapScience = new BufferedReader(inputPaintMapScience);
		
		this.planetMap = new PlanetMap(this.mapWidth, this.mapHeight, startPos, targetPos);
		
		double yCount = 0.0;
		
		bufRead_mapTerrain.readLine();
		bufRead_mapTerrain.readLine();
		bufRead_mapTerrain.readLine();
		
		bufRead_mapScience.readLine();
		bufRead_mapScience.readLine();
		bufRead_mapScience.readLine();
		
		String sciInput = "";
		int yPos = 0;
		while ((inputLine = bufRead_mapTerrain.readLine()) != null) { //might consider breaking based on map height check
			sciInput = bufRead_mapScience.readLine();
			
			int spaceIndex = 0;
			int nextSpaceIndex = inputLine.indexOf(" ", spaceIndex +1);
			int sciSpaceIndex = 0;
			int sciNextSpaceIndex = sciInput.indexOf(" ", sciSpaceIndex +1);
			
			for (int i = 0; i < mapWidth; i++) {
				// row 0
				// pull pixel rgb values as substring
				// grab three integer values at a time from the input line
				// each value can be 1 to 3 digits long
				// if next whitespace location exists read substring up to white space.
				// convert to red integer value
				// 
				
				
				String tempStr;
				System.out.println("location " + i + " " + yPos + "  \n" );
				
				
				tempStr = inputLine.substring(spaceIndex, nextSpaceIndex);				
				int R = Integer.parseInt(tempStr);
				System.out.print("_R_" + tempStr);
					
				spaceIndex = nextSpaceIndex +1;
				nextSpaceIndex = inputLine.indexOf(" ", spaceIndex +1);
				
				tempStr = inputLine.substring(spaceIndex, nextSpaceIndex);
				int G = Integer.parseInt(tempStr);
				System.out.print("_G_" + tempStr);
				
				spaceIndex = nextSpaceIndex +1;
				nextSpaceIndex = inputLine.indexOf(" ", spaceIndex +1);
				
				tempStr = inputLine.substring(spaceIndex, nextSpaceIndex);
				int B = Integer.parseInt(tempStr);
				System.out.println("_B_" + tempStr);
				
				spaceIndex = nextSpaceIndex +1;
				nextSpaceIndex = inputLine.indexOf(" ", spaceIndex +1);
				
				//System.out.println(new Color(R,G,B).toString());
				//System.out.println("color to letter= " + getLetterFromColor(new Color(R,G,B)));
				
				
				String colorToLetter = getLetterFromColor(new Color(R,G,B));
				
				if(colorToLetter.equals("N") || colorToLetter.equals("R") || colorToLetter.equals("S") || colorToLetter.equals("G")){
					System.out.println("add to planet " + colorToLetter);
					planetMap.setTile(new MapTile(getLetterFromColor(new Color(R,G,B))), i, yPos);
				}


						
				String sciTempStr;
				// next location
				sciTempStr = sciInput.substring(sciSpaceIndex, sciNextSpaceIndex);				
				int sR = Integer.parseInt(sciTempStr);
				System.out.print("_sR_" + sciTempStr);
					
				sciSpaceIndex = sciNextSpaceIndex +1;
				sciNextSpaceIndex = sciInput.indexOf(" ", sciSpaceIndex +1);
				
				sciTempStr = sciInput.substring(sciSpaceIndex, sciNextSpaceIndex);
				int sG = Integer.parseInt(sciTempStr);
				System.out.print("_sG_" + sciTempStr);
				
				sciSpaceIndex = sciNextSpaceIndex +1;
				sciNextSpaceIndex = sciInput.indexOf(" ", sciSpaceIndex +1);
				
				sciTempStr = sciInput.substring(sciSpaceIndex, sciNextSpaceIndex);
				int sB = Integer.parseInt(sciTempStr);
				System.out.println("_sB_" + sciTempStr);
				
				sciSpaceIndex = sciNextSpaceIndex +1;
				sciNextSpaceIndex = sciInput.indexOf(" ", sciSpaceIndex +1);
				
				colorToLetter = getLetterFromColor(new Color(sR,sG,sB));
				
				if(colorToLetter.equals("Y") || colorToLetter.equals("M") || colorToLetter.equals("C") || colorToLetter.equals("O")){
					System.out.print("add to science " + colorToLetter);
					scienceLocations.putScience(new Coord(i, yPos), Science.getEnum(colorToLetter));
				}
				
				
				if(sR == 100 && sG == 100 && sB>=100 && sB<200 ){
					System.out.println("Integer.toString(sB) " + (Integer.toString(sB)));
					System.out.println("Integer.toString(sB) " + (Integer.toString(sB)).substring(1));
					String rName = "ROVER_" + Integer.toString(sB).substring(1);
					System.out.println("found rover= " + rName);
					roverLocations.putRover(RoverName.getEnum(rName), new Coord(i, yPos));
				}
				
				System.out.println("---------------------------");
			}
			yPos++;
		}
	}
	
	
	private String getLetterFromColor(Color color){
		String returnLetter = "";
		
		String colorString = color.toString();

		switch (colorString) {
		case "java.awt.Color[r=0,g=0,b=0]": returnLetter = "X"; //Black=  Void or None or chasm-abyss-fissure-crevasse
			break;	
		case "java.awt.Color[r=63,g=72,b=204]": // Blue=  Rock 
			returnLetter = "R";
			break;
		case "java.awt.Color[r=255,g=255,b=255]": // White=  Soil
			returnLetter = "N";
			break;
		case "java.awt.Color[r=255,g=242,b=0]":  // Yellow=  Sand 
			returnLetter = "S";
			break;
		case "java.awt.Color[r=237,g=28,b=36]": // Red=  Radioactive
			returnLetter = "Y";
			break;
		case "java.awt.Color[r=153,g=217,b=234]": // Lt Turquoise=  Crystal
			returnLetter = "C";
			break;
		case "java.awt.Color[r=0,g=162,b=232]": // Turquoise=  Minera
			returnLetter = "M";
			break;
		case "java.awt.Color[r=34,g=177,b=76]": // Green=   Organic
			returnLetter = "O";
			break;

		default:
			returnLetter = "N"; // defaults to soil or no science
			break;	
		}
		return returnLetter;
	}
	
	public static Coord extractCoord(String inputString) {
		if (inputString.lastIndexOf(" ") != -1) {
			String xPosStr = inputString.substring(0, inputString.indexOf(" "));
			String yPosStr = inputString.substring(inputString.indexOf(" ") +1, inputString.lastIndexOf(" "));
			return new Coord(Integer.parseInt(xPosStr), Integer.parseInt(yPosStr));
		}
		return null;
	}

	
	// placeholder hack code till something better is inserted
	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		} catch (NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
	
	public String makeMapString(){
		StringBuilder printMap = new StringBuilder();
		printMap.append(this.mapName + "\n");
		printMap.append(this.planetMap.getWidth() + " " 
							+ this.planetMap.getHeight() + " Map_Width_Height\n");
		printMap.append(planetMap.getStartPosition().xpos + " " 
							+ planetMap.getStartPosition().ypos + " StartPosition(x,y)\n");
		printMap.append(planetMap.getTargetPosition().xpos + " " 
							+ planetMap.getTargetPosition().ypos + " TargetPosition(x,y)\n");
		
		printMap.append("KEY:<Terrain> R = Rock; G = Gravel; S = Sand; X = abyss;  <Science> Y = Radioactive; C = Crystal; M = Mineral; O = Organic; <Rover> ##\n");
		
		// print column numbers
		printMap.append(" "); // shift right one space
		for (int i = 0; i < mapWidth; i++) {
			printMap.append(i + " ");
			// set spacing on number of digits in column number
			if(i < 10){
				printMap.append(" ");
			}
		}
		printMap.append("\n");
		
		// draw top row of lines
		int rowCount = 0;
		for (int h = 0; h < mapWidth; h++) {
			printMap.append(" __");
		}
		printMap.append("\n");
		
		for (int j = 0; j < mapHeight; j++) {
			for (int i = 0; i < mapWidth; i++) {
				// check for rover
				if (roverLocations.containsCoord(new Coord(i, j))) {
					String rNum = roverLocations.getName(new Coord(i, j)).toString();
					printMap.append("|" + rNum.substring(6));
				} else {
					printMap.append("|  ");
				}
			}
			printMap.append("| " + rowCount++ + "\n"); // Print row numbers at end of row
			for (int k = 0; k < mapWidth; k++) {
				Coord tcor = new Coord(k, j);
				if (planetMap.getTile(tcor).getTerrain() != Terrain.SOIL) {
					printMap.append("|");
					printMap.append(planetMap.getTile(tcor).getTerrain().getTerString());
					if (scienceLocations.checkLocation(tcor)) {
						printMap.append(scienceLocations.scanLocation(tcor).getSciString());
					} else {
						printMap.append("_");
					}

				} else if (planetMap.getTile(tcor).getTerrain() == Terrain.SOIL) {
					printMap.append("|_");
					if (scienceLocations.checkLocation(tcor)) {
						printMap.append(scienceLocations.scanLocation(tcor).getSciString());
					} else {
						printMap.append("_");
					}

				} else {

					printMap.append("|__");
				}
			}
			printMap.append("|\n");
		}

		String printMapString = printMap.toString();
		return printMapString;
	}
	
	
	public void saveToDisplayTextFile(String fileName) throws IOException {		
		String printMapString = makeMapString();
		
		try {
			File file = new File(fileName);
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(printMapString);
	
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAsPaintMapFiles(){
		
		
	}
	
	public static void main(String[] args) throws IOException {
		PaintMapUtility cptm = new PaintMapUtility();
		cptm.parseInputFromMaintMapFiles("PalletMap.txt");
		
		cptm.saveToDisplayTextFile("convertedPaintMapToTextMap.txt");		
		
	}
	
	/**
	 * Color Value list (values in RGB)
	 * 
	 * None=  Black=   java.awt.Color[r=0,g=0,b=0]
	 * Rock=  Blue=   java.awt.Color[r=63,g=72,b=204]
	 * Soil=  White=   java.awt.Color[r=255,g=255,b=255]
	 * Sand =  Yellow=   java.awt.Color[r=255,g=242,b=0]
	 * Gravel =  Lt Grey=   java.awt.Color[r=195,g=195,b=195]
	 * 
	 * Radioactive=  Red=   java.awt.Color[r=237,g=28,b=36]
	 * crystal=  Lt Turquoise=   java.awt.Color[r=153,g=217,b=234]
	 * Mineral=  Turquoise=   java.awt.Color[r=0,g=162,b=232]
	 * Organic=  Green=   java.awt.Color[r=34,g=177,b=76]
	 * 
	 * Player Rover base color=   Rover Grey=   java.awt.Color[r=100,g=100,b=100]
	 * Sample Rover base color=   Pink=   java.awt.Color[r=255,g=174,b=200]
	 * 
	 * 
	 * 



Available

Orange=   java.awt.Color[r=255,g=127,b=39]

Purple=   java.awt.Color[r=163,g=73,b=164]

Dark Red=   java.awt.Color[r=136,g=0,b=21]

Gold=   java.awt.Color[r=255,g=201,b=14]

Lime=   java.awt.Color[r=181,g=230,b=29]

Blue-Grey=   java.awt.Color[r=112,g=146,b=190]


	 */
}


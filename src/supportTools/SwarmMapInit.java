package supportTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import enums.RoverName;
import enums.Science;
import enums.Terrain;
import json.GlobalReader;
import json.MyWriter;

public class SwarmMapInit {

	private String mapName;
	private int mapWidth;
	private int mapHeight;
	private PlanetMap planetMap;
	private RoverLocations roverLocations;
	private ScienceLocations scienceLocations;
	private Coord startPosCoord = null;
	private Coord targetPosCoord = null;

	public SwarmMapInit(int notUsed) { // for testing purposes
		this.mapWidth = 5;
		this.mapHeight = 5;
		this.planetMap = new PlanetMap(mapWidth, mapHeight);
		this.planetMap.loadSmallExampleTestPlanetMapTerrain();

		this.roverLocations = new RoverLocations();
		this.roverLocations.loadExampleTestRoverLocations();

		this.scienceLocations = new ScienceLocations();
		this.scienceLocations.loadExampleTestScienceLocations();

		this.mapName = "";
		this.startPosCoord = new Coord(1,1);
		this.targetPosCoord = new Coord(2,2);
	}

	public SwarmMapInit() {
		this.mapWidth = 0;
		this.mapHeight = 0;
		this.planetMap = new PlanetMap(mapWidth, mapHeight);
		this.roverLocations = new RoverLocations();
		this.scienceLocations = new ScienceLocations();
		this.mapName = "";
	}

	public SwarmMapInit(String name, int width, int height, PlanetMap planetMapIn, RoverLocations roverLoc,
			ScienceLocations scienceLoc) {
		this.mapWidth = width;
		this.mapHeight = height;
		this.planetMap = planetMapIn;
		this.roverLocations = roverLoc;
		this.scienceLocations = scienceLoc;
		this.mapName = name;
		this.startPosCoord = planetMapIn.getStartPosition();
		this.targetPosCoord = planetMapIn.getTargetPosition();
	}


	public void saveToJson(String fileName) {
		MyWriter mapwriter = new MyWriter(this, fileName);
	}

	public void loadFromJson(String fileName) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.enableComplexMapKeySerialization()
				.create();
		GlobalReader gread = new GlobalReader(fileName);
		JSONObject jInit = new JSONObject();// getJSONObject()
		jInit = gread.getJSONObject();
		SwarmMapInit tswarm = new SwarmMapInit();

		tswarm = gson.fromJson(jInit.toJSONString(), new TypeToken<SwarmMapInit>() {
		}.getType());
		this.mapWidth = tswarm.mapWidth;
		this.mapHeight = tswarm.mapHeight;
		this.planetMap = tswarm.planetMap;
		this.roverLocations = tswarm.roverLocations;
		this.scienceLocations = tswarm.scienceLocations;
		this.mapName = tswarm.mapName;
		this.startPosCoord = tswarm.planetMap.getStartPosition();
		this.targetPosCoord = tswarm.planetMap.getTargetPosition();
	}

	public int getMapWidth() {
		return mapWidth;
	}

	public int getMapHeight() {
		return mapHeight;
	}

	public PlanetMap getPlanetMap() {
		return planetMap;
	}

	public RoverLocations getRoverLocations() {
		return roverLocations;
	}

	public ScienceLocations getScienceLocations() {
		return scienceLocations;
	}

	public void parseInputFromDisplayTextFile(String fileName) throws IOException {
		this.roverLocations = new RoverLocations();
		this.scienceLocations = new ScienceLocations();

		FileReader input = new FileReader(fileName);
		BufferedReader bufRead = new BufferedReader(input);
		String myLine = null;

		// line 1 - map name
		this.mapName = bufRead.readLine();
		System.out.println("MapInit: " + this.mapName);

		// line 2 - map width and height
		Coord mapSize = extractCoord(bufRead.readLine());
		this.mapWidth = mapSize.xpos;
		this.mapHeight = mapSize.ypos;
		
		// line 3 - start position (x, y) coordinate
		Coord startPos = extractCoord(bufRead.readLine());

		// line 4 - target position x coordinate
		Coord targetPos = extractCoord(bufRead.readLine());
		
		// line 5 - skip past the map letter key
		bufRead.readLine();
		
		// line 6 - skip past the column number lines
		bufRead.readLine();
		
		// line 7 - skip past the top row of underline characters
		bufRead.readLine();
		
		this.planetMap = new PlanetMap(this.mapWidth, this.mapHeight, startPos, targetPos);
		
		double yCount = 0.0;
		
		
		while ((myLine = bufRead.readLine()) != null) { //might consider breaking based on map height check
			int yPos = (int) yCount;

			for (int i = 0; i < mapWidth; i++) {
				// grab the 2nd and 3rd character in a 3 character block based on i
				String tstr = myLine.substring(i * 3 + 1, i * 3 + 3);
				if (isInteger(tstr)) {
					String rName = "ROVER_" + tstr;
					
					roverLocations.putRover(RoverName.getEnum(rName), new Coord(i, yPos));
				} else if (tstr.startsWith("__") || tstr.startsWith("  ")) {
					// do nothing
					
				} else {
					String posOne = tstr.substring(0, 1);
					if (!posOne.equals("_")) {
						
						planetMap.setTile(new MapTile(posOne), i, yPos);
					} else {
						
						planetMap.setTile(new MapTile("N"), i, yPos);
					}

					String posTwo = tstr.substring(1, 2);
					if (!posTwo.equals("_")) {
						
						scienceLocations.putScience(new Coord(i, yPos), Science.getEnum(posTwo));
					} else {
						// do nothing
					}
				}
			}
			
			yCount += 0.5;
		}
	}

	
	public void printToDisplayTextFile() {	
		String printMapString = makeInitString();		
		System.out.println("MapInit: " + printMapString);
	}

	
	public void saveToDisplayTextFile(String fileName) throws IOException {		
		String printMapString = makeInitString();
		
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
	
	
	public String makeInitString(){
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

	
	
	/*
	 * These are only used for testing and development
	 */
	
	public void loadExampleTest() { // for testing purposes
		this.mapWidth = 30;
		this.mapHeight = 30;
		this.planetMap = new PlanetMap(mapWidth, mapHeight);
		this.planetMap.loadExampleTestPlanetMapTerrain();

		this.roverLocations = new RoverLocations();
		this.roverLocations.loadExampleTestRoverLocations();

		this.scienceLocations = new ScienceLocations();
		this.scienceLocations.loadExampleTestScienceLocations();

		this.mapName = "";
	}

	public void loadSmallExampleTest() { // for testing purposes
		this.mapWidth = 5;
		this.mapHeight = 5;
		this.planetMap = new PlanetMap(mapWidth, mapHeight);
		this.planetMap.loadSmallExampleTestPlanetMapTerrain();

		this.roverLocations = new RoverLocations();
		this.roverLocations.loadSmallExampleTestRoverLocations();

		this.scienceLocations = new ScienceLocations();
		this.scienceLocations.loadSmallExampleTestScienceLocations();

		this.mapName = "";
	}

}

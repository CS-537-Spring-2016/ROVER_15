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
	}


	public void saveToJson(String fileName) {
		MyWriter mapwriter = new MyWriter(this, fileName);
	}

	public void loadFromJson(String fileName) {
		Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
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

		// line 1
		this.mapName = bufRead.readLine();
		System.out.println(this.mapName);

		// line 2
		// mapWidth = Integer.parseInt(bufRead.readLine());
		String mapWidthStr = bufRead.readLine();
		System.out.println(mapWidthStr);

		this.mapWidth = (int) Integer.parseInt(mapWidthStr);
		System.out.println("int version " + this.mapWidth);

		// line 3
		String mapHeightStr = bufRead.readLine();
		System.out.println(mapHeightStr);
		this.mapHeight = (int) Integer.parseInt(mapHeightStr); 

		this.planetMap = new PlanetMap(this.mapWidth, this.mapHeight);

		double yCount = 0.0;
		// line 4
		System.out.print(yCount + "-" + bufRead.readLine() + " things found: ");
		System.out.println("");

		while ((myLine = bufRead.readLine()) != null) {
			int yPos = (int) yCount;
			System.out.print(yCount + "-" + myLine + " things found: ");
			for (int i = 0; i < mapWidth; i++) {
				String tstr = myLine.substring(i * 3 + 1, i * 3 + 3);
				if (isInteger(tstr)) {
					String rName = "ROVER_" + tstr;
					System.out.print(rName);
					roverLocations.putRover(RoverName.getEnum(rName), new Coord(i, yPos));
				} else if (tstr.startsWith("__") || tstr.startsWith("  ")) {
					System.out.print(tstr + " - ");
				} else {
					String posOne = tstr.substring(0, 1);
					if (!posOne.equals("_")) {
						System.out.print("posOne: " + Terrain.getEnum(posOne) + " ");
						planetMap.setTile(new MapTile(posOne), i, yPos);
					} else {
						System.out.print("posOne: emmpty  ");
						planetMap.setTile(new MapTile("N"), i, yPos);
					}

					String posTwo = tstr.substring(1, 2);
					if (!posTwo.equals("_")) {
						System.out.print("posTwo: " + Science.getEnum(posTwo) + " ");
						scienceLocations.putScience(new Coord(i, yPos), Science.getEnum(posTwo));
					} else {
						System.out.print("posTwo: empty  ");
					}
				}
			}

			System.out.println("");
			yCount += 0.5;
		}
	}

	public void printToDisplayTextFile() {
		StringBuilder printMap = new StringBuilder();
		printMap.append("map name");
		printMap.append(mapHeight + "\n");
		printMap.append(mapWidth + "\n");

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
			printMap.append("|\n");
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
		System.out.println(printMapString);
	}

	public void saveToDisplayTextFile(String fileName) throws IOException {

		StringBuilder printMap = new StringBuilder();
		printMap.append(fileName + "\n");
		printMap.append(mapHeight + "\n");
		printMap.append(mapWidth + "\n");

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
			printMap.append("|\n");
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
		
		System.out.println(printMapString);
		
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

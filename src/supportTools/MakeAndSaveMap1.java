package supportTools;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.RoverLocations;
import common.ScanMap;
import common.ScienceLocations;
import enums.Science;
import enums.Terrain;
import json.MyWriter;

public class MakeAndSaveMap1 {

	public static void main(String[] args) throws IOException {
		
		//String name, int width, int height, PlanetMap planetMapIn, RoverLocations roverLoc, ScienceLocations scienceLoc)
		
		SwarmMapInit mapInit = new SwarmMapInit("100 x 100 map", 100, 100, new PlanetMap(100, 200), new RoverLocations(), new ScienceLocations());

		mapInit.saveToDisplayTextFile("largMap100x100blank.txt");
		
		
//		mapInit.loadExampleTest();
//		
//		//mapInit.getRoverLocations().printRovers();
//		
//		mapInit.saveToDisplayTextFile("mapset2.txt");
		
	}

}

package testUtillities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import enums.RoverName;
import enums.Science;
import enums.Terrain;
import supportTools.SwarmMapInit;

public class ParseInputMap {
	
	 public static void main(String[] args) throws Exception {
	        System.out.println("The ParseInputMap is running.");
	    	String mapName = null;
	        int mapWidth = 0; 
	        int mapHeight = 0; 
	    	PlanetMap planetMap; 
	        RoverLocations roverLocations = new RoverLocations(); 
	        ScienceLocations scienceLocations = new ScienceLocations(); 
	        
	        SwarmMapInit initMap = new SwarmMapInit();
	
	        
	        FileReader input = new FileReader("mapText.txt");
			BufferedReader bufRead = new BufferedReader(input);
			String myLine = null;
			
			//line 1
			mapName = bufRead.readLine();
			System.out.println(mapName);
			
			//line 2
			//mapWidth = Integer.parseInt(bufRead.readLine());
			String mapWidthStr = bufRead.readLine();
			System.out.println(mapWidthStr);
			
			mapWidth = (int)Integer.parseInt(mapWidthStr);
			System.out.println("int version " + mapWidth);
			
			//line 3
			String mapHeightStr = bufRead.readLine();
			System.out.println(mapHeightStr);
			mapHeight = (int)Integer.parseInt(mapHeightStr);
			
			planetMap = new PlanetMap(mapWidth, mapHeight);
			
			double yCount = 0.0;
			//line 4
			System.out.print(yCount + "-" + bufRead.readLine() + " things found: ");
			System.out.println("");
			
			
			while ( (myLine = bufRead.readLine()) != null)
			{   
				int yPos = (int) yCount;
				System.out.print(yCount + "-" + myLine + " things found: ");
				for(int i = 0; i < mapWidth; i++){
					String tstr = myLine.substring(i * 3 + 1, i * 3 + 3);
					if(isInteger(tstr)){
						String rName = "ROVER_" + tstr;
						System.out.print(rName);
						roverLocations.putRover(RoverName.getEnum(rName), new Coord(i, yPos));
					} else if(tstr.startsWith("__") || tstr.startsWith("  ")){
						System.out.print( tstr + " - ");
					} else {
						String posOne = tstr.substring(0, 1);
						if(!posOne.equals("_")){
							System.out.print( "posOne: " + Terrain.getEnum(posOne) + " ");
							planetMap.setTile(new MapTile(posOne), i, yPos);
						} else {
							System.out.print( "posOne: emmpty  ");
							planetMap.setTile(new MapTile("N"), i, yPos);
						}
							
						String posTwo = tstr.substring(1, 2);
						if(!posTwo.equals("_")){
							System.out.print( "posTwo: " + Science.getEnum(posTwo) + " ");
							scienceLocations.putScience(new Coord(i, yPos), Science.getEnum(posTwo));
						} else {
							System.out.print( "posTwo: empty  ");
						}
					}			
				}
			
				System.out.println("");
				yCount += 0.5;
			}
			
			
			roverLocations.printRovers();
			//SwarmMapInit mapInit = new SwarmMapInit(mapName, mapWidth, mapHeight, planetMap, roverLocations, scienceLocations);
			
			//mapInit.printToDisplayText("yep");
			
	 }
	 
	 // placeholder hack code till something better is inserted
	 public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    } catch(NullPointerException e) {
		        return false;
		    }
		    // only got here if we didn't return false
		    return true;
		}
	
}

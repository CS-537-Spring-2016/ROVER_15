package testUtillities;


import javax.swing.SwingUtilities;

import org.json.simple.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import json.GlobalReader;
import supportTools.SwarmMapInit;



public class GUImapDisplay {
	
    private static int mapWidth = 40;
    private static int mapHeight = 40;
	private static PlanetMap planetMap; // = new PlanetMap(mapWidth, mapHeight); 
    private static RoverLocations roverLocations = new RoverLocations();
    private static ScienceLocations scienceLocations = new ScienceLocations();
    private static SwarmMapInit startStuff;
	
    static GUIdisplay3 mainPanel;
	static MyGUIWorker3 myWorker;
	
	   public static void main(String[] args) throws Exception {
	        System.out.println("The GUImapDisplay is running.");
//	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//	        
//	        GlobalReader gread = new GlobalReader(4);
//	        
//	        JSONObject jmap = new JSONObject();// getJSONObject()
//	        
//	        jmap = gread.getJSONObject();
//	        
//	        planetMap = gson.fromJson(jmap.toJSONString(), new TypeToken<PlanetMap>(){}.getType());
//	        
//
//			
	        startStuff = new SwarmMapInit();
	        startStuff.loadFromJson("sinit");
	        
	        mapWidth = startStuff.getMapWidth();
	        mapHeight = startStuff.getMapHeight();
	        planetMap = startStuff.getPlanetMap();
	        scienceLocations = startStuff.getScienceLocations();
	        roverLocations = startStuff.getRoverLocations();
	        
	        
			mainPanel = new GUIdisplay3(mapWidth, mapHeight);
			myWorker = new MyGUIWorker3(mainPanel);
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					GUIdisplay3.createAndShowGui(myWorker, mainPanel);
				}
			});
			
			updateGUIDisplay();
			
  
	        //System.out.println("GUImapDisplay new SwarmSimulationInit(1)");	     
	        
	        System.out.println(roverLocations.getHashMapClone());
	        

	        

	        
	        System.out.println(scienceLocations.getHashMapClone());
	        
	        //startStuff.saveSwarmSimulationInit("sinit");
	        
	        
	   }
	
		static void updateGUIDisplay() throws Exception{
			//myWorker.displayRovers(roverLocations);
			//myWorker.displayActivity(roverLocations, scienceLocations);
			myWorker.displayFullMap(roverLocations, scienceLocations, planetMap);
		}

}

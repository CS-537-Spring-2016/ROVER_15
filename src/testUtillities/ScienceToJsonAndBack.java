package testUtillities;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.Coord;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import enums.Science;

public class ScienceToJsonAndBack {
	
//    private static int mapWidth;
//    private static int mapHeight;
//	private static PlanetMap planetMap; // = new PlanetMap(mapWidth, mapHeight); 
//    private static RoverLocations roverLocations;
//     private static ScienceLocations sciLoc;

	
	
	   public static void main(String[] args) throws Exception {
	        System.out.println("The ScienceToJsonAndBack is running.");
	        
	        Gson gson = new GsonBuilder()
	        		.setPrettyPrinting()
	        		.enableComplexMapKeySerialization()
	        		.create();
	        
//	        ScienceLocations sciLoc = new ScienceLocations ();
//	        sciLoc.loadSmallExampleTestScienceLocations();
//	        
//	        String jSciLoc = gson.toJson(sciLoc);
//	        
//	        System.out.println(jSciLoc);
//	        
//	        
//	        
//	        HashMap<String, Science> scienceHash = new HashMap<String, Science>();
//	        
//	        scienceHash.put("one", Science.RADIOACTIVE);
//			scienceHash.put("two", Science.CRYSTAL);
//			
//			System.out.println(scienceHash);
//			
//			String jStrSciHash = gson.toJson(scienceHash);
//			
//			System.out.println(jStrSciHash);
//			
//			//returnList = gson.fromJson(jsonEqListString, new TypeToken<HashMap<String, Science>>(){}.getType());
//	        
//	        System.out.println(new Coord(2,7));
	        
	        
	        
	        
	        HashMap<Coord, Science> scienceHash2 = new HashMap<Coord, Science>();
	        
	        scienceHash2.put(new Coord(4,5), Science.RADIOACTIVE);
			scienceHash2.put(new Coord(3,8), Science.CRYSTAL);
			
			System.out.println(scienceHash2);
			
			String jStrSciHash2 = gson.toJson(scienceHash2);
			
			System.out.println(jStrSciHash2);
			
			
			
//	        HashMap<Science, Coord> scienceHash3 = new HashMap<Science, Coord>();
//	        
//	        scienceHash3.put(Science.RADIOACTIVE, new Coord(4,5));
//			scienceHash3.put(Science.CRYSTAL, new Coord(3,8));
//			
//			System.out.println(scienceHash3);
//			
//			String jStrSciHash3 = gson.toJson(scienceHash3);
//			
//			System.out.println(jStrSciHash3);
	        
	   }

}

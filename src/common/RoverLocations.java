package common;

import java.util.HashMap;
import java.util.Map.Entry;

import enums.RoverName;
import enums.Science;

public class RoverLocations {
	
	private HashMap<RoverName, Coord> roverHash;

	public RoverLocations(){
		roverHash = new HashMap<RoverName, Coord>();
	}
	
	public RoverLocations(HashMap<RoverName, Coord> rovHash){
		roverHash = (HashMap<RoverName, Coord>) rovHash.clone();
	}
	

	
	public synchronized boolean moveRover(RoverName rname, Coord loc){
		if(roverHash.containsValue(loc)){
			return false;
		}			
		roverHash.put(rname,  loc);			
		return true;
	}
	
	public synchronized Coord getLocation(RoverName rname){
		return roverHash.get(rname);
	}
	
	public synchronized RoverName getName(Coord loc){
		for (Entry<RoverName, Coord> entry : roverHash.entrySet()) {
            if (entry.getValue().equals(loc)) {
                return entry.getKey();
            }
        }	
		return null;
	}
	
	public synchronized boolean containsCoord (Coord loc){
		return roverHash.containsValue(loc);
	}
	
	public void printRovers(){
		for(RoverName rovloc : roverHash.keySet()){
			String key = rovloc.toString();
            String value = roverHash.get(rovloc).toString();  
            System.out.println(key + " " + value);  
    	}
	}
	
	public synchronized HashMap<RoverName, Coord> getHashMapClone(){	
		return (HashMap<RoverName, Coord>) roverHash.clone();
	}
	
	public synchronized void putRover(RoverName rname, Coord rloc){	
		roverHash.put(rname, rloc);
	}
	
	public RoverLocations clone(){
		return new RoverLocations(this.roverHash);
	}
	
	
	
	/*
	 * These are only used for testing and development
	 */
	
	public void loadExampleTestRoverLocations(){
        // place all the rovers into the map in their initial positions
        // TODO - have initial positions loaded from a file instead of hard coded
		roverHash.put(RoverName.ROVER_01, new Coord(1,1));
		roverHash.put(RoverName.ROVER_02, new Coord(2,1));
		roverHash.put(RoverName.ROVER_03, new Coord(3,1));
		roverHash.put(RoverName.ROVER_04, new Coord(4,1));
		roverHash.put(RoverName.ROVER_05, new Coord(5,1));
		roverHash.put(RoverName.ROVER_06, new Coord(6,1));
		roverHash.put(RoverName.ROVER_07, new Coord(7,1));
		roverHash.put(RoverName.ROVER_08, new Coord(8,1));
		roverHash.put(RoverName.ROVER_09, new Coord(9,1));
		roverHash.put(RoverName.ROVER_10, new Coord(10,1));
		roverHash.put(RoverName.ROVER_11, new Coord(11,1));
		roverHash.put(RoverName.ROVER_12, new Coord(12,1));
		roverHash.put(RoverName.ROVER_13, new Coord(13,1));
		roverHash.put(RoverName.ROVER_14, new Coord(14,1));
		roverHash.put(RoverName.ROVER_15, new Coord(15,1));
		roverHash.put(RoverName.ROVER_16, new Coord(16,1));
		roverHash.put(RoverName.ROVER_17, new Coord(17,1));
		roverHash.put(RoverName.ROVER_18, new Coord(18,1));
		roverHash.put(RoverName.ROVER_19, new Coord(19,1));
		roverHash.put(RoverName.ROVER_20, new Coord(20,1));
		
		// test rovers
		roverHash.put(RoverName.ROVER_00, new Coord(4,20));
		roverHash.put(RoverName.ROVER_99, new Coord(4,7));
	}
	
	public void loadSmallExampleTestRoverLocations(){
		
		// test rovers
		roverHash.put(RoverName.ROVER_00, new Coord(4,20));
		roverHash.put(RoverName.ROVER_99, new Coord(4,7));
	}
}

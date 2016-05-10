package testUtillities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Objects;

import common.Coord;
import common.ScienceLocations;
import enums.Science;


public class SubsettingTest {
	
	
	
	public static void main(String[] args) {

		ScienceLocations scienceLocations = new ScienceLocations();
		
		System.out.println(scienceLocations.getHashMapClone());
		
		
		
		HashMap<Coord, Science> sciHash = new HashMap<Coord, Science>();
		HashMap<Coord, Science> filteredSciHash = new HashMap<Coord, Science>();
		
		sciHash = scienceLocations.getHashMapClone();
					
	    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
	        if (Objects.equals(Science.RADIOACTIVE, entry.getValue())) {
	        	filteredSciHash.put(entry.getKey(), Science.RADIOACTIVE);
	            System.out.println( entry.getKey());
	        }
	    }
	    
	    System.out.println("");
	    System.out.println("filteredSciHash " + filteredSciHash);
		
	}
	

}

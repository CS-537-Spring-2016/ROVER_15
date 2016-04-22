package common;

import java.util.HashMap;
import java.util.Map.Entry;

import enums.RoverName;
import enums.Science;

public class ScienceLocations {
	
		// can only have one science at any single location
		private HashMap<Coord, Science> scienceHash;

		public ScienceLocations(){
			scienceHash = new HashMap<Coord, Science>();
		}
		
		public ScienceLocations(HashMap<Coord, Science> sciHash){
			scienceHash = (HashMap<Coord, Science>) sciHash.clone();
		}
		
		
		public synchronized boolean checkLocation(Coord loc){
			return scienceHash.containsKey(loc);
		}
		
		public synchronized Science scanLocation(Coord loc){
			if(checkLocation(loc)){
				return scienceHash.get(loc);
			} else {
				return Science.NONE;
			}
		}
		
		public synchronized Science takeScience(Coord loc){
			if(checkLocation(loc)){
				Science sci = scienceHash.get(loc);
				this.removeLocation(loc);
				return sci;
			} else {
				return Science.NONE;
			}
		}
		
		private synchronized void putLocation(Coord loc, Science sci){
			this.scienceHash.put(loc, sci);
		}
		
		private synchronized void removeLocation(Coord loc){
			this.scienceHash.remove(loc);
		}
		
		public synchronized HashMap<Coord, Science> getHashMapClone(){	
			return (HashMap<Coord, Science>) scienceHash.clone();
		}
		
		public void printScience(){
			for(Coord rovloc : scienceHash.keySet()){
				String key = rovloc.toString();
	            String value = scienceHash.get(rovloc).toString();  
	            System.out.println(key + " " + value);  
	    	}
		}
		
		public synchronized void putScience(Coord sloc, Science sci){	
			scienceHash.put(sloc, sci);
		}
		
		
		
		/*
		 * These are only used for testing and development
		 */
		
		public void loadExampleTestScienceLocations(){
			// put some sample science in the world
			scienceHash.put(new Coord(20,14), Science.RADIOACTIVE);
			scienceHash.put(new Coord(21,20), Science.CRYSTAL);
			scienceHash.put(new Coord(7,3), Science.MINERAL);
			scienceHash.put(new Coord(9,15), Science.ORGANIC);
			scienceHash.put(new Coord(17,12), Science.RADIOACTIVE);
			scienceHash.put(new Coord(10,11), Science.CRYSTAL);
			scienceHash.put(new Coord(19,3), Science.MINERAL);
			scienceHash.put(new Coord(12,24), Science.ORGANIC);
		}
		
		public void loadSmallExampleTestScienceLocations(){
			// put some sample science in the world
			scienceHash.put(new Coord(20,20), Science.RADIOACTIVE);
		}
		
}

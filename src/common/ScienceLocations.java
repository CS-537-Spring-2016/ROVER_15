package common;

import java.util.HashMap;
import java.util.Map.Entry;

import enums.Science;

public class ScienceLocations {
	
		// can only have one science at any single location
		private HashMap<Coord, Science> scienceHash = new HashMap<Coord, Science>();

		public ScienceLocations(){
	        // place all the Science into the map in their initial positions
	        // TODO - have initial positions loaded from a file instead of hard coded

			// put some sample science in the world
			scienceHash.put(new Coord(20,20), Science.RADIOACTIVE);
			scienceHash.put(new Coord(21,20), Science.CRYSTAL);
			scienceHash.put(new Coord(7,3), Science.MINERAL);
			scienceHash.put(new Coord(9,15), Science.ORGANIC);
			scienceHash.put(new Coord(17,12), Science.RADIOACTIVE);
			scienceHash.put(new Coord(10,11), Science.CRYSTAL);
			scienceHash.put(new Coord(19,3), Science.MINERAL);
			scienceHash.put(new Coord(12,24), Science.ORGANIC);
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
		
}

package supportTools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.ScanMap;
import enums.Science;
import enums.Terrain;
import json.MyWriter;

public class MakeAndSaveMap1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PlanetMap map1 = new PlanetMap(10, 10);
		MapTile tile = new MapTile(Terrain.ROCK, Science.RADIOACTIVE, 2, false);
		map1.setTile(tile, 4, 7);
		
		MyWriter mapwriter = new MyWriter(map1, 3);
		
		
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		MapTile mtile1 = new MapTile(Terrain.ROCK, Science.CRYSTAL, 1, true);	
		MapTile[][] mapmap = new MapTile[5][5];
				
		for(int j= 0; j< 5; j++){
			for(int i= 0; i< 5; i++){
				mapmap[i][j] = new MapTile();	
				//System.out.print("("+i+","+j+"); ");
			}	
			System.out.println("");
		}
		mapmap[3][2] = mtile1;
		
		String jsonMap = gson.toJson(mapmap);
		System.out.println(jsonMap);


	}

}

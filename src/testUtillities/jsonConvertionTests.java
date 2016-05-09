package testUtillities;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Coord;
import common.MapTile;
import common.ScanMap;
import enums.Science;
import enums.Terrain;
import json.MyWriter;

public class jsonConvertionTests {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
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
		
		ScanMap sMap = new ScanMap(mapmap, 5, new Coord(6,7));
		
		new MyWriter(sMap, 2);
		
		String jsonScanMap = gson.toJson(sMap);  //money shot - object to json string
		
		
		
		System.out.println(jsonMap.toString());
		
		System.out.println("###################################################");
		
		System.out.println(jsonScanMap);
		
		

		ScanMap sMap2 = null;
		
		sMap2 = gson.fromJson(jsonScanMap , ScanMap.class);  //money shot two - json string to object
		
		MapTile[][] mapmap2 = sMap2.getScanMap();
		
		for(int j= 0; j< 5; j++){
			for(int i= 0; i< 5; i++){
				if(mapmap2[i][j].getHasRover()){
					System.out.print("X ");
				} else {
					System.out.print("_ ");
				}
			}	
			System.out.println("");
		}
		
//		Object jsonString;
//		//JSONObject jsonObject = (JSONObject) jsonMap;
//		JSONObject json = (JSONObject)new JSONParser().parse((String) jsonMap);
		
		
	}

}

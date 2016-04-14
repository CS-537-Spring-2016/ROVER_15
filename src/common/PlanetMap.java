package common;

public class PlanetMap {
	private MapTile[][] planetMap;
	private int mapWidth;
	private int mapHeight;
	
	public PlanetMap(){
		this.mapHeight = 0;
		this.mapWidth = 0;
		this.planetMap = null;
	}
	
	// width is number of columns is xloc, height is number of rows is yloc
	public PlanetMap(int width, int height){
		this.mapHeight = height;
		this.mapWidth = width;
		this.planetMap = new MapTile[width][height];
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				this.planetMap[i][j] = new MapTile();
			}
		}
	}
	
	public PlanetMap(int width, int height, String fname){
		this.mapHeight = height;
		this.mapWidth = width;
		this.planetMap = new MapTile[width][height];
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				this.planetMap[i][j] = new MapTile();
			}
		}
	}
	
	public PlanetMap(String filename){

	}
	
	public PlanetMap(PlanetMap planetMapIn) {
		planetMap = planetMapIn.planetMap;
		mapWidth = planetMapIn.mapWidth;
		mapHeight = planetMapIn.mapHeight;
	}

	public void setTile(MapTile tile, int xloc, int yloc){
		this.planetMap[xloc][yloc] = tile;
	}
	
	public MapTile getTile(Coord coord){
		return this.planetMap[coord.xpos][coord.ypos];
	}
	
	public MapTile getTile(int xloc, int yloc){
		return this.planetMap[xloc][yloc];
	}
	
	// assumes edge size is an odd number
	public ScanMap getScanMap(Coord coord, int edgeSize, RoverLocations rloc, ScienceLocations sciloc){
		int startx = coord.xpos - (edgeSize -1)/2;
		int starty = coord.ypos - (edgeSize -1)/2;
		MapTile aTile;
		MapTile[][] tMap = new MapTile[edgeSize][edgeSize];
		
		for(int j= 0; j< edgeSize; j++){
			for(int i= 0; i< edgeSize; i++){
				if((i + startx) < 0 || (i + startx) >= mapWidth || (j + starty) < 0 || (j + starty) >= mapHeight){
					aTile = new MapTile(0); // makes a MapTile with terrain = NONE
				} else {
					
					// It is important to make a copy of the map tile to prevent
					// passing the tile by reference and corrupting the original planetMap
					aTile = planetMap[i + startx][j + starty].getCopyOfMapTile();
				}
				Coord tempCoord = new Coord(i + startx, j + starty);
				// check and add rover to tile
				if(rloc.containsCoord(tempCoord)){
					aTile.setHasRoverTrue();
				}
				
				// check and add Science if on map
				if(sciloc.checkLocation(tempCoord)){
					aTile.setSciecne(sciloc.scanLocation(tempCoord));
				}
				
				//System.out.println("PlanetMap: getScan index: " + i + " " + j);
				tMap[i][j] = aTile;
			}	
		}
		
		//System.out.println("PLANET_MAP: ---- print roverLoc ----");
			//rloc.printRovers();
			//System.out.println("PLANET_MAP: ^^^^^ print roverLoc ^^^^");	
		return new ScanMap(tMap, edgeSize, coord);
	}
	
	public int getWidth(){
		return mapWidth;
	}
	
	public int getHeight(){
		return mapHeight;
	}
	
	public void loadExampleTestPlanetMapTerrain(){
		// temporary use for creating planet terrain for testing
		
		this.mapHeight = 40;
		this.mapWidth = 40;
		this.planetMap = new MapTile[mapWidth][mapHeight];
		for(int j=0;j<mapHeight;j++){
			for(int i=0;i<mapWidth;i++){
				this.planetMap[i][j] = new MapTile();
			}
		}
		
		this.planetMap[7][7] = new MapTile("R"); 
		this.planetMap[7][8] = new MapTile("R"); 
		this.planetMap[8][7] = new MapTile("R"); 
		this.planetMap[8][8] = new MapTile("R");
		
		this.planetMap[15][16] = new MapTile("R"); 
		this.planetMap[15][17] = new MapTile("R"); 
		this.planetMap[15][18] = new MapTile("R"); 
		this.planetMap[15][19] = new MapTile("R"); 
		this.planetMap[14][18] = new MapTile("R"); 
		this.planetMap[14][19] = new MapTile("R"); 
		this.planetMap[14][20] = new MapTile("R"); 
		this.planetMap[14][21] = new MapTile("R"); 
		
		this.planetMap[6][23] = new MapTile("R");
		this.planetMap[7][23] = new MapTile("R");
		this.planetMap[7][23] = new MapTile("R");
		this.planetMap[8][24] = new MapTile("R");
		this.planetMap[8][25] = new MapTile("R");
		
		this.planetMap[24][10] = new MapTile("S");
		this.planetMap[24][11] = new MapTile("S");
		this.planetMap[24][12] = new MapTile("S");
		this.planetMap[25][10] = new MapTile("S");
		this.planetMap[25][11] = new MapTile("S");
		this.planetMap[25][12] = new MapTile("S");
		this.planetMap[25][13] = new MapTile("S");
		this.planetMap[26][10] = new MapTile("S");
		this.planetMap[26][11] = new MapTile("S");
		this.planetMap[26][12] = new MapTile("S");
		this.planetMap[26][13] = new MapTile("S");
	}
	
	public void loadSmallExampleTestPlanetMapTerrain(){
		// temporary use for creating planet terrain for testing
		
		this.mapHeight = 5;
		this.mapWidth = 5;
		this.planetMap = new MapTile[mapWidth][mapHeight];
		for(int j=0;j<mapHeight;j++){
			for(int i=0;i<mapWidth;i++){
				this.planetMap[i][j] = new MapTile();
			}
		}
		
		this.planetMap[2][2] = new MapTile("R"); 
		this.planetMap[3][2] = new MapTile("R"); 
		this.planetMap[1][4] = new MapTile("R"); 
	
		this.planetMap[3][3] = new MapTile("S");
	}
}

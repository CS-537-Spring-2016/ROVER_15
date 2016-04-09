package common;

import enums.RoverName;

public class RoverMap {
	private RoverName[][] rovergrid;
		
	// width is number of columns is xloc, height is number of rows is yloc
	public RoverMap(int width, int height){
		this.rovergrid = new RoverName[width][height];
		for(int j=0;j<height;j++){
			for(int i=0;i<width;i++){
				this.rovergrid[i][j] = RoverName.NONE;
			}
		}
	}
	
	public void setTile(RoverName rname, int xloc, int yloc){
		this.rovergrid[xloc][yloc] = rname;
	}
	
	public void moveRover(RoverName rname, int xloc, int yloc){
		if(this.rovergrid[xloc][yloc] == RoverName.NONE){
			this.rovergrid[xloc][yloc] = rname;
			// clear previous tile ... hmmm
		}
	}
	
	public RoverName getTile(int xloc, int yloc){
		return this.rovergrid[xloc][yloc];
	}

}

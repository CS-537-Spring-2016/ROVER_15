package swarmBots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.Coord;
import common.MapTile;
import common.ScanMap;
import enums.Terrain;


//Making an addition to this file to check whether a remote alternat push will change it

// rearanged the 2nd and 3rd line in the following comment

/**
<<<<<<< HEAD
 * The seed that this program is built on is a chat program example found here:
 * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
 * publishing their code examples
 * 
 * ROVER_15  Spec:	Drive = Treads, Tool 1 = Drill, Tool 2 = Excavator 
 */
||||||| merged common ancestors
 * The seed that this program is built on is a chat program example found here:
 * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
 * publishing their code examples
 * 
 * ROVER_15  Spec:	Drive = wheels, Tool 1 = spectral sensor, Tool 2 = range extender
 */
=======
* The seed that this program is built on is a chat program example found here:
* publishing their code examples
* * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
*/
>>>>>>> origin/master

public class ROVER_15 {

	BufferedReader in;
	PrintWriter out;
	String rovername;
	ScanMap scanMap;
	int sleepTime;
	String SERVER_ADDRESS = "localhost";
	static final int PORT_ADDRESS = 9537;
	public static boolean[] goingNESW = {false,false,false,true};
	public static String[] cardinals = {"N","E","S","W"};
	public static int counter = 0;
	public ROVER_15() {
		// constructor
		System.out.println("ROVER_15 rover object constructed");
		rovername = "ROVER_15";
		SERVER_ADDRESS = "localhost";
		// this should be a safe but slow timer value
		sleepTime = 300; // in milliseconds - smaller is faster, but the server will cut connection if it is too small
	}

	public ROVER_15(String serverAddress) {
		// constructor
		System.out.println("ROVER_15 rover object constructed");
		rovername = "ROVER_15";
		SERVER_ADDRESS = serverAddress;
		sleepTime = 200; // in milliseconds - smaller is faster, but the server will cut connection if it is too small
	}

	/**
	 * Connects to the server then enters the processing loop.
	 */
	private void run() throws IOException, InterruptedException {

		// Make connection and initialize streams
		//TODO - need to close this socket
		Socket socket = new Socket(SERVER_ADDRESS, PORT_ADDRESS); // set port here
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);

		//Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Process all messages from server, wait until server requests Rover ID
		// name
		while (true) {
			String line = in.readLine();
			if (line.startsWith("SUBMITNAME")) {
				out.println(rovername); // This sets the name of this instance
				// of a swarmBot for identifying the
				// thread to the server
				break;
			}
		}

		// ******** Rover logic *********
		// int cnt=0;
		String line = "";
		String tline = "";

		
		
		boolean stuck = false; // just means it did not change locations between requests,
		// could be velocity limit or obstruction etc.
		boolean blocked = false;
		boolean blocked_byNothing = false;
		

		int currentDir = 3;
		Coord currentLoc = null;
		Coord previousLoc = null;
<<<<<<< HEAD
		Coord targetLoc = null;
||||||| merged common ancestors
=======
		
>>>>>>> origin/master

		// start Rover controller process
		while (true) {

			// currently the requirements allow sensor calls to be made with no
			// simulated resource cost


			// **** location call ****
			out.println("LOC");
			line = in.readLine();
<<<<<<< HEAD
			if (line == null) {
				System.out.println("ROVER_15 check connection to server");
				line = "";
			}
||||||| merged common ancestors
            if (line == null) {
            	System.out.println("ROVER_15 check connection to server");
            	line = "";
            }
=======
           if (line == null) {
           	System.out.println("ROVER_15 check connection to server");
           	line = "";
           }
>>>>>>> origin/master
			if (line.startsWith("LOC")) {
				// loc = line.substring(4);
				currentLoc = extractLOC(line);
			}
			System.out.println("ROVER_15 currentLoc at start: " + currentLoc);

			// after getting location set previous equal current to be able to check for stuckness and blocked later
			previousLoc = currentLoc;

			// **** finding jackpot ****
			// TODO: Verify that target location can be found here and parsing the coordinates is accurate
			
			if (line.startsWith("TARGET")) {
				targetLoc = (Coord) extractTARGET_LOC(line);
			}
			System.out.println("ROVER_15 targetLoc: " + targetLoc);
			System.out.println("JACKPOT!");




			// **** get equipment listing ****			
			ArrayList<String> equipment = new ArrayList<String>();
			equipment = getEquipment();
			//System.out.println("ROVER_15 equipment list results drive " + equipment.get(0));
			System.out.println("ROVER_15 equipment list results " + equipment + "\n");



			// ***** do a SCAN *****
			//System.out.println("ROVER_15 sending SCAN request");
			this.doScan();
			scanMap.debugPrintMap();
<<<<<<< HEAD





			// ***** MOVING *****
			// try moving east 5 block if blocked
			if (blocked) {
				for (int i = 0; i < 5; i++) {
					out.println("MOVE E");
					//System.out.println("ROVER_15 request move E");
					Thread.sleep(300);
||||||| merged common ancestors
			
			
			

			
			// ***** MOVING *****
			// try moving east 5 block if blocked
			if (blocked) {
				for (int i = 0; i < 5; i++) {
					out.println("MOVE E");
					//System.out.println("ROVER_15 request move E");
					Thread.sleep(300);
=======
			
			// Coord obj =  extractTargetLOC(sStr);
			//Driller Moving Logic
			out.println("TARGET_LOC");
			line = in.readLine();
			// pull the MapTile array out of the ScanMap object
			MapTile[][] scanMapTiles = scanMap.getScanMap();
			int centerIndex = (scanMap.getEdgeSize() - 1)/2;
			// tile S = y + 1; N = y - 1; E = x + 1; W = x - 1
			
			if(blocked){
				currentDir = getRandomDirection(currentDir);
				blocked = false;
				counter = 0;
			}
			else if(blocked_byNothing){
				List<Integer> allowedDirections = getDirectionsToTargetLocation();
				currentDir = getRandomDirection(currentDir,allowedDirections);
				counter = 0;
				blocked_byNothing = false;
			}
			
			if(goingNESW[currentDir]){
				if(roverStuckIncurrentDir(currentDir,scanMapTiles,centerIndex)){
					blocked = true;
					counter = 0;
				}		
				else if(counter > 10){
					blocked_byNothing = true;
					counter = 0;
>>>>>>> origin/master
				}
<<<<<<< HEAD
				blocked = false;
				//reverses direction after being blocked
				goingSouth = !goingSouth;
			} else {

				// pull the MapTile array out of the ScanMap object
				MapTile[][] scanMapTiles = scanMap.getScanMap();
				int centerIndex = (scanMap.getEdgeSize() - 1)/2;
				// tile S = y + 1; N = y - 1; E = x + 1; W = x - 1

				if (goingSouth) {
					// check scanMap to see if path is blocked to the south
					// (scanMap may be old data by now)
					if (scanMapTiles[centerIndex][centerIndex +1].getHasRover() 
							|| scanMapTiles[centerIndex][centerIndex +1].getTerrain() == Terrain.ROCK
							|| scanMapTiles[centerIndex][centerIndex +1].getTerrain() == Terrain.NONE) {
						blocked = true;
					} else {
						// request to server to move
						out.println("MOVE S");
						//System.out.println("ROVER_15 request move S");
					}

				} else {
					// check scanMap to see if path is blocked to the north
					// (scanMap may be old data by now)
					//System.out.println("ROVER_15 scanMapTiles[2][1].getHasRover() " + scanMapTiles[2][1].getHasRover());
					//System.out.println("ROVER_15 scanMapTiles[2][1].getTerrain() " + scanMapTiles[2][1].getTerrain().toString());

					if (scanMapTiles[centerIndex][centerIndex -1].getHasRover() 
							|| scanMapTiles[centerIndex][centerIndex -1].getTerrain() == Terrain.ROCK
							|| scanMapTiles[centerIndex][centerIndex -1].getTerrain() == Terrain.NONE) {
						blocked = true;
					} else {
						// request to server to move
						out.println("MOVE N");
						//System.out.println("ROVER_15 request move N");
					}					
||||||| merged common ancestors
				blocked = false;
				//reverses direction after being blocked
				goingSouth = !goingSouth;
			} else {

				// pull the MapTile array out of the ScanMap object
				MapTile[][] scanMapTiles = scanMap.getScanMap();
				int centerIndex = (scanMap.getEdgeSize() - 1)/2;
				// tile S = y + 1; N = y - 1; E = x + 1; W = x - 1

				if (goingSouth) {
					// check scanMap to see if path is blocked to the south
					// (scanMap may be old data by now)
					if (scanMapTiles[centerIndex][centerIndex +1].getHasRover() 
							|| scanMapTiles[centerIndex][centerIndex +1].getTerrain() == Terrain.ROCK
							|| scanMapTiles[centerIndex][centerIndex +1].getTerrain() == Terrain.NONE) {
						blocked = true;
					} else {
						// request to server to move
						out.println("MOVE S");
						//System.out.println("ROVER_15 request move S");
					}
					
				} else {
					// check scanMap to see if path is blocked to the north
					// (scanMap may be old data by now)
					//System.out.println("ROVER_15 scanMapTiles[2][1].getHasRover() " + scanMapTiles[2][1].getHasRover());
					//System.out.println("ROVER_15 scanMapTiles[2][1].getTerrain() " + scanMapTiles[2][1].getTerrain().toString());
					
					if (scanMapTiles[centerIndex][centerIndex -1].getHasRover() 
							|| scanMapTiles[centerIndex][centerIndex -1].getTerrain() == Terrain.ROCK
							|| scanMapTiles[centerIndex][centerIndex -1].getTerrain() == Terrain.NONE) {
						blocked = true;
					} else {
						// request to server to move
						out.println("MOVE N");
						//System.out.println("ROVER_15 request move N");
					}					
=======
				else{
					counter += 1;
					out.println("MOVE "+cardinals[currentDir]);
					blocked = false;
>>>>>>> origin/master
				}
				
			}
			

			// another call for current location
			out.println("LOC");
			line = in.readLine();
			if (line.startsWith("LOC")) {
				currentLoc = extractLOC(line);
			}

			System.out.println("ROVER_15 currentLoc after recheck: " + currentLoc);
			System.out.println("ROVER_15 previousLoc: " + previousLoc);

			// test for stuckness
			stuck = currentLoc.equals(previousLoc);

			System.out.println("ROVER_15 stuck test " + stuck);
			System.out.println("ROVER_15 blocked test " + blocked);

<<<<<<< HEAD
			// TODO - logic to calculate where to move next



||||||| merged common ancestors
			// TODO - logic to calculate where to move next

			
			
=======
			
>>>>>>> origin/master
			Thread.sleep(sleepTime);

			System.out.println("ROVER_15 ------------ bottom process control --------------"); 

		}

	}
<<<<<<< HEAD

||||||| merged common ancestors
	
=======

	private List<Integer> getDirectionsToTargetLocation() throws IOException {
		String line = "";
		List<Integer> possibleDirections = new ArrayList<Integer>();
		out.println("LOC");
		line = in.readLine();
		Coord currentLocation = extractLOC(line);
		out.println("TARGET_LOC");
		line = in.readLine();
		Coord targetLocation = extractTargetLOC(line);
		
		if(currentLocation.xpos < targetLocation.xpos){
			possibleDirections.add(1);
		}
		else if(currentLocation.xpos > targetLocation.xpos){
			possibleDirections.add(3);
		}
		if(currentLocation.ypos < targetLocation.ypos){
			possibleDirections.add(2);
		}
		else if(currentLocation.xpos > targetLocation.ypos){
			possibleDirections.add(0);
		}
		
		return possibleDirections;
	}

	private boolean roverStuckIncurrentDir(int currentDir, MapTile[][] scanMapTiles, int centerIndex) {
		boolean returnValue = false;
		switch (cardinals[currentDir]) {
		case "N":
			
			if(scanMapTiles[centerIndex][centerIndex - 1].getHasRover() || scanMapTiles[centerIndex][centerIndex - 1].getTerrain() == Terrain.ROCK
					|| scanMapTiles[centerIndex][centerIndex - 1].getTerrain() == Terrain.NONE )
			{
				returnValue = true;
			}
			break;
		case "E":
			if(scanMapTiles[centerIndex + 1][centerIndex].getHasRover() || scanMapTiles[centerIndex + 1][centerIndex].getTerrain() == Terrain.ROCK
					|| scanMapTiles[centerIndex + 1][centerIndex].getTerrain() == Terrain.NONE)
			{
				returnValue = true;
			}
			break; 
		case "S":
			if(scanMapTiles[centerIndex][centerIndex + 1].getHasRover() || scanMapTiles[centerIndex][centerIndex + 1].getTerrain() == Terrain.ROCK
					|| scanMapTiles[centerIndex][centerIndex + 1].getTerrain() == Terrain.NONE)
			{
				returnValue = true;
			}
			break; 
		case "W":
			if(scanMapTiles[centerIndex - 1][centerIndex].getHasRover() || scanMapTiles[centerIndex - 1][centerIndex].getTerrain() == Terrain.ROCK
					|| scanMapTiles[centerIndex - 1][centerIndex].getTerrain() == Terrain.NONE)
			{
				returnValue = true;
			}
			break;
		
		}
		
		return returnValue;
	}

>>>>>>> origin/master
	// ################ Support Methods ###########################
<<<<<<< HEAD

||||||| merged common ancestors
	
=======
	
	public int getRandomDirection(int current) {
		Random r = new Random();
		int Low = 0;
		int High = 4;
		int Result = current;
		goingNESW[current] = false;
		
		while (current == Result){
			Result = r.nextInt(High-Low) + Low;
		}
		goingNESW[Result] = true;
		return Result;
	}
	
	public int getRandomDirection(int current, List<Integer> allowedDirections  ) {
		Random r = new Random();
		int Low = 0;
		int High = 4;
		int Result = current;
		goingNESW[current] = false;
		
		while ((! allowedDirections.contains(Result)) || Result == current){
			Result = r.nextInt(High-Low) + Low;
		}
		goingNESW[Result] = true;
		return Result;
	}

>>>>>>> origin/master
	private void clearReadLineBuffer() throws IOException{
		while(in.ready()){
			//System.out.println("ROVER_15 clearing readLine()");
			String garbage = in.readLine();	
		}
	}


	// method to retrieve a list of the rover's equipment from the server
	private ArrayList<String> getEquipment() throws IOException {
		//System.out.println("ROVER_15 method getEquipment()");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		out.println("EQUIPMENT");

		String jsonEqListIn = in.readLine(); //grabs the string that was returned first
		if(jsonEqListIn == null){
			jsonEqListIn = "";
		}
		StringBuilder jsonEqList = new StringBuilder();
		//System.out.println("ROVER_15 incomming EQUIPMENT result - first readline: " + jsonEqListIn);

		if(jsonEqListIn.startsWith("EQUIPMENT")){
			while (!(jsonEqListIn = in.readLine()).equals("EQUIPMENT_END")) {
				if(jsonEqListIn == null){
					break;
				}
				//System.out.println("ROVER_15 incomming EQUIPMENT result: " + jsonEqListIn);
				jsonEqList.append(jsonEqListIn);
				jsonEqList.append("\n");
				//System.out.println("ROVER_15 doScan() bottom of while");
			}
		} else {
			// in case the server call gives unexpected results
			clearReadLineBuffer();
			return null; // server response did not start with "EQUIPMENT"
		}

		String jsonEqListString = jsonEqList.toString();		
		ArrayList<String> returnList;		
		returnList = gson.fromJson(jsonEqListString, new TypeToken<ArrayList<String>>(){}.getType());		
		//System.out.println("ROVER_15 returnList " + returnList);

		return returnList;
	}


	// sends a SCAN request to the server and puts the result in the scanMap array
	public void doScan() throws IOException {
		//System.out.println("ROVER_15 method doScan()");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		out.println("SCAN");

		String jsonScanMapIn = in.readLine(); //grabs the string that was returned first
		if(jsonScanMapIn == null){
			System.out.println("ROVER_15 check connection to server");
			jsonScanMapIn = "";
		}
		StringBuilder jsonScanMap = new StringBuilder();
		System.out.println("ROVER_15 incomming SCAN result - first readline: " + jsonScanMapIn);

		if(jsonScanMapIn.startsWith("SCAN")){	
			while (!(jsonScanMapIn = in.readLine()).equals("SCAN_END")) {
				//System.out.println("ROVER_15 incomming SCAN result: " + jsonScanMapIn);
				jsonScanMap.append(jsonScanMapIn);
				jsonScanMap.append("\n");
				//System.out.println("ROVER_15 doScan() bottom of while");
			}
		} else {
			// in case the server call gives unexpected results
			clearReadLineBuffer();
			return; // server response did not start with "SCAN"
		}
		//System.out.println("ROVER_15 finished scan while");

		String jsonScanMapString = jsonScanMap.toString();
		// debug print json object to a file
		//new MyWriter( jsonScanMapString, 0);  //gives a strange result - prints the \n instead of newline character in the file

		//System.out.println("ROVER_15 convert from json back to ScanMap class");
		// convert from the json string back to a ScanMap object
		scanMap = gson.fromJson(jsonScanMapString, ScanMap.class);		
	}


	
	
	// this takes the LOC response string, parses out the x and x values and
	// returns a Coord object
	public static Coord extractLOC(String sStr) {
		sStr = sStr.substring(4);
		System.out.println("WHAT ARE YOU?" + sStr);
		if (sStr.lastIndexOf(" ") != -1) {
			String xStr = sStr.substring(0, sStr.lastIndexOf(" "));
			//System.out.println("extracted xStr " + xStr);

			String yStr = sStr.substring(sStr.lastIndexOf(" ") + 1);
			//System.out.println("extracted yStr " + yStr);
			return new Coord(Integer.parseInt(xStr), Integer.parseInt(yStr));
		}
		return null;
	}
	
<<<<<<< HEAD
	// this takes the TARGET_LOC response string, parses out the x and x values and
	// returns a Coord object
	public static Coord extractTARGET_LOC(String tsStr) {
		tsStr = tsStr.substring(4);
		if (tsStr.lastIndexOf(" ") != -1) {
			String txStr = tsStr.substring(0, tsStr.lastIndexOf(" "));
			//System.out.println("extracted txStr " + txStr);

			String tyStr = tsStr.substring(tsStr.lastIndexOf(" ") + 1);
			//System.out.println("extracted tyStr " + tyStr);
			return new Coord(Integer.parseInt(txStr), Integer.parseInt(tyStr));
		}
		return null;
	}
	

	// one of the motion dictating method (will be moved and adjusted to the appropriate location)
	public void zigzagMotion(double[][] dct,
			int block_size, int channel){

		double[][] temp_dct = new double[block_size][block_size];

		for ( int i = 0; i < dct.length; i += 8 ) {
			for ( int j = 0; j < dct[i].length; j += 8 ) {

				for ( int i1 = 0; i1 < dct.length; i1++ ) {
					for ( int j1 = 0; j1 < dct[i1].length; j1++ ) {
						temp_dct[i1][j1] = dct[i][j];
					}
				}

				//				for ( CodeRunLengthPair p : temp_i_rep ) {
				//					intermediate_rep.add( p );
				//				}
			}
		}
	}
||||||| merged common ancestors
	// one of the motion dictating method (will be moved and adjusted to the appropriate location)
	public void zigzagMotion(double[][] dct,
			int block_size, int channel){

		double[][] temp_dct = new double[block_size][block_size];

		for ( int i = 0; i < dct.length; i += 8 ) {
			for ( int j = 0; j < dct[i].length; j += 8 ) {

				for ( int i1 = 0; i1 < dct.length; i1++ ) {
					for ( int j1 = 0; j1 < dct[i1].length; j1++ ) {
						temp_dct[i1][j1] = dct[i][j];
					}
				}

//				for ( CodeRunLengthPair p : temp_i_rep ) {
//					intermediate_rep.add( p );
//				}
			}
		}
	}
=======
	public static Coord extractTargetLOC(String sStr) {
        sStr = sStr.substring(11);
        if (sStr.lastIndexOf(" ") != -1) {
            String xStr = sStr.substring(0, sStr.lastIndexOf(" "));
            // System.out.println("extracted xStr " + xStr);

            String yStr = sStr.substring(sStr.lastIndexOf(" ") + 1);
            // System.out.println("extracted yStr " + yStr);
            return new Coord(Integer.parseInt(xStr), Integer.parseInt(yStr));
        }
        return null;
    }
	
	
>>>>>>> origin/master

	/**
	 * Runs the client
	 */
	public static void main(String[] args) throws Exception {
		ROVER_15 client = new ROVER_15();
		client.run();
	}
}
package swarmBots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.Communication;
import common.Coord;
import common.MapTile;
import common.ScanMap;
import enums.Terrain;


//Making an addition to this file to check whether a remote alternat push will change it

// rearanged the 2nd and 3rd line in the following comment

/**
<<<<<<< HEAD
 * The seed that this program is built on is a chat program example found here:
 * publishing their code examples
 * * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
 */
||||||| merged common ancestors
* The seed that this program is built on is a chat program example found here:
* publishing their code examples
* * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
*/
=======
* The seed that this program is built on is a chat program example found here:
* publishing their code examples
*
* * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
*/
>>>>>>> 9309b415026705a958e52dd74ea3e998c0b0ebc5

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



		boolean stuck = false; // just means it did not change locations between requests,
		// could be velocity limit or obstruction etc.
		boolean blocked = false;
		boolean blocked_byNothing = false;


		int currentDir = 3;
		Coord currentLoc = null;
		Coord previousLoc = null;

		Queue<Coord> targets = new LinkedList<>();

		/* Commenting out so rover goes to jackpot first
		 * TODO: Test why rover gets stuck approaching Coord(1,1) 
		 * 
		 * Coord firstTL = new Coord(15,36);
		 * Coord secondTL = new Coord(16,20);
		 * Coord thirdTL = new Coord(20,4);
		 * Coord fourthTL = new Coord(50,5);
		 * 
		 * targets.add(firstTL);
		 * targets.add(secondTL);
		 * targets.add(thirdTL);
		 * targets.add(fourthTL); */


		// start Rover controller process
		while (true) {

			// currently the requirements allow sensor calls to be made with no
			// simulated resource cost


			// **** location call ****
			out.println("LOC");
			line = in.readLine();
			if (line == null) {
				System.out.println("ROVER_15 check connection to server");
				line = "";
			}
			if (line.startsWith("LOC")) {
				// loc = line.substring(4);
				currentLoc = extractLOC(line);
			}
			System.out.println("ROVER_15 currentLoc at start: " + currentLoc);

			// after getting location set previous equal current to be able to check for stuckness and blocked later
			previousLoc = currentLoc;



			// **** get equipment listing ****			
			ArrayList<String> equipment = new ArrayList<String>();
			equipment = getEquipment();
			//System.out.println("ROVER_15 equipment list results drive " + equipment.get(0));
			System.out.println("ROVER_15 equipment list results " + equipment + "\n");



			// ***** do a SCAN *****
			//System.out.println("ROVER_15 sending SCAN request");
			this.doScan();
			scanMap.debugPrintMap();



			//***** Communication *****
			String url = "http://23.251.155.186:3000/api/global";
			Communication com = new Communication(url);



			//***** Driller Moving Logic *****
			out.println("TARGET_LOC");
			line = in.readLine();
			// pull the MapTile array out of the ScanMap object
			MapTile[][] scanMapTiles = scanMap.getScanMap();
			int centerIndex = (scanMap.getEdgeSize() - 1)/2;
			// tile S = y + 1; N = y - 1; E = x + 1; W = x - 1


			// posting what we see to the global map for all other rovers to see
			com.postScanMapTiles(currentLoc, scanMapTiles);

			/* TODO: Grab science Coord from global map and add to targets queue.
			 * Make sure to exclude science in Tiles where TERRAIN = ROCK;
			 */


			if(blocked){
				if(roverStuckIncurrentDir(currentDir,scanMapTiles,centerIndex)){
					currentDir = getRandomDirection(currentDir);
				}
				else if(counter % 25 == 0){
					currentDir = getRandomDirection(currentDir);
				}

			}
			if(blocked_byNothing){
				List<Integer> allowedDirections = getDirectionsToTargetLocation(targets);
				currentDir = getRandomDirection(currentDir,allowedDirections);

			}
			counter -= 1;
			if(counter < 5){
				blocked_byNothing = true;
				blocked = false;
			}
			if(roverStuckIncurrentDir(currentDir,scanMapTiles,centerIndex)){
				blocked = true;
				blocked_byNothing = false;
				counter = 50;
			}
			if( ! roverStuckIncurrentDir(currentDir,scanMapTiles,centerIndex)){
				out.println("MOVE "+cardinals[currentDir]);
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


			Thread.sleep(sleepTime);

			System.out.println("ROVER_15 ------------ bottom process control --------------"); 

		}

	}

	private List<Integer> getDirectionsToTargetLocation(Queue<Coord> targets) throws IOException {
		String line = "";
		List<Integer> possibleDirections = new ArrayList<Integer>();
		out.println("LOC");
		line = in.readLine();
		Coord currentLocation = extractLOC(line);
		Coord targetLocation = null;

		out.println("TARGET_LOC");
		line = in.readLine();
		Coord jackpotLocation = extractTargetLOC(line);


		// if there are science locations in target queue, get closest one
		if(!targets.isEmpty()){
			//			targetLocation = targets.element();
			targetLocation = getClosestTarget(currentLocation, targets);
			System.out.println("Current target = " + targetLocation);
		}
		// else go to the jackpot location 
		else{
			targets.add(jackpotLocation);
			targetLocation = targets.element();

			//TODO: Implement zig zag pattern to GATHER from all tiles within jackpot
		}

		// now our Rover Would have reached the target location by this line.
		if((currentLocation.ypos == targetLocation.ypos) && (currentLocation.xpos == targetLocation.xpos)){
			// collect science.
			out.println("GATHER");						
			targetLocation = targets.poll();
		}
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

	// makes rover pursue closest target first
	private Coord getClosestTarget(Coord currentLocation, Queue<Coord> targets) {
		double closestTarget = Double.MAX_VALUE;
		Coord closestCoord = null;

		for (Coord t : targets){
			double tXpos = t.xpos;
			double tYpos = t.ypos;
			double cXpos = currentLocation.xpos;
			double cYpos = currentLocation.ypos;

			double distance = distanceFormula(tXpos, tYpos, cXpos, cYpos);
			if (distance < closestTarget){
				closestTarget = distance;
				closestCoord = t;
			}
		}
		return closestCoord;
	}

	private double distanceFormula(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow((x2-x1),2)+Math.pow((y2-y1),2));
	}

	public static int getRandom(int max){ 
		return (int) (Math.random()*max);
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

	// ################ Support Methods ###########################

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

		while ((! allowedDirections.contains(Result))){// || Result == current){
			Result = r.nextInt(High-Low) + Low;
		}
		goingNESW[Result] = true;
		return Result;
	}

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
				//System.out.println("ROVER_15 incoming SCAN result: " + jsonScanMapIn);
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
		if (sStr.lastIndexOf(" ") != -1) {
			String xStr = sStr.substring(0, sStr.lastIndexOf(" "));
			//System.out.println("extracted xStr " + xStr);

			String yStr = sStr.substring(sStr.lastIndexOf(" ") + 1);
			//System.out.println("extracted yStr " + yStr);
			return new Coord(Integer.parseInt(xStr), Integer.parseInt(yStr));
		}
		return null;
	}

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



	/**
	 * Runs the client
	 */
	public static void main(String[] args) throws Exception {
		ROVER_15 client = new ROVER_15();
		client.run();
	}
}
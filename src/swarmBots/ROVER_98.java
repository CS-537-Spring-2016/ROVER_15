package swarmBots;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import common.Coord;
import common.MapTile;
import common.ScanMap;
import enums.Terrain;

/**
 * The seed that this program is built on is a chat program example found here:
 * http://cs.lmu.edu/~ray/notes/javanetexamples/ Many thanks to the authors for
 * publishing their code examples
 */

public class ROVER_98 {

	BufferedReader in;
	PrintWriter out;
	String rovername;
	ScanMap scanMap;
	int sleepTime;
	String SERVER_ADDRESS = "localhost";
	static final int PORT_ADDRESS = 9537;

	public ROVER_98() {
		// constructor
		System.out.println("ROVER_98 rover object constructed");
		rovername = "ROVER_98";
		SERVER_ADDRESS = "localhost";
		// this should be a safe but slow timer value
		sleepTime = 300; // in milliseconds - smaller is faster, but the server will cut connection if it is too small
	}
	
	public ROVER_98(String serverAddress) {
		// constructor
		System.out.println("ROVER_98 rover object constructed");
		rovername = "ROVER_98";
		SERVER_ADDRESS = serverAddress;
		sleepTime = 200; // in milliseconds - smaller is faster, but the server will cut connection if it is too small
	}

	/**
	 * Connects to the server then enters the processing loop.
	 */
	private void run() throws IOException, InterruptedException {

		// Make connection to SwarmServer and initialize streams
		Socket socket = null;
		try {
			socket = new Socket(SERVER_ADDRESS, PORT_ADDRESS);

			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
	
			// Process all messages from server, wait until server requests Rover ID
			// name - Return Rover Name to complete connection
			while (true) {
				String line = in.readLine();
				if (line.startsWith("SUBMITNAME")) {
					out.println(rovername); // This sets the name of this instance
											// of a swarmBot for identifying the
											// thread to the server
					break;
				}
			}
	
			
			// ********* Rover logic setup *********
			
			String line = "";
			Coord rovergroupStartPosition = null;
			Coord targetLocation = null;
			
			/**
			 *  Get initial values that won't change
			 */
			// **** get equipment listing ****			
			ArrayList<String> equipment = new ArrayList<String>();
			equipment = getEquipment();
			System.out.println(rovername + " equipment list results " + equipment + "\n");
			
			
			// **** Request START_LOC Location from SwarmServer ****
			out.println("START_LOC");
			line = in.readLine();
            if (line == null) {
            	System.out.println(rovername + " check connection to server");
            	line = "";
            }
			if (line.startsWith("START_LOC")) {
				rovergroupStartPosition = extractLocationFromString(line);
			}
			System.out.println(rovername + " START_LOC " + rovergroupStartPosition);
			
			
			// **** Request TARGET_LOC Location from SwarmServer ****
			out.println("TARGET_LOC");
			line = in.readLine();
            if (line == null) {
            	System.out.println(rovername + " check connection to server");
            	line = "";
            }
			if (line.startsWith("TARGET_LOC")) {
				targetLocation = extractLocationFromString(line);
			}
			System.out.println(rovername + " TARGET_LOC " + targetLocation);
			
			

			
	
	
			boolean goingSouth = false;
			boolean goingEast = true;
			boolean stuck = false; // just means it did not change locations between requests,
									// could be velocity limit or obstruction etc.
			boolean blocked = false;
	
			String[] cardinals = new String[4];
			cardinals[0] = "N";
			cardinals[1] = "E";
			cardinals[2] = "S";
			cardinals[3] = "W";
	
			String currentDir = cardinals[0];
			Coord currentLoc = null;
			Coord previousLoc = null;
	

			/**
			 *  ####  Rover controller process loop  ####
			 */
			while (true) {
	
				
				// **** Request Rover Location from SwarmServer ****
				out.println("LOC");
				line = in.readLine();
	            if (line == null) {
	            	System.out.println(rovername + " check connection to server");
	            	line = "";
	            }
				if (line.startsWith("LOC")) {
					// loc = line.substring(4);
					currentLoc = extractLocationFromString(line);
					
				}
				System.out.println(rovername + " currentLoc at start: " + currentLoc);
				
				// after getting location set previous equal current to be able to check for stuckness and blocked later
				previousLoc = currentLoc;		
				
				

				
		
	
				// ***** do a SCAN *****

				// gets the scanMap from the server based on the Rover current location
				doScan();
				// prints the scanMap to the Console output for debug purposes
				scanMap.debugPrintMap();
				
		
				
				

				
	
				
				// ***** MOVING *****
				// try moving east 5 block if blocked
				
	
					// pull the MapTile array out of the ScanMap object
					MapTile[][] scanMapTiles = scanMap.getScanMap();
					int centerIndex = (scanMap.getEdgeSize() - 1)/2;
					// tile S = y + 1; N = y - 1; E = x + 1; W = x - 1
					
	
					if (goingEast) {
						// Checks to see if there is science on current tile, if not it moves East
						System.out.println("ROVER_98: scanMapTiles[centerIndex][centerIndex].getScience().getSciString() " + scanMapTiles[centerIndex][centerIndex].getScience().getSciString());
						if (!scanMapTiles[centerIndex][centerIndex].getScience().getSciString().equals("N")) {
							System.out.println("ROVER_98 request GATHER");
							out.println("GATHER");
							
						} else {
							// request to server to move
							out.println("MOVE E");
							System.out.println("ROVER_98 request move E");
						}
						
					} 			

				
	
				// another call for current location
				out.println("LOC");
				line = in.readLine();
				if(line == null){
					System.out.println("ROVER_98 check connection to server");
					line = "";
				}
				if (line.startsWith("LOC")) {
					currentLoc = extractLocationFromString(line);
					
				}
	
	
				// test for stuckness
				stuck = currentLoc.equals(previousLoc);
	
				//System.out.println("ROVER_98 stuck test " + stuck);
				System.out.println("ROVER_98 blocked test " + blocked);
	
				// TODO - logic to calculate where to move next
	
				
				// this is the Rovers HeartBeat, it regulates how fast the Rover cycles through the control loop
				Thread.sleep(sleepTime);
				
				System.out.println("ROVER_98 ------------ bottom process control --------------"); 
			}
		
		// This catch block closes the open socket connection to the server
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        if (socket != null) {
	            try {
	            	socket.close();
	            } catch (IOException e) {
	            	System.out.println("ROVER_98 problem closing socket");
	            }
	        }
	    }

	} // END of Rover main control loop
	
	// ####################### Support Methods #############################
	
	private void clearReadLineBuffer() throws IOException{
		while(in.ready()){
			//System.out.println("ROVER_98 clearing readLine()");
			in.readLine();	
		}
	}
	

	// method to retrieve a list of the rover's EQUIPMENT from the server
	private ArrayList<String> getEquipment() throws IOException {
		//System.out.println("ROVER_98 method getEquipment()");
		Gson gson = new GsonBuilder()
    			.setPrettyPrinting()
    			.enableComplexMapKeySerialization()
    			.create();
		out.println("EQUIPMENT");
		
		String jsonEqListIn = in.readLine(); //grabs the string that was returned first
		if(jsonEqListIn == null){
			jsonEqListIn = "";
		}
		StringBuilder jsonEqList = new StringBuilder();
		//System.out.println("ROVER_98 incomming EQUIPMENT result - first readline: " + jsonEqListIn);
		
		if(jsonEqListIn.startsWith("EQUIPMENT")){
			while (!(jsonEqListIn = in.readLine()).equals("EQUIPMENT_END")) {
				if(jsonEqListIn == null){
					break;
				}
				//System.out.println("ROVER_98 incomming EQUIPMENT result: " + jsonEqListIn);
				jsonEqList.append(jsonEqListIn);
				jsonEqList.append("\n");
				//System.out.println("ROVER_98 doScan() bottom of while");
			}
		} else {
			// in case the server call gives unexpected results
			clearReadLineBuffer();
			return null; // server response did not start with "EQUIPMENT"
		}
		
		String jsonEqListString = jsonEqList.toString();		
		ArrayList<String> returnList;		
		returnList = gson.fromJson(jsonEqListString, new TypeToken<ArrayList<String>>(){}.getType());		
		//System.out.println("ROVER_98 returnList " + returnList);
		
		return returnList;
	}
	

	// sends a SCAN request to the server and puts the result in the scanMap array
	public void doScan() throws IOException {
		//System.out.println("ROVER_98 method doScan()");
		Gson gson = new GsonBuilder()
    			.setPrettyPrinting()
    			.enableComplexMapKeySerialization()
    			.create();
		out.println("SCAN");

		String jsonScanMapIn = in.readLine(); //grabs the string that was returned first
		if(jsonScanMapIn == null){
			System.out.println("ROVER_98 check connection to server");
			jsonScanMapIn = "";
		}
		StringBuilder jsonScanMap = new StringBuilder();
		System.out.println("ROVER_98 incomming SCAN result - first readline: " + jsonScanMapIn);
		
		if(jsonScanMapIn.startsWith("SCAN")){	
			while (!(jsonScanMapIn = in.readLine()).equals("SCAN_END")) {
				//System.out.println("ROVER_98 incomming SCAN result: " + jsonScanMapIn);
				jsonScanMap.append(jsonScanMapIn);
				jsonScanMap.append("\n");
				//System.out.println("ROVER_98 doScan() bottom of while");
			}
		} else {
			// in case the server call gives unexpected results
			clearReadLineBuffer();
			return; // server response did not start with "SCAN"
		}
		//System.out.println("ROVER_98 finished scan while");

		String jsonScanMapString = jsonScanMap.toString();
		// debug print json object to a file
		//new MyWriter( jsonScanMapString, 0);  //gives a strange result - prints the \n instead of newline character in the file

		//System.out.println("ROVER_98 convert from json back to ScanMap class");
		// convert from the json string back to a ScanMap object
		scanMap = gson.fromJson(jsonScanMapString, ScanMap.class);		
	}
	

	// this takes the server response string, parses out the x and x values and
	// returns a Coord object	
	public static Coord extractLocationFromString(String sStr) {
		int indexOf;
		indexOf = sStr.indexOf(" ");
		sStr = sStr.substring(indexOf +1);
		if (sStr.lastIndexOf(" ") != -1) {
			String xStr = sStr.substring(0, sStr.lastIndexOf(" "));
			//System.out.println("extracted xStr " + xStr);

			String yStr = sStr.substring(sStr.lastIndexOf(" ") + 1);
			//System.out.println("extracted yStr " + yStr);
			return new Coord(Integer.parseInt(xStr), Integer.parseInt(yStr));
		}
		return null;
	}
	

	/**
	 * Runs the client
	 */
	public static void main(String[] args) throws Exception {
		ROVER_98 client = new ROVER_98();
		client.run();
	}
}
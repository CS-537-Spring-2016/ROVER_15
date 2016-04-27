package controlServer;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Coord;
import common.MapTile;
import common.PlanetMap;
import common.Rover;
import common.RoverLocations;
import common.ScanMap;
import common.ScienceLocations;
import enums.RoverDriveType;
import enums.RoverName;
import enums.RoverToolType;
import enums.Science;
import enums.Terrain;
import supportTools.SwarmMapInit;


/**
 * The seed that this program is built on is a chat program example found here:
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * Many thanks to the authors for publishing their code examples
 */

public class SwarmServer {

    /**
     * The port that the server listens on.
     */
    private static final int PORT = 9537; // because ... csula class number
    
    private static SwarmMapInit mapInit = new SwarmMapInit();
    
    private static String mapFileName = "MapDefault.txt";

    private static int mapWidth = 0;
    private static int mapHeight = 0;
    private static PlanetMap planetMap = null; // = new PlanetMap(mapWidth, mapHeight); 
    private static RoverLocations roverLocations = new RoverLocations();
    private static ScienceLocations scienceLocations = new ScienceLocations();
    private static ArrayList<Science> collectedScience_0 = new ArrayList<Science>();
    private static ArrayList<Science> collectedScience_1 = new ArrayList<Science>();
    private static ArrayList<Science> collectedScience_2 = new ArrayList<Science>();
    private static ArrayList<ArrayList<Science>> corpCollectedScience = new ArrayList<ArrayList<Science>>();
    
    private static long countdownTimer;
    private static boolean roversAreGO;
    
	static GUIdisplay mainPanel;
	static MyGUIWorker myWorker;
	
    static GUIdisplay2 mainPanel2;
	static MyGUIWorker2 myWorker2;
	
	static GUIdisplay3 mainPanel3;
	static MyGUIWorker3 myWorker3;
    
	// Length of time allowed for the rovers to get back to the retrieval zone
	static final int MAXIMUM_ACTIVITY_TIME_LIMIT = 600000; // 10 Minutes = 600,000
	static Timer countDownTimer;
	static long startTime;
	
	// These are the velocity or speed values for the different drive systems
	// Changes these as necessary for good simulation balance
    static final int WHEELS_TIME_PER_SQUARE = 500;
    static final int TREADS_TIME_PER_SQUARE = 1000;
    static final int WALKER_TIME_PER_SQUARE = 1200;
    
    // limit of how many Calls can be made to the swarm server during a 1 second span
    static final int CALLS_PER_SECOND_LIMIT = 500;
    
    // minimum time in milliseconds that has to pass before another Gather can be done
    static final long GATHER_TIME_PER_TILE = 2500;
    
 // length of a side of the scan map array !!! must be odd number !!!
    static final int STANDARD_SCANMAP_RANGE = 7;
    static final int BOOSTED_SCANMAP_RANGE = 11; // range extender increased to 11 by popular demand
    
    /**
     * The application main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
    	ActionListener timeLimitListener = new TimeLimitStop();
    	countDownTimer = new Timer(MAXIMUM_ACTIVITY_TIME_LIMIT, timeLimitListener); 
    	countDownTimer.start();
    	startTime = System.currentTimeMillis();
		
		roversAreGO = true;
		
    	// if a command line argument is included it is used as the map filename
    	for (String s: args){
    		mapFileName = s;
    	}
        System.out.println("The Swarm server is running.");
        ServerSocket listener = new ServerSocket(PORT);
        
        corpCollectedScience.add(collectedScience_0);
        corpCollectedScience.add(collectedScience_1);
        corpCollectedScience.add(collectedScience_2);
        
        mapInit.parseInputFromDisplayTextFile(mapFileName);        
        
        mapHeight = mapInit.getMapHeight();
        mapWidth = mapInit.getMapWidth();
        planetMap = mapInit.getPlanetMap();
        roverLocations = mapInit.getRoverLocations();
        scienceLocations = mapInit.getScienceLocations();
        
        countdownTimer = System.currentTimeMillis();
        
		mainPanel = new GUIdisplay(mapWidth, mapHeight);
		myWorker = new MyGUIWorker(mainPanel);
        
       
		mainPanel2 = new GUIdisplay2(mapWidth, mapHeight);
		myWorker2 = new MyGUIWorker2(mainPanel2);
		
		mainPanel3 = new GUIdisplay3(mapWidth, mapHeight, MAXIMUM_ACTIVITY_TIME_LIMIT);
		myWorker3 = new MyGUIWorker3(mainPanel3);
		
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// currently sending it when calling the updateGUIDisplay() method
//**			GUIdisplay.createAndShowGui(myWorker, mainPanel);
				//GUIdisplay2.createAndShowGui(myWorker2, mainPanel2);
				GUIdisplay3.createAndShowGui(myWorker3, mainPanel3);
				try {
					updateGUIDisplay();
				} catch (Exception e) {				
					e.printStackTrace();
				}
			}
		});
		       
        try {
            while (true) {
                new Handler(listener.accept()).start();
            }
        } finally {
            listener.close();
        }         
    }

    /**
     * A handler thread class.  Handlers are spawned from the listening
     * loop and are responsible for a dealing with a single client
     * and processing its messages.
     */
    private static class Handler extends Thread {
    	//currently using these as the Rover Object attributes
    	// this should be replaced with a Rover Object
        private String roverNameString;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        
        // keeps track of Rover's current location
        private int xpos = 0;
        private int ypos = 0;
        

        /**
         * Constructs a handler thread, squirreling away the socket.
         * All the interesting work is done in the run method.
         */
        public Handler(Socket socket) {
            this.socket = socket;
        }

        /**
         * Services this thread's client by repeatedly requesting a Rover nameType
         * Then runs the Rover environment simulator server process        
         */
        public void run() {
            try {
                // Create character streams for the socket.
                in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
         

                // Request a name from this client.  Keep requesting until returned.             
                while (true) {
                    out.println("SUBMITNAME");
                    roverNameString = in.readLine();
                    System.out.println(roverNameString + " has connected to server");
                    if (roverNameString == null) {
                        return;
                    } else {
                    	break;
                    }
                }
                
                // TODO check to see if this rover thread already exists.
                // if exists and is active - refuse connection
                // if exists and socket is not active - reconnect to that socket
                // enforce time limit between reconnection to minimize spamming
                
                // make and instantiate a Rover object connected to this thread
                RoverName rname = RoverName.getEnum(roverNameString); 
                //System.out.println("SWARM: make a rover name " + rname);
                Rover rover = new Rover(rname);
                
                
                
                // ##### Run the Rover server process #####
                while (roversAreGO) {	
                	//read command input from the Rover
                    String input = in.readLine();

                    //condition the input to empty string if null
                    if (input == null) {
                    	input = "";
                    }
                    
                    // check requests per second
                    // if rover is too greedy drop their connection - checks how many total requests have been made in the last second
                    long roverServerRequestsPerSecond = rover.getRoverRequestCount();
                   	
                    if(roverServerRequestsPerSecond > CALLS_PER_SECOND_LIMIT){
                    	System.out.println("SWARM_"+roverNameString+ "_thread: too many requests per second - dropping connection");
                    	in.close();
                    	socket.close();
                    }
                    
                    // debug checking
                    //System.out.println("SWARM_SERVER_"+roverNameString+ "_thread: recieved command " + input);
                    
                      
                    
                    /**
                	 * ******************** MOVE **********************
                	 */                   
                    if (input.startsWith("MOVE")){
                    	//System.out.println("SWARM: ------ MOVE ------"); //debug test input parsing
                    	// trim header off of input string
                    	String dir = input.substring(5);	
                    	
                    	// invoke the doMove method to update the Rover position in the RoverLocations (roverLocations) static object
                    	// this method also returns a Coord with the Rover position after the move attempt.
                    	doMove(rover, dir); 
                    	
                    	// Update the GUI display with all the new rover locations when any individual rover moves
            	    	updateGUIDisplay();
                    	
            	    	
                    
        	    	/**
                	 * ******************** LOC **********************
                	 */
                    // gets the current position of the rover	
                    } else if (input.startsWith("LOC")){  
                    	//System.out.println("SWARM: ------ LOC ------"); //debug test input parsing
                    	// does not need to synchronize-lock scienceLocations because not changing any values
            	    	Coord roverPos = roverLocations.getLocation(rover.getRoverName());
            	    	xpos = roverPos.xpos;
            	    	ypos = roverPos.ypos;
                    	out.println("LOC " + xpos + " " + ypos);
                    	
                    
                    	
                    /**
                	 * ***************** START_LOC *******************
                	 */
                    // gets the current position of the rover	
                    } else if (input.startsWith("START_LOC")){  
                    	//System.out.println("SWARM: ------ START_LOC ------"); //debug test input parsing
                    	// does not need to synchronize-lock scienceLocations because not changing any values
                    	Coord startPos = planetMap.getStartPosition();
                    	out.println("START_LOC " + startPos.xpos + " " + startPos.ypos);
                        	
                        	
                        
                    	
                	/**
                	 * **************** TARGET_LOC ********************
                	 */
                    // gets the current position of the rover	
                    } else if (input.startsWith("TARGET_LOC")){  
                    	//System.out.println("SWARM: ------ TARGET_LOC ------"); //debug test input parsing
                    	// does not need to synchronize-lock scienceLocations because not changing any values
                    	Coord targetPos = planetMap.getTargetPosition();
                    	out.println("TARGET_LOC " + targetPos.xpos + " " + targetPos.ypos);
                            	
                            	
                    	
                	
                	/**
                	 * ******************** SCAN **********************
                	 */
                    // return json array of map area close around the rover
                	// may check rover tool for mastcam to increase range  of map results - maybe
                    } else if (input.startsWith("SCAN")){
                    	
                    	String jsonScanMap = retriveScanMap(rover);
                    	
            			out.println("SCAN"); //returns command header as check
            			
            			//return json string to Rover
            			out.println(jsonScanMap.toString());

            			//to mark the end of the json string
            			out.println("SCAN_END");
                    	
                    	
            			
        			/**
                	 * ******************** TIMER **********************
                	 */
                    // gets the current position of the rover	
                    } else if (input.startsWith("TIMER")){  
                    	int timeRemaining = 0;
                    	timeRemaining = (MAXIMUM_ACTIVITY_TIME_LIMIT - (int)(System.currentTimeMillis() - startTime)) / 1000;
                    	out.println("TIMER " + timeRemaining);
            			
                    	
            			
                	/**
                	 * ******************* GATHER ***********************
                	 */	
                    	// collect the science using either a drill or harvester
                    	// GATHER is a command with no return response
                    } else if(input.startsWith("GATHER")) {
                        
                    	// does not need to synchronize-lock roverLocations because not changing any values
                    	Coord roverPos = roverLocations.getLocation(rover.getRoverName());
                    	
                    	// lock scienceLocations because this requires checking then changing it
                    	synchronized (scienceLocations){
	                    	// true if this coordinate is in the scienceLocations hashmap and gather cooldown has been satisfied
	                    	if(scienceLocations.checkLocation(roverPos)
	                    			&& (rover.getRoverLastGatherTime() + GATHER_TIME_PER_TILE < (System.currentTimeMillis()))){ 
	                    		
	                    		if((rover.getTool_1() == RoverToolType.DRILL || (rover.getTool_2() == RoverToolType.DRILL) 
	                    				 && (planetMap.getTile(roverPos).getTerrain() == Terrain.ROCK || planetMap.getTile(roverPos).getTerrain() == Terrain.GRAVEL))){
	                    			// remove the science from scienceLocations and store in rover scienceCargo	
	                    			Science foundScience = scienceLocations.takeScience(roverPos);
	                    			rover.scienceCargo.add(foundScience);
	                    			corpCollectedScience.get(getCorpNumber(rover)).add(foundScience);
	                    			System.out.println("SwarmServer: corp " + getCorpNumber(rover) + " total science = " + corpCollectedScience.get(getCorpNumber(rover)).size());
	                    		}
	                    		
	                    		if((rover.getTool_1() == RoverToolType.EXCAVATOR || (rover.getTool_2() == RoverToolType.EXCAVATOR) 
	                    				 && (planetMap.getTile(roverPos).getTerrain() == Terrain.SOIL || planetMap.getTile(roverPos).getTerrain() == Terrain.SAND))){
	                    			// remove the science from scienceLocations and store in rover scienceCargo	
	                    			Science foundScience = scienceLocations.takeScience(roverPos);
	                    			rover.scienceCargo.add(foundScience);
	                    			corpCollectedScience.get(getCorpNumber(rover)).add(foundScience);
	                    			System.out.println("SwarmServer: corp " + getCorpNumber(rover) + " total science = " + corpCollectedScience.get(getCorpNumber(rover)).size());
	                    		}
	                    	}
	                    	scoreDisplayUpdate();
                    	} //END synchronized lock
                 	
                    	
                    	
                    	
	            	/**
	            	 * ******************* CARGO ***********************
	            	 */	 	                
                    } else if(input.startsWith("CARGO")) {
                    // Check to see what is in the rovers cargo hold (collected science).
                    	Gson gson = new GsonBuilder()
                    			.setPrettyPrinting()
                    			.enableComplexMapKeySerialization()
                    			.create();
                    	// return contents of scienceCargo
                    	String jsonCargoList = gson.toJson(rover.scienceCargo);
                    	
                    	out.println("CARGO"); //returns command header as check
                    	
                    	// return an ArrayList of rover equipment - json string?
                    	out.println(jsonCargoList.toString());
                    	
                    	out.println("CARGO_END");
                    	
                    	
                    	
                    	
                	/**
	            	 * ******************* EQUIPMENT ***********************
	            	 */	
                    } else if(input.startsWith("EQUIPMENT")) {
                    	Gson gson = new GsonBuilder()
                    			.setPrettyPrinting()
                    			.enableComplexMapKeySerialization()
                    			.create();
                    	ArrayList<String> eqList = new ArrayList<String>();
                    	
                    	eqList.add(rover.getRoverDrive().toString());
                    	eqList.add(rover.getTool_1().toString());
                    	eqList.add(rover.getTool_2().toString());
                    	
                    	String jsonEqList = gson.toJson(eqList);
                    	
                    	//System.out.println("SWARM_"+roverNameString+ "_thread: returning work EQUIPMENT");
                    	out.println("EQUIPMENT"); //returns command header as check
                    	
                    	// return an ArrayList of rover equipment - json string?
                    	out.println(jsonEqList.toString());
                    	
                    	out.println("EQUIPMENT_END");
                    	
                    
                    	
                    	
                	/**
	            	 * *********** DEFAULT - no recognizable command received ****************
	            	 */		       	
                    } else {
                    	//default response
                    	out.println("");
                    }
                    
                             
                }
            } catch (IOException e) {
                System.out.println(e);
            } catch (Exception e) {
				e.printStackTrace();
			} finally {
                try {
                    socket.close();
                } catch (IOException e) {  }
            }
        }
        
        
        // ########################################################################################################
        // support methods
        

        // *** SCAN ***
		private String retriveScanMap(Rover thisRover) {
			//System.out.println("SWARM: ------ SCAN ------"); //debug test input parsing
			Gson gson = new GsonBuilder()
        			.setPrettyPrinting()
        			.enableComplexMapKeySerialization()
        			.create();
			Coord roverPos = roverLocations.getLocation(thisRover.getRoverName());
			
			// length of a side of the scan map array !!! must be odd number !!!
			int scanRange = STANDARD_SCANMAP_RANGE;
			// Adjust scanMap range with use of scan range booster
			if(thisRover.getTool_1() == RoverToolType.RANGE_BOOTER || thisRover.getTool_2() == RoverToolType.RANGE_BOOTER){
				scanRange = BOOSTED_SCANMAP_RANGE;
			}

			// because I don't want to accidentally change the original
			HashMap<Coord, Science> sciHash = scienceLocations.getHashMapClone();
			
			// This method builds a temporary science locations list based on Rover equipment
			HashMap<Coord, Science> filteredScienceLocations = new HashMap<Coord, Science>();
			
			// Check what Scan Tools Rover is equipped with and filter scan results based on this
			if(thisRover.getTool_1() == RoverToolType.RADIATION_SENSOR || thisRover.getTool_2() == RoverToolType.RADIATION_SENSOR){
			    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
			        if (Objects.equals(Science.RADIOACTIVE, entry.getValue())) {
			        	filteredScienceLocations.put(entry.getKey(), Science.RADIOACTIVE);
			        }
			    }
			}
			
			if(thisRover.getTool_1() == RoverToolType.CHEMICAL_SENSOR || thisRover.getTool_2() == RoverToolType.CHEMICAL_SENSOR){
			    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
			        if (Objects.equals(Science.ORGANIC, entry.getValue())) {
			        	filteredScienceLocations.put(entry.getKey(), Science.ORGANIC);
			        }
			    }
			}
			
			if(thisRover.getTool_1() == RoverToolType.SPECTRAL_SENSOR || thisRover.getTool_2() == RoverToolType.SPECTRAL_SENSOR){
			    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
			        if (Objects.equals(Science.CRYSTAL, entry.getValue())) {
			        	filteredScienceLocations.put(entry.getKey(), Science.CRYSTAL);
			        }
			    }
			}
			
			if(thisRover.getTool_1() == RoverToolType.RADAR_SENSOR || thisRover.getTool_2() == RoverToolType.RADAR_SENSOR){
			    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
			        if (Objects.equals(Science.MINERAL, entry.getValue())) {
			        	filteredScienceLocations.put(entry.getKey(), Science.MINERAL);
			        }
			    }
			}
			
			// pass parameters to PlanetMap class to get a subset map of the surrounding area
			ScanMap scanMap = planetMap.getScanMap(roverPos, scanRange, roverLocations, new ScienceLocations(filteredScienceLocations));
			             	
			// convert scanMap object to json and return to rover
			String jsonScanMap = gson.toJson(scanMap);
			
			return jsonScanMap;		                 	
		}
    }
    

   
    // ** MOVE **
    static Coord doMove(Rover thisRover, String requestedMoveDir) throws Exception{ 
    	// *** pay close attention to this "synchronized" and make sure it works as intended ***
    	// MOVE has to lock the roverLocations list because it needs to change it's contents
    	synchronized (roverLocations){
	    	Coord roverPos = roverLocations.getLocation(thisRover.getRoverName());
	    	int xCurrentPos = roverPos.xpos;
	    	int yCurrentPos = roverPos.ypos;
	    	

	    		    	
	    	// ********* WHEELS **********	
    		// Respond based on the rover drive type AND
    		// Check that is has been at a minimum of "time limeit per second" seconds (WHEEL velocity) since the rover last moved
	    	// AND check that it is not stuck in the sand
	    	if(thisRover.getRoverDrive() == RoverDriveType.WHEELS
	    			&& thisRover.getRoverLastMoveTime() + WHEELS_TIME_PER_SQUARE < (System.currentTimeMillis() )
	    			&& planetMap.getTile(roverPos).getTerrain() != Terrain.SAND){
	    			    		
	    			if(requestedMoveDir.equals("N")){
	    				yCurrentPos = yCurrentPos - 1;
	    				if(!isValid_Y(yCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
		    		
		    		//check planetMap (immutable)
		    		MapTile possibleMovetoTile = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if(possibleMovetoTile.getTerrain() != Terrain.ROCK && possibleMovetoTile.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move north, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    	
	    			if(requestedMoveDir.equals("S")){
	    					yCurrentPos = yCurrentPos + 1;
		    				if(!isValid_Y(yCurrentPos)){
		    					// On the edge, returns rovers current position unchanged
		    					return roverPos;
		    				}
		    				
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move south, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    		
	    			if(requestedMoveDir.equals("E")){
			    		xCurrentPos = xCurrentPos + 1;
	    				if(!isValid_X(xCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover	
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move east, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    			
	    			if(requestedMoveDir.equals("W")){
			    		xCurrentPos = xCurrentPos - 1;
	    				if(!isValid_X(xCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move east, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    	}
	    	
	    	// ********* TREADS **********
	    	// TREADS dont get stuck
	    	if(thisRover.getRoverDrive() == RoverDriveType.TREADS
	    			&& thisRover.getRoverLastMoveTime() + TREADS_TIME_PER_SQUARE < (System.currentTimeMillis()) ){
	    			    		
	    			if(requestedMoveDir.equals("N")){
		    		yCurrentPos = yCurrentPos - 1;
    				if(!isValid_Y(yCurrentPos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover 		
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move north, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    	
	    			if(requestedMoveDir.equals("S")){
		    		yCurrentPos = yCurrentPos + 1;
    				if(!isValid_Y(yCurrentPos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover		    		
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move south, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    			
	    			if(requestedMoveDir.equals("E")){
			    		xCurrentPos = xCurrentPos + 1;
	    				if(!isValid_X(xCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
				    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover
					    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
					    			// if moveRover call is successful then update latest move time value
					    			thisRover.updateMoveTime();
					    		} else {
					    			//System.out.println("no move east, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    			
	    			if(requestedMoveDir.equals("W")){
			    		xCurrentPos = xCurrentPos - 1;
	    				if(!isValid_X(xCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
				    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover	
					    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
					    			// if moveRover call is successful then update latest move time value
					    			thisRover.updateMoveTime();
					    		} else {
					    			//System.out.println("no move west, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    	}
		    	

	    	// ********* WALKER **********
	    	// WALKERS also get stuck in sand
	    	if(thisRover.getRoverDrive() == RoverDriveType.WALKER
	    			&& thisRover.getRoverLastMoveTime() + WALKER_TIME_PER_SQUARE < (System.currentTimeMillis()) 
	    			&& planetMap.getTile(roverPos).getTerrain() != Terrain.SAND){
	    		
	    			if(requestedMoveDir.equals("N")){
		    		yCurrentPos = yCurrentPos - 1;
    				if(!isValid_Y(yCurrentPos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if( moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover		    		
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move north, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    	
	    			if(requestedMoveDir.equals("S")){
		    		yCurrentPos = yCurrentPos + 1;
    				if(!isValid_Y(yCurrentPos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
			    		if( moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
				    			// if moveRover call is successful then update latest move time value
				    			thisRover.updateMoveTime();
				    		} else {
				    			//System.out.println("no move south, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    			
	    			if(requestedMoveDir.equals("E")){
			    		xCurrentPos = xCurrentPos + 1;
	    				if(!isValid_X(xCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
				    		if( moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover	
					    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
					    			// if moveRover call is successful then update latest move time value
					    			thisRover.updateMoveTime();
					    		} else {
					    			//System.out.println("no move east, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    			
	    			if(requestedMoveDir.equals("W")){
			    		xCurrentPos = xCurrentPos - 1;
	    				if(!isValid_X(xCurrentPos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xCurrentPos, yCurrentPos);
				    		if( moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover
					    		if(roverLocations.moveRover(thisRover.getRoverName(), new Coord(xCurrentPos, yCurrentPos))){
					    			// if moveRover call is successful then update latest move time value
					    			thisRover.updateMoveTime();
					    		} else {
					    			//System.out.println("no move west, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    	}
	    	 		    	
	    	return new Coord(xCurrentPos,yCurrentPos);
    	} // *** release syncro lock on roverLocations
    }
    
	static boolean checkValidLocation(Coord loc){
		return (loc.xpos >= 0 && loc.xpos < mapWidth && loc.ypos >= 0 && loc.ypos < mapHeight);
	}
	
	static boolean isValid_Y(int ypos){
		return (ypos >= 0 && ypos < mapHeight);
	}
	
	static boolean isValid_X(int xpos){
		return (xpos >= 0 && xpos < mapWidth);
	}
    
	static void updateGUIDisplay() throws Exception{
		//myWorker.displayRovers(roverLocations);
		//myWorker.displayActivity(roverLocations, scienceLocations);
		//myWorker.displayFullMap(roverLocations, scienceLocations, planetMap);
		//myWorker2.displayFullMap(roverLocations.clone(), scienceLocations, planetMap);
		myWorker3.displayFullMap(roverLocations.clone(), scienceLocations, planetMap);
	}
	
	static void scoreDisplayUpdate() throws Exception{
		myWorker3.displayScore(corpCollectedScience);
	}
	
	static void stopRoverAreGO(){
		roversAreGO = false;
		countDownTimer.stop();
	}
	
	// sad face - more hard coded bs
	private static int getCorpNumber(Rover aRover){
		int tnum = 0;
		String roverNumber = aRover.getRoverName().toString().substring(6);
		// check for Blue Corp - return int 1
		if(roverNumber.equals("01") || roverNumber.equals("02") || roverNumber.equals("03") 
				|| roverNumber.equals("04") || roverNumber.equals("05") || roverNumber.equals("06") 
				|| roverNumber.equals("07") || roverNumber.equals("08") || roverNumber.equals("09")){
			tnum = 1;
		
			// check for Green Corp - return int 2
		} else if(roverNumber.equals("10") || roverNumber.equals("11") || roverNumber.equals("12") 
				|| roverNumber.equals("13") || roverNumber.equals("14") || roverNumber.equals("15") 
				|| roverNumber.equals("16") || roverNumber.equals("17") || roverNumber.equals("18")){
			tnum = 2;
		} 
		return tnum;
	}
}

class TimeLimitStop implements ActionListener {	
	public void actionPerformed(ActionEvent event) {
		SwarmServer.stopRoverAreGO();
		System.out.println("Time is up - Return mission is launching");
		Toolkit.getDefaultToolkit().beep();
	}
}
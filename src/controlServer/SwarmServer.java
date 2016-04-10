package controlServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Map.Entry;

import javax.swing.SwingUtilities;

import org.json.simple.JSONArray;

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


/**
 * The seed that this program is built on is a chat program example found here:
 * http://cs.lmu.edu/~ray/notes/javanetexamples/
 * Many thanks to the authors for publishing their code examples
 */

public class SwarmServer {

    /**
     * The port that the server listens on.
     */
    private static final int PORT = 9537; // because ... class number

    // TODO - these should actually be loaded from a file along with the map
    private static int mapWidth = 30;
    private static int mapHeight = 30;
    private static PlanetMap planetMap = new PlanetMap(mapWidth, mapHeight); 
    private static RoverLocations roverLocations = new RoverLocations();
    private static ScienceLocations scienceLocations = new ScienceLocations();
    
//  static GUIdisplay mainPanel;
//	static MyGUIWorker myWorker;
	
    static GUIdisplay2 mainPanel;
	static MyGUIWorker2 myWorker;
    
	// These are the velocity or speed values for the different drive systems
	// Changes these as necessary for good simulation balance
    static final int WHEELS_TIME_PER_SQUARE = 500;
    static final int TREADS_TIME_PER_SQUARE = 1000;
    static final int WALKER_TIME_PER_SQUARE = 1200;
    
    // limit of how many Calls can be made to the swarm server during a 1 second span
    static final int CALLS_PER_SECOND_LIMIT = 500;
    
    // minimum time in milliseconds that has to pass before another Gather can be done
    static final long GATHER_TIME_RESET = 2000;
    
 // length of a side of the scan map array !!! must be odd number !!!
    static final int STANDARD_SCANMAP_RANGE = 7;
    static final int BOOSTED_SCANMAP_RANGE = 11;
    
    /**
     * The application main method, which just listens on a port and
     * spawns handler threads.
     */
    public static void main(String[] args) throws Exception {
        System.out.println("The Swarm server is running.");
        ServerSocket listener = new ServerSocket(PORT);
       
//		mainPanel = new GUIdisplay();
//		myWorker = new MyGUIWorker(mainPanel);
		
		mainPanel = new GUIdisplay2(mapWidth, mapHeight);
		myWorker = new MyGUIWorker2(mainPanel);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//TODO - send a copy of the planetMap to GUI to use as background image
				// currently sending it when calling the updateGUIDisplay() method
				GUIdisplay2.createAndShowGui(myWorker, mainPanel);
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
                
                // make and instantiate a Rover object connected to this thread
                RoverName rname = RoverName.getEnum(roverNameString); 
                System.out.println("SWARM: make a rover name " + rname);
                Rover rover = new Rover(rname);
                
                //System.out.println("SWARM: returned from making rover " + rname);
                
                // TODO - the question is whether to store the science in the rover thread or in the rover object
                // Make an arrayList to hold any collected Science
                //ArrayList<Science> scienceCargo = new ArrayList<Science>();
                
                
                // ##### Run the Rover server process #####
                while (true) {
                	//read command input from the Rover
                	
                	System.out.println("SWARM_"+roverNameString+ "_thread: wait for a read line");
                    String input = in.readLine();

                    //condition the input to empty string if null
                    if (input == null) {
                    	input = "";
                    }
                    
                    // TODO - check requests per second
                    // if rover is too greedy drop their connection
                    long roverServerRequestsPerSecond = rover.getRoverRequestCount();
                   	System.out.println("SWARM_"+roverNameString+ "_thread: roverServerRequestsPerSecond= " + roverServerRequestsPerSecond);
                   	
                    if(roverServerRequestsPerSecond > CALLS_PER_SECOND_LIMIT){
                    	System.out.println("SWARM_"+roverNameString+ "_thread: too many requests per second - dropping connection");
                    	socket.close();
                    }
                    
                    // debug checking
                    System.out.println("SWARM_"+roverNameString+ "_thread: " + input);
                    
                    // Testing the input headers and respond with requested data
                      
                    // *************** MOVE *****************
                    if (input.startsWith("MOVE")){
                    	//System.out.println("SWARM: ------ MOVE ------"); //debug test input parsing
                    	// trim header off of input string
                    	String dir = input.substring(5);	
                    	//System.out.println("SWARM: recived request move " + dir);
                    	
                    	Coord locate = doMove(rover, dir); 
                    	
                    	// Update the GUI display with all the new rover locations when any individual rover moves
            	    	updateGUIDisplay();
            	    	
                    	//System.out.println("SWARM_"+roverNameString+ "_thread: new location " + locate);
                    	
            	    	
                    
                    // *************** LOC *****************
                    // gets the current position of the rover	
                    } else if (input.startsWith("LOC")){  
                    	//System.out.println("SWARM: ------ LOC ------"); //debug test input parsing
                    	// does not need to synchronize-lock scienceLocations because not changing any values
            	    	Coord roverPos = roverLocations.getLocation(rover.getRoverName());
            	    	xpos = roverPos.xpos;
            	    	ypos = roverPos.ypos;
                    	out.println("LOC " + xpos + " " + ypos);
                    	
                	
                    	
                    // *************** SCAN *****************
                    // return json array of map area close around the rover
                	// may check rover tool for mastcam to increase range  of map results - maybe
                    } else if (input.startsWith("SCAN")){
                    	//System.out.println("SWARM: ------ SCAN ------"); //debug test input parsing
                    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    	Coord roverPos = roverLocations.getLocation(rover.getRoverName());
                    	
                    	// length of a side of the scan map array !!! must be odd number !!!
                    	int scanRange = STANDARD_SCANMAP_RANGE;
                    	// Adjust scanMap range with use of scan range booster
                    	if(rover.getTool_1() == RoverToolType.RANGE_BOOTER || rover.getTool_2() == RoverToolType.RANGE_BOOTER){
                    		scanRange = BOOSTED_SCANMAP_RANGE;
                    	}
                    	              
                    	// make modified scienceLocations based on scanner tools available
 
                    	// because I don't want to accidentally change the original
                    	HashMap<Coord, Science> sciHash = scienceLocations.getHashMapClone();
                    	
                    	HashMap<Coord, Science> filteredScienceLocations = new HashMap<Coord, Science>();
                    	
                    	// if(tool == radioactive)
                    	if(rover.getTool_1() == RoverToolType.RADIATION_SENSOR || rover.getTool_2() == RoverToolType.RADIATION_SENSOR){
	                	    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
	                	        if (Objects.equals(Science.RADIOACTIVE, entry.getValue())) {
	                	        	filteredScienceLocations.put(entry.getKey(), Science.RADIOACTIVE);
	                	        }
	                	    }
                    	}
                    	
                    	if(rover.getTool_1() == RoverToolType.CHEMICAL_SENSOR || rover.getTool_2() == RoverToolType.CHEMICAL_SENSOR){
	                	    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
	                	        if (Objects.equals(Science.ORGANIC, entry.getValue())) {
	                	        	filteredScienceLocations.put(entry.getKey(), Science.ORGANIC);
	                	        }
	                	    }
                    	}
                    	
                    	if(rover.getTool_1() == RoverToolType.SPECTRAL_SENSOR || rover.getTool_2() == RoverToolType.SPECTRAL_SENSOR){
	                	    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
	                	        if (Objects.equals(Science.CRYSTAL, entry.getValue())) {
	                	        	filteredScienceLocations.put(entry.getKey(), Science.CRYSTAL);
	                	        }
	                	    }
                    	}
                    	
                    	if(rover.getTool_1() == RoverToolType.RADAR_SENSOR || rover.getTool_2() == RoverToolType.RADAR_SENSOR){
	                	    for (Entry<Coord, Science> entry : sciHash.entrySet()) {
	                	        if (Objects.equals(Science.MINERAL, entry.getValue())) {
	                	        	filteredScienceLocations.put(entry.getKey(), Science.MINERAL);
	                	        }
	                	    }
                    	}
                    	
                    	
                    	ScanMap scanMap = planetMap.getScanMap(roverPos, scanRange, roverLocations, new ScienceLocations(filteredScienceLocations));
                    	//System.out.println("SWARM: printing scanMap"); //debug test input parsing
                    	//scanMap.debugPrintMap();
                    	
                    	//System.out.println("SWARM: --- contents of roverLocations ---"); //debug test input parsing
                    	//roverLocations.printRovers();
                    	//System.out.println("SWARM: --- contents of roverLocations ---"); //debug test input parsing
                    	             	
                    	// convert scanMap object to json and return to rover
                    	String jsonScanMap = gson.toJson(scanMap);
                    	
                    	//System.out.println("SWARM:" + jsonScanMap);
                    	
                    	out.println("SCAN"); //returns command header as check
                    	
                    	//return json string to Rover
                    	out.println(jsonScanMap.toString());

                    	//to mark the end of the json string
                    	out.println("SCAN_END");
                    	
                    	
                    	
                    // *************** GATHER *****************
                    	// collect the science using either a drill or harvester
                    	// GATHER is a command with no return response
                    } else if(input.startsWith("GATHER")) {
                        
                    	// does not need to synchronize-lock scienceLocations because not changing any values
                    	Coord roverPos = roverLocations.getLocation(rover.getRoverName());
                    	
                    	// lock scienceLocations because this requires checking then changing it
                    	synchronized (scienceLocations){
	                    	// true if this coordinate is in the scienceLocations hashmap and gather cooldown has been satisfied
	                    	if(scienceLocations.checkLocation(roverPos)
	                    			&& (rover.getRoverLastGatherTime() + GATHER_TIME_RESET < (System.currentTimeMillis()))){ 
	                    		
	                    		if((rover.getTool_1() == RoverToolType.DRILL || (rover.getTool_2() == RoverToolType.DRILL) 
	                    				 && (planetMap.getTile(roverPos).getTerrain() == Terrain.ROCK || planetMap.getTile(roverPos).getTerrain() == Terrain.GRAVEL))){
	                    			// remove the science from scienceLocations and store in rover scienceCargo	
	                    			 rover.scienceCargo.add(scienceLocations.takeScience(roverPos));
	                    		}
	                    		
	                    		if((rover.getTool_1() == RoverToolType.EXCAVATOR || (rover.getTool_2() == RoverToolType.EXCAVATOR) 
	                    				 && (planetMap.getTile(roverPos).getTerrain() == Terrain.SOIL || planetMap.getTile(roverPos).getTerrain() == Terrain.SAND))){
	                    			// remove the science from scienceLocations and store in rover scienceCargo	
	                    			 rover.scienceCargo.add(scienceLocations.takeScience(roverPos));
	                    		}
	                    	}
                    	}
                 	
                    	
                    	
                    	
                    	
                   // **************** CARGO ******************
                    } else if(input.startsWith("CARGO")) {
                    // Check to see what is in the rovers cargo hold (collected science).
                    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    	// return contents of scienceCargo
                    	String jsonCargoList = gson.toJson(rover.scienceCargo);
                    	
                    	out.println("CARGO"); //returns command header as check
                    	
                    	// return an ArrayList of rover equipment - json string?
                    	out.println(jsonCargoList.toString());
                    	
                    	out.println("CARGO_END");
                    	
                    	
                    	
                   // **************** EQUIPMENT ******************	
                    } else if(input.startsWith("EQUIPMENT")) {
                    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
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
                    	
                    } else {
                    	//default response
                    	out.println(input);
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
    }
    
   // ########################################################################################################
   // support methods
    
    static Coord doMove(Rover rover, String dir) throws Exception{ 
    	// *** pay close attention to this "synchronized" and make sure it works as intended ***
    	// MOVE has to lock the roverLocations list because it needs to change it's contents
    	synchronized (roverLocations){
	    	Coord roverPos = roverLocations.getLocation(rover.getRoverName());
	    	int xpos = roverPos.xpos;
	    	int ypos = roverPos.ypos;
	    	
	    	
	    	// actual results will depend on rover type drive system and terrain features
	    	// also must check other rover positions and prevent collision

	    	//System.out.println("SWARM: drive type " + rover.getRoverDrive());
	    	//System.out.println("SWARM: last time " + rover.getRoverLastMoveTime());
	    	//System.out.println("SWARM: time diff " + ( (System.currentTimeMillis() + WHEELS_TIME_PER_SQUARE - rover.getRoverLastMoveTime() )));
	    	
	    	
	    	// ********* WHEELS **********	
    		// Respond based on the rover drive type AND
    		// Check that is has been at a minimum of 1 second (WHEEL velocity) since the rover last moved
	    	// AND check that it is not stuck in the sand
	    	if(rover.getRoverDrive() == RoverDriveType.WHEELS
	    			&& rover.getRoverLastMoveTime() + WHEELS_TIME_PER_SQUARE < (System.currentTimeMillis() )
	    			&& planetMap.getTile(roverPos).getTerrain() != Terrain.SAND){
	    		
	    			//System.out.println("SWARM: " + rover.getRoverName() + " making valid move"); //debug status out
	    		
	    			if(dir.equals("N")){
	    				ypos = ypos - 1;
	    				if(!isValid_Y(ypos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
		    		
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went north 1"); //debug status out
				    		} else {
				    			//System.out.println("no move north, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    	
	    			if(dir.equals("S")){
	    					ypos = ypos + 1;
		    				if(!isValid_Y(ypos)){
		    					// On the edge, returns rovers current position unchanged
		    					return roverPos;
		    				}
		    				
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went south 1"); //debug status out
				    		} else {
				    			//System.out.println("no move south, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    		
	    			if(dir.equals("E")){
			    		xpos = xpos + 1;
	    				if(!isValid_X(xpos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went east 1"); //debug status out
				    		} else {
				    			//System.out.println("no move east, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    			
	    			if(dir.equals("W")){
			    		xpos = xpos - 1;
	    				if(!isValid_X(xpos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went east 1"); //debug status out
				    		} else {
				    			//System.out.println("no move east, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    	}
	    	
	    	// ********* TREADS **********
	    	// TREADS dont get stuck
	    	if(rover.getRoverDrive() == RoverDriveType.TREADS
	    			&& rover.getRoverLastMoveTime() + TREADS_TIME_PER_SQUARE < (System.currentTimeMillis()) ){
	    		
	    			//System.out.println("SWARM: " + rover.getRoverName() + " making valid move"); //debug status out
	    		
	    			if(dir.equals("N")){
		    		ypos = ypos - 1;
    				if(!isValid_Y(ypos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went north 1"); //debug status out
				    		} else {
				    			//System.out.println("no move north, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    	
	    			if(dir.equals("S")){
		    		ypos = ypos + 1;
    				if(!isValid_Y(ypos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went south 1"); //debug status out
				    		} else {
				    			//System.out.println("no move south, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    			
	    			if(dir.equals("E")){
			    		xpos = xpos + 1;
	    				if(!isValid_X(xpos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
				    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover
					    		
					    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
					    			rover.updateMoveTime();
					    			//System.out.println("went east 1"); //debug status out
					    		} else {
					    			//System.out.println("no move east, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    			
	    			if(dir.equals("W")){
			    		xpos = xpos - 1;
	    				if(!isValid_X(xpos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
				    		if(moveThere.getTerrain() != Terrain.ROCK && moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover
					    		
					    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
					    			rover.updateMoveTime();
					    			//System.out.println("went west 1"); //debug status out
					    		} else {
					    			//System.out.println("no move west, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    	}
		    	

	    	// ********* WALKER **********
	    	// WALKERS also get stuck in sand
	    	if(rover.getRoverDrive() == RoverDriveType.WALKER
	    			&& rover.getRoverLastMoveTime() + WALKER_TIME_PER_SQUARE < (System.currentTimeMillis()) 
	    			&& planetMap.getTile(roverPos).getTerrain() != Terrain.SAND){
	    		
	    			//System.out.println("SWARM: " + rover.getRoverName() + " making valid move"); //debug status out
	    		
	    			if(dir.equals("N")){
		    		ypos = ypos - 1;
    				if(!isValid_Y(ypos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if( moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went north 1"); //debug status out
				    		} else {
				    			//System.out.println("no move north, rover in the way"); //debug status out
				    		}
			    		}
	    			}
		    	
	    			if(dir.equals("S")){
		    		ypos = ypos + 1;
    				if(!isValid_Y(ypos)){
    					// On the edge, returns rovers current position unchanged
    					return roverPos;
    				}
		    		//check planetMap (immutable)
		    		MapTile moveThere = planetMap.getTile(xpos, ypos);
			    		if( moveThere.getTerrain() != Terrain.NONE){
				    		// Move to the new map square, unless occupied by another rover
				    		
				    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
				    			rover.updateMoveTime();
				    			//System.out.println("went south 1"); //debug status out
				    		} else {
				    			//System.out.println("no move south, rover in the way"); //debug status out
				    		}
			    		}
	    			}
	    			
	    			if(dir.equals("E")){
			    		xpos = xpos + 1;
	    				if(!isValid_X(xpos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
				    		if( moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover
					    		
					    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
					    			rover.updateMoveTime();
					    			//System.out.println("went east 1"); //debug status out
					    		} else {
					    			//System.out.println("no move east, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    			
	    			if(dir.equals("W")){
			    		xpos = xpos - 1;
	    				if(!isValid_X(xpos)){
	    					// On the edge, returns rovers current position unchanged
	    					return roverPos;
	    				}
			    		//check planetMap (immutable)
			    		MapTile moveThere = planetMap.getTile(xpos, ypos);
				    		if( moveThere.getTerrain() != Terrain.NONE){
					    		// Move to the new map square, unless occupied by another rover
					    		
					    		if(roverLocations.moveRover(rover.getRoverName(), new Coord(xpos, ypos))){
					    			rover.updateMoveTime();
					    			//System.out.println("went west 1"); //debug status out
					    		} else {
					    			//System.out.println("no move west, rover in the way"); //debug status out
					    		}
				    		}
		    			}
	    	}
	    	 	
	    	Coord newPos = new Coord(xpos,ypos);
	    	return newPos;
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
		myWorker.displayFullMap(roverLocations, scienceLocations, planetMap);
	}
}

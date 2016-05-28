package common;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
	public String moveUsingAStar(Coord current, Coord target, MapTile[][] scanMapTiles) {

		/******find width and height of map*****/
		// TODO: get from http://23.251.155.186:3000/api/global/size
		String result = "";
		int col = 0;
		int row = 0;

		/*******Populate grid from global map*******/
		
		/* TODO: How do we get map in here to check for terrain type?
		 * if(!terrain.equals("ROCK")){
		 * new Coord(x, y, "rock"); 
		 */
		Coord[][] grid = new Coord[row][col];

		/*******************A* Algorithm*******************/
		
		// initialize depth (g value)
		current.setG(0);
		current.setH(distBetween(current, target));

		// create open and closed lists
		Comparator<Coord> comparator = new fValueComparator();
		PriorityQueue<Coord> open = new PriorityQueue<Coord>(10, comparator);
		List<Coord> closed = new ArrayList<>();

		// add source node to the open queue
		open.add(current);

		// while open is not empty
		while (!open.isEmpty()){

			// get Coord with the lowest f from the open list
			Coord currentCoord = open.peek();

			// remove current Coord from open list
			open.remove(currentCoord);
			// add current Tile to closed list 
			closed.add(currentCoord); 

			// if we're at the target, we're done
			if (currentCoord.equals(target)){
				Coord end = target;
				StringBuilder sb = new StringBuilder();
				while (!end.equals(current)) {
					sb.append(end.getParentDirection());					
					end = end.getParent();
				}
				result = sb.reverse().toString();
				
				// result has the directions to tell rover where to move
				return result;
			}		
			
			/****************GO THROUGH EACH POSSIBLE NEIGHBOR*******************/
			Coord next = null;   // neighbor
			Coord prior = null;

			// checks if currentCoord can move to the Right
			if (currentCoord.getY() < col-1){
				next = grid[currentCoord.x][(currentCoord.y)+1];

				if (!next.name.equals("rock")) {

					next.setG(currentCoord.getG() + 1);
					next.setH(distBetween (next, target));

					// if we already visited "next" we want to see if we can find a better path 
					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	
						
						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentCoord);
							if (!open.contains(next)) open.add(next);					
						}
					} else{
						next.setParent(currentCoord);
						if (!open.contains(next)) open.add(next);
					}
				}
			}

			// checks if currentCoord can move to the Left
			if (currentCoord.getY() > 0){
				next = grid[currentCoord.x][currentCoord.y-1];

				if (!next.name.equals("rock")) {
					next.setG(currentCoord.getG() + 1);
					next.setH(distBetween (next, target));

					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	

						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentCoord);
							if (!open.contains(next)) open.add(next);					
						}
					} else{
						next.setParent(currentCoord);
						if (!open.contains(next)) open.add(next);
					}
				}
			}

			// checks if currentCoord can move Up
			if (currentCoord.getX() > 0){
				next = grid[currentCoord.x -1][currentCoord.y];

				if (!next.name.equals("rock")) {
					next.setG(currentCoord.getG() + 1);
					next.setH(distBetween (next, target));

					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	

						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentCoord);
							if (!open.contains(next)) open.add(next);				
						}
					} else{
						next.setParent(currentCoord);
						if (!open.contains(next)) open.add(next);
					}
				}
			}

			// checks if currentCoord can move Down
			if (currentCoord.getX() < row-1) {
				next = grid[currentCoord.x+1][currentCoord.y];

				if (!next.name.equals("rock")) {
					next.setG(currentCoord.getG() + 1);
					next.setH(distBetween (next, target));

					// if we already visited "next" we want to see if we can find a better path 
					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	

						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentCoord);
							if (!open.contains(next)) open.add(next);					
						}
					} else{
						next.setParent(currentCoord);
						if (!open.contains(next)) open.add(next);
					}
				}
			}
		}
		return result;
	}

	// using the distance formula to get Euclidean distance between 2 Coords
	private int distBetween(Coord currentTile, Coord next) {
		double d = Math.sqrt(Math.pow(next.x - currentTile.x, 2) + Math.pow(next.y - currentTile.y, 2));
		return Double.valueOf(d).intValue();
	}
	
	public class Coord{
		private int x;
		private int y;
		private int g;
		private double h;
		private String name;
		Coord[][] grid;
		private Coord parent;

		public Coord(int x, int y, String name) {
			this.x = x;
			this.y = y;
			this.name = name;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getG() {
			return g;
		}

		public void setG(int g) {
			this.g = g;
		}

		public double getH() {
			return h;
		}

		public void setH(double h) {
			this.h = h;
		}

		public double getF() {
			return (g + h);
		}

		public String getName() {
			return name;
		}

		public Coord getLeft() {
			return grid[x][y-1];
		}

		public Coord getRight() {
			return grid[x][y+1];
		}

		public Coord getUp() {
			return grid[x-1][y];
		}

		public Coord getDown() {
			return grid[x+1][y];
		}

		public void setParent(Coord parent) {
			this.parent = parent;
		}

		public Coord getParent() {
			return parent;
		}

		// cardinal directions for the rover
		public char getParentDirection() {
			if (parent.x < x) {
				return 'S';
			} else if (parent.x > x) {
				return 'N';
			} else if (parent.y < y) {
				return 'E';			
			} else if (parent.y > y){
				return 'W';
			} else return '?';
		}

		@Override
		public String toString(){
			return name;
		}
	}
	public class fValueComparator implements Comparator<Coord> {
		@Override
		public int compare(Coord x, Coord y) {
			return (int) (x.getF() - y.getF());
		}
	}
}

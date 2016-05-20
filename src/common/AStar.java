package common;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
	public String searchFromGridFile(File file) {

		/******find width and height of map*****/
		String result = "";
		int col = 0;
		int row = 0;

		/*******Populate grid from global map*******/
		
		Tile[][] grid = new Tile[row][col];
		Tile startTile = null;
		Tile endTile = null;    // endTile is whatever the current target is
		

		/*******************A* Algorithm*******************/
		
		// initialize depth (g value)
		startTile.setG(0);
		startTile.setH(distBetween(startTile, endTile));

		// create open and closed lists
		Comparator<Tile> comparator = new fValueComparator();
		PriorityQueue<Tile> open = new PriorityQueue<Tile>(10, comparator);
		List<Tile> closed = new ArrayList<>();

		// add source node to the open queue
		open.add(startTile);

		// while open is not empty
		while (!open.isEmpty()){

			// get Tile with the lowest f from the open list
			Tile currentTile = open.peek();

			// remove current Tile from open list
			open.remove(currentTile);
			// add current Tile to closed list 
			closed.add(currentTile); 

			// if we're at the destination Tile, we're done
			if (currentTile.equals(endTile)){
				Tile end = endTile;
				StringBuilder sb = new StringBuilder();
				while (!end.equals(startTile)) {
					sb.append(end.getParentDirection());					
					end = end.getParent();
				}
				result = sb.reverse().toString();
				return result;
			}		
			
			/****************GO THROUGH EACH POSSIBLE NEIGHBOR*******************/
			Tile next = null;   // neighbor
			Tile prior = null;

			// checks if currentTile can move to the Right
			if (currentTile.getY() < col-1){
				next = grid[currentTile.x][(currentTile.y)+1];

				if (!next.name.equals("rock")) {

					next.setG(currentTile.getG() + 1);
					next.setH(distBetween (next, endTile));

					// if we already visited "next" we want to see if we can find a better path 
					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	
						
						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentTile);
							if (!open.contains(next)) open.add(next);					
						}
					} else{
						next.setParent(currentTile);
						if (!open.contains(next)) open.add(next);
					}
				}
			}

			// checks if currentTile can move to the Left
			if (currentTile.getY() > 0){
				next = grid[currentTile.x][currentTile.y-1];

				if (!next.name.equals("rock")) {
					next.setG(currentTile.getG() + 1);
					next.setH(distBetween (next, endTile));

					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	

						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentTile);
							if (!open.contains(next)) open.add(next);					
						}
					} else{
						next.setParent(currentTile);
						if (!open.contains(next)) open.add(next);
					}
				}
			}

			// checks if currentTile can move Up
			if (currentTile.getX() > 0){
				next = grid[currentTile.x -1][currentTile.y];

				if (!next.name.equals("rock")) {
					next.setG(currentTile.getG() + 1);
					next.setH(distBetween (next, endTile));

					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	

						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentTile);
							if (!open.contains(next)) open.add(next);				
						}
					} else{
						next.setParent(currentTile);
						if (!open.contains(next)) open.add(next);
					}
				}
			}

			// checks if currentTile can move Down
			if (currentTile.getX() < row-1) {
				next = grid[currentTile.x+1][currentTile.y];

				if (!next.name.equals("rock")) {
					next.setG(currentTile.getG() + 1);
					next.setH(distBetween (next, endTile));

					// if we already visited "next" we want to see if we can find a better path 
					if (closed.contains(next)){
						prior = closed.get(closed.indexOf(next));	

						if (next.getG() < prior.getG()){
							closed.remove(prior);
							next.setParent(currentTile);
							if (!open.contains(next)) open.add(next);					
						}
					} else{
						next.setParent(currentTile);
						if (!open.contains(next)) open.add(next);
					}
				}
			}
		}
		return result;
	}

	// using the distance formula to get Euclidean distance between 2 Tiles
	private int distBetween(Tile currentTile, Tile next) {
		double d = Math.sqrt(Math.pow(next.x - currentTile.x, 2) + Math.pow(next.y - currentTile.y, 2));
		return Double.valueOf(d).intValue();
	}
	
	public class Tile{
		private int x;
		private int y;
		private int g;
		private double h;
		private String name;
		Tile[][] grid;
		private Tile parent;

		public Tile(int x, int y, String name) {
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

		public Tile getLeft() {
			return grid[x][y-1];
		}

		public Tile getRight() {
			return grid[x][y+1];
		}

		public Tile getUp() {
			return grid[x-1][y];
		}

		public Tile getDown() {
			return grid[x+1][y];
		}

		public void setParent(Tile parent) {
			this.parent = parent;
		}

		public Tile getParent() {
			return parent;
		}

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
	public class fValueComparator implements Comparator<Tile> {
		@Override
		public int compare(Tile x, Tile y) {
			return (int) (x.getF() - y.getF());
		}
	}
}

package common;

import java.awt.Color;
import java.awt.geom.Line2D;

public class LineSegment {
	public int X1;
	public int X2;
	public int Y1;
	public int Y2;
	public Color lineColor;
	
	public LineSegment(int x1, int x2, int y1, int y2, Color color){
		this.X1 = x1;
		this.X2 = x2;
		this.Y1 = y1;
		this.Y2 = y2;
		this.lineColor = color;
	}
}

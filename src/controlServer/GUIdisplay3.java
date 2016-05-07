package controlServer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import common.Coord;
import common.GraphicTile;
import common.LineSegment;
import common.PlanetMap;
import common.RoverLocations;
import common.ScienceLocations;
import enums.Science;
import enums.Terrain;

public class GUIdisplay3 extends JPanel implements MyGUIAppendable3 {
	public static final int TILE_SIZE = 20;
	public final int FONT_SIZE = 18;

	private JTextArea area;
	private int width;
	private int height;
	private int pixelWidth;
	private int pixelHeight;
	private List<GraphicTile> graphicTiles;
	private List<LineSegment> lineSegments;
	private JTextField countdownClock;
	private JTextField countdownTitle;
	private Timer timer;
	private JTextField blueScoreTextArea;
	private JTextField greenScoreTextArea;
	private JTextField sampleScoreTextArea;

	private Color EXTRA_LIGHT_GREY = new Color(220, 220, 220);

	public GUIdisplay3() {
	}

	public GUIdisplay3(int width, int height, long timeLimit) {
		this.width = width;
		this.height = height;
		this.pixelWidth = (this.width * TILE_SIZE);
		this.pixelHeight = (this.height * TILE_SIZE);
		graphicTiles = new ArrayList<>();
		lineSegments = new ArrayList<>();
		countDownClock(timeLimit);
		displayScoreTextInit();
	}

	private void displayScoreTextInit() {
		blueScoreTextArea = new JTextField();
		blueScoreTextArea.setFont(new Font("sansserif", Font.PLAIN, FONT_SIZE));
		blueScoreTextArea.setBackground(Color.BLUE);
		blueScoreTextArea.setBorder(null);
		blueScoreTextArea.setEditable(false);
		blueScoreTextArea.setHorizontalAlignment(JTextField.CENTER);
		blueScoreTextArea.setForeground(Color.WHITE);
		blueScoreTextArea.setText("BLUE: 0");

		greenScoreTextArea = new JTextField();
		greenScoreTextArea.setFont(new Font("sansserif", Font.PLAIN, FONT_SIZE));
		greenScoreTextArea.setBackground(Color.GREEN);
		greenScoreTextArea.setBorder(null);
		greenScoreTextArea.setEditable(false);
		greenScoreTextArea.setHorizontalAlignment(JTextField.CENTER);
		greenScoreTextArea.setText("GREEN: 0");

		sampleScoreTextArea = new JTextField();
		sampleScoreTextArea.setFont(new Font("sansserif", Font.PLAIN, FONT_SIZE));
		sampleScoreTextArea.setBackground(Color.RED);
		sampleScoreTextArea.setBorder(null);
		sampleScoreTextArea.setEditable(false);
		sampleScoreTextArea.setHorizontalAlignment(JTextField.CENTER);
		sampleScoreTextArea.setText("SAMPLE: 0");
	}

	/**
	 * got the idea from:
	 * http://stackoverflow.com/questions/4314725/java-making-time-to-decrease-
	 * to-zero-by-swing-timer
	 */

	private void countDownClock(long timeLimit) {
		// Count down clock
		countdownTitle = new JTextField();
		countdownTitle.setFont(new Font("sansserif", Font.PLAIN, FONT_SIZE));
		countdownTitle.setBackground(Color.ORANGE);
		countdownTitle.setBorder(null);
		countdownTitle.setEditable(false);
		countdownTitle.setHorizontalAlignment(JTextField.CENTER);

		countdownClock = new JTextField();
		countdownClock.setFont(new Font("sansserif", Font.PLAIN, FONT_SIZE));
		countdownClock.setBackground(Color.ORANGE);
		countdownClock.setBorder(null);
		countdownClock.setEditable(false);
		countdownClock.setHorizontalAlignment(JTextField.CENTER);

		// create a 1 seconds delay
		timer = new Timer(1000, new ActionListener() {
			// private long time = 60 * 1000; //60 seconds
			private long time = timeLimit; // 10 minutes

			public void actionPerformed(ActionEvent e) {
				if (time >= 0) {
					long s = ((time / 1000) % 60);
					long m = (((time / 1000) / 60) % 60);
					long h = ((((time / 1000) / 60) / 60) % 60);
					// countdownClock.setText(h + " h " + m + " m " + s + " s");
					countdownTitle.setText("Time Remaining");
					countdownClock.setText(m + " mins " + s + " s");
					time -= 1000;
				}
			}
		});
		timer.start();
	}

	@Override
	public void append(String text) {
		area.append(text);
	}

	@Override
	public void clearDisplay() {
		area.setText("");
	}

	@Override
	public void setText(String text) {
		area.setText(text);
	}

	@Override
	public void setScores(String scoreBlue, String scoreGreen, String scoreSample) {
		// TODO Auto-generated method stub
		blueScoreTextArea.setText("BLUE: " + scoreBlue);
		greenScoreTextArea.setText("GREEN: " + scoreGreen);
		sampleScoreTextArea.setText("SAMPLE: " + scoreSample);
	}

	@Override
	public void drawThisGraphicTileArray(ArrayList<GraphicTile> gtarraylist,
			ArrayList<LineSegment> lineSegmentArrayList) {
		this.graphicTiles = gtarraylist;
		this.lineSegments = lineSegmentArrayList;
		repaint();
	}

	/**
	 * got the idea from:
	 * http://stackoverflow.com/questions/15870608/creating-a-draw-rectangle-
	 * filled-with-black-color-function-in-java-for-a-grid
	 * 
	 **/
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (GraphicTile graphicTile : graphicTiles) {
			graphicTile.drawTile(g);
		}
		g.setColor(EXTRA_LIGHT_GREY);
		// Draw all the grid squares TILE_SIZE x TILE_SIZE pixels
		g.drawRect(0, 0, pixelWidth, pixelHeight);
		for (int i = 0; i <= pixelWidth; i += TILE_SIZE) {
			g.drawLine(i, 0, i, pixelHeight);
		}
		for (int i = 0; i <= pixelHeight; i += TILE_SIZE) {
			g.drawLine(0, i, pixelWidth, i);
		}
		// draw the start and target location outlines
		for (LineSegment lineSegment : lineSegments) {
			g.setColor(lineSegment.lineColor);
			g.drawLine(lineSegment.X1, lineSegment.Y1, lineSegment.X2, lineSegment.Y2);
		}

	}

	// Set the size of the map display
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(this.pixelWidth + 200, this.pixelHeight + 50);
	}

	// Format the countdownclock
	private void buildDisplay() {
		this.setLayout(new BorderLayout());

		JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));

		JPanel emptyPanel = new JPanel();
		emptyPanel.setPreferredSize(new Dimension(160, 500));

		container.add(countdownTitle);
		container.add(countdownClock);
		container.add(blueScoreTextArea);
		container.add(greenScoreTextArea);
		container.add(sampleScoreTextArea);
		container.add(emptyPanel);

		container.setPreferredSize(new Dimension(160, 50));
		add(container, BorderLayout.EAST);
		this.repaint();

	}

	static void createAndShowGui(MyGUIWorker3 myWorker, GUIdisplay3 mainPanel) {
		// add a Prop Change listener here to listen for
		// DONE state then call get() on myWorker
		myWorker.execute();

		JFrame frame = new JFrame("GUIdisplay");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainPanel.buildDisplay();
		JScrollPane scrollPane = new JScrollPane(mainPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.setContentPane(scrollPane);
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

}

// #############################################################################################

class MyGUIWorker3 extends SwingWorker<Void, String> {
	private MyGUIAppendable3 myAppendable;
	private String msg;
	private RoverLocations roverLoc;
	private ScienceLocations sciloc;

	public MyGUIWorker3(MyGUIAppendable3 myAppendable) {
		this.myAppendable = myAppendable;
	}

	@Override
	protected Void doInBackground() throws Exception {
		// not sure what to do with this one
		return null;
	}

	public void printOut(String msg) {
		this.msg = msg;
		try {
			doInBackground();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearDisplay() {
		myAppendable.clearDisplay();
	}

	public void displayGraphicMap(RoverLocations roverLoc, ScienceLocations sciloc, PlanetMap planetMap) {
		int mWidth = planetMap.getWidth();
		int mHeight = planetMap.getHeight();

		ArrayList<GraphicTile> graphicTiles = new ArrayList<GraphicTile>();

		// make the graphic tiles and place in array
		for (int j = 0; j < mHeight; j++) {
			for (int i = 0; i < mWidth; i++) {
				// scan through the map - left to right, top to bottom
				Coord tcor = new Coord(i, j);
				GraphicTile gtile = new GraphicTile(tcor.xpos, tcor.ypos);
				// first check for a rover and add to graphicTile if found
				if (roverLoc.containsCoord(tcor)) {
					String rNum = roverLoc.getName(tcor).toString();
					// make a tile with rover number
					gtile.setRoverName(rNum.substring(6));
					// then check if there is a terrain feature (if not SOIL
					// then add terrain to graphicTile )
				}
				if (planetMap.getTile(tcor).getTerrain() != Terrain.SOIL) {
					gtile.setTerrain(planetMap.getTile(tcor).getTerrain());
				}
				if (sciloc.checkLocation(tcor)) {
					gtile.setScience(sciloc.scanLocation(tcor));
				}
				graphicTiles.add(gtile);
			}
		}

		// Load array with target and start location outline lineSegments
		ArrayList<LineSegment> lineSegmentArrayList = new ArrayList<LineSegment>();
		int tileSize = GUIdisplay3.TILE_SIZE;
		int boxSize;
		int minSafePos_x;
		int maxSafePos_x;
		int minSafePos_y;
		int maxSafePos_y;
		Color boxColor;
		Coord startPos = planetMap.getStartPosition();
		int strtPos_x = startPos.xpos;
		int strtPos_y = startPos.ypos;
		Coord targetPos = planetMap.getTargetPosition();
		int targPos_x = targetPos.xpos;
		int targPos_y = targetPos.ypos;
		int startOffset = PlanetMap.START_LOCATION_SIZE / 2;
		int targetOffset = PlanetMap.TARGET_LOCATION_SIZE / 2;

		minSafePos_x = Math.max(strtPos_x - startOffset, 0);
		maxSafePos_x = Math.min(strtPos_x + startOffset, planetMap.getWidth());
		minSafePos_y = Math.max(strtPos_y - startOffset, 0);
		maxSafePos_y = Math.min(strtPos_y + startOffset, planetMap.getHeight());
		boxSize = PlanetMap.START_LOCATION_SIZE;
		boxColor = Color.MAGENTA;

		lineSegmentArrayList.add(new LineSegment(minSafePos_x * tileSize, (maxSafePos_x * tileSize) + tileSize,
				minSafePos_y * tileSize, minSafePos_y * tileSize, boxColor));

		lineSegmentArrayList.add(new LineSegment(minSafePos_x * tileSize, (maxSafePos_x * tileSize) + tileSize,
				(maxSafePos_y * tileSize) + tileSize, (maxSafePos_y * tileSize) + tileSize, boxColor));

		lineSegmentArrayList.add(new LineSegment(minSafePos_x * tileSize, minSafePos_x * tileSize,
				minSafePos_y * tileSize, (maxSafePos_y * tileSize) + tileSize, boxColor));

		lineSegmentArrayList
				.add(new LineSegment((maxSafePos_x * tileSize) + tileSize, (maxSafePos_x * tileSize) + tileSize,
						minSafePos_y * tileSize, (maxSafePos_y * tileSize) + tileSize, boxColor));

		minSafePos_x = Math.max(targPos_x - targetOffset, 0);
		maxSafePos_x = Math.min(targPos_x + targetOffset, planetMap.getWidth() - 1);
		minSafePos_y = Math.max(targPos_y - targetOffset, 0);
		maxSafePos_y = Math.min(targPos_y + targetOffset, planetMap.getHeight() - 1);
		boxSize = PlanetMap.TARGET_LOCATION_SIZE;
		boxColor = Color.RED;

		lineSegmentArrayList.add(new LineSegment(minSafePos_x * tileSize, (maxSafePos_x * tileSize) + tileSize,
				minSafePos_y * tileSize, minSafePos_y * tileSize, boxColor));

		lineSegmentArrayList.add(new LineSegment(minSafePos_x * tileSize, (maxSafePos_x * tileSize) + tileSize,
				(maxSafePos_y * tileSize) + tileSize, (maxSafePos_y * tileSize) + tileSize, boxColor));

		lineSegmentArrayList.add(new LineSegment(minSafePos_x * tileSize, minSafePos_x * tileSize,
				minSafePos_y * tileSize, (maxSafePos_y * tileSize) + tileSize, boxColor));

		lineSegmentArrayList
				.add(new LineSegment((maxSafePos_x * tileSize) + tileSize, (maxSafePos_x * tileSize) + tileSize,
						minSafePos_y * tileSize, (maxSafePos_y * tileSize) + tileSize, boxColor));

		myAppendable.drawThisGraphicTileArray(graphicTiles, lineSegmentArrayList);
	}

	public void displayFullMap(RoverLocations roverLoc, ScienceLocations sciloc, PlanetMap planetMap) {
		displayGraphicMap(roverLoc, sciloc, planetMap);
	}

	@Override
	protected void process(List<String> chunks) {
		for (String text : chunks) {
			myAppendable.append(text);
		}
	}

	public void displayScore(ArrayList<ArrayList<Science>> corpCollectedScience) {
		String tempBlueScore = Integer.toString(corpCollectedScience.get(1).size());
		String tempGreenScore = Integer.toString(corpCollectedScience.get(2).size());
		String tempSampleScore = Integer.toString(corpCollectedScience.get(0).size());
		myAppendable.setScores(tempBlueScore, tempGreenScore, tempSampleScore);
	}
}

interface MyGUIAppendable3 {
	public void drawThisGraphicTileArray(ArrayList<GraphicTile> graphicTileArraylist,
			ArrayList<LineSegment> lineSegmentArrayList);

	public void append(String text);

	public void setText(String text);

	public void clearDisplay();

	public void setScores(String scoreBlue, String scoreGreen, String scoreSample);

}

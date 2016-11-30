import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class GradientArt {
	
	static int squaresPerSide = 12;
	static int squareSize = 30;
	static int howManyFoci = 3;
	
	//HashMap<Point, Square> sequence = new HashMap<>();
	static ArrayList<ArrayList<Square>> seq = new ArrayList<ArrayList<Square>>();
	
	static int defaultHeight = (squaresPerSide*2+1)*squareSize;
	

	static Random r = new Random();
	
	static float fociHue = r.nextFloat();
	static float hueShift = fociHue + (r.nextFloat()*0.2f + 0.1f);
	
	public static void main(String[] args) throws IOException{

		int howMany = 10;

		for(int i = 0; i < howMany; i++){
			randomInit();
			seq.add(generatePattern());
		}
		
		int steps = 60;
		
		for(int i = 0; i < howMany-1; i++){
			for(int j = 0; j < steps; j++){
				BufferedImage a = generateImage(seq.get(i), seq.get(i+1), j/ (float) steps);
				ImageIO.write(a, "png", new File("C:\\Users\\Robert\\Pictures\\Art\\GradientArt\\output" + (j + i*steps) + ".png"));
			}
		}
		
		
	}
	
	private static float sigmoidMap(double in){
		return (float) (-1/(1 + Math.exp(8*(in-0.5))) + 1);
	}
	
	private static void randomInit(){
		fociHue = r.nextFloat();
		hueShift = fociHue + (r.nextFloat()*0.3f + 0.2f);
	}
	
	private static ArrayList<Square> generatePattern(){

		//Color fociColor = Color.getHSBColor(fociHue, r.nextFloat(), r.nextFloat());
		//g.setColor(fociColor);
		//fociColor.get
		
		List<Point> foci = pickFoci(howManyFoci);
		
		ArrayList<Square> currentDrawing = new ArrayList<>();
		
		for(int i = 0; i < squaresPerSide; i++){
			for(int j = 0; j < squaresPerSide; j++){
				
				int dist = distFromNearestFocus(i, j, foci);
				
				Square s = new Square(i, j, (float) mapDistToFocusToRatio(dist), (float) mapDistToHue(dist), (float) mapDistToSat(dist), (float) mapDistToBright(dist));
				
				currentDrawing.add(s);
			}
		}
		return currentDrawing;
	}
	
	private static BufferedImage generateImage(ArrayList<Square> a, ArrayList<Square> b, float ratio){
		ratio = sigmoidMap(ratio);
		
		BufferedImage art;
		Graphics g;
		
		art = new BufferedImage(defaultHeight, defaultHeight, BufferedImage.TYPE_INT_RGB);
		g = art.getGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, defaultHeight, defaultHeight);
		
		for(int i = 0; i < squaresPerSide*squaresPerSide; i++){
			
			g.setColor(Color.getHSBColor(mix(a.get(i).hue, b.get(i).hue, ratio), mix(a.get(i).sat, b.get(i).sat, ratio), mix(a.get(i).val, b.get(i).val, ratio)));
			drawSquare(a.get(i).x, a.get(i).y, mix(a.get(i).ratio, b.get(i).ratio, ratio), g);
			
		}
		
		return art;
	}
	
	private static float mix(float a, float b, float r){
		return b*r + a*(1.0f-r);
	}
	
	
	private static void testFocusDistMapping(){
		for(int i = -5; i < 5; i++){
			System.out.println(mapDistToFocusToRatio(i));
			}
	}
	
	private static double mapDistToHue(int dist){
		int avgDist = 2;
		int distdelta = dist-avgDist;
		//return Math.log(distdelta) - 1;
		//double hueDelta = distdelta/8.0;
		double hueDelta = -0.25*Math.pow(dist, .25) + 0.5f;
		return (fociHue - hueShift)*hueDelta + fociHue;
	}
	
	private static double mapDistToFocusToRatio(int dist){
		//System.out.println(dist);
		int avgDist = 3;
		int distdelta = dist-avgDist;
		return 1 - distdelta/7.0;
		//return -1*Math.pow(dist, .25) +2;
		//return 0.5;
	}
	
	private static double mapDistToSat(int dist){
		//return 0.7;
		return -0.25*Math.pow(dist, .5) + 1;
	}
	
	private static double mapDistToBright(int dist){
//		return 0.7;
		return -0.25*Math.pow(dist, .5) + 1;
	}
	
	private static int distFromNearestFocus(int x, int y, List<Point> foci){
		int minDist = Integer.MAX_VALUE;
		for(int i = 0; i < foci.size(); i++){
			int dist = Math.abs(foci.get(i).x - x) + Math.abs(foci.get(i).y - y);
			minDist = Math.min(dist, minDist);
		}
		return minDist;
	}
	
	private static List<Point> pickFoci(int count){
		Random r = new Random();
		ArrayList<Point> out = new ArrayList<>();
		for(int i = 0; i < count; i++){
			out.add(new Point(r.nextInt(squaresPerSide), r.nextInt(squaresPerSide)));
		}
		
		return out;
	}
	
	private static void drawSquare(int xindex, int yindex, double ratio, Graphics g){
		int xStart = (int) ((squareSize*2)*xindex + squareSize/2*3 - squareSize*ratio/2.0);
		int yStart = (int) ((squareSize*2)*yindex + squareSize/2*3 - squareSize*ratio/2.0);
		
		g.fillRect(xStart, yStart, (int) (squareSize*ratio), (int) (squareSize*ratio));
	}
	
	static class Square{
		int x;
		int y;
		float ratio;
		float hue;
		float sat;
		float val;
		public Square(int x, int y, float ratio, float hue, float sat, float val) {
			super();
			this.x = x;
			this.y = y;
			this.ratio = ratio;
			this.hue = hue;
			this.sat = sat;
			this.val = val;
		}
		
		
	}
}

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class GradientArt {
	
	static int squaresPerSide = 10;
	static int squareSize = 10;
	static int howManyFoci = 3;
	
	static int defaultHeight = (squaresPerSide*2+1)*squareSize;
	
	static BufferedImage art;
	static Graphics g;
	static Random r = new Random();
	
	static float fociHue = r.nextFloat();
	static float hueShift = r.nextFloat();
	
	public static void main(String[] args) throws IOException{
		art = new BufferedImage(defaultHeight, defaultHeight, BufferedImage.TYPE_INT_RGB);
		g = art.getGraphics();
		

		
		Color fociColor = Color.getHSBColor(fociHue, r.nextFloat(), r.nextFloat());
		g.setColor(fociColor);
		//fociColor.get
		
		List<Point> foci = pickFoci(howManyFoci);
		
		for(int i = 0; i < squareSize; i++){
			for(int j = 0; j < squareSize; j++){
				
				int dist = distFromNearestFocus(i, j, foci);
				
				g.setColor(Color.getHSBColor((float) mapDistToHue(dist), 0.7f, 0.7f));
				drawSquare(i, j, mapDistToFocusToRatio(dist));
				
			}
		}
		
		ImageIO.write(art, "png", new File("C:\\Users\\Robert\\Pictures\\Art\\GradientArt\\output.png"));
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
		double hueDelta = distdelta/3.0;
		return (fociHue - hueShift)*hueDelta + fociHue;
	}
	
	private static double mapDistToFocusToRatio(int dist){
		//System.out.println(dist);
		int avgDist = 2;
		int distdelta = dist-avgDist;
		//return Math.log(distdelta) - 1;
		return 1 - distdelta/3.0;
		//return 0.5;
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
	
	private static void drawSquare(int xindex, int yindex, double ratio){
		int xStart = (int) ((squareSize*2)*xindex + squareSize/2*3 - squareSize*ratio/2.0);
		int yStart = (int) ((squareSize*2)*yindex + squareSize/2*3 - squareSize*ratio/2.0);
		
		g.fillRect(xStart, yStart, (int) (squareSize*ratio), (int) (squareSize*ratio));
	}
}

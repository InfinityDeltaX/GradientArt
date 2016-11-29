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
	
	public static void main(String[] args) throws IOException{
		art = new BufferedImage(defaultHeight, defaultHeight, BufferedImage.TYPE_INT_RGB);
		g = art.getGraphics();
		
		
		List<Point> foci = pickFoci(3);
		
		for(int i = 0; i < squareSize; i++){
			for(int j = 0; j < squareSize; j++){
				
				drawSquare(i, j, 1.9);
			}
		}
		
		ImageIO.write(art, "png", new File("C:\\Users\\Robert\\Pictures\\Art\\GradientArt\\output.png"));
	}
	
	private static int distFromNearestFocus(int x, int y, List<Point> foci){
		int minDist = 0;
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

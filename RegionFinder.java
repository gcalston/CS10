import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 * 
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 */
public class RegionFinder {
	private static final int maxColorDiff = 50;				// how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50; 				// how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
															// so the identified regions are in a list of lists of points
	
	public RegionFinder() {
		this.image = null;
	}

	public RegionFinder(BufferedImage image) {
		this.image = image;		
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public BufferedImage getRecoloredImage() {
		return recoloredImage;
	}

	/**
	 * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
	 */
	public void findRegions(Color targetColor) {
		// System.out.println(image.getWidth());
		ArrayList<ArrayList<Point>> foundRegions = new ArrayList<ArrayList<Point>>();
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
								
				Color intialColor = new Color(image.getRGB(x,y));
				if (visited.getRGB(x,y) == 0 && colorMatch(intialColor, targetColor)) {
					Point startingPoint = new Point(x,y);
					ArrayList<Point> pointsToCheck = new ArrayList<Point>();
					pointsToCheck.add(startingPoint);
					// System.out.println(startingPoint);
				
					ArrayList<Point> newRegion = new ArrayList<Point>();
					
					while (!pointsToCheck.isEmpty()) {
						Point newPoint = pointsToCheck.remove(pointsToCheck.size() - 1);
						newRegion.add(newPoint);
						// System.out.println(newRegion);
						
						visited.setRGB( (int)newPoint.getX(),(int)newPoint.getY(), 1);
						int radius = 2;
					
						for (int ny = Math.max(0,((int)(newPoint.getY()) - radius )); 
						ny < Math.min(image.getHeight(), (int) (newPoint.getY()) + radius); 
						ny++) {
							for (int nx = Math.max(0, (int) (newPoint.getX()) - radius); 
							nx < Math.min(image.getWidth(), (int) (newPoint.getX()) + radius);
							nx++) {
								Color neighborColor = new Color(image.getRGB(nx,ny));
								
								if (colorMatch(targetColor, neighborColor) && visited.getRGB(nx, ny) == 0) {
										Point neighbor = new Point(nx, ny);
										pointsToCheck.add(neighbor);
										visited.setRGB(nx, ny, 1);
										
								}
							}
						}
					}
					if (newRegion.size() > minRegion) {
						foundRegions.add(newRegion);
						
						
						
					}
				}
				
			}
		}
		this.regions = foundRegions;
		System.out.println(regions);
	}
	


	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	private static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		int num = 0;
		num += Math.abs(c1.getRed()-c2.getRed()) +  Math.abs(c1.getGreen()-c2.getGreen()) +
				Math.abs(c1.getBlue()-c2.getBlue());
		
		return (num <= maxColorDiff);		
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		
		ArrayList<Point> largest = null;
		
		
		if (!regions.isEmpty()) {
			largest = regions.get(0);
			for(ArrayList<Point> region : regions) {
				if(region.size() > largest.size()) {
				largest = region;
				System.out.println(largest);
			}	 
			
		   }
		}
		return largest;
	}

	/**
	 * Sets recoloredImage to be a copy of image, 
	 * but with each region a uniform random color, 
	 * so we can see where they are
	 */
	public void recolorImage() {
		// First copy the original
		recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
		// Now recolor the regions in it
		// iterate through returned arraylist from largestRegion()
		// and c.setRGB() on all of those objects in the list to the targetColor
		// TODO: YOUR CODE HERE
	
		for(ArrayList<Point> array : regions) {
			Color randomColor = new Color((int)(Math.random() * 16777216));
			for (Point pt: array) {
				recoloredImage.setRGB((int) pt.getX(), (int) pt.getY(), randomColor.getRGB());
				
			}
		}
	}
}

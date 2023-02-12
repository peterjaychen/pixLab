import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.*;
import java.util.*;
import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture.  This class inherits from 
 * SimplePicture and allows the student to add functionality to
 * the Picture class.  
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture 
{
  ///////////////////// constructors //////////////////////////////////
  
  /**
   * Constructor that takes no arguments 
   */
  public Picture ()
  {
    /* not needed but use it to show students the implicit call to super()
     * child constructors always call a parent constructor 
     */
    super();  
  }
  
  /**
   * Constructor that takes a file name and creates the picture 
   * @param fileName the name of the file to create the picture from
   */
  public Picture(String fileName)
  {
    // let the parent class handle this fileName
    super(fileName);
  }
  
  /**
   * Constructor that takes the width and height
   * @param height the height of the desired picture
   * @param width the width of the desired picture
   */
  public Picture(int height, int width)
  {
    // let the parent class handle this width and height
    super(width,height);
  }
  
  /**
   * Constructor that takes a picture and creates a 
   * copy of that picture
   * @param copyPicture the picture to copy
   */
  public Picture(Picture copyPicture)
  {
    // let the parent class do the copy
    super(copyPicture);
  }
  
  /**
   * Constructor that takes a buffered image
   * @param image the buffered image to use
   */
  public Picture(BufferedImage image)
  {
    super(image);
  }
  
  ////////////////////// methods ///////////////////////////////////////
  


  /**
   * Method to return a string with information about this picture.
   * @return a string with information about the picture such as fileName,
   * height and width.
   */
  public String toString()
  {
    String output = "Picture, filename " + getFileName() + 
      " height " + getHeight() 
      + " width " + getWidth();
    return output;
    
  }
  
  /** Method to set the blue to 0 */
  public void zeroBlue()
  {
    Pixel[][] pixels = this.getPixels2D();
    for (Pixel[] rowArray : pixels)
    {
      for (Pixel pixelObj : rowArray)
      {
        pixelObj.setBlue(0);
      }
    }
  }
  
  public void keepOnlyBlue()
  {
	  Pixel[][] pixels = this.getPixels2D();
	  for (Pixel[] rowArray : pixels)
	  {
		  for (Pixel pixelObj : rowArray)
		  {
			pixelObj.setRed(0);
			pixelObj.setGreen(0);
		  }
	  }
  }
  
  public void negate()
  {
	  Pixel[][] pixels = this.getPixels2D();
	  for(Pixel[] rowArray : pixels)
	  {
		  for(Pixel pixelObj : rowArray)
		  {
			  int red = 255 - pixelObj.getRed();
			  pixelObj.setRed(red);
			  int green = 255 - pixelObj.getGreen();
			  pixelObj.setGreen(green);
			  int blue = 255 - pixelObj.getBlue();
			  pixelObj.setBlue(blue);
		  }
	  }
  }
  
  public void grayScale()
  {
	  Pixel[][] pixels = this.getPixels2D();
	  for(Pixel[] rowArray : pixels)
	  {
		  for(Pixel pixelObj : rowArray)
		  {
			  int red = pixelObj.getRed();
			  int green = pixelObj.getGreen();
			  int blue = pixelObj.getBlue();
			  int average = (red + green + blue) / 3;
			  pixelObj.setRed(average);
			  pixelObj.setBlue(average);
			  pixelObj.setGreen(average);
		  }
	  }
  }
  /** To pixelate by diving the area into size x size
   * 
   */
  public void pixelate(int size)
  {
    Pixel[][] pixels = this.getPixels2D();

    int length = pixels[0].length;
    int width = pixels.length;

    // int resultLength = length/size;
    // int widthResult = width/size;
    
    int numValidPix = 0;

    int red = 0;
    int blue = 0;
    int green = 0;

	
	for(int picWidth = 0; picWidth < pixels[0].length; picWidth += size)
	{
		for(int picLength = 0; picLength < pixels.length; picLength += size)
		{
		  // reset the color values
		  red = 0;
		  green = 0;
		  blue = 0;
    
			for (int i = picLength; i < picLength + size; i++)
			{
			  for (int j = picWidth; j < picWidth + size; j++)
			  {
				  if(i < pixels.length && j < pixels[0].length)
				  {
					Pixel pixelObj = pixels[i][j];
					red += pixelObj.getRed();
					green += pixelObj.getGreen();
					blue += pixelObj.getBlue();
					numValidPix++;
				  }
			  }
			}
			int averageRed = red / (numValidPix);
			int averageGreen = green/ (numValidPix);
			int averageBlue = blue / (numValidPix);
			numValidPix = 0;
			
			for (int i = picLength; i < picLength + size; i++)
			{
				for (int j = picWidth; j < picWidth + size; j++)
				{
				  if(i < pixels.length && j < pixels[0].length)
						pixels[i][j].setColor(new Color(averageRed, averageGreen, averageBlue));
				}
			}
		}
	}
 }

  /** Method that blurs the picture
   * @param	size	Blur size, greater is more blur
   * @return 		Blurred picture
   */
  public Picture blur(int size)
  {
  	Pixel [][] pixels = this.getPixels2D();
  	Picture result = new Picture(this); 
  	Pixel [][] resultPixels = result.getPixels2D();
	   
    for(int col = 0; col < pixels[0].length; col++)
    {
      for(int row = 0; row < pixels.length; row++)
      {
        resultPixels[row][col].setColor(averagePixel(row, col, pixels, size));
      }
    }
	
    return result;
  }
  
  public Color averagePixel(int i, int j, Pixel [][] pixel, int size)
  {
	  int colStart = j - (size/2);
	  int colEnd = colStart + (size - 1);
	  
	  int rowStart = i - (size / 2);
	  int rowEnd = rowStart + (size - 1);
	  
	  int numValidPixels = 0;
	  
		int redTotal = 0;
		int greenTotal = 0;
		int blueTotal = 0; 
	  
	  for(int col = colStart; col <= colEnd; col++)
	  {
      for(int row = rowStart; row <= rowEnd; row++)
      {
        if((row >= 0 && row < pixel.length) && (col < pixel[0].length && col >= 0)) {
          redTotal += pixel[row][col].getRed();
          greenTotal += pixel[row][col].getGreen();
          blueTotal += pixel[row][col].getBlue();	
          numValidPixels++;
        }
      }
	  }

    // This is causing half of my life problems
	  // System.out.println(numValidPixels);
	  // Color returnColor = new Color(1, 1,1);
	  // if(numValidPixels != 0)
    // WHY IS THIS ZEROOOOO
    if (numValidPixels == 0)
    {
      System.out.println("Tralfaze");
    }

		Color returnColor = new Color(redTotal/numValidPixels, greenTotal/numValidPixels, blueTotal/numValidPixels);
	  return returnColor;
  }
/** Method that enhances a picture by getting average Color around
 * a pixel then applies the following formula:
 *
 * pixelColor <- 2 * currentValue - averageValue
 *
 * size is the area to sample for blur.
 *
 * @param size Larger means more area to average around pixel
 * and longer compute time.
 * @return enhanced picture
 */
  public Picture enhance(int size)
  {
    Pixel[][] pixels = this.getPixels2D();
    Picture result = new Picture(pixels.length, pixels[0].length);
    Pixel[][] resultPixels = result.getPixels2D();

    for(int col = 0; col < pixels[0].length; col++)
    {
      for(int row = 0; row < pixels.length; row++)
      {
        Color aveColor = averagePixel(row, col, pixels, size);
        int redAve = aveColor.getRed();
        int greenAve= aveColor.getGreen();
        int blueAve = aveColor.getBlue();
        
        Pixel currentPixel = pixels[row][col];
        int currentRed = currentPixel.getRed();
        int currentGreen = currentPixel.getGreen();
        int currentBlue = currentPixel.getBlue();
        
        int newRed = 2 * currentRed - redAve;
        int newGreen = 2 * currentGreen - greenAve;
        int newBlue = 2 * currentBlue - blueAve;
        resultPixels[row][col].setColor(new Color(newRed, newGreen, newBlue));
      }
    }
    return result;
  }
  
  /** Method that mirrors the picture around a 
    * vertical mirror in the center of the picture
    * from left to right */
  public void mirrorVertical()
  {
    Pixel[][] pixels = this.getPixels2D();
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int width = pixels[0].length;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; col < width / 2; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][width - 1 - col];
        rightPixel.setColor(leftPixel.getColor());
      }
    } 
  }
  
  /** Mirror just part of a picture of a temple */
  public void mirrorTemple()
  {
    int mirrorPoint = 276;
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    int count = 0;
    Pixel[][] pixels = this.getPixels2D();
    
    // loop through the rows
    for (int row = 27; row < 97; row++)
    {
      // loop from 13 to just before the mirror point
      for (int col = 13; col < mirrorPoint; col++)
      {
        
        leftPixel = pixels[row][col];      
        rightPixel = pixels[row]                       
                         [mirrorPoint - col + mirrorPoint];
        rightPixel.setColor(leftPixel.getColor());
      }
    }
  }
  
  /** copy from the passed fromPic to the
    * specified startRow and startCol in the
    * current picture
    * @param fromPic the picture to copy from
    * @param startRow the start row to copy to
    * @param startCol the start col to copy to
    */
  public void copy(Picture fromPic, 
                 int startRow, int startCol)
  {
    Pixel fromPixel = null;
    Pixel toPixel = null;
    Pixel[][] toPixels = this.getPixels2D();
    Pixel[][] fromPixels = fromPic.getPixels2D();
    for (int fromRow = 0, toRow = startRow; 
         fromRow < fromPixels.length &&
         toRow < toPixels.length; 
         fromRow++, toRow++)
    {
      for (int fromCol = 0, toCol = startCol; 
           fromCol < fromPixels[0].length &&
           toCol < toPixels[0].length;  
           fromCol++, toCol++)
      {
        fromPixel = fromPixels[fromRow][fromCol];
        toPixel = toPixels[toRow][toCol];
        toPixel.setColor(fromPixel.getColor());
      }
    }   
  }

  /** Method to create a collage of several pictures */
  public void createCollage()
  {
    Picture flower1 = new Picture("flower1.jpg");
    Picture flower2 = new Picture("flower2.jpg");
    this.copy(flower1,0,0);
    this.copy(flower2,100,0);
    this.copy(flower1,200,0);
    Picture flowerNoBlue = new Picture(flower2);
    flowerNoBlue.zeroBlue();
    this.copy(flowerNoBlue,300,0);
    this.copy(flower1,400,0);
    this.copy(flower2,500,0);
    this.mirrorVertical();
    this.write("collage.jpg");
  }
  
  
  /** Method to show large changes in color 
    * @param edgeDist the distance for finding edges
    */
  public void edgeDetection(int edgeDist)
  {
    Pixel leftPixel = null;
    Pixel rightPixel = null;
    Pixel[][] pixels = this.getPixels2D();
    Color rightColor = null;
    for (int row = 0; row < pixels.length; row++)
    {
      for (int col = 0; 
           col < pixels[0].length - 1; col++)
      {
        leftPixel = pixels[row][col];
        rightPixel = pixels[row][col+1];
        rightColor = rightPixel.getColor();
        if (leftPixel.colorDistance(rightColor) > 
            edgeDist)
          leftPixel.setColor(Color.BLACK);
        else
          leftPixel.setColor(Color.WHITE);
      }
    }
  }
  
  
  /* Main method for testing - each class in Java can have a main 
   * method 
   */
  public static void main(String[] args) 
  {
    Picture beach = new Picture("images/beach.jpg");
    Picture swan = new Picture("images/swan.jpg");
    swan.explore();
    Picture newPic = swan.enhance(10);
    //swan.pixelate(48);
    newPic.explore();
  }
  
} // this } is the end of class Picture, put all new methods before this

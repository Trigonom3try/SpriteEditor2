
package SpriteEditor2;

import java.awt.Color;
import java.io.Serializable;

public class Image implements Serializable
{
    public int width;
    public int height;
    public String name = "Untitled";
    
    public Color [][] colors;
    
    protected Image(Image i)     // creates an image from another image
    {
        width = i.width;    height = i.height;  colors = i.colors;
    }
    protected Image(int w, int h)   // constructs an empty image of specified dimensions
    {
        width = w;
        height = h;
        colors = new Color[height][width];
        
        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x)
                colors[y][x] = null;
    }
    protected Image(Image i, int x1, int y1, int x2, int y2)    // creates an image by cutting out a piece of another image
    {
        width = Math.abs(x2 - x1) + 1;
        height = Math.abs(y2 - y1) + 1;
        colors = new Color[height][width];
             
        int startX = Math.min(x1, x2);
        int startY = Math.min(y1, y2);
        
        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x)           
                colors[y][x] = i.colors[startY + y][startX + x];            
    }
    
    protected void cut(int x1, int y1, int x2, int y2)      // cuts out a rectangular chunk of the image
    {
        int startX = Math.min(x1, x2);
        int startY = Math.min(y1, y2);
        
        int endX = Math.max(x1, x2);
        int endY = Math.max(y1, y2);
        
        for (int y = startY; y <= endY; ++y)
            for (int x = startX; x <= endX; ++x)
                colors[y][x] = null;
    }
    
    protected void reverse(int x1, int y1, int x2, int y2)  // reverses a chunk of the image from left to right
    {
        int startX = Math.min(x1, x2);       
        int startY = Math.min(y1, y2);
        
        int endX = Math.max(x1, x2);
        int endY = Math.max(y1, y2);
        
        int midX = ( endX - startX ) / 2;
        
        for (int y = 0; y <= endY - startY; ++y)
            for (int x = 0; x <= midX; ++x)
            {
                Color temp = colors[startY + y][startX + x];
                colors[startY + y][startX + x] = colors[startY + y][endX - x];
                colors[startY + y][endX - x] = temp;
            }
    }
    
    protected void flip(int x1, int y1, int x2, int y2)     // flips a chunk of the image from top to bottom
    {
        int startX = Math.min(x1, x2);       
        int startY = Math.min(y1, y2);
        
        int endX = Math.max(x1, x2);
        int endY = Math.max(y1, y2);
        
        int midY = ( endY - startY ) / 2;
        
        for (int y = 0; y <= midY; ++y)
            for (int x = 0; x <= endX - startX; ++x)
            {
                Color temp = colors[startY + y][startX + x];
                colors[startY + y][startX + x] = colors[endY - y][startX + x];
                colors[endY - y][startX + x] = temp;
            }
    }
    
    public String toString()
    {
        String s = "Printing image ... " + "\tWidth = " + width + "\tHeight = " + height + "\n";
        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x)
            {
                if (colors[y][x] == null) s += "t\tColors[" + y + "][" + x + "] = NULL";
                else s += "t\tColors[" + y + "][" + x + "] = " + colors[y][x].toString() + "\n";
            }
        return s;
    }
}


package SpriteEditor2;

import java.awt.Color;

public class Brush 
{
    protected enum BrushMode { NORMAL, MOVE, ERASE, FILL, SELECT, PASTE, DRAW_LINE, DRAW_RECT, DRAW_OVAL }    // no copy or cut
    
    protected Color color;
    protected BrushMode brushMode;
    protected boolean [][] shape = { {true} };  // init the shape to 1x1, a single pixel filler
    protected boolean [][] tempShape = shape;   // it is important to store the current shape when swictching to drawLine, drawRect, and drawOval, so that it switches back after
    protected Image copied;
    protected int X;            // brush location coordinates are actual pixel values *
    protected int Y;
    protected boolean hasP1;    // default == false
    protected boolean hasP2;
    protected int X1;       // selection, movement, and draw_shape coordinates are all actual pixel values
    protected int X2;
    protected int Y1;
    protected int Y2;
    
    protected Brush()
    {
        color = Color.black;
        brushMode = BrushMode.NORMAL;
    }
}

    
                            //  Oh tell you me you love me, I need someone, on days like this I do, on days like this I do
                            //  You ain't nobody til you got somebody, you ain't nobody til you got somebody
                            //  No You ain't nobody til you got somebody, you ain't nobody til you got somebody 
                          

package SpriteEditor2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;

public class Canvas extends JPanel      
{
    protected int pixelSize = 10;              // this just gets inited to 10          
    protected final Color defaultColor = Color.LIGHT_GRAY;
    protected final int sideMargin = 10;      // refers to the area on the left, right, and top of the rectangular "frame"
    protected final int bottomMargin = 75;    // refers to the area below the frame, just above the button panel
    
    protected Sprite sprite;
    protected Layer activeLayer;
    protected ArrayList<Layer> lastCopies = new ArrayList<>();      // keep a bunch of copies of prior states of the image for the UNDO function * (not too many, tho...)
    
    protected int camX = 0;
    protected int camY = 0;
    
    protected int frameHeight;
    protected int frameWidth;
    
    protected boolean drawGrid = true;
    
    protected int pixelWidth = pixelSize;
    protected int pixelHeight = pixelSize;

    protected int virtualPixelX = 0;
    protected int virtualPixelY = 0;

    protected int selectedX1 = -10;
    protected int selectedY1 = -10;
    protected int selectedX2 = -10;
    protected int selectedY2 = -10;
    
    protected Canvas(Sprite s)
    {
        sprite = s;
        activeLayer = sprite.layers.get(0);  //  here we set the active layer to the first layer in the spriteSS
        activeLayer.active = true;  
        
        for (Layer l : sprite.layers)
            if (l != activeLayer) l.active = false;
         // need this loop b/c when we save sprites, sometimes we save them from a layer that is not the first, thus making the active boolean true when the sprite gets collapsed
        
        addMouseListener(new MouseClickListener());
        addMouseMotionListener(new MouseMovementListener());
      
    }
    
    
    
    
    
    
    
    
    
    public void paintComponent(Graphics g)      // this is the longest, craziest repaint method I've written and seen so far
    { 
      //  System.out.println("in Canvas repaint()");
        g.setColor(defaultColor);
        g.fillRect(0, 0, SpriteEditor2.editorWindow.getWidth(), SpriteEditor2.editorWindow.getHeight());
         
        setFrameDimensions();
     
        for (int y = 0; y < frameHeight; y += pixelHeight)
            for (int x = 0; x < frameWidth; x += pixelWidth)
            {       
                                    
                virtualPixelX = (camX + x) / pixelSize;             // this section is where we determine where to start rendering each rect and their dimensions with maths
                virtualPixelY = (camY + y) / pixelSize;
                
                if ((camX + x) % pixelSize != 0) pixelWidth = pixelSize - (camX % pixelSize); 
                else if (x + pixelSize > frameWidth) 
                {
                    if (camX % pixelSize != 0) pixelWidth = camX % pixelSize;
                    pixelWidth = pixelSize; 
                }
                else pixelWidth = pixelSize;
                
                if ((camY + y) % pixelSize != 0 ) pixelHeight = pixelSize - (camY % pixelSize);  
                else if (y + pixelSize > frameHeight) 
                {
                    if (camY % pixelSize != 0) pixelHeight = camY % pixelSize;
                    else pixelHeight = pixelSize;
                }
                else pixelHeight = pixelSize;
                
                
                
                for (Layer l: sprite.layers)        // looping through the layers on each vPixel is easier than doing the vice versa. Maybe?
                    if (l.active) 
                    {
                        if (virtualPixelX < l.Xoffset | virtualPixelX >= l.width + l.Xoffset | virtualPixelY < l.Yoffset | virtualPixelY >= l.height + l.Yoffset) continue;
                        if (l.colors[virtualPixelY - l.Yoffset][virtualPixelX - l.Xoffset] == null) 
                        {
                            g.setColor(defaultColor.brighter());
                            for (Layer r : sprite.layers)
                            {
                                if (!(!r.active && r.visible)) continue;
                                if (virtualPixelX < r.Xoffset | virtualPixelX >= r.width + r.Xoffset | virtualPixelY < r.Yoffset | virtualPixelY >= r.height + r.Yoffset) continue; 
                                if (r.colors[virtualPixelY - r.Yoffset][virtualPixelX - r.Xoffset] != null) 
                                    g.setColor(r.colors[virtualPixelY - r.Yoffset][virtualPixelX - r.Xoffset]);
                            }
                        } 
                        else g.setColor(l.colors[virtualPixelY - l.Yoffset][virtualPixelX - l.Xoffset]); 
                        g.fillRect(x + sideMargin, y + sideMargin, pixelWidth, pixelHeight);
                        break;      // doesn't bother looping through layers after coloring the active
                    }
                    else if (l.visible)
                    {
                        if (virtualPixelX < l.Xoffset | virtualPixelX >= l.width + l.Xoffset | virtualPixelY < l.Yoffset | virtualPixelY >= l.height + l.Yoffset) continue;
                        if (l.colors[virtualPixelY - l.Yoffset][virtualPixelX - l.Xoffset] == null) continue;
                        g.setColor(l.colors[virtualPixelY - l.Yoffset][virtualPixelX - l.Xoffset].darker());     // darken colors of subactive layers
                        g.fillRect(x + sideMargin, y + sideMargin, pixelWidth, pixelHeight);
                    } 
                if (drawGrid & pixelSize >= 4)
                {
                    g.setColor(Color.black);
                    g.drawRect(x + sideMargin, y + sideMargin, pixelWidth, pixelHeight);
                }
                
                switch (SpriteEditor2.brush.brushMode)
                {
                    case SELECT :      
                    {
                        if (SpriteEditor2.brush.hasP2)
                        {
                            if (virtualPixelX == SpriteEditor2.brush.X1) selectedX1 = x;    // sideMargin to be added to these at draw time
                            if (virtualPixelX == SpriteEditor2.brush.X2) selectedX2 = x + pixelWidth;
                            if (virtualPixelY == SpriteEditor2.brush.Y1) selectedY1 = y;
                            if (virtualPixelY == SpriteEditor2.brush.Y2) selectedY2 = y + pixelHeight;  // we add these to X2,Y2 such that the rect gets drawn to the end of the vpixel
                        }
                        break;
                    }
                    case FILL :
                    {
                        break;
                    }
                    case MOVE :
                    {
                        break;
                    }
                    case PASTE :
                    {
                        break;
                    }
                    case NORMAL :   // colors up to 1 vpixel of the brush shape for brushmode Normal
                    { 
                        if (SpriteEditor2.brush.X + ( SpriteEditor2.brush.shape[0].length/2 ) * pixelSize < sideMargin) break;
                        if (SpriteEditor2.brush.Y + ( SpriteEditor2.brush.shape.length/2 ) * pixelSize < sideMargin) break;
                        if (SpriteEditor2.brush.X - ( SpriteEditor2.brush.shape[0].length/2 ) * pixelSize > frameWidth + sideMargin) break;
                        if (SpriteEditor2.brush.Y - ( SpriteEditor2.brush.shape.length/2 ) * pixelSize > frameHeight + sideMargin) break;
                        
                        int virtualBrushX = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize;
                        int virtualBrushY = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize;
                        int deltaX = virtualBrushX - virtualPixelX;
                        int deltaY = virtualBrushY - virtualPixelY;
                        int Xindex = SpriteEditor2.brush.shape[0].length/2 + deltaX;
                        int Yindex = SpriteEditor2.brush.shape.length/2 + deltaY;
                        
                        try 
                        { 
                            if (Math.abs(deltaX) > SpriteEditor2.brush.shape[0].length/2 | Math.abs(deltaY) > SpriteEditor2.brush.shape.length/2) break;
                            if (SpriteEditor2.brush.shape[SpriteEditor2.brush.shape.length/2 + deltaY][SpriteEditor2.brush.shape[0].length/2 + deltaX])
                            {
                                g.setColor(SpriteEditor2.brush.color);
                                g.fillRect(x + sideMargin, y + sideMargin, pixelWidth, pixelHeight);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException aio) 
                        {
                        /*     Actually, this is sort of brilliant.  Suppose you had a 10 x 10 brush shape, and the brush was centered at vp(15,15) , and the
                               vpixel you were testing was at vp1(10,15) : deltaX would then be equal to 15-10 = 5, such that deltaX + shape.length/2 = 5+5 = 10, which
                               is out of bounds for the array. If the vpixel you were testing was vp2(20,15), deltaX would = 15-20 = -5, such that 
                               deltaX + shape.length/2 = 5-5 = 0, which is the first element in the shape array; this means that the brush gets drawn from Right to Left !!**
                               Suppose you were testing vp3(11,15) : deltaX = 15-11 = 4, deltaX + half = 4+5 = 9, which is the last element in the array * 
                               For the program to pass the first conditional, but still fail the second, does not mean that array elements of the brush shape get skipped over
                               because the elements at those vpixels which fail the second conditional, as demonstrated here, actually fall out of bounds of the array.
                               There really is no way that i can think of to create more fool-proof logic than what I've set down, except for simply allowing the compiler
                               to throw the exception, and instead of having the program terminate, simply do nothing because as I said it doesn't interfere
                               with the elements that are actually in the brush, and the loop can just continue to check the next vpixel    
                            
                               I do not understand either my commentary or my code anymore but I do not care because this is all for Caramia.  */
                        }
                        break;
                    }
                    case ERASE :    // virtually the same as Normal, colors up to 1 vpixel of the brushshape defaultColor.brighter for brushmode Erase
                    {
                        if (SpriteEditor2.brush.X + ( SpriteEditor2.brush.shape[0].length/2 ) * pixelSize < sideMargin) break;
                        if (SpriteEditor2.brush.Y + ( SpriteEditor2.brush.shape.length/2 ) * pixelSize < sideMargin) break;
                        if (SpriteEditor2.brush.X - ( SpriteEditor2.brush.shape[0].length/2 ) * pixelSize > frameWidth + sideMargin) break;
                        if (SpriteEditor2.brush.Y - ( SpriteEditor2.brush.shape.length/2 ) * pixelSize > frameHeight + sideMargin) break;
                        
                        int virtualBrushX = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize;
                        int virtualBrushY = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize;
                        int deltaX = virtualBrushX - virtualPixelX;
                        int deltaY = virtualBrushY - virtualPixelY;
                        int Xindex = SpriteEditor2.brush.shape[0].length/2 + deltaX;
                        int Yindex = SpriteEditor2.brush.shape.length/2 + deltaY;
                        
                        try 
                        { 
                            if (Math.abs(deltaX) > SpriteEditor2.brush.shape[0].length/2 | Math.abs(deltaY) > SpriteEditor2.brush.shape.length/2) break;
                            if (SpriteEditor2.brush.shape[SpriteEditor2.brush.shape.length/2 + deltaY][SpriteEditor2.brush.shape[0].length/2 + deltaX])
                            {
                                g.setColor(defaultColor.brighter());
                                g.fillRect(x + sideMargin, y + sideMargin, pixelWidth, pixelHeight);
                            }
                        }
                        catch (ArrayIndexOutOfBoundsException aio) {}
                        break;
                    }
                    
                    
                    default :   // for modes drawRect, drawLine, and drawOval. Similar to normal and erase except that it starts at one end of the shape instead of the middle
                    { 
                        if (!SpriteEditor2.brush.hasP1) break;
                        int virtualBrushX = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize;
                        int virtualBrushY = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize;
                        
                        int startX = Math.min(virtualBrushX, SpriteEditor2.brush.X1 + activeLayer.Xoffset);
                        int startY = Math.min(virtualBrushY, SpriteEditor2.brush.Y1 + activeLayer.Yoffset);
                        
                        int endX = Math.max(virtualBrushX, SpriteEditor2.brush.X1 + activeLayer.Xoffset);
                        int endY = Math.max(virtualBrushY, SpriteEditor2.brush.Y1 + activeLayer.Yoffset);
                        
                        if (virtualPixelX < startX | virtualPixelY < startY | virtualPixelX > endX | virtualPixelY > endY) break;
                        
                        if (SpriteEditor2.brush.shape[virtualPixelY - startY][virtualPixelX - startX]) 
                            {
                                g.setColor(SpriteEditor2.brush.color);
                                g.fillRect(x + sideMargin, y + sideMargin, pixelWidth, pixelHeight);
                            }
                    }
                }
            }               
                        // end of for-loop
        
        
            if (SpriteEditor2.brush.brushMode == Brush.BrushMode.SELECT)
            {
                if (SpriteEditor2.brush.hasP2)
                {
                    g.setColor(Color.GRAY);
                    int startX = Math.min(selectedX1, selectedX2);
                    int startY = Math.min(selectedY1, selectedY2);
                    int width = Math.max(selectedX1, selectedX2) - startX;
                    int height = Math.max(selectedY1, selectedY2) - startY;
                    
                    g.drawRect(startX + sideMargin, startY + sideMargin, width, height);
                    g.drawRect(startX + sideMargin - 1, startY + sideMargin - 1, width - 1, height - 1);
                    g.drawRect(startX + sideMargin + 1, startY + sideMargin + 1, width + 1, height + 1);      
                }
                else if (SpriteEditor2.brush.hasP1)
                {
                    g.setColor(Color.GRAY);     
                    int startX = Math.min(SpriteEditor2.brush.X, SpriteEditor2.brush.X1);
                    int startY = Math.min(SpriteEditor2.brush.Y, SpriteEditor2.brush.Y1);
                    int width = Math.max(SpriteEditor2.brush.X, SpriteEditor2.brush.X1) - startX;
                    int height = Math.max(SpriteEditor2.brush.Y, SpriteEditor2.brush.Y1) - startY;
                    
                    g.drawRect(startX, startY, width, height);
                }
            }
        }          
                    // end of paintComponent()
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    class MouseClickListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
      //      System.out.println("MOUSE CLICKED");
            int virtualBrushX = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize - activeLayer.Xoffset;    // the offsets need to be subtracted somewhere along the line
            int virtualBrushY = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize - activeLayer.Yoffset;    // so I figured, subtract them once, here, and make code readable
     //       System.out.println("VbrushX = " + virtualBrushX + "\tVbrushY = " + virtualBrushY);
            switch (SpriteEditor2.brush.brushMode)
            {
                case NORMAL :
                {
                    drawBrushLogic(virtualBrushX, virtualBrushY, false);    break;
                }
                case ERASE :
                {
                    drawBrushLogic(virtualBrushX, virtualBrushY, true);    break;
                }
                case FILL :
                {
                    fillLogic(virtualBrushX, virtualBrushY, activeLayer.colors[virtualBrushY][virtualBrushX]);
                    break;
                }
                case PASTE :
                {
               //     System.out.println("In PASTE : ");
                    if (SpriteEditor2.brush.copied == null) break;
                    for (int y = 0; y < SpriteEditor2.brush.copied.height; ++y)
                        for (int x = 0; x < SpriteEditor2.brush.copied.width; ++x)
                        {                        
                            int X = virtualBrushX - SpriteEditor2.brush.copied.width/2 + x;
                            int Y = virtualBrushY - SpriteEditor2.brush.copied.height/2 + y;
                      //      System.out.println("\tx = " + x + "\ty = " + y + "\tX = " + X + "\tY = " + Y);
                            if (X < 0 | Y < 0 | X >= activeLayer.width | Y >= activeLayer.height) 
                            {
                       //         System.out.println("\tX = " + X + "\tY = " + Y + ", continuing ...");
                                continue;
                            }
                     //       if (SpriteEditor2.brush.copied.colors[y][x] == null) System.out.println("\tSetting colors[" + Y + "][" + X + "] to NULL");
                     //       else System.out.println("\tSetting colors[" + Y + "][" + X + "] to " + SpriteEditor2.brush.copied.colors[y][x].toString());
                            activeLayer.colors[Y][X] = SpriteEditor2.brush.copied.colors[y][x];
                        }                    
                    break;
                }
                case SELECT :
                {
                    SpriteEditor2.brush.hasP1 = false;      SpriteEditor2.brush.hasP2 = false;      // don't need to reset coordinate values, that gets done elsewhere
                    break;
                }
                case MOVE :
                {
                    break;
                }
                default :
                {
                    break;
                }    
            }
            repaint();          
        }
        public void mouseEntered(MouseEvent e)
        {
            SpriteEditor2.brush.X = e.getX();
            SpriteEditor2.brush.Y = e.getY();
            repaint();
        }
        public void mouseExited(MouseEvent e)
        {
            SpriteEditor2.brush.X = -100;
            SpriteEditor2.brush.Y = -100;
            repaint();
        }
        public void mousePressed(MouseEvent e)
        {
            switch (SpriteEditor2.brush.brushMode)
            {
                case MOVE :
                {
                    SpriteEditor2.brush.X1 = SpriteEditor2.brush.X;
                    SpriteEditor2.brush.Y1 = SpriteEditor2.brush.Y;
                    
                    break;
                }
                case SELECT :
                {                  
                    SpriteEditor2.brush.hasP1 = true;
                    SpriteEditor2.brush.hasP2 = false;
                    SpriteEditor2.brush.X1 = SpriteEditor2.brush.X;
                    SpriteEditor2.brush.Y1 = SpriteEditor2.brush.Y; 
                    break;
                }
                case DRAW_RECT :
                {
            //        System.out.println("In DRAW_RECT : ");
                    SpriteEditor2.brush.X1 = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize - activeLayer.Xoffset;
                    SpriteEditor2.brush.Y1 = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize - activeLayer.Yoffset;
           //         System.out.println("\tX1 = " + SpriteEditor2.brush.X1 + "Y1 = " + SpriteEditor2.brush.Y1);
                    SpriteEditor2.brush.hasP1 = true;
                    break;
                }
                case DRAW_LINE :
                {
                    SpriteEditor2.brush.X1 = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize - activeLayer.Xoffset;
                    SpriteEditor2.brush.Y1 = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize - activeLayer.Yoffset;
                    SpriteEditor2.brush.hasP1 = true;
                    break;
                }
            }
        }
        public void mouseReleased(MouseEvent e)     // mouseReleased is where the brush shape is finalized and reset and the vpixels colored for each drawShape mode
        {
            switch (SpriteEditor2.brush.brushMode)
            {
                case SELECT :
                {
                    SpriteEditor2.brush.X2 = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize;          // these are virtual pixel values
                    SpriteEditor2.brush.Y2 = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize; 
                    SpriteEditor2.brush.X1 = ( SpriteEditor2.brush.X1 - sideMargin + camX ) / pixelSize;
                    SpriteEditor2.brush.Y1 = ( SpriteEditor2.brush.Y1 - sideMargin + camY ) / pixelSize;
                    
                    if (SpriteEditor2.brush.X1 < activeLayer.Xoffset) SpriteEditor2.brush.X1 = activeLayer.Xoffset;
                    else if (SpriteEditor2.brush.X1 >= activeLayer.width + activeLayer.Xoffset) SpriteEditor2.brush.X1 = activeLayer.width - 1 + activeLayer.Xoffset;
                    if (SpriteEditor2.brush.X2 < activeLayer.Xoffset) SpriteEditor2.brush.X2 = activeLayer.Xoffset;
                    else if (SpriteEditor2.brush.X2 >= activeLayer.width + activeLayer.Xoffset) SpriteEditor2.brush.X2 = activeLayer.width - 1 + activeLayer.Xoffset;
                    
                    if (SpriteEditor2.brush.Y1 < activeLayer.Yoffset) SpriteEditor2.brush.Y1 = activeLayer.Yoffset;
                    else if (SpriteEditor2.brush.Y1 >= activeLayer.height + activeLayer.Yoffset) SpriteEditor2.brush.Y1 = activeLayer.height - 1 + activeLayer.Yoffset;
                    if (SpriteEditor2.brush.Y2 < activeLayer.Yoffset) SpriteEditor2.brush.Y2 = activeLayer.Yoffset;
                    else if (SpriteEditor2.brush.Y2 >= activeLayer.height + activeLayer.Yoffset) SpriteEditor2.brush.Y2 = activeLayer.height - 1 + activeLayer.Yoffset;
                    
                    SpriteEditor2.brush.hasP2 = true;                   
                    break;
                }
                case DRAW_RECT :
                {
                    SpriteEditor2.brush.X2 = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize - activeLayer.Xoffset; 
                    SpriteEditor2.brush.Y2 = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize - activeLayer.Yoffset;
                    
                    drawShapeLogic();
                    
                    SpriteEditor2.brush.shape = new boolean[1][1];
                    SpriteEditor2.brush.shape[0][0] = true;
                    SpriteEditor2.brush.hasP1 = false;
                }
                case DRAW_LINE :
                {
                    SpriteEditor2.brush.X2 = ( SpriteEditor2.brush.X - sideMargin + camX ) / pixelSize - activeLayer.Xoffset; 
                    SpriteEditor2.brush.Y2 = ( SpriteEditor2.brush.Y - sideMargin + camY ) / pixelSize - activeLayer.Yoffset;
                    
                    drawShapeLogic();
                    
                    SpriteEditor2.brush.shape = new boolean[1][1];
                    SpriteEditor2.brush.shape[0][0] = true;
                    SpriteEditor2.brush.hasP1 = false;
                }
                default :
                {
                    break;
                }
            }
            repaint();
        }
    }
    class MouseMovementListener implements MouseMotionListener
    {
        public void mouseMoved(MouseEvent e)
        {
            SpriteEditor2.brush.X = e.getX();
            SpriteEditor2.brush.Y = e.getY();
            repaint();
        }
        public void mouseDragged(MouseEvent e)
        {
            SpriteEditor2.brush.X = e.getX();
            SpriteEditor2.brush.Y = e.getY();
            
            int virtualBrushX = (SpriteEditor2.brush.X - sideMargin + camX) / pixelSize - activeLayer.Xoffset;
            int virtualBrushY = (SpriteEditor2.brush.Y - sideMargin + camY) / pixelSize - activeLayer.Yoffset;
            
       //    System.out.println("MOUSE DRAGGED");
       //     System.out.println("\tVbrushX = " + virtualBrushX + "\tVbrushY = " + virtualBrushY);
            
            switch (SpriteEditor2.brush.brushMode)
            {
                case NORMAL :
                {
                    drawBrushLogic(virtualBrushX, virtualBrushY, false);    break;
                }
                case ERASE :
                {
                    drawBrushLogic(virtualBrushX, virtualBrushY, true);     break;
                }
                case DRAW_LINE :
                {
                    SpriteEditor2.brush.shape = drawLine(SpriteEditor2.brush.X1, SpriteEditor2.brush.Y1, virtualBrushX, virtualBrushY);
                    break;
                }
                case DRAW_RECT :
                {
                    SpriteEditor2.brush.shape = drawRect(SpriteEditor2.brush.X1, SpriteEditor2.brush.Y1, virtualBrushX, virtualBrushY);
                    break;
                }
                case DRAW_OVAL :
                {
                    // create the brush shape
                    break;
                }
                case MOVE :
                {
                    int deltaX = ( SpriteEditor2.brush.X1 - SpriteEditor2.brush.X ) / pixelSize;    // dividing these by something slows the movement down to be more controllable
                    int deltaY = ( SpriteEditor2.brush.Y1 - SpriteEditor2.brush.Y ) / pixelSize;    // still mad freaking wonky tho, for at least one reason ...
                    
                    if (camX + deltaX < 0) deltaX = -camX;
                    if (camY + deltaY < 0) deltaY = -camY;
                    if (camX + frameWidth + deltaX > sprite.width * pixelSize) deltaX = sprite.width*pixelSize - frameWidth - camX;
                    if (camY + frameHeight + deltaY > sprite.height * pixelSize) deltaY = sprite.height*pixelSize -frameHeight - camY;
                    
                    camX += deltaX;     camY += deltaY;
                    break;
                }
                default :
                {
                    break;
                }
            }
            repaint();
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    protected void drawBrushLogic(int virtualBrushX, int virtualBrushY, boolean eraserOn)       //  colors from the center of the brush shape
    {
        int virtualPixelX = 0;
        int virtualPixelY = 0;
        for (int y = 0; y < SpriteEditor2.brush.shape.length; ++y)
            for (int x = 0; x < SpriteEditor2.brush.shape[y].length; ++x)
            {
                if (!SpriteEditor2.brush.shape[y][x]) continue;
                virtualPixelX = virtualBrushX - SpriteEditor2.brush.shape[0].length/2 + x;
                virtualPixelY = virtualBrushY - SpriteEditor2.brush.shape.length/2 + y;
                if (SpriteEditor2.brush.shape.length % 2 == 0) virtualPixelY ++;        // we have to offset the coloring by 1 for even-sized brushes, just like we had to 
                if (SpriteEditor2.brush.shape[0].length % 2 == 0) virtualPixelX ++;     // offset the drawing of the brush by 1 to align more properly with the cursor
                
                if (virtualPixelX < 0 | virtualPixelY < 0 | virtualPixelX >= activeLayer.width | virtualPixelY >= activeLayer.height) continue;

                if (eraserOn) activeLayer.colors[virtualPixelY][virtualPixelX] = null;
                else activeLayer.colors[virtualPixelY][virtualPixelX] = SpriteEditor2.brush.color;
            }
    }
    
    protected void drawShapeLogic()     // colors the shape of the brush from the origin shape[0][0] i.e. top right, instead of the center, for lines, rects, and ovals 
                                        // doesn't need parameters, instead references brush data fields
    {
        int virtualPixelX = 0;
        int virtualPixelY = 0;
        
        int startX = Math.min(SpriteEditor2.brush.X1, SpriteEditor2.brush.X2);
        int startY = Math.min(SpriteEditor2.brush.Y1, SpriteEditor2.brush.Y2);
        
        int endX = Math.max(SpriteEditor2.brush.X1, SpriteEditor2.brush.X2);
        int endY = Math.max(SpriteEditor2.brush.Y1, SpriteEditor2.brush.Y2);
        
        try 
        {
            for (int y = 0; y <= endY - startY; ++y)
                for (int x = 0; x <= endX - startX; ++x)
                {
                    if (!SpriteEditor2.brush.shape[y][x]) continue;     // this throws an AIOE, and instead of finding out why I have the program catch it and do nothing
                    virtualPixelX = startX + x;
                    virtualPixelY = startY + y;

                    if (virtualPixelX < 0 | virtualPixelY < 0 | virtualPixelX >= activeLayer.width | virtualPixelY >= activeLayer.height) continue;

                    activeLayer.colors[virtualPixelY][virtualPixelX] = SpriteEditor2.brush.color;
                }
        }   catch (ArrayIndexOutOfBoundsException aio) {}
    }
    
    
    
    
    
    
    
    
    
    
    protected void fillLogic(int x, int y, Color c)    // c is the color of the tile clicked on                                                      
    {
        try 
        {
            if (y + 1 < activeLayer.height)
            {
                activeLayer.colors[y][x] = SpriteEditor2.brush.color;
                if (activeLayer.colors[y + 1][x] == c) fillLogic(x, y + 1, c);
            }
            if (x + 1 < activeLayer.width)
            {
                if (activeLayer.colors[y][x + 1] == c) fillLogic(x + 1, y, c);
                activeLayer.colors[y][x] = SpriteEditor2.brush.color;
            }
            if (y - 1 >= 0)
            {
                if (activeLayer.colors[y - 1][x] == c) fillLogic(x, y - 1, c);
                activeLayer.colors[y][x] = SpriteEditor2.brush.color;
            }
            if (x - 1 >= 0)
            {
                if (activeLayer.colors[y][x - 1] == c) fillLogic(x - 1, y, c);
                activeLayer.colors[y][x] = SpriteEditor2.brush.color;
            } 
        }
        catch (StackOverflowError so) {} // this creates a stack overflow for greater than 100^2 pixels, doesnt matter just click again on the screen or draw big rects and fill them
    }  
    
    
    protected void undo()
    {
        if (lastCopies.size() == 0) return;
        activeLayer = lastCopies.get(lastCopies.size()-1-1);
        lastCopies.remove(lastCopies.size()-1);
        repaint();
    }
    
    protected void setActiveLayer(int layerIndex)
    {
        activeLayer.active = false;
        activeLayer = sprite.layers.get(layerIndex);
        activeLayer.active = true;
        if (activeLayer.visible) SpriteEditor2.editorWindow.buttonPanel.setVisible.setSelected(true);
        else SpriteEditor2.editorWindow.buttonPanel.setVisible.setSelected(false);
        SpriteEditor2.editorWindow.setTitleBarText();
        repaint();
    }
    
    protected void setPixelSize(int ps)
    {
        double ratioX = toDouble(camX) / toDouble(sprite.width * pixelSize);
        double ratioY = toDouble(camY) / toDouble(sprite.height * pixelSize);
        
        pixelSize = ps;
        
        camX = toInt( toDouble(sprite.width * pixelSize) * ratioX );
        camY = toInt( toDouble(sprite.height * pixelSize) * ratioY );
        repaint();
    }
    
    public void setFrameDimensions()     
    {
        if (sprite.width*pixelSize >= SpriteEditor2.editorWindow.getWidth() - sideMargin - sideMargin) frameWidth = SpriteEditor2.editorWindow.getWidth() - sideMargin - sideMargin;
        else frameWidth = sprite.width*pixelSize;
        if (sprite.height*pixelSize >= SpriteEditor2.editorWindow.getHeight() - sideMargin - bottomMargin) frameHeight = SpriteEditor2.editorWindow.getHeight() - sideMargin - bottomMargin;
        else frameHeight = sprite.height*pixelSize;         
    }
    
    protected double toDouble(int a)        // ahh, where'd these two come from?  ;)
    {
        return ( (double)a ) / 100;
    }
    protected int toInt(double d)
    {
        return (int)(100 * d);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    //  these next three methods each return a brush-shape which is a 2d array of booleans
    
    
    protected boolean [][] drawRect(int x1, int y1, int x2, int y2)     // the algorithm for plotting a rectangle is an easy one
    {
        System.out.println("In drawRect : ");
        System.out.println("\tx1 = " + x1 + "\ty1 = " + y1 + "\t\tx2 = " + x2 + "\ty2 = " + y2);
        int width = Math.abs(x1 - x2) + 1;
        int height = Math.abs(y1 - y2) + 1;
        
        boolean [][] rectShape = new boolean[height][width];
        
        for (int y = 0; y < height; ++y)
            for (int x = 0; x < width; ++x)
            {
                if (y == 0 | y == height - 1 | x == 0 | x == width - 1) rectShape[y][x] = true;
                else rectShape[y][x] = false;
            }
        return rectShape;
    }
    
    
    
    
    
    protected boolean [][] drawLine(int x1, int y1, int x2, int y2)     // This is my implementation of the naive line drawing algorithm. It's not that slow on i7 
                                                                        // and it performs well enough for my standards (only drops a pixel here and there, etc)
    {
        int deltaX = x2 - x1;
        int deltaY = y2 - y1;
        
        boolean [][] lineShape = new boolean[Math.abs(deltaY) + 1][Math.abs(deltaX) + 1]; 
        
        if (deltaX == 0)
        {
            for (int y = 0; y < lineShape.length; ++y)
                lineShape[y][0] = true;
            return lineShape;
        }
        if (deltaY == 0)
        {
            for (int x = 0; x < lineShape[0].length; ++x)
                lineShape[0][x] = true;
            return lineShape;
        }
        
        double m = (double)deltaY / deltaX;     // the slope of the line from p0 to pEnd
                                                // in the program, the slope is actually the opposite sign from what you'd expect because y decreases in raster displays
                                                // the y intercept is unnecessary because all lines get plotted from brush.shape[0][0] which is basically the origin

        if (m > 1)      // steep case : for every y, one x : positive (but really negative :P )
        {
            int x = 0;      
            
            for (int y = 0; y < lineShape.length; ++y)
            {                            
                x = round( y / m);      // given each y and the slope, we solve for x 
                lineShape[y][x] = true;
            }
        }
        else if (m > 0 & m <= 1)    // narrow case : for every x, one y : positive
        {
            int y = 0;
            for (int x = 0; x < lineShape[0].length; ++x)
            {
                y = round( m * x );     // given each x and the slope, we solve for y
                lineShape[y][x] = true;
            }
        }
        else if (m < 0 & m >= -1)
        {
            m = Math.abs(m);
            int y = 0;
            for (int x = 0; x < lineShape[0].length; ++x)
            {
                y = round( m * x );
                lineShape[lineShape.length - 1 - y][x] = true;      // this is how we populate the shape "from the bottom"
            }
        }
        else        // m < -1, "negative" but really positive 
        {
            m = Math.abs(m);
            int x = 0;
            for (int y = 0; y < lineShape.length; ++y)
            {
                x = round( y / m);
                lineShape[y][lineShape[0].length - 1 - x] = true;   // populate the shape "from the right"
            }
        }
            
        return lineShape;
    }
    
    
    
    
    
    protected boolean [][] drawOval(int x1, int y1, int x2, int y2)
    {
        boolean [][] ovalShape = new boolean[Math.abs(y2 - y1) + 1][Math.abs(x2 - x1) + 1];
        
        return ovalShape;
    }
    
    
    
    protected int round(double d)
    {
        if (d % (int)d >= .5) return (int)d + 1;
        return (int)d;
    }
}

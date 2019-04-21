
package SpriteEditor2;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.*;
import java.awt.GridLayout;

public class BrushTools extends JFrame
{
    protected BrushShapePreviewer squareShapePreviews = new BrushShapePreviewer(true);
    protected BrushShapePreviewer circleShapePreviews = new BrushShapePreviewer(false);
    protected ButtonPanel buttonPanel = new ButtonPanel();
    
    protected BrushTools()
    {
        setSize(706,238 + 162);       // do not change these hard-coded values, they are proven ("magic constants")
        setTitle("Brush Tools");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3,1));
        add(squareShapePreviews);
        add(circleShapePreviews);
        add(buttonPanel);
        setVisible(true);
    }
    
    
    class BrushShapePreviewer extends JPanel
    {
        protected BrushShape [] shapes;
        protected boolean squareCircle;         // true for square, false for circle
        
        protected BrushShapePreviewer(boolean sc)
        {
            squareCircle = sc;
            if (sc) shapes = new BrushShape[10];
            else shapes = new BrushShape[8];
            for (int i = 0; i < shapes.length; ++i)
                shapes[i] = new BrushShape(i+1, squareCircle);
            addMouseListener(new ShapeSelector());
        }
        public void paintComponent(Graphics g)
        {
            g.setColor(SpriteEditor2.canvas.defaultColor);
            g.fillRect(0,0,700,140);
            g.setColor(Color.black);
            
            for (int i = 0; i < shapes.length; ++i)
            {
                if (shapes[i].isSelected)
                {
                    for (int j = 0; j < 5; ++j)
                    {
                        g.drawRect(70*i + j, j, 70-2*j, 70-2*j);
                    }        // draws the highlight for a selected shape
                    
                }
                for (int y = 0; y < shapes[i].shape.length; ++y)    
                    for (int x = 0; x < shapes[i].shape.length; ++x)
                        if (shapes[i].shape[y][x]) 
                            g.fillRect(i*70 + 10 + 5*x, 10 + 5*y, 5, 5);    // draws the pixelated shape
            }
        }
    }
    
    class BrushShape
    {
        protected boolean [][] shape ;
        protected boolean isSelected = false;
        
        protected BrushShape(int size, boolean squareCircle)  // true for square, false for circle  
        {
            if (squareCircle)
            {
                shape = new boolean[size][size];
                for (int y = 0; y < size; ++y)
                    for (int x = 0; x < size; ++x)
                        shape[y][x] = true;
            }
            else            
            {
                switch (size)       // hard-instantiating circle shapes for each size. Fire up the program to see what they look like rendered
                {
                    case 1 : 
                    {
                        boolean [][] s = { {false, true, false} , {true, true, true} , {false, true, false} };
                        shape = s;  break;
                    }
                    case 2 : 
                    {
                        boolean [][] s = { {false,true,true,false}, {true,true,true,true}, {true,true,true,true}, {false,true,true,false} };
                        shape = s;  break;
                    }
                    case 3 : 
                    {
                        boolean [][] s = 
                        {
                            {false,true,true,true,false}, {true,true,true,true,true}, {true,true,true,true,true}, {true,true,true,true,true}
                                , {false,true,true,true,false} 
                        };
                        shape = s;  break;
                    }
                    case 4 : 
                    {
                        boolean [][] s = 
                        {
                            {false,false,true,true,false,false}, {false,true,true,true,true,false}, {true,true,true,true,true,true,},
                            {true,true,true,true,true,true,}, {false,true,true,true,true,false}, {false,false,true,true,false,false}
                        };
                        shape = s;  break;
                    }
                    case 5 : 
                    {   
                        boolean [][] s = 
                        {
                            {false,false,true,true,true,false,false}, {false,true,true,true,true,true,false},
                            {true,true,true,true,true,true,true}, {true,true,true,true,true,true,true}, {true,true,true,true,true,true,true},
                            {false,true,true,true,true,true,false}, {false,false,true,true,true,false,false}
                        };
                    }
                    case 6 : 
                    {
                        boolean [][] s =
                        {
                            {false,false,true,true,true,true,false,false}, {false,true,true,true,true,true,true,false},
                            {true,true,true,true,true,true,true,true}, {true,true,true,true,true,true,true,true},
                            {true,true,true,true,true,true,true,true}, {true,true,true,true,true,true,true,true},
                            {false,true,true,true,true,true,true,false}, {false,false,true,true,true,true,false,false}
                        };
                        shape = s;  break;
                    }
                    case 7 : 
                    {
                        boolean [][] s = 
                        {
                            {false,false,false,true,true,true,false,false,false}, {false,false,true,true,true,true,true,false,false},
                            {false,true,true,true,true,true,true,true,false}, {true,true,true,true,true,true,true,true,true},
                            {true,true,true,true,true,true,true,true,true}, {true,true,true,true,true,true,true,true,true},
                            {false,true,true,true,true,true,true,true,false}, {false,false,true,true,true,true,true,false,false},
                            {false,false,false,true,true,true,false,false,false}
                        };
                        shape = s;  break;
                    }
                    case 8 : 
                    {
                        boolean [][] s = 
                        {
                            {false,false,false,true,true,true,true,false,false,false}, {false,false,true,true,true,true,true,true,false,false},
                            {false,true,true,true,true,true,true,true,true,false}, {true,true,true,true,true,true,true,true,true,true},
                            {true,true,true,true,true,true,true,true,true,true}, {true,true,true,true,true,true,true,true,true,true},
                            {true,true,true,true,true,true,true,true,true,true}, {false,true,true,true,true,true,true,true,true,false},
                            {false,false,true,true,true,true,true,true,false,false}, {false,false,false,true,true,true,true,false,false,false}
                        };
                        shape = s; break;
                    }
                    default : break;
                }
            }
        }
    }
    
    class ShapeSelector implements MouseListener
    {
        public void mouseClicked(MouseEvent e)
        {
            int index = e.getX() / 70; 
            BrushShapePreviewer bsp = (BrushShapePreviewer)e.getSource();
            if (index >= bsp.shapes.length) return;
            if (SpriteEditor2.brush.brushMode != Brush.BrushMode.NORMAL && SpriteEditor2.brush.brushMode != Brush.BrushMode.ERASE)
            {
                for (Button b : buttonPanel.buttons) b .setSelected(false);
                SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
            }           // in case the brush is not in normal or erase mode, we automatically switch it back to normal when clicking to change the shapes

            bsp.shapes[index].isSelected = true;
            SpriteEditor2.brush.shape = bsp.shapes[index].shape;

            if (bsp.squareCircle)
            {
                for (int i = 0; i < squareShapePreviews.shapes.length; ++i)
                    if (i != index) squareShapePreviews.shapes[i].isSelected = false;
                for (int i = 0; i < circleShapePreviews.shapes.length; ++i)
                    circleShapePreviews.shapes[i].isSelected = false;
            }
            else 
            {
                for (int i = 0; i < circleShapePreviews.shapes.length; ++i)
                    if (i != index) circleShapePreviews.shapes[i].isSelected = false;
                for (int i = 0; i < squareShapePreviews.shapes.length; ++i)
                    squareShapePreviews.shapes[i].isSelected = false;
            }        
            squareShapePreviews.repaint();  circleShapePreviews.repaint();
        }
        public void mousePressed(MouseEvent e)      //      \(^.^)>
        {}
        public void mouseReleased(MouseEvent e)     //      <(^.^)>
        {}
        public void mouseEntered(MouseEvent e)      //      <(^.^)/
        {}
        public void mouseExited(MouseEvent e)
        {}
        public void mouseDragged(MouseEvent e)
        {}
        public void mouseMoved(MouseEvent e)
        {}   
    }
   
    
    class ButtonPanel extends JPanel
    {
        protected Button erase = new Button("Erase");
        protected Button fill = new Button("Fill");
        protected Button drawLine = new Button("DrawLine");
        protected Button drawRect = new Button("DrawRect");
        protected Button drawOval = new Button("DrawOval");
        protected Button select = new Button("Select");
        protected Button copy = new Button("Copy");
        protected Button cut = new Button("Cut");
        protected Button paste = new Button("Paste");
        protected Button move = new Button("Move");
        protected Button reverse = new Button("Reverse");
        protected Button flip = new Button("Flip");
        protected Button [] buttons = { erase, fill, drawLine, drawRect, drawOval, select, copy, cut, paste, move, reverse, flip } ;
        
        protected ButtonPanel()
        {
            setLayout(new GridLayout(3,3));
            for (Button b : buttons)
                add(b);
        }
    }
   
    class Button extends JButton
    {
        protected boolean isSelected;
        protected Button(String label)
        {
            setText(label);
            setSelected(false);
            addActionListener(new ButtonListener());
        }
        public void setSelected(boolean b)
        {
            if (b)
            {
                isSelected = true;
                setBackground(Color.LIGHT_GRAY);    
            }
            else
            {
                isSelected = false;
                setBackground(Color.LIGHT_GRAY.brighter());
            }
        }
    }
    
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String label = ( (JButton)e.getSource() ).getText();
            
            
            if (label.equals("Erase"))
            {
                if (buttonPanel.erase.isSelected)
                {
                    buttonPanel.erase.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                }
                else 
                {
                    buttonPanel.erase.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.ERASE;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.erase) b.setSelected(false);
                    }
                }
            }
            
            if (label.equals("Fill"))
            {
                if (buttonPanel.fill.isSelected)
                {
                    buttonPanel.fill.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                }
                else 
                {
                    buttonPanel.fill.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.FILL;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.fill) b.setSelected(false);
                    }
                }
            }
            if (label.equals("DrawLine"))
            {
                if (buttonPanel.drawLine.isSelected)
                {
                    buttonPanel.drawLine.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                    SpriteEditor2.brush.shape = SpriteEditor2.brush.tempShape;
                }
                else 
                {
                    buttonPanel.drawLine.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.DRAW_LINE;
                    SpriteEditor2.brush.shape = new boolean[1][1];
                    SpriteEditor2.brush.shape[0][0] = false;
                    SpriteEditor2.brush.hasP1 = false;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.drawLine) b.setSelected(false);
                    }
                }
            }
            if (label.equals("DrawRect"))
            {
                if (buttonPanel.drawRect.isSelected)
                {
                    buttonPanel.drawRect.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                    SpriteEditor2.brush.shape = SpriteEditor2.brush.tempShape;
                }
                else 
                {
                    buttonPanel.drawRect.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.DRAW_RECT;
                    SpriteEditor2.brush.shape = new boolean[1][1];
                    SpriteEditor2.brush.shape[0][0] = false;
                    SpriteEditor2.brush.hasP1 = false;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.drawRect) b.setSelected(false);
                    }
                    SpriteEditor2.brush.hasP1 = false;
                    SpriteEditor2.brush.hasP2 = false;
                }
            }
            if (label.equals("DrawOval"))
            {
                if (buttonPanel.drawOval.isSelected)
                {
                    buttonPanel.drawOval.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                    SpriteEditor2.brush.shape = SpriteEditor2.brush.tempShape;
                }
                else 
                {
                    buttonPanel.drawOval.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.DRAW_OVAL;
                    SpriteEditor2.brush.tempShape = SpriteEditor2.brush.shape;
                    SpriteEditor2.brush.shape = new boolean[1][1];
                    SpriteEditor2.brush.shape[0][0] = false;
                    SpriteEditor2.brush.hasP1 = false;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.drawOval) b.setSelected(false);
                    }
                    SpriteEditor2.brush.hasP1 = false;
                    SpriteEditor2.brush.hasP2 = false;
                }
            }
            if (label.equals("Select"))
            {
                if (buttonPanel.select.isSelected)
                {
                    buttonPanel.select.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                    SpriteEditor2.brush.hasP1 = false;
                    SpriteEditor2.brush.hasP2 = false;
                }
                else 
                {
                    buttonPanel.select.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.SELECT;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.select) b.setSelected(false);
                    }
                    SpriteEditor2.brush.hasP1 = false;
                    SpriteEditor2.brush.hasP2 = false;
                }
            }
            if (label.equals("Copy"))       // different logic
            {
                if (SpriteEditor2.brush.brushMode != Brush.BrushMode.SELECT) return;
                
                if (!SpriteEditor2.brush.hasP2) return;
                 
                SpriteEditor2.brush.copied = new Image(SpriteEditor2.canvas.activeLayer, SpriteEditor2.brush.X1 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y1 - SpriteEditor2.canvas.activeLayer.Yoffset, SpriteEditor2.brush.X2 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y2 - SpriteEditor2.canvas.activeLayer.Yoffset);
            }
            
            
            
            if (label.equals("Cut"))        //  when you cut out a piece of the image, it wipes the selection rectangle 
            {
                if (SpriteEditor2.brush.brushMode != Brush.BrushMode.SELECT) return;
                
                if (!SpriteEditor2.brush.hasP2) return;
                System.out.println("X1 = " + SpriteEditor2.brush.X1 + "\tY1 = " + SpriteEditor2.brush.Y1);
                System.out.println("X2 = " + SpriteEditor2.brush.X2 + "\tY2 = " + SpriteEditor2.brush.Y2);
                SpriteEditor2.brush.copied = new Image(SpriteEditor2.canvas.activeLayer, SpriteEditor2.brush.X1 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y1 - SpriteEditor2.canvas.activeLayer.Yoffset, SpriteEditor2.brush.X2 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y2 - SpriteEditor2.canvas.activeLayer.Yoffset);
                
                SpriteEditor2.canvas.activeLayer.cut(SpriteEditor2.brush.X1 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y1 - SpriteEditor2.canvas.activeLayer.Yoffset, SpriteEditor2.brush.X2 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y2 - SpriteEditor2.canvas.activeLayer.Yoffset);
                
                SpriteEditor2.canvas.repaint();
            }
            
            
            
            if (label.equals("Paste"))
            {
                if (buttonPanel.paste.isSelected)
                {
                    buttonPanel.paste.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                }
                else 
                {
                    buttonPanel.paste.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.PASTE;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.paste) b.setSelected(false);
                    }
                }
            }
            
            
            
            if (label.equals("Reverse"))
            {
                if (SpriteEditor2.brush.brushMode != Brush.BrushMode.SELECT) return;
                if (!SpriteEditor2.brush.hasP2) return;
                System.out.println("Reverse pressed, checks passed ...");
                SpriteEditor2.canvas.activeLayer.reverse(SpriteEditor2.brush.X1 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y1 - SpriteEditor2.canvas.activeLayer.Yoffset, SpriteEditor2.brush.X2 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y2 - SpriteEditor2.canvas.activeLayer.Yoffset);
                
                SpriteEditor2.canvas.repaint();
            }
            
            
            
            if (label.equals("Flip"))
            {
                if (SpriteEditor2.brush.brushMode != Brush.BrushMode.SELECT) return;
                if (!SpriteEditor2.brush.hasP2) return;
                System.out.println("Flip pressed, checks passed ...");
                SpriteEditor2.canvas.activeLayer.flip(SpriteEditor2.brush.X1 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y1 - SpriteEditor2.canvas.activeLayer.Yoffset, SpriteEditor2.brush.X2 - SpriteEditor2.canvas.activeLayer.Xoffset, SpriteEditor2.brush.Y2 - SpriteEditor2.canvas.activeLayer.Yoffset);
                
                SpriteEditor2.canvas.repaint();
            }
            
            
            
            if (label.equals("Move")) 
            {
                if (buttonPanel.move.isSelected)
                {
                    buttonPanel.move.setSelected(false);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.NORMAL;
                }
                else 
                {
                    buttonPanel.move.setSelected(true);
                    SpriteEditor2.brush.brushMode = Brush.BrushMode.MOVE;
                    for (Button b : buttonPanel.buttons)
                    {
                        if (b != buttonPanel.move) b.setSelected(false);
                    }
                }     
            }
        }
    }
}

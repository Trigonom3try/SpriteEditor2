
package SpriteEditor2;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Palette extends JFrame
{
    protected String name;
    protected ArrayList<Color> colors = new ArrayList();
    protected ArrayList<String> colorNames = new ArrayList();
    protected DemoPanel demoPanel = new DemoPanel();
    protected ButtonPanel buttonPanel = new ButtonPanel();
    
    protected int selectedColorIndex = -10;     // the -10 serves as a sort of null value for the index, since no index value can be less than 0
    
    private static final int width = 500;
    private static final int height = 500;      
    private static final int colorSize = 100;      // the width of each color demo square
    private static final int maxRows = height / colorSize;
    private static final int maxColumns = width / colorSize;
    
    protected boolean isDefault;      // whether or not it's the default palette
    
    
    protected Palette(boolean d)     
    {
        setSize(width,height + 50);     // 50 on the bottom for some buttons
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);      // found a use for something new
        add(demoPanel, BorderLayout.CENTER);
             
        if (d) createDefaultPalette();
        else add(buttonPanel, BorderLayout.SOUTH);
        activate();
    }
    
    protected Palette(String n)
    {
        this(false);
        name = n;
        setTitle("Palette : " + name); 
        activate();
    }
    
    protected Palette(String n, Color c, String colorName)
    {
        this(false);
        name = n;
        setTitle("Palette : " + name);
        colors.add(c);      colorNames.add(colorName);   
        demoPanel.repaint();
        activate();
    }
    
    
    protected void createDefaultPalette()
    {
        name = "Default";
        setTitle("Palette: " + name);
        addMouseListener(new ClickListener());
        
        colorNames.add("Red");
        colorNames.add("Blue");
        colorNames.add("Green");
    //    names.add("Yellow");
        colorNames.add("Orange");
   //     names.add("Purple");
        colorNames.add("Pink");
   //     names.add("Brown");
        colorNames.add("Black");
        colorNames.add("Gray");
        colorNames.add("White");
        
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        colors.add(Color.GREEN);
    //    colors.add(Color.Yellow);
        colors.add(Color.ORANGE);
 //       colors.add(Color.PURPLE);
        colors.add(Color.PINK);
 //       colors.add(Color.BROWN);
        colors.add(Color.BLACK);
        colors.add(Color.GRAY);
        colors.add(Color.WHITE);   
        
        demoPanel.repaint();
    }
    
    
    
    class DemoPanel extends JPanel
    {
        public DemoPanel()
        {
            addMouseListener(new ClickListener());
        }
        public void paintComponent(Graphics g)
        {
      //      System.out.println("in Palette repaint()");
          //  System.out.println("\tSelected color index = " + selectedColorIndex);
            g.setColor(Color.LIGHT_GRAY);       // light gray is the default color for the demo panel
            g.fillRect(0,0, width, height);
            
            for (int i = 0; i < colors.size(); ++i)
            {
                int X = i % maxColumns;
                int Y = i / maxRows;
               
                g.setColor(colors.get(i));
                g.fillRect(X*colorSize, Y*colorSize, colorSize, colorSize);
                
                if (i == selectedColorIndex)        // code for highlighting the selected color
                {
                //    System.out.println("\t" + i + " equal to " + selectedColorIndex);
                    if (colors.get(i) == Color.black) g.setColor(Color.white);
                    else g.setColor(Color.black);
                    
                    for (int r = 0; r < 3; ++r)         // draws 3 nexted rectangles, i.e. a rectangle outline 3 pixels thick
                        g.drawRect( (X*colorSize) + r, (Y*colorSize) + r, colorSize-r, colorSize-r);
                }
           //     else System.out.println("\t" + i + "  NOT equal " + selectedColorIndex);
            }
        }
    }
    
    class ButtonPanel extends JPanel
    {
        protected JButton deletePalette = new JButton("Delete Palette");
        protected JButton deleteColor = new JButton("Delete Color");
        protected JButton savePalette = new JButton("Save Palette");
        
        protected ButtonPanel()
        {
            setLayout(new GridLayout(1,3));
            deletePalette.addActionListener(new ButtonListener());
            deleteColor.addActionListener(new ButtonListener());
            savePalette.addActionListener(new ButtonListener());
            
            add(deletePalette);     add(deleteColor);   add(savePalette);
        }
    }
    class ClickListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent e)
        {
            int x = e.getX() / colorSize;
            int y = e.getY() / colorSize;
            
            selectedColorIndex = (y * maxColumns) + x;      
            System.out.println(selectedColorIndex);
            
            if (selectedColorIndex >= colors.size()) selectedColorIndex = -10;
            else SpriteEditor2.colorCreator.setColor(colors.get(selectedColorIndex));
                       
            setTitleBarText();
            demoPanel.repaint();
        }
    }
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String label = ( (JButton)e.getSource() ).getText();
            
            if (label.equals("Delete Palette")) deletePalette();
            if (label.equals("Delete Color")) deleteColor();
            if (label.equals("Save Palette")) savePalette();
        }
    }
    
    void savePalette()  // saving a palette doesn't ask for a palette name because you gave it one during creation
    {
        try {   IO_Methods.savePalette(this);     }
        catch (FileNotFoundException fnf) { System.out.println("fnf caught"); }
        catch (IOException io) { System.out.println("io caught"); }
        
        JOptionPane paletteSaved = new JOptionPane();
        paletteSaved.showMessageDialog(null, "Palette saved successfully");
    }
    void deletePalette()
    {
        JOptionPane deletePane = new JOptionPane();
        if (deletePane.showConfirmDialog(null, "Are you sure you want to delete this palette?") == 0)
        {
            SpriteEditor2.palettes.remove(this);
            File f = new File(name + ".p");
            if (f.exists()) 
            {
               if (f.delete()) System.out.println("File found, deleting ...");
               else System.out.println("There was a problem deleting the file " + f.getName());
            }
            dispose();
        } 
    }
    void deleteColor()
    {
        JOptionPane deletePane = new JOptionPane();
        if (selectedColorIndex < 0) deletePane.showMessageDialog(null, "No color selected");
        else if (deletePane.showConfirmDialog(null, "Are you sure you want to delete the selected color?") == 0)
        {
            colors.remove(selectedColorIndex);
            colorNames.remove(selectedColorIndex);
            selectedColorIndex = -10;
            setTitleBarText();
            repaint();
        }
        
    }      
    
    void addColor(Color c, String n)
    {
        colors.add(c);      colorNames.add(n);
        repaint();
    }
    
    void activate()
    {
        addMouseListener(new ClickListener());
        buttonPanel.deletePalette.addActionListener(new ButtonListener());
        buttonPanel.deleteColor.addActionListener(new ButtonListener());
        buttonPanel.savePalette.addActionListener(new ButtonListener());
        setVisible(true);
    }
    
    void setTitleBarText()
    {
        if (selectedColorIndex == -10) setTitle("Palette: " + name);
        else setTitle("Palette: " + name + "    Color: " + colorNames.get(selectedColorIndex));
    }
    
    protected void repaintDemoPanel()
    {
        demoPanel.repaint();
    }
}

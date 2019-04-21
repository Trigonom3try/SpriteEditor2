
package SpriteEditor2;

import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

public class LayerManager extends JFrame 
{
    protected JButton newLayer = new JButton("New Layer");
    protected JButton previous = new JButton("<");
    protected JButton next = new JButton(">");
    protected JButton delete = new JButton("Delete Layer");
    protected JButton rename = new JButton("Rename Layer");
    protected JButton properties = new JButton("Properties");
    
    protected LayerManager()
    {
        rename.addActionListener(new ButtonListener());
        properties.addActionListener(new ButtonListener());
        setLayout(new GridLayout(3,1));
        if (SpriteEditor2.canvas.sprite.layers.size() == 1)
        {
            setSize(125, 225); 
            newLayer.addActionListener(new ButtonListener());
            add(newLayer);
        }
        else
        {
            setSize(300, 75);
            add(new ButtonPanel());  
        }
        add(rename);
        add(properties);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    class ButtonPanel extends JPanel
    {
        protected ButtonPanel()
        {
            setLayout(new GridLayout(1,4));
            newLayer.addActionListener(new ButtonListener());
            previous.addActionListener(new ButtonListener());
            next.addActionListener(new ButtonListener());
            delete.addActionListener(new ButtonListener());
            add(newLayer); 
            add(previous);
            add(next);
            add(delete);
        }
    }
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String label = ( (JButton)e.getSource() ).getText();
            
            if (label.equals("New Layer"))
            {
                new LayerPropertyWindow();          // This just now... Idk i had moment of thought about programming like how i had made my life so much eaiser by having
            }                                       // a LayerPropertyWindow class
            if (label.equals("Delete Layer"))
            {
                JOptionPane DaPaneUvDeff = new JOptionPane();       
                if (SpriteEditor2.canvas.sprite.layers.size() == 1) 
                {
                    if (DaPaneUvDeff.showConfirmDialog(null, "Are you sure you want to quit?") == 0) System.exit(0);    // Nice. Didn't think I'd find a use for that one
                }    
                else if (DaPaneUvDeff.showConfirmDialog(null, "Are you sure you want to delete this layer?") == 0)
                    SpriteEditor2.canvas.sprite.deleteLayer(SpriteEditor2.canvas.activeLayer.index);
                SpriteEditor2.editorWindow.setTitleBarText();
                SpriteEditor2.canvas.setFrameDimensions();
                SpriteEditor2.canvas.repaint();           
            }
            if (label.equals("Rename Layer"))       // pressing ok sets s equal to "". Pressing cancel sets s equal to null
            {
                JOptionPane renamePane = new JOptionPane();
                String s = "";
                try {    s = renamePane.showInputDialog(null, "New layer name: ");   } // you can set s = null, but it won't throw an NPE 
                catch (NullPointerException npe) {}
                if (s == null | s.equals(""))    // you cannot use the .equals(string) method for strings to check the equality of a null string (basically just null) with null
                    return;            
                SpriteEditor2.canvas.activeLayer.name = s;
                SpriteEditor2.editorWindow.setTitleBarText();
            }
            if (label.equals("<"))
            {
                if (SpriteEditor2.canvas.activeLayer.index == 0) return;
                SpriteEditor2.canvas.setActiveLayer(SpriteEditor2.canvas.activeLayer.index - 1);    // setActiveLayer() invokes setTitleBarText() and repaint()
            }
            if (label.equals(">"))
            {
                if (SpriteEditor2.canvas.activeLayer.index == SpriteEditor2.canvas.sprite.layers.size() - 1) return;
                SpriteEditor2.canvas.setActiveLayer(SpriteEditor2.canvas.activeLayer.index + 1);
            }
            if (label.equals("Properties")) 
            {             
                System.out.println("Layer Width = " + SpriteEditor2.canvas.activeLayer.width + "\tLayer Height = " + SpriteEditor2.canvas.activeLayer.height);
                System.out.println("Xoffset = " + SpriteEditor2.canvas.activeLayer.Xoffset + "\tYoffset = " + SpriteEditor2.canvas.activeLayer.Yoffset);
            }
        }
    }
}

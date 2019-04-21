
package SpriteEditor2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


public class LayerPropertyWindow extends JFrame
{
    private int width;
    private int height;
    private int Xoffset;
    private int Yoffset;
    private String name;
    
    private JLabel widthLabel = new JLabel("Width (/1500) : ");
    private JLabel heightLabel = new JLabel("Height (/800) : ");
    private JLabel XoffsetLabel = new JLabel("X offset (/1500) : ");
    private JLabel YoffsetLabel = new JLabel("Y offset (/800) : ");
    private JLabel nameLabel = new JLabel("Name of layer : ");
    
    private JTextField widthPrompt = new JTextField();
    private JTextField heightPrompt = new JTextField();
    private JTextField XoffsetPrompt = new JTextField();
    private JTextField YoffsetPrompt = new JTextField();
    private JTextField namePrompt = new JTextField();
    private JButton accept = new JButton("Accept");
    private JButton cancel = new JButton("Cancel");
    
    protected LayerPropertyWindow()
    {
        setSize(300,250);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("New layer properties: ");
        setLayout(new GridLayout(6,2));
        accept.addActionListener(new ButtonListener());
        cancel.addActionListener(new ButtonListener());
        add(widthLabel);       add(widthPrompt);
        add(heightLabel);      add(heightPrompt);
        add(XoffsetLabel);     add(XoffsetPrompt); 
        add(YoffsetLabel);     add(YoffsetPrompt);
        add(nameLabel);        add(namePrompt);
        add(accept);            add(cancel);
        setVisible(true);
    }
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String label = ( (JButton)e.getSource() ).getText();
            if (label.equals(("Cancel")))
            {
                dispose();
                return;
            }
            JOptionPane errorPane = new JOptionPane();
            String name = namePrompt.getText();
            
            try { width = Integer.parseInt(widthPrompt.getText()); }
            catch (NumberFormatException nf) {  errorPane.showMessageDialog(null, "Error: Invalid width entry"); return;    }
            try { height = Integer.parseInt(heightPrompt.getText()); }
            catch (NumberFormatException nf) {  errorPane.showMessageDialog(null, "Error: Invalid height entry"); return;    }
            try { Xoffset = Integer.parseInt(XoffsetPrompt.getText()); }
            catch (NumberFormatException nf) {  errorPane.showMessageDialog(null, "Error: Invalid X offset entry"); return;   }    
            try { Yoffset = Integer.parseInt(YoffsetPrompt.getText()); }
            catch (NumberFormatException nf) {  errorPane.showMessageDialog(null, "Error: Invalid Y offset entry"); return;   }   
            
            if (width <= 0) 
            {
                errorPane.showMessageDialog(null, "Error: width cannot be negative or zero");   return;
            }
            if (height <= 0) 
            {
                errorPane.showMessageDialog(null, "Error: height cannot be negative or zero");  return;
            }
            if (Xoffset < 0) 
            {
                errorPane.showMessageDialog(null, "Error: X offset cannot be negative");    return;
            }
            if (Yoffset < 0) 
            {
                errorPane.showMessageDialog(null, "Error: Y offset cannot be negative");    return;
            }
        
            if (SpriteEditor2.canvas == null)       // in the case of creating a new sprite, meaning canvas has to be reset to null if you're working on a sprite and want to make new
            { 
                SpriteEditor2.canvas = new Canvas(new Sprite(new Layer(name, width, height, Xoffset, Yoffset)));  
                SpriteEditor2.editorWindow = new EditorWindow(); 
                
                SpriteEditor2.brush = new Brush();
                SpriteEditor2.brushTools = new BrushTools();
                SpriteEditor2.colorCreator = new ColorCreator();
                SpriteEditor2.layerManager = new LayerManager();
                SpriteEditor2.canvasController = new CanvasController();
            }
            else              // in the case of adding a layer to the current sprite
            {                
                SpriteEditor2.canvas.sprite.addLayer(new Layer(name, width, height, Xoffset, Yoffset));
                SpriteEditor2.canvas.setFrameDimensions();
                SpriteEditor2.canvas.setActiveLayer(SpriteEditor2.canvas.sprite.layers.size() - 1);     // setActiveLayer invokes both setTitleBarText() and repaint()
                
                LayerManager tempLM = SpriteEditor2.layerManager;       // make a new LM for sprites with >1 layer
                SpriteEditor2.layerManager = new LayerManager();
                tempLM.dispose();        
            }      
            dispose();
        }
    }
}
        
            
           
            
           

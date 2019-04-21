
package SpriteEditor2;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.io.*;

public class InitialSelectionWindow extends JFrame
{
    protected JButton newSprite = new JButton("New");
    protected JButton loadSprite = new JButton("Load");
    
    protected InitialSelectionWindow()
    {
        
        setSize(350,200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("SpriteEditor_v0.2  by T3y_Soft");
        setLayout(new GridLayout(3, 1));
        add(new JLabel("Please make a selection : "));
        newSprite.addActionListener(new ButtonListener());
        loadSprite.addActionListener(new ButtonListener());
        add(newSprite);
        add(loadSprite);
        setVisible(true);
    }
    
    class ButtonListener implements ActionListener 
    {
        public void actionPerformed(ActionEvent e)
        {
            if (((JButton)e.getSource()).getText().equals("New"))
            {
                new LayerPropertyWindow();      // true because it's the start of the program
            }
            else
            {
                JOptionPane loadPane = new JOptionPane();
                // code here for what happens when you press either the OK or Cancel buttons on a JOptionPane
                String fileName = loadPane.showInputDialog("Load file : ");
                Sprite s = null;
                
                try {   s = IO_Methods.loadSprite(fileName);   }
                catch (FileNotFoundException fnf)   {   loadPane.showMessageDialog(null, "Error: File \"" + fileName + ".s2\" was not found");     return; }
                catch (IOException io)  { System.out.println("io caught"); return; }
                catch (ClassNotFoundException cnf)   { System.out.println("cnf caught"); return;}
                
                if (s == null) 
                {
                    System.out.println("s equal to NULL");  return;
                }
                SpriteEditor2.canvas = new Canvas(s);
                SpriteEditor2.brush = new Brush();
                SpriteEditor2.editorWindow = new EditorWindow();
                SpriteEditor2.brushTools = new BrushTools();
                SpriteEditor2.colorCreator = new ColorCreator();
                SpriteEditor2.layerManager = new LayerManager();
                SpriteEditor2.canvasController = new CanvasController();
            }
            dispose();
        }
    }
}
    


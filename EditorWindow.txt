
package SpriteEditor2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Scanner;

public class EditorWindow extends JFrame
{
    private final int defaultWidth = 475;   // these are the smallest dimensions the window can have. enough width to contain the button panel, and an arbitrarily chosen height
    private final int defaultHeight = 400;
    
    protected ButtonPanel buttonPanel = new ButtonPanel();
    
    protected EditorWindow()           // note that the canvas is not added in the EW constructor. it is remotely called later cuz it is dependent upon there being a constructed EW
    {
        setSize( defaultWidth, defaultHeight );
        setResizable(true);     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitleBarText();
        add(SpriteEditor2.canvas, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);           // the ButtonPanel object doesn't need to have a global reference in the code
        
        if (SpriteEditor2.canvas.activeLayer.visible) buttonPanel.setVisible.setSelected(true);
        
        setVisible(true);
    }
    
    class ButtonPanel extends JPanel 
    {
        private JButton save = new JButton("Save");
        private JButton load = new JButton("Load");
        private JButton newSprite = new JButton("New");
        private JButton undo = new JButton("UNDO");
        private JButton properties = new JButton("Properties");
        protected JCheckBox setVisible = new JCheckBox("Visible");
        private JCheckBox drawGrid = new JCheckBox("Grid");
    
        protected ButtonPanel()
        {
            setLayout(new GridLayout(1,4));
            save.addActionListener(new ButtonListener());
            load.addActionListener(new ButtonListener());
            newSprite.addActionListener(new ButtonListener());
            undo.addActionListener(new ButtonListener());
            properties.addActionListener(new ButtonListener());
            setVisible.addActionListener(new CheckBoxListener());
            drawGrid.addActionListener(new CheckBoxListener());
            drawGrid.setSelected(true);
            add(save);      add(load);      add(newSprite);     add(undo);      add(properties);    add(setVisible);    add(drawGrid);          
        }
    
        class ButtonListener implements ActionListener 
        {
            public void actionPerformed(ActionEvent e)  
            {
                String label = ((JButton)e.getSource()).getText();
        
                if (label.equals("Save")) Save();
                if (label.equals("Load")) Load();
                if (label.equals("New")) New();
                if (label.equals("UNDO")) SpriteEditor2.canvas.undo();   
                if (label.equals("Properties")) 
                {
                    System.out.println("Sprite width = " + SpriteEditor2.canvas.sprite.width + "\tSprite height = " + SpriteEditor2.canvas.sprite.height);
                }
            }     
        }
        class CheckBoxListener extends AbstractAction
        {
            public void actionPerformed(ActionEvent e)
            {
                JCheckBox source = (JCheckBox)e.getSource();
                if (source == setVisible)
                {    if (SpriteEditor2.canvas.activeLayer.visible)
                    {
                        setVisible.setSelected(false);
                        SpriteEditor2.canvas.activeLayer.visible = false;
                    }
                    else SpriteEditor2.canvas.activeLayer.visible = true;
                }
                else
                {
                    if (SpriteEditor2.canvas.drawGrid)
                    {
                        drawGrid.setSelected(false);
                        SpriteEditor2.canvas.drawGrid = false;
                        SpriteEditor2.canvas.repaint();
                    }
                    else
                    {
                        SpriteEditor2.canvas.drawGrid = true;
                        SpriteEditor2.canvas.repaint();
                    }
                }
            }
        }
        
        
        protected void New()
        {
            JOptionPane surePrompt = new JOptionPane();
            if (surePrompt.showConfirmDialog(null, "Are you sure? All unsaved progress will be lost") == 0)     // 0 y, 1 n, 2 c
            {
                SpriteEditor2.canvas = null;            // canvas must be null in order for logic within the LayerPropertyWindow class to work
                SpriteEditor2.editorWindow.dispose();
                SpriteEditor2.layerManager.dispose();
                SpriteEditor2.colorCreator.dispose();
                SpriteEditor2.brushTools.dispose();
                SpriteEditor2.canvasController.dispose();
                new LayerPropertyWindow();
            }
        }
        protected void Save()
        {
            JOptionPane savePane = new JOptionPane();
            String spriteName = savePane.showInputDialog("Save sprite as : ");
            
            
            try {   IO_Methods.saveSprite(SpriteEditor2.canvas.sprite);    }
            catch (IO_Methods.DuplicateNameException dn) 
            {
               if (savePane.showConfirmDialog(null, "Warning: File \"" + spriteName + ".s2\" already exists. Would you like to replace it?") == 0)
               {
                   File f = new File(spriteName + ".s2");
                   f.delete();
                   SpriteEditor2.canvas.sprite.name = spriteName;
                   try {   IO_Methods.saveSprite(SpriteEditor2.canvas.sprite);    }    // This time it works because we deleted the file with the duplicate name
                   catch (IO_Methods.DuplicateNameException dn2) {}
                   catch (FileNotFoundException fnf) {}
                   catch (IOException io) {}
                   catch (ClassNotFoundException cnf) {}
               } 
            }
            catch (FileNotFoundException fnf) {}
            catch (IOException io) {}
            catch (ClassNotFoundException cnf) {}
            
            setTitleBarText();           
        }
        
        protected void Load()
        {
            JOptionPane loadPane = new JOptionPane();
            Sprite s = null;
            String spriteName = loadPane.showInputDialog("Load sprite : ");
            if (SpriteEditor2.canvas.sprite.name.equals(spriteName)) return;    // haha! yes
            try {   s = IO_Methods.loadSprite(spriteName);   }
            catch (FileNotFoundException fnf) 
            {
                loadPane.showMessageDialog(null, "Error: File " + spriteName + ".s2\" was not found");
                return;
            }
            catch (IOException io) 
            {
                loadPane.showMessageDialog(null, "Error: There was a problem loading the file");
                return;
            }
            catch (ClassNotFoundException cnf) 
            {
                loadPane.showMessageDialog(null, "Error: There was a problem loading the file");
                return;
            }
            
            if (loadPane.showConfirmDialog(null, "Are you sure? All unsaved progress will be lost") == 0)
            {   
                EditorWindow tempEW = SpriteEditor2.editorWindow;
                LayerManager tempLM = SpriteEditor2.layerManager;
                
                SpriteEditor2.canvas = new Canvas(s);
                SpriteEditor2.editorWindow = new EditorWindow();               
                SpriteEditor2.layerManager = new LayerManager();        
                tempEW.dispose();   tempLM.dispose();
                
                System.out.println("Sprite name : " + SpriteEditor2.canvas.sprite.name);
            }   // else nothing happens and the program just goes back to you looking at ur sprte liek a lame ass chode
        }
    }
    
    protected void setTitleBarText()
    {
        setTitle(SpriteEditor2.canvas.sprite.name + "    Layer: " + (SpriteEditor2.canvas.activeLayer.index + 1) + "/" + SpriteEditor2.canvas.sprite.layers.size() + " \"" + SpriteEditor2.canvas.activeLayer.name + "\"");
    }
}

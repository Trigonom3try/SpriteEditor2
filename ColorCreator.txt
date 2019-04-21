
package SpriteEditor2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.Scanner;

public class ColorCreator extends JFrame
{
    protected JSlider R = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0);
    protected JSlider G = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0);
    protected JSlider B = new JSlider(SwingConstants.HORIZONTAL, 0, 255, 0);
    protected JLabel Rlabel = new JLabel("Red : " + R.getValue());
    protected JLabel Glabel = new JLabel("Green : " + G.getValue());
    protected JLabel Blabel = new JLabel("Blue : " + B.getValue());
    private DemoPanel demoPanel = new DemoPanel();                    // the demo panel must be accessible by other classes/method within this module
                                                                        // the slider and button panels do not
    
    protected ColorCreator()
    {
        setSize(350, 450);
        setResizable(false);
        setTitle("Color Creator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // the color selector's an essential part of the app, might as well have this
        setLayout(new GridLayout(3, 1));
        add(new SliderPanel());
        add(demoPanel);
        add(new ButtonPanel());
        setVisible(true);
    }
    class SliderPanel extends JPanel
    {
        protected SliderPanel()
        {
            setLayout(new GridLayout(6,2));
            R.addChangeListener(new ColorChanger());
            G.addChangeListener(new ColorChanger());
            B.addChangeListener(new ColorChanger());
            add(R);                         add(Rlabel);
            add(new Button("<R", true));     add(new Button("R>",true ));
            add(G);                         add(Glabel);
            add(new Button("<G", true));     add(new Button("G>", true));
            add(B);                         add(Blabel);
            add(new Button("<B", true));     add(new Button("B>", true));
        }
    }
    
    protected void setColor(Color c)
    {
        R.setValue(c.getRed());     Rlabel.setText(c.getRed() + "");
        G.setValue(c.getGreen());   Glabel.setText(c.getGreen() + "");
        B.setValue(c.getBlue());    Blabel.setText(c.getBlue() + "");
        SpriteEditor2.brush.color = c;
        demoPanel.repaint();
    }
    
    
    class DemoPanel extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            g.setColor(SpriteEditor2.brush.color);
            g.fillRect(0,0,350,150);;
        }
    }
    
    class ButtonPanel extends JPanel
    {
        protected ButtonPanel()
        {
            setLayout(new GridLayout(3,1));
            add(new Button("Save Color", false));
            add(new Button("New Palette", false));
            add(new Button("Load Palette", false));
            add(new Button("Default Palette", false));
            add(new Button("# of Palettes", false));
        }
    } 
    class Button extends JButton
    {
        protected Button(String label, boolean isColorButton)
        {
            super(label);
            if (isColorButton) addActionListener(new ColorButtonListener());
            else addActionListener(new PaletteButtonListener());
        }
    }
    
    class ColorChanger implements ChangeListener
    {
        public void stateChanged(ChangeEvent c)
        {
          //  deselectPaletteColors();
            SpriteEditor2.brush.color = new Color(R.getValue(), G.getValue(), B.getValue());
            if ( (JSlider)c.getSource() == R ) Rlabel.setText("Red : " + R.getValue());
            if ( (JSlider)c.getSource() == G ) Glabel.setText("Green : " + G.getValue());
            if ( (JSlider)c.getSource() == B ) Blabel.setText("Blue : " + B.getValue());
            demoPanel.repaint();       
        }
    } 
    
    class ColorButtonListener implements ActionListener     // pressing a color button also deselects any selected custom or default color from a palette
    {
        public void actionPerformed(ActionEvent e)    
        {
            String label = ( (Button)e.getSource() ).getText();
            if (label.equals("<R"))
            {
                if (R.getValue() == 0) return;
                R.setValue(R.getValue() - 1);
                Rlabel.setText("Red : " + R.getValue());
            }
            if (label.equals("R>"))
            {
                if (R.getValue() == 255) return;
                R.setValue(R.getValue() + 1);
                Rlabel.setText("Red : " + R.getValue());
            }
            if (label.equals("<G"))
            {
                if (G.getValue() == 0) return;
                G.setValue(G.getValue() - 1);
                Glabel.setText("Green : " + G.getValue());
            }
            if (label.equals("G>"))
            {
                if (G.getValue() == 255) return;
                G.setValue(G.getValue() + 1);
                Glabel.setText("Green : " + G.getValue());
            }
            if (label.equals("<B"))
            {
                if (B.getValue() == 0) return;
                B.setValue(B.getValue() - 1);
                Blabel.setText("Blue : " + B.getValue());
            }
            if (label.equals("B>"))
            {
                if (B.getValue() == 255) return;
                B.setValue(B.getValue() + 1);
                Blabel.setText("Blue : " + B.getValue());
            }      
            demoPanel.repaint();
       //     deselectPaletteColors();
        }
    }
    
    class PaletteButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String label = ( (Button)e.getSource() ).getText();
            if (label.equals("Save Color")) saveColor();
            if (label.equals("New Palette")) newPalette();
            if (label.equals("Load Palette")) loadPalette();
            if (label.equals("Default Palette")) defaultPalette(); 
            if (label.equals("# of Palettes")) System.out.println("# of Palettes: " + SpriteEditor2.palettes.size());
        }
    }
 
    
    void saveColor()
    {
        JOptionPane namePrompt = new JOptionPane();
        String colorName = namePrompt.showInputDialog(null, "Save color as: ");
        if (colorName.equals(""))
        {
            namePrompt.showMessageDialog(this, "Invalid color name");
            return;
        }
        if (colorName == null) return;
        
        String paletteName = namePrompt.showInputDialog(null, "Save to palette: ");
        
        if (paletteName.equals(""))
        {
            namePrompt.showMessageDialog(this, "Invalid palette name");
            return;
        }
        if (paletteName == null) return;
        
        for (Palette p : SpriteEditor2.palettes)
        {
            if (p.name.equals(paletteName))
            {
                for (String s : p.colorNames)
                {
                    if (s.equals(colorName))
                    {
                        namePrompt.showMessageDialog(this, "Error: There is already a color with that name on the palette");
                        return;
                    }
                }
                p.addColor(new Color(R.getValue(), G.getValue(), B.getValue()), colorName);
                if (!p.isVisible()) 
                {
                    p.setVisible(true);
                    return;
                }
            }
        }
        
        File f = new File(paletteName + ".p");
        if ( !f.exists() )
        {
            if (namePrompt.showConfirmDialog(this, "Palette " + paletteName + " was not found. Would you like to create it?") == 0)
            {
                SpriteEditor2.palettes.add(new Palette(paletteName, new Color(R.getValue(), G.getValue(), B.getValue()), colorName));
                return;
            }
            else return;
        }
        
        else 
        {
            try 
            {   
                SpriteEditor2.palettes.add(IO_Methods.loadPalette(paletteName));   
                SpriteEditor2.palettes.get(SpriteEditor2.palettes.size() - 1).addColor(new Color(R.getValue(), G.getValue(), B.getValue()), colorName); 
            }
            catch (FileNotFoundException fnf) {}
            catch (IOException io) {}
            catch (ClassNotFoundException cnf) {}
        }
        
    }
    
    
    void newPalette()
    {
        JOptionPane paletteNamePrompt = new JOptionPane();
        String paletteName = paletteNamePrompt.showInputDialog("Palette name : ");
        
        if (paletteName == null) return;
        
        if (paletteName.equals("")) 
        {
            paletteNamePrompt.showMessageDialog(null, "Error: Invalid name entry");  
            return;
        }
        for (Palette p : SpriteEditor2.palettes)
        {
            if (p.name.equals(paletteName))
            {
                if (p.isVisible())
                {
                    paletteNamePrompt.showMessageDialog(this, "Palette " + p.name + " is already open");   
                    return;
                }
                else if (paletteNamePrompt.showConfirmDialog(this, "Palette " + p.name + " exists, would you like to open it?") == 0)
                {
                    p.setVisible(true);
                    return;
                }
                else return;
            }
        }
        Palette p = null;
        
        File f = new File(paletteName + ".p");
        if (f.exists())
        {
            if (paletteNamePrompt.showConfirmDialog(this, "Palette " + paletteName + " exists, would you like to open it?") == 0)
            {
                try {   p = IO_Methods.loadPalette(paletteName);    }
                catch (FileNotFoundException fnf) {}
                catch (IOException io) {}
                catch (ClassNotFoundException cnf) {}
            }
            else return;
        }    
        else p = new Palette(paletteName);
        SpriteEditor2.palettes.add(p);    
    }
    
    void loadPalette()
    {
        JOptionPane paletteNamePrompt = new JOptionPane();
        String paletteName = paletteNamePrompt.showInputDialog("Load palette: ");
        
        if (paletteName == null) return;
        if (paletteName.equals(""))
        {
            paletteNamePrompt.showMessageDialog(null, "Error: Invalid name entry");
            return;
        }
        
        if (paletteName.equals("Default") | paletteName.equals("default"))
        {
            paletteNamePrompt.showMessageDialog(null, "Error: Palette cannot have the same name as the Default palette");
            return;
        }
        
        for (Palette p : SpriteEditor2.palettes)
        {
            if (p.name.equals(paletteName))
            {
                if (p.isVisible())
                {
                    paletteNamePrompt.showMessageDialog(null, "Palette " + paletteName + " is already open");
                    return;
                }
                else 
                {
                    p.setVisible(true);
                    return;
                }
            }
        }
        Palette p = null;
        try {   p = IO_Methods.loadPalette(paletteName);    }
        catch (FileNotFoundException fnf) 
        {
            if (paletteNamePrompt.showConfirmDialog(null, "Error: Palette " + paletteName + " was not found, would you like to create it?") == 0)
                p = new Palette(paletteName);
            else return;
        }
        catch (IOException io) {}
        catch (ClassNotFoundException cnf) {}
        
        SpriteEditor2.palettes.add(p);
    }
    
    
    
    void defaultPalette()
    {
        for (Palette p : SpriteEditor2.palettes)
        {
            if (p.name.equals("Default"))
            {
                if (p.isVisible());
                else p.setVisible(true);
                return;
            }
        }
        SpriteEditor2.palettes.add(new Palette(true));
    }
    
    
   
   
   void deselectPaletteColors()
   {
       for (Palette p : SpriteEditor2.palettes)
       {
           p.selectedColorIndex = -10;  p.demoPanel.repaint();
       }
   }
}
n
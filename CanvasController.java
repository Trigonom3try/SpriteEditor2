
package SpriteEditor2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class CanvasController extends JFrame        // need to fix the layout for this class
{
    private static final int maxPixelSize = 20;     // this value could theoretically be more than 20 but 20 is really all you need (and I don't want weird shit happening for >20)
    private static final int maxPixelSpeed = maxPixelSize;  // this value can theoretically anything but not too much, here the max speed is by 1 max-size vpixel
    
    private int pixelMovementSpeed = 1;
    private JPanel movementPanel = new JPanel();
    private JPanel pixelSizeButtonPanel = new JPanel();
    private JPanel pixelSpeedButtonPanel = new JPanel();
    
    private JSlider pixelSizeSlider = new JSlider(SwingConstants.HORIZONTAL, 1, maxPixelSize, 10);
    private JSlider pixelSpeedSlider = new JSlider(SwingConstants.HORIZONTAL, 1, maxPixelSpeed, 1);
    
    private JLabel pixelSizeLabel = new JLabel(("Pixel size: " + SpriteEditor2.canvas.pixelSize));
    private JLabel pixelSpeedLabel = new JLabel("Speed: " + pixelMovementSpeed);
    
    public CanvasController()
    {
        setSize(300,400);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        movementPanel.add(new Button("<"), BorderLayout.WEST);
        movementPanel.add(new Button(">"), BorderLayout.EAST);
        movementPanel.add(new Button("^"), BorderLayout.NORTH);
        movementPanel.add(new Button("v"), BorderLayout.SOUTH);
        
        pixelSizeButtonPanel.setLayout(new GridLayout(1,2));        // I think that the fact that width is 2 here might be screwing things up for the containing grid layout
        pixelSizeButtonPanel.add(new Button("<psize"));
        pixelSizeButtonPanel.add(new Button("psize>"));
        pixelSizeSlider.addChangeListener(new SliderListener());
        pixelSpeedButtonPanel.setLayout(new GridLayout(1,2));
        pixelSpeedButtonPanel.add(new Button("<pspeed"));
        pixelSpeedButtonPanel.add(new Button("pspeed>"));
        pixelSpeedSlider.addChangeListener(new SliderListener());
        
        setLayout(new GridLayout(6,1));     // it's doing a 4x2, not a 6x1 for some reason. Not sure exactly why     
        add(movementPanel);
        add(pixelSpeedLabel);
        add(pixelSpeedSlider);
        add(pixelSpeedButtonPanel);
        add(pixelSizeLabel);
        add(pixelSizeSlider);
        add(pixelSizeButtonPanel);
        
        setVisible(true);
    }
    
    class Button extends JButton
    {
        public Button(String text)
        {
            super(text);
            addActionListener(new ButtonListener());
        }
    }
    class ButtonListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            String label = ( (JButton)e.getSource() ).getText();
            
            if (label.equals("<psize"))
            {
                if (SpriteEditor2.canvas.pixelSize == 1) return;
                SpriteEditor2.canvas.setPixelSize(SpriteEditor2.canvas.pixelSize - 1);
                pixelSizeSlider.setValue(pixelSizeSlider.getValue() - 1);
            }
            
            else if (label.equals("psize>"))
            {
                if (SpriteEditor2.canvas.pixelSize == maxPixelSize) return;
                SpriteEditor2.canvas.setPixelSize(SpriteEditor2.canvas.pixelSize + 1);
                pixelSizeSlider.setValue(pixelSizeSlider.getValue() + 1);
            }
            else if (label.equals("<pspeed"))
            {
                if (pixelMovementSpeed == 1) return;
                pixelMovementSpeed --;
                pixelSpeedSlider.setValue(pixelMovementSpeed);
            }
            else if (label.equals("pspeed>"))
            {
                if (pixelMovementSpeed == maxPixelSize) return;
                pixelMovementSpeed ++;
                pixelSpeedSlider.setValue(pixelMovementSpeed);
            }
            else if (label.equals("^")) 
            {
                if (SpriteEditor2.canvas.camY - pixelMovementSpeed <= 0) SpriteEditor2.canvas.camY = 0;
                else SpriteEditor2.canvas.camY -= pixelMovementSpeed;
            }
            
            else if (label.equals("v")) 
            {
                if (SpriteEditor2.canvas.camY + SpriteEditor2.canvas.frameHeight + pixelMovementSpeed >= SpriteEditor2.canvas.sprite.height * SpriteEditor2.canvas.pixelSize)
                    SpriteEditor2.canvas.camY = SpriteEditor2.canvas.sprite.height * SpriteEditor2.canvas.pixelSize - SpriteEditor2.canvas.frameHeight;
                else SpriteEditor2.canvas.camY += pixelMovementSpeed;             
            }
            else if (label.equals("<")) 
            {
                if (SpriteEditor2.canvas.camX - pixelMovementSpeed <= 0) SpriteEditor2.canvas.camX = 0;
                else SpriteEditor2.canvas.camX -= pixelMovementSpeed;
            }          
            else if (label.equals(">")) 
            {
                if (SpriteEditor2.canvas.camX + SpriteEditor2.canvas.frameWidth + pixelMovementSpeed >= SpriteEditor2.canvas.sprite.width * SpriteEditor2.canvas.pixelSize)
                    SpriteEditor2.canvas.camX = SpriteEditor2.canvas.sprite.width * SpriteEditor2.canvas.pixelSize - SpriteEditor2.canvas.frameWidth;
                else SpriteEditor2.canvas.camX += pixelMovementSpeed;
            }          
            SpriteEditor2.canvas.repaint();
        }
    }
    
    class SliderListener implements ChangeListener
    {
        public void stateChanged(ChangeEvent e)
        {
            JSlider source = (JSlider)e.getSource();
            if (source == pixelSizeSlider)
            {              
                SpriteEditor2.canvas.setPixelSize(source.getValue());
                pixelSizeLabel.setText("Pixel size: " + source.getValue());
                SpriteEditor2.canvas.repaint();      
            }
            else
            {
                pixelMovementSpeed = source.getValue();
                pixelSpeedLabel.setText("Speed: " + pixelMovementSpeed);
            }
        }
    }
}

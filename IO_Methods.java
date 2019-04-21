
package SpriteEditor2;

import java.awt.Color;
import javax.swing.JOptionPane;
import java.io.*;

public class IO_Methods         // this is where all the exceptions get thrown, NOT where they are handled. That happens in the GUI *
{
    protected static void savePalette(Palette p)    throws FileNotFoundException, IOException
    {
        //  savePalette doesn't throw a DuplicateNameException because one would have already been thrown in newPalette
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(p.name + ".p"));
        
        oos.writeObject(p);     // writeObject doesn't throw a ClassNotFoundException
        oos.close();
    }
    
    protected static Palette loadPalette(String paletteName)    throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(paletteName + ".p"));    // throws FnF automatically 
        
        Palette p = (Palette) ois.readObject();
        
        p.activate();       // palette must be activated before being deployed
        
        ois.close();
        return p;
    }
    
  
    
        // There is no newSprite() method in IO_Methods because sprites don't start out with a name. You make the sprite, then are prompted for a name at save-time.
        // This results in somewhat different logic.
    
    protected static void saveSprite(Sprite s)  throws DuplicateNameException, FileNotFoundException, IOException, ClassNotFoundException
    {
        File f = new File(s.name + ".s2");
        if (f.exists()) throw new DuplicateNameException();
        
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
        
        oos.writeObject(s);
        oos.close();
    }
    
    public static Sprite loadSprite(String spriteName)  throws FileNotFoundException, IOException, ClassNotFoundException  // methods for loading sprite and image are both public
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(spriteName + ".s2"));     // here is where the 3 exceptions can get thrown
        
        Sprite s = (Sprite)ois.readObject();
        ois.close();
        return s;
    }
    
    public static Image loadImage(String imageName)  throws FileNotFoundException, IOException, ClassNotFoundException
    {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(imageName + ".s2"));
        
        Sprite s = (Sprite)ois.readObject();
        
        ois.close();
        return s.layers.get(0);     // loads returns the first layer of a sprite object loaded from a file, cuz the sprite editor only saves full sprite objects which can be 1 layer
    }   
    
    
         // You know, there are so many ways to deal with errors and other contingencies, even if it's just with local conditionals
        // So I figured, why not stretch my legs a bit here, and do something new? Working more with try-catch, I figured out how to create my own exceptions easily enough
    
    static class DefaultPaletteException extends Exception       // thrown when a new color is added to the Default palette      
    {}
    static class DuplicateNameException extends Exception     // thrown when a duplicate .s2 or .p file is found, when a palette is already open, or a duplicate color is on a palette
    {}
    static class PaletteNotOpenException extends Exception  // thrown when trying to save a color to a palette that isn't already open
    {}
}


package SpriteEditor2;

import java.io.Serializable;
import java.awt.Color;
import java.util.ArrayList;
import java.io.File;

public class Sprite implements Serializable
{
    public ArrayList<Layer> layers = new ArrayList<>();      // these will be 2D animations 
    
    public int width;        // these are virtual pixel values
    public int height;       // the width of the sprite is the sum of the maximum width of its layers plus the maximum X offset of its layers. Height is the same but for Y
    
    public String name = "Untitled";
    
    public Sprite()
    {}
    
    public Sprite(Sprite s)
    {
        width = s.width;
        height = s.height;
        name = s.name;
        layers = s.layers;
    }
    
    public Sprite(Layer l)
    {
        width = l.width + l.Xoffset;
        height = l.height + l.Yoffset;
        layers.add(l);
    }
    
    protected void addLayer(Layer l)
    { 
        layers.add(l);
        l.index = layers.size()-1;
        findDimensions();
    }
    protected void deleteLayer(int layerIndex)
    {
        for (int i = 0; i < layers.size(); ++i)         // there are two ways of doing this, and this one might save just like an infinitesmal amout of processing
            if (i > layerIndex) layers.get(i).index--;
        
        if (layerIndex == 0) SpriteEditor2.canvas.setActiveLayer(layerIndex + 1);
        else SpriteEditor2.canvas.setActiveLayer(layerIndex - 1);
        layers.remove(layerIndex);
        findDimensions();   
    }
    
    protected void findDimensions()     // takes the sums of the max widths / heights and offsets of the layers and those values become the width and height of the sprite
    {
        int maxTotalWidth = layers.get(0).Xoffset + layers.get(0).width;
        int maxTotalHeight = layers.get(0).Yoffset + layers.get(0).height;
        
        for (Layer r : layers)
        {
            if (r.Xoffset + r.width > maxTotalWidth) maxTotalWidth = r.Xoffset + r.width;
            if (r.Yoffset + r.height> maxTotalHeight) maxTotalHeight = r.Yoffset + r.height;
        }           
        width = maxTotalWidth;
        height = maxTotalHeight;
    }
}

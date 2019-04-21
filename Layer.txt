
package SpriteEditor2;

public class Layer extends Image
{
    public int Xoffset;
    public int Yoffset;
    public int index;
    public boolean active;
    public boolean visible;
    
    public Layer(String n, int w, int h, int x, int y)
    {       
        super(w,h);
        if (n.equals("")); else name = n; 
        Xoffset = x; Yoffset = y;              
    }
}
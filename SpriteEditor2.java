

                                                    //      There is a light out in the dark
                                                    //      There is a light out in the dark
                                                    //      There is a light out in the dark, so take my hand ...


package SpriteEditor2;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class SpriteEditor2      // by T3y_soft
{
    protected static final int maxHeight = 1500;
    protected static final int maxWidth = 800;
    protected static final int maxPixelSize = 15;
    
    protected static Brush brush;          
    protected static Canvas canvas;
    protected static EditorWindow editorWindow;
    protected static ColorCreator colorCreator;
    protected static ColorSelector colorSelector;
    protected static LayerManager layerManager;
    protected static BrushTools brushTools;
    protected static CanvasController canvasController;
    
    protected static ArrayList<Palette> palettes = new ArrayList<>();
    
    public static void main (String [] args)
    {
        new InitialSelectionWindow();
    }
}

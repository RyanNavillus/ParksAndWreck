package game;

import com.polaris.engine.render.Texture;
import org.lwjgl.opengl.GL11;

/**
 * Created by Sam on 9/30/2017.
 */
public class Track {

    private static Texture[] textures = new Texture[6];

    public double xPos;
    public double yPos;
    public double rotation;
    public double scale = 16;
    public int texture;

    public Track(double xPos, double yPos, double rotation, int texture){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
        this.texture = texture;
    }

    public void render(double delta){

    }

    public static void setTrackTextures(Texture[] texts){
        textures = texts;
    }

    public static Texture[] getTrackTextures(){
        return textures;
    }


}

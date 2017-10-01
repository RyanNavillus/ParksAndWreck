package game;

import com.polaris.engine.render.Texture;
import org.lwjgl.opengl.GL11;

/**
 * Created by Sam on 9/30/2017.
 */
public class Track {

    private static Texture[] textures = new Texture[6];

    private double xPos;
    private double yPos;
    private double rotation;
    private double scale = 16;
    private int texture;

    public Track(double xPos, double yPos, double rotation, int texture){
        this.xPos = xPos;
        this.yPos = yPos;
        this.rotation = rotation;
        this.texture = texture;
    }

    public void render(double delta){
        GL11.glPushMatrix();

        GL11.glTranslatef((float) (xPos + scale / 2), (float) (yPos + scale / 2), 0);
        GL11.glRotatef((float) (rotation * 180 / Math.PI - 90), 0, 0, 1);
        GL11.glTranslatef((float) -(xPos + scale / 2), (float) -(yPos + scale / 2), 0);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glColor4f(0f, 0f, 0f, .2f);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        textures[texture].bind();

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(0 + xPos, 0 + yPos);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(0 + xPos, scale + yPos);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(scale + xPos, scale + yPos);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(scale + xPos, 0 + yPos);
        GL11.glEnd();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPopMatrix();
    }

    public static void setTrackTextures(Texture[] texts){
        textures = texts;
    }


}

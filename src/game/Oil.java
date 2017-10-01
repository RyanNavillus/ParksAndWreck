package game;

import com.polaris.engine.render.Texture;
import org.lwjgl.opengl.GL11;

/**
 * Created by Sam on 9/30/2017.
 */
public class Oil {

    private static Texture[] textures = new Texture[4];

    private double xPos;
    private double yPos;
    private double rotation;

    double scale;

    public Oil(double x, double y, double rotation){
        xPos = x;
        yPos = y;
        this.rotation = rotation;

        scale = 5;
    }

    public void render(double delta){
        GL11.glPushMatrix();

        GL11.glTranslatef((float) (xPos + scale / 2), (float) (yPos + scale / 2), 0);
        GL11.glRotatef((float) (rotation / Math.PI - 90), 0, 0, 1);
        GL11.glTranslatef((float) -(xPos + scale / 2), (float) -(yPos + scale / 2), 0);

        GL11.glColor3f(1f, 1f, 1f);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        int texture = (int) scale / 30;

        if (texture > 4)
            texture = 4;

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

        GL11.glPopMatrix();
    }

    public double getxPos(){
        return xPos;
    }

    public double getyPos(){
        return yPos;
    }

    public double getScale(){
        return scale;
    }

    public void grow(double ammount){
        double area = Math.pow(scale, 2) + Math.pow(ammount, 2);
        scale = Math.sqrt(area);

        for(Oil oil : World.getOils()){
            if (!this.equals(oil) && Math.abs(xPos - oil.getxPos()) < oil.scale && Math.abs(yPos - oil.getyPos()) < oil.scale ){
                World.getOils().remove(this);
                oil.grow(this.getScale());
                break;
            }
        }
    }

    public boolean equals(Oil oil){
        return xPos == oil.getxPos() && yPos == oil.getyPos();
    }

    public static void setOilTextures(Texture[] texts){
        textures = texts;
    }

}

package game;

import org.lwjgl.opengl.GL11;

/**
 * Created by Sam on 9/29/2017.
 */
public class ParkingSpot {

    boolean invertY;

    public ParkingSpot(double x, double y, boolean invertY){
        //xPos = x;
       // yPos = y;
        this.invertY = invertY;
    }

    public void render(){

        /*GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(0, 0);
        GL11.glVertex2d(0, 1000);
        GL11.glVertex2d(190, 1000);
        GL11.glVertex2d(190, 0);
        GL11.glEnd();*/

        /*int invert = 1;
        if (invertY)
            invert = -1;

        //left
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(0 + xPos, 0 + yPos);
        GL11.glVertex2d(5 + xPos, 0 + yPos);
        GL11.glEnd();

        //up
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(5 + xPos, 0 + yPos);
        GL11.glVertex2d(5 + xPos, 5 * invert + yPos);
        GL11.glEnd();

        //right
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glEnd();*/
    }
}

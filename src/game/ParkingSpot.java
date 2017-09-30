package game;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by Sam on 9/29/2017.
 */
public class ParkingSpot {

    public static final int WIDTH = 60;
    public static final int HEIGHT = 90;
    double xPos, yPos;
    int direction;

    public ParkingSpot(double x, double y, int direction){
        xPos = x;
        yPos = y;

        if (direction > 4){
            System.out.println("Parking spot direction needs to be a number 0-3!");
            direction = 0;
        }

        this.direction = direction;
    }

    public static ArrayList<ParkingSpot> CreateParkingArea(int x, int y, int width, int direction){
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();
        int xOffset = 0;
        int yOffset = 0;

        for (int i = 0; i < width; i++){
            parkingSpots.add(new ParkingSpot(x + xOffset, y + yOffset, direction));

            if (direction == 0 || direction == 2){
                xOffset = i * (ParkingSpot.WIDTH - 5);
            } else {
                yOffset = i * (ParkingSpot.WIDTH - 5);
            }
        }

        return parkingSpots;
    }

    public void render(double delta){
        GL11.glColor3f(1.0f, 1.0f, 1.0f);

        int invert = 1;
        if (direction == 2 || direction == 3)
            invert = -1;

        double xPos = this.xPos;
        double yPos = this.yPos;

        if (direction == 2){
            yPos += HEIGHT;
        }

        if (direction == 3){
            xPos += HEIGHT;
        }

        //vertical parking spot
        if (direction == 0 || direction == 2) {
            //left
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(0 + xPos, 0 + yPos);
            GL11.glVertex2d(0 + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(5 + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(5 + xPos, 0 + yPos);

            //up
            GL11.glVertex2d(5 + xPos, 0 + yPos);
            GL11.glVertex2d(5 + xPos, 5 * invert + yPos);
            GL11.glVertex2d(WIDTH - 5 + xPos, 5 * invert + yPos);
            GL11.glVertex2d(WIDTH - 5 + xPos, 0 + yPos);

            //right
            GL11.glVertex2d(WIDTH - 5 + xPos, 0 + yPos);
            GL11.glVertex2d(WIDTH - 5 + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(WIDTH + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(WIDTH + xPos, 0 + yPos);
            GL11.glEnd();
        } else {
            //top
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(0 + xPos, 0 + yPos);
            GL11.glVertex2d(0 + xPos, 5 + yPos);
            GL11.glVertex2d(HEIGHT * invert + xPos, 5 + yPos);
            GL11.glVertex2d(HEIGHT * invert + xPos, 0 + yPos);

            //middle
            GL11.glVertex2d(0 + xPos, 5 + yPos);
            GL11.glVertex2d(0 + xPos, WIDTH - 5 + yPos);
            GL11.glVertex2d(5 * invert + xPos, WIDTH - 5 + yPos);
            GL11.glVertex2d(5 * invert + xPos, 5 + yPos);

            //bottom
            GL11.glVertex2d(0 + xPos, WIDTH - 5 + yPos);
            GL11.glVertex2d(0 + xPos, WIDTH + yPos);
            GL11.glVertex2d(HEIGHT * invert + xPos, WIDTH + yPos);
            GL11.glVertex2d(HEIGHT * invert + xPos, WIDTH - 5 + yPos);
            GL11.glEnd();
        }

    }
}

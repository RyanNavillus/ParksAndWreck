package game;

import org.lwjgl.opengl.GL11;

import com.sun.corba.se.spi.activation._ActivatorImplBase;

import java.util.ArrayList;

/**
 * Created by Sam on 9/29/2017.
 */
public class ParkingSpot {

	public static int count = 0;
    public static final int WIDTH = 90;
    public static final int HEIGHT = 130;
    double xPos, yPos;
    int direction;
    private Car car;
    private float[] color = new float[3];
    public int id;
    
    public ParkingSpot(double x, double y, int direction){
        xPos = x;
        yPos = y;
        color[0] = 1.0f;
        color[1] = 1.0f;
        color[2] = 1.0f;

        if (direction > 4){
            System.out.println("Parking spot direction needs to be a number 0-4!");
            direction = 0;
        }

        this.direction = direction;
        count++;
        id = count;
    }

    public static ArrayList<ParkingSpot> createParkingArea(int x, int y, int width, int direction){
        ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();
        int xOffset = 0;
        int yOffset = 0;

        for (int i = 0; i < width; i++){
            
            if (direction == 0 || direction == 2 || direction == 4){
                xOffset = i * (ParkingSpot.WIDTH - 5);
            } else {
                yOffset = i * (ParkingSpot.WIDTH - 5);
            }
            
            parkingSpots.add(new ParkingSpot(x + xOffset, y + yOffset, direction));
        }

        return parkingSpots;
    }

    public void render(double delta){
        GL11.glColor3f(color[0], color[1], color[2]);

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
        } else if (direction == 1 || direction == 3){
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
        } else {
            //left
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2d(0 + xPos, 0 + yPos);
            GL11.glVertex2d(0 + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(5 + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(5 + xPos, 0 + yPos);

            //right
            GL11.glVertex2d(WIDTH - 5 + xPos, 0 + yPos);
            GL11.glVertex2d(WIDTH - 5 + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(WIDTH + xPos, HEIGHT * invert + yPos);
            GL11.glVertex2d(WIDTH + xPos, 0 + yPos);
            GL11.glEnd();
        }
    }

    public boolean containsCar(Car car) {
    	double centerX = car.getX();
    	double centerY = car.getY();
    	double xPos2, yPos2;
    	if (direction == 1 )
    	{
    		xPos2 = xPos + HEIGHT;
    		yPos2 = yPos + WIDTH;
    	} else if (direction == 3 )
    	{
    		xPos2 = xPos + HEIGHT;
    		yPos2 = yPos + WIDTH;
    	} else 
    	{
    		xPos2 = xPos + WIDTH;
    		yPos2 = yPos + HEIGHT;
    	}
    	System.out.println("id: " + id + "(" + xPos + ", " + yPos + ")" + "(" + xPos2 + ", " + yPos2 + ")");

    	if(centerX > xPos && centerX < (xPos + WIDTH) && centerY < yPos && centerY > (yPos + HEIGHT)) 
    	{
    		//System.out.println("Parked in parking spot: " + id);
        	System.out.println("id: " + id + "(" + xPos + ", " + yPos + ")" + "(" + (xPos + WIDTH) + ", " + (yPos + HEIGHT) + ")");
    		return true;
    	}
    	return false;
    }
    
    public void parkCar(Car car){
        this.car = car;
    }

    public boolean isAvailable(){
        return car == null ? true : false;
    }

    public void setColor(float r, float g, float b){
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }
}

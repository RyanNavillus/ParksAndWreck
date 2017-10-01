package game;

import org.lwjgl.opengl.GL11;

/**
 * Created by Sam on 9/30/2017.
 */
public class Oil {

    private double xPos;
    private double yPos;

    double scale;

    public Oil(double x, double y){
        xPos = x;
        yPos = y;

        scale = 5;
    }

    public void render(double delta){
        GL11.glColor3f(1f, 1f, 0f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(0 + xPos, 0 + yPos);
        GL11.glVertex2d(0 + xPos, scale + yPos);
        GL11.glVertex2d(scale + xPos, scale + yPos);
        GL11.glVertex2d(scale + xPos, 0 + yPos);
        GL11.glEnd();
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
                System.out.println(oil);
                System.out.println(this);
                World.getOils().remove(this);
                oil.grow(this.getScale());
                break;
            }
        }
    }

    public boolean equals(Oil oil){
        return xPos == oil.getxPos() && yPos == oil.getyPos();
    }

}

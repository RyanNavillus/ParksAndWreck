package game;

import com.polaris.engine.App;
import com.polaris.engine.gui.GuiScreen;
import com.polaris.engine.render.Shader;
import com.polaris.engine.util.MathHelper;
import org.dyn4j.dynamics.World;
import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 9/29/2017.
 */
public class GuiParkingLot extends GuiScreen<GameSettings> {

    private ArrayList<ParkingSpot> parkingSpots = new ArrayList<>();

    public GuiParkingLot(App<GameSettings> app) {
        super(app);

        /*for (int i = 0; i < 15; i++){
            parkingSpots.add(new ParkingSpot(700 + i * (ParkingSpot.WIDTH - 5), 500, 0));
        }*/

        parkingSpots = ParkingSpot.createParkingArea(100, 100, 50, 0);
    }

    public void render(double delta)
    {
        super.render(delta);

        GL11.glPushMatrix();
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 1920, 1080, 0, -1, 1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glColor3f(0.0f,0.0f,0.0f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(0, 0);
        GL11.glVertex2d(0, 1080);
        GL11.glVertex2d(1920, 1080);
        GL11.glVertex2d(1920, 0);
        GL11.glEnd();

        for (int i = 0; i < parkingSpots.size(); i++){
            parkingSpots.get(i).render(delta);
        }

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(-1, 1, -1, 1, -1, 1);
        GL11.glPopMatrix();
    }

    public void update(double delta){
        super.update(delta);
    }

}

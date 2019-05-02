package raytracing.raycaster;

import org.joml.*;
import renderer.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class RayTracingDisplay extends Renderer
{
    public static void main(String[] args)
    {
        new RayTracingDisplay().run();
    }
    
    RayCaster  caster;
    List<Wall> walls = new ArrayList<>();
    
    List<Vector2f> points = new ArrayList<>();
    
    @Override
    public void initPostGL()
    {
        setWindowSize(new Vector2i(400, 400));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        caster = new RayCaster(new Vector2f(100, 200), 10);
        
        for (int i = 0; i < 5; i++)
        {
            float x1 = ThreadLocalRandom.current().nextInt(screenBounds.x);
            float x2 = ThreadLocalRandom.current().nextInt(screenBounds.x);
            float y1 = ThreadLocalRandom.current().nextInt(screenBounds.y);
            float y2 = ThreadLocalRandom.current().nextInt(screenBounds.y);
            
            walls.add(new Wall(new Vector2f(x1, y1), new Vector2f(x2, y2)));
        }
        walls.add(new Wall(new Vector2f(0, 0), new Vector2f(screenBounds.x, 0)));
        walls.add(new Wall(new Vector2f(screenBounds.x, 0), new Vector2f(screenBounds.x, screenBounds.y)));
        walls.add(new Wall(new Vector2f(screenBounds.x, screenBounds.y), new Vector2f(0, screenBounds.y)));
        walls.add(new Wall(new Vector2f(0, screenBounds.y), new Vector2f(0, 0)));
        
    }
    
    @Override
    public void update()
    {
        caster.pos.set(cursor);
        points = caster.look(walls);
    }
    
    @Override
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        this.walls.forEach(Wall::render);
        caster.render();
        
        this.points.forEach(p -> {
            glColor4f(1, 1, 1, 0.5f);
            Shapes.drawLine(caster.pos, p, 1);
            Shapes.drawFilledCircle(p, 6);
            glColor4f(1, 1, 1, 1);
        });
    }
}

package raytracing.raymarcher;

import org.joml.*;
import renderer.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class RayMarcherDisplay extends Renderer
{
    public static void main(String[] args)
    {
        new RayMarcherDisplay().run();
    }
    
    RayMarcher ray;
    
    List<Circle> circles = new ArrayList<>();
    List<Float>  lengths = new ArrayList<>();
    
    @Override
    public void initPostGL()
    {
        setWindowSize(new Vector2i(400, 400));
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        ray = new RayMarcher(new Vector2f(100, 200), new Vector2f(1, 0));
        
        for (int i = 0; i < 5; i++)
        {
            int r = ThreadLocalRandom.current().nextInt(10, 50);
            int x = ThreadLocalRandom.current().nextInt(r, screenBounds.x - r);
            int y = ThreadLocalRandom.current().nextInt(r, screenBounds.y - r);
            
            circles.add(new Circle(new Vector2f(x, y), r));
        }
    }
    
    @Override
    public void update()
    {
        lengths = ray.march(circles, screenBounds);
    }
    
    @Override
    public void render()
    {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        ray.render(lengths);
        
        circles.forEach(Circle::render);
    }
}

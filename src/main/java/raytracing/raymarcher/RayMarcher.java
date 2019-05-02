package raytracing.raymarcher;

import org.joml.*;
import renderer.Shapes;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class RayMarcher
{
    public Vector2f pos;
    public Vector2f dir;
    
    public RayMarcher(Vector2f pos, Vector2f dir)
    {
        this.pos = pos;
        this.dir = dir;
    }
    
    public List<Float> march(List<Circle> objs, Vector2i screenBounds)
    {
        List<Float> returnPoints = new ArrayList<>();
        
        Vector2f current = new Vector2f(this.pos);
        
        int counter = 0;
        while (counter++ < 1000)
        {
            float best = Float.MAX_VALUE;
            for (Circle obj : objs)
            {
                float dist = obj.signedDistance(current);
                if (dist < best)
                {
                    best = dist;
                }
            }
            
            current.add(dir.mul(best, new Vector2f()));
            returnPoints.add(best);
            
            if (best < 0.1 || isOffscreen(current, screenBounds))
            {
                break;
            }
        }
        
        return returnPoints;
    }
    
    private boolean isOffscreen(Vector2f current, Vector2i screenBounds)
    {
        return current.x < 0 || current.x > screenBounds.x || current.y < 0 || current.y > screenBounds.y;
    }
    
    public void render(List<Float> distances)
    {
        glPushMatrix();
        glColor4f(1, 0, 0.75f, 0.75f);
        
        glTranslatef(this.pos.x, this.pos.y, 0);
        
        for (Float distance : distances)
        {
            Shapes.drawHollowCircle(new Vector2f(0), distance);
            Shapes.drawLine(new Vector2f(0), this.dir.mul(distance, new Vector2f()), 1);
            Vector2f next = this.dir.mul(distance, new Vector2f());
            glTranslatef(next.x, next.y, 0);
        }
        
        glColor4f(1, 1, 1, 1);
        glPopMatrix();
    }
}

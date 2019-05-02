package raytracing.raymarcher;

import org.joml.*;
import renderer.Shapes;

import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class Circle
{
    public Vector2f pos;
    public Vector2f vel;
    public float    radius;
    
    public Circle(Vector2f pos, float radius)
    {
        this.pos = pos;
        this.radius = radius;
        this.vel = new Vector2f(ThreadLocalRandom.current().nextFloat(), ThreadLocalRandom.current().nextFloat());
    }
    
    public void render()
    {
        Shapes.drawHollowCircle(pos, radius);
    }
    
    public void update(Vector2i bounds)
    {
        this.pos.add(vel);
        if (this.pos.x < this.radius || this.pos.x > bounds.x - this.radius)
        {
            this.vel.mul(-1, 1);
        }
    
        if (this.pos.y < this.radius || this.pos.y > bounds.y - this.radius)
        {
            this.vel.mul(1, -1);
        }
    }
    
    public float signedDistance(Vector2f pos)
    {
        return pos.distance(this.pos) - this.radius;
    }
    
    public void renderHighlight()
    {
        glColor3f(0.5f, 0.5f, 0.5f);
        Shapes.drawFilledCircle(pos, radius);
        glColor3f(1, 1, 1);
    }
}

package raytracing.raycaster;

import org.joml.Vector2f;
import renderer.Shapes;

import static org.lwjgl.opengl.GL11.*;

public class Ray
{
    public Vector2f pos;
    public Vector2f dir;
    
    public Ray(Vector2f pos, Vector2f dir)
    {
        this.pos = pos;
        this.dir = dir;
    }
    
    public void lookAt(Vector2f dir)
    {
        this.dir.x = dir.x - this.pos.x;
        this.dir.y = dir.y - this.pos.y;
        this.dir.normalize();
    }
    
    public void render()
    {
        render(10);
    }
    
    public void render(float distance)
    {
        glPushMatrix();
        glTranslatef(this.pos.x, this.pos.y, 0);
        Shapes.drawLine(new Vector2f(0), this.dir.mul(distance, new Vector2f()), 1);
        glPopMatrix();
    }
    
    public Vector2f cast(Wall w)
    {
        float x1 = w.a.x;
        float y1 = w.a.y;
        float x2 = w.b.x;
        float y2 = w.b.y;
        
        float x3 = this.pos.x;
        float y3 = this.pos.y;
        float x4 = this.pos.x + this.dir.x;
        float y4 = this.pos.y + this.dir.y;
        
        
        float denominator = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (denominator == 0)
        {
            return null;
        }
        
        float tNumerator = (x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4);
        float uNumerator = (x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3);
        
        float t = tNumerator / denominator;
        float u = -uNumerator / denominator;
        
        if (u > 0)
        {
            if (t > 0 && 1 > t)
            {
                float x = x1 + t * (x2 - x1);
                float y = y1 + t * (y2 - y1);
                
                return new Vector2f(x, y);
            }
        }
        
        return null;
    }
    
}

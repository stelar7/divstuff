package raytracing.raymarcher;

import org.joml.Vector2f;
import renderer.Shapes;

public class Circle
{
    public Vector2f pos;
    public float    radius;
    
    public Circle(Vector2f pos, float radius)
    {
        this.pos = pos;
        this.radius = radius;
    }
    
    public void render()
    {
        Shapes.drawHollowCircle(pos, radius);
    }
    
    public float signedDistance(Vector2f pos)
    {
        return pos.distance(this.pos) - this.radius;
    }
}

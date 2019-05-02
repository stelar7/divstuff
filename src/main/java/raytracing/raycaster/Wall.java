package raytracing.raycaster;

import org.joml.Vector2f;
import renderer.Shapes;

public class Wall
{
    public Vector2f a;
    public Vector2f b;
    
    public Wall(Vector2f a, Vector2f b)
    {
        this.a = a;
        this.b = b;
    }
    
    public void render()
    {
        Shapes.drawLine(a, b, 5);
    }
}

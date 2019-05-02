package raytracing;

import org.joml.Vector2f;
import renderer.Shapes;

import java.util.*;
import java.util.stream.Collectors;

public class RayCaster
{
    public Vector2f  pos;
    public List<Ray> rays = new ArrayList<>();
    
    public RayCaster(Vector2f pos, int rayInterval)
    {
        this.pos = pos;
        for (int i = 0; i < 360; i += rayInterval)
        {
            this.rays.add(new Ray(pos, fromAngle(i)));
        }
    }
    
    public List<Vector2f> look(Wall w)
    {
        return this.rays.stream().map(r -> r.cast(w)).filter(Objects::nonNull).collect(Collectors.toList());
    }
    
    public List<Vector2f> look(List<Wall> walls)
    {
        List<Vector2f> bestRays = new ArrayList<>();
        
        for (Ray ray : this.rays)
        {
            Vector2f closest = new Vector2f(Float.MAX_VALUE);
            float    best    = Float.MAX_VALUE;
            
            for (Wall wall : walls)
            {
                Vector2f point = ray.cast(wall);
                if (point != null)
                {
                    float distance = point.distance(this.pos);
                    if (distance < best)
                    {
                        best = distance;
                        closest = point;
                    }
                }
            }
            if (best != Float.MAX_VALUE)
            {
                bestRays.add(closest);
            }
        }
        return bestRays;
    }
    
    public Vector2f fromAngle(int angle)
    {
        return new Vector2f((float) Math.cos(Math.toRadians(angle)), (float) Math.sin(Math.toRadians(angle)));
    }
    
    public void render()
    {
        Shapes.drawFilledCircle(this.pos, 2);
        //this.rays.forEach(Ray::render);
    }
}

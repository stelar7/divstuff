package renderer;

import org.joml.Math;
import org.joml.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Shapes
{
    
    public static void drawTriangle(Vector2f center, float size)
    {
        glBegin(GL_TRIANGLES);
        glVertex2f(center.x + size, center.y + size);
        glVertex2f(center.x, center.y - size);
        glVertex2f(center.x - size, center.y + size);
        glEnd();
    }
    
    
    public static void drawSegmentedLine(List<Vector2i> points, Vector2f cellSize, int size)
    {
        glBegin(GL_LINE_STRIP);
        glLineWidth(size);
        for (final Vector2i point : points)
        {
            glVertex2f(point.x * cellSize.x + cellSize.x / 2, point.y * cellSize.y + cellSize.y / 2);
        }
        glLineWidth(1);
        glEnd();
        
    }
    
    public static void drawFilledCircle(Vector2f center, float radius)
    {
        glBegin(GL_TRIANGLE_FAN);
        
        int quality = 300;
        
        for (int i = 0; i <= quality; i++)
        {
            double angle = 2 * Math.PI * i / quality;
            double x     = center.x + Math.cos(angle) * radius;
            double y     = center.y + Math.sin(angle) * radius;
            glVertex2d(x, y);
        }
        
        glEnd();
    }
    
    
    public static void drawHollowCircle(Vector2f center, float radius)
    {
        glBegin(GL_LINE_LOOP);
        
        int quality = 300;
        
        for (int i = 0; i <= quality; i++)
        {
            double angle = 2 * Math.PI * i / quality;
            double x     = center.x + Math.cos(angle) * radius;
            double y     = center.y + Math.sin(angle) * radius;
            glVertex2d(x, y);
        }
        glEnd();
    }
    
    public static void drawLine(Vector2f from, Vector2f to, float size)
    {
        glLineWidth(size);
        glBegin(GL_LINES);
        glVertex3f(from.x, from.y, 0);
        glVertex3f(to.x, to.y, 0);
        glEnd();
        glLineWidth(1);
    }
    
    
    public static void drawSquare(Vector2i p, Vector2f w)
    {
        glBegin(GL_QUADS);
        glVertex2f(p.x, p.y);
        glVertex2f(p.x + w.x, p.y);
        glVertex2f(p.x + w.x, p.y - w.y);
        glVertex2f(p.x, p.y - w.y);
        glEnd();
    }
    
    
}

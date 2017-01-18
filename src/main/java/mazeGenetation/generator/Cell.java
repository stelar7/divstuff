package mazeGenetation.generator;

import lombok.*;
import org.joml.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

@Getter
@ToString
public class Cell
{
    
    Vector2i pos;
    boolean[] walls   = new boolean[4];
    boolean   visited = false;
    Vector3f  color   = new Vector3f(1, 1, 1);
    
    public Cell(int x, int y)
    {
        this.pos = new Vector2i(x, y);
        Arrays.fill(walls, true);
    }
    
    public void setColor(final float r, final float g, final float b)
    {
        color.set(r, g, b);
    }
    
    public boolean hasWall(final Cell cell)
    {
        if (pos.x > cell.pos.x)
        {
            return hasWall(Direction.LEFT);
        }
        
        if (pos.x < cell.pos.x)
        {
            return hasWall(Direction.RIGHT);
        }
        
        if (pos.y > cell.pos.y)
        {
            return hasWall(Direction.DOWN);
        }
        
        if (pos.y < cell.pos.y)
        {
            return hasWall(Direction.UP);
        }
        
        return true;
    }
    
    public boolean hasWall(Direction dir)
    {
        switch (dir)
        {
            case UP:
                return walls[1];
            case DOWN:
                return walls[3];
            case LEFT:
                return walls[0];
            case RIGHT:
                return walls[2];
            default:
                return false;
        }
    }
    
    public void setWall(Direction dir, boolean flag)
    {
        switch (dir)
        {
            case UP:
                walls[1] = flag;
                return;
            case DOWN:
                walls[3] = flag;
                return;
            case LEFT:
                walls[0] = flag;
                return;
            case RIGHT:
                walls[2] = flag;
                return;
            default:
                break;
        }
    }
    
    public void render(final Vector2i mazeSize, final Vector2f size)
    {
        int x = (int) java.lang.Math.floor(pos.x * size.x);
        int y = (int) java.lang.Math.floor(pos.y * size.y);
        
        Vector2f tl = new Vector2f(x, y);
        Vector2f tr = new Vector2f(x + size.x, y);
        Vector2f bl = new Vector2f(x, y - size.y);
        Vector2f br = new Vector2f(x + size.x, y - size.y);
        
        glColor3f(color.x, color.y, color.z);
        if (hasWall(Direction.LEFT))
        {
            Shapes.drawLine(tl, bl, 1);
        }
        
        if (hasWall(Direction.UP))
        {
            Shapes.drawLine(tl, tr, 1);
        }
        
        if (hasWall(Direction.RIGHT))
        {
            Shapes.drawLine(tr, br, 1);
        }
        
        if (hasWall(Direction.DOWN))
        {
            Shapes.drawLine(br, bl, 1);
        }
        glColor3f(1, 1, 1);
    }
    
    
    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }
}

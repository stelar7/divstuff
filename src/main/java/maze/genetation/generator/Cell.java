package maze.genetation.generator;

import lombok.*;
import org.joml.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

@Getter
@Setter
@ToString
public class Cell
{
    
    private Vector2i pos;
    private boolean  visited;
    
    private boolean[] walls = new boolean[4];
    private Vector3f  color = new Vector3f(1, 1, 1);
    
    private int xDraw;
    private int yDraw;
    
    private Vector2f tl;
    private Vector2f tr;
    private Vector2f bl;
    private Vector2f br;
    
    public Cell(int x, int y, Vector2f size)
    {
        this.pos = new Vector2i(x, y);
        Arrays.fill(walls, true);
        
        x = (int) java.lang.Math.floor(pos.x * size.x);
        y = (int) java.lang.Math.floor(pos.y * size.y);
        
        tl = new Vector2f(x, y);
        tr = new Vector2f(x + size.x, y);
        bl = new Vector2f(x, y - size.y);
        br = new Vector2f(x + size.x, y - size.y);
        
    }
    
    public void setColor(final float r, final float g, final float b)
    {
        color.set(r, g, b);
    }
    
    public boolean hasWallTowards(final Cell cell)
    {
        if (pos.x > cell.pos.x)
        {
            return hasWallInDirection(Direction.LEFT);
        }
        
        if (pos.x < cell.pos.x)
        {
            return hasWallInDirection(Direction.RIGHT);
        }
        
        if (pos.y > cell.pos.y)
        {
            return hasWallInDirection(Direction.DOWN);
        }
        
        if (pos.y < cell.pos.y)
        {
            return hasWallInDirection(Direction.UP);
        }
        
        return true;
    }
    
    public boolean hasWallInDirection(Direction dir)
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
    
    public void render()
    {
        
        glColor3f(color.x, color.y, color.z);
        if (hasWallInDirection(Direction.LEFT))
        {
            Shapes.drawLine(tl, bl, 1);
        }
        
        if (hasWallInDirection(Direction.UP))
        {
            Shapes.drawLine(tl, tr, 1);
        }
        
        if (hasWallInDirection(Direction.RIGHT))
        {
            Shapes.drawLine(tr, br, 1);
        }
        
        if (hasWallInDirection(Direction.DOWN))
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

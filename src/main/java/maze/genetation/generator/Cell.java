package maze.genetation.generator;

import org.joml.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

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
    
    public Cell(final int x, final int y, Vector2f size)
    {
        this.pos = new Vector2i(x, y);
        Arrays.fill(walls, true);
        
        int localx = (int) java.lang.Math.floor(pos.x * size.x);
        int localy = (int) java.lang.Math.floor(pos.y * size.y);
        
        tl = new Vector2f(localx, localy);
        tr = new Vector2f(localx + size.x, localy);
        bl = new Vector2f(localx, localy - size.y);
        br = new Vector2f(localx + size.x, localy - size.y);
        
    }
    
    public Vector2i getPos()
    {
        return pos;
    }
    
    public void setPos(Vector2i pos)
    {
        this.pos = pos;
    }
    
    public boolean isVisited()
    {
        return visited;
    }
    
    public void setVisited(boolean visited)
    {
        this.visited = visited;
    }
    
    public boolean[] getWalls()
    {
        return walls;
    }
    
    public void setWalls(boolean[] walls)
    {
        this.walls = walls;
    }
    
    public Vector3f getColor()
    {
        return color;
    }
    
    public void setColor(Vector3f color)
    {
        this.color = color;
    }
    
    public int getxDraw()
    {
        return xDraw;
    }
    
    public void setxDraw(int xDraw)
    {
        this.xDraw = xDraw;
    }
    
    public int getyDraw()
    {
        return yDraw;
    }
    
    public void setyDraw(int yDraw)
    {
        this.yDraw = yDraw;
    }
    
    public Vector2f getTl()
    {
        return tl;
    }
    
    public void setTl(Vector2f tl)
    {
        this.tl = tl;
    }
    
    public Vector2f getTr()
    {
        return tr;
    }
    
    public void setTr(Vector2f tr)
    {
        this.tr = tr;
    }
    
    public Vector2f getBl()
    {
        return bl;
    }
    
    public void setBl(Vector2f bl)
    {
        this.bl = bl;
    }
    
    public Vector2f getBr()
    {
        return br;
    }
    
    public void setBr(Vector2f br)
    {
        this.br = br;
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

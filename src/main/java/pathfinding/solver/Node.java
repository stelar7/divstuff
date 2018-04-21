package pathfinding.solver;

import org.joml.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public class Node
{
    
    public static final  Vector3f openColor   = new Vector3f(0, 1, 0);
    public static final  Vector3f closedColor = new Vector3f(1, 0, 0);
    public static final  Vector3f pathColor   = new Vector3f(0, 0, 1);
    public static final  Vector3f finalColor  = new Vector3f(1, 0.5f, 0.75f);
    private static final Random   random      = new Random();
    private float g;
    private float f;
    private float h;
    private float      wallChance = 0.1f;
    private List<Node> neighbors  = new ArrayList<>();
    private Node previous;
    private boolean isWall        = false;
    private boolean allowDiagonal = false;
    private Vector2i pos;
    private Vector3f color = new Vector3f(1, 1, 1);
    
    public void setG(float g)
    {
        this.g = g;
    }
    
    public void setF(float f)
    {
        this.f = f;
    }
    
    public void setH(float h)
    {
        this.h = h;
    }
    
    public void setWallChance(float wallChance)
    {
        this.wallChance = wallChance;
    }
    
    public void setNeighbors(List<Node> neighbors)
    {
        this.neighbors = neighbors;
    }
    
    public void setPrevious(Node previous)
    {
        this.previous = previous;
    }
    
    public void setWall(boolean wall)
    {
        isWall = wall;
    }
    
    public void setAllowDiagonal(boolean allowDiagonal)
    {
        this.allowDiagonal = allowDiagonal;
    }
    
    public void setPos(Vector2i pos)
    {
        this.pos = pos;
    }
    
    public static Vector3f getOpenColor()
    {
        return openColor;
    }
    
    public static Vector3f getClosedColor()
    {
        return closedColor;
    }
    
    public static Vector3f getPathColor()
    {
        return pathColor;
    }
    
    public static Vector3f getFinalColor()
    {
        return finalColor;
    }
    
    public static Random getRandom()
    {
        return random;
    }
    
    public float getG()
    {
        return g;
    }
    
    public float getF()
    {
        return f;
    }
    
    public float getH()
    {
        return h;
    }
    
    public float getWallChance()
    {
        return wallChance;
    }
    
    public List<Node> getNeighbors()
    {
        return neighbors;
    }
    
    public Node getPrevious()
    {
        return previous;
    }
    
    public boolean isWall()
    {
        return isWall;
    }
    
    public boolean isAllowDiagonal()
    {
        return allowDiagonal;
    }
    
    public Vector2i getPos()
    {
        return pos;
    }
    
    public Vector3f getColor()
    {
        return color;
    }
    
    public Node(int x, int y, boolean useWall)
    {
        pos = new Vector2i(x, y);
        
        if (useWall)
        {
            isWall = random.nextFloat() < wallChance;
        }
        
    }
    
    protected static Node getNodeAt(final List<Node> nodes, final int x, final int y)
    {
        for (final Node node : nodes)
        {
            if (node.pos.x == x && node.pos.y == y)
            {
                return node;
            }
        }
        return null;
    }
    
    public void render(final Vector2f size)
    {
        int x = (int) java.lang.Math.floor(pos.x * size.x);
        int y = (int) java.lang.Math.floor(pos.y * size.y);
        
        glColor3f(0, 0, 0);
        if (!isWall)
        {
            glColor3f(color.x, color.y, color.z);
        }
        Shapes.drawSquare(new Vector2i(x, y), size);
    }
    
    public void addNeighbors(List<Node> nodes)
    {
        neighbors.add(getNodeAt(nodes, pos.x + 1, pos.y));
        neighbors.add(getNodeAt(nodes, pos.x - 1, pos.y));
        neighbors.add(getNodeAt(nodes, pos.x, pos.y + 1));
        neighbors.add(getNodeAt(nodes, pos.x, pos.y - 1));
        if (allowDiagonal)
        {
            neighbors.add(getNodeAt(nodes, pos.x + 1, pos.y + 1));
            neighbors.add(getNodeAt(nodes, pos.x - 1, pos.y - 1));
            neighbors.add(getNodeAt(nodes, pos.x + 1, pos.y - 1));
            neighbors.add(getNodeAt(nodes, pos.x - 1, pos.y + 1));
        }
        
        neighbors.removeIf(Objects::isNull);
    }
    
    public void setColor(final float r, final float g, final float b)
    {
        color.set(r, g, b);
    }
    
    public void setColor(final Vector3f other)
    {
        color.set(other);
    }
}

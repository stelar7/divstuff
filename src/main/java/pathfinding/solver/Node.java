package pathfinding.solver;

import lombok.*;
import org.joml.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;

@Getter
@Setter
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

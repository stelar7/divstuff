package mazesolver;

import mazeGenetation.generator.*;
import org.joml.*;
import pathfinding.solver.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;


public class NodeCell extends Node
{
    Cell     cell;
    NodeCell previous;
    List<NodeCell> neighbors = new ArrayList<>();
    
    public NodeCell(final int x, final int y, Cell cell)
    {
        super(x, y, false);
        
        this.cell = cell;
    }
    
    protected static Cell getCellAt(final List<Cell> nodes, final int x, final int y)
    {
        for (final Cell node : nodes)
        {
            if (node.getPos().x == x && node.getPos().y == y)
            {
                return node;
            }
        }
        return null;
    }
    
    protected static NodeCell getNodeCellAt(final List<NodeCell> nodes, final int x, final int y)
    {
        for (final NodeCell node : nodes)
        {
            if (node.getPos().x == x && node.getPos().y == y)
            {
                return node;
            }
        }
        return null;
    }
    
    public void addNodeCellNeighbors(List<NodeCell> nodes)
    {
        neighbors.add(getNodeCellAt(nodes, getPos().x + 1, getPos().y));
        neighbors.add(getNodeCellAt(nodes, getPos().x - 1, getPos().y));
        neighbors.add(getNodeCellAt(nodes, getPos().x, getPos().y + 1));
        neighbors.add(getNodeCellAt(nodes, getPos().x, getPos().y - 1));
        if (isAllowDiagonal())
        {
            neighbors.add(getNodeCellAt(nodes, getPos().x + 1, getPos().y + 1));
            neighbors.add(getNodeCellAt(nodes, getPos().x - 1, getPos().y - 1));
            neighbors.add(getNodeCellAt(nodes, getPos().x + 1, getPos().y - 1));
            neighbors.add(getNodeCellAt(nodes, getPos().x - 1, getPos().y + 1));
        }
        
        neighbors.removeIf(Objects::isNull);
    }
    
    public void render(final Vector2i mazeSize, final Vector2f size)
    {
        int x = (int) java.lang.Math.floor(getPos().x * size.x);
        int y = (int) java.lang.Math.floor(getPos().y * size.y);
        
        Vector2f halfSize     = size.mul(0.5f, new Vector2f());
        Vector2f halfHalfSize = halfSize.mul(0.5f, new Vector2f());
        Vector2i halfSizeInt  = new Vector2i((int) halfHalfSize.x, (int) -halfHalfSize.y);
        
        glColor3f(getColor().x, getColor().y, getColor().z);
        Shapes.drawSquare(new Vector2i(x, y).add(halfSizeInt, new Vector2i()), size.sub(halfSize, new Vector2f()));
        cell.render(mazeSize, size);
    }
}

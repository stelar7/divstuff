package maze.genetation.generator;

import maze.genetation.generator.Cell.*;
import org.joml.*;
import renderer.*;

import java.util.*;
import java.util.stream.*;

import static org.lwjgl.opengl.GL11.*;

public class RecurciveBacktrackingMazeGenerator extends MazeGenerator
{
    
    private Cell current;
    private Random      random = new Random();
    private Queue<Cell> stack  = new ArrayDeque<>();
    
    public RecurciveBacktrackingMazeGenerator(Vector2i maze, Vector2i cellCount)
    {
        super(maze, cellCount);
        
        // 1.
        current = cells.get(0);
        current.setVisited(true);
    }
    
    List<Cell> getNeighbours(Cell cell)
    {
        List<Cell> list  = new ArrayList<>();
        
        int x  = cell.getPos().x;
        int x1 = x + 1;
        int x2 = x - 1;
        
        int y  = cell.getPos().y;
        int y1 = y + 1;
        int y2 = y - 1;
        
        for (final Cell c : cells)
        {
            if (c.getPos().x == x)
            {
                if (c.getPos().y == y1 || c.getPos().y == y2)
                {
                    list.add(c);
                }
            }
            
            if (c.getPos().y == y)
            {
                if (c.getPos().x == x1 || c.getPos().x == x2)
                {
                    list.add(c);
                }
            }
        }
        
        return list;
    }
    
    
    // 2.
    @Override
    public void nextStep()
    {
        // 1.
        
        if (finished || cells.stream().allMatch(Cell::isVisited))
        {
            finished = true;
            return;
        }
        
        // 2.
        List<Cell> unvisited = getNeighbours(current).stream().filter(c -> !c.isVisited()).collect(Collectors.toList());
        if (!unvisited.isEmpty())
        {
            int choice = random.nextInt(unvisited.size());
            
            
            Cell chosen = unvisited.get(choice);
            stack.add(current);
            
            
            // 3.
            if (current.getPos().x > chosen.getPos().x)
            {
                current.setWall(Direction.LEFT, false);
                chosen.setWall(Direction.RIGHT, false);
            }
            
            if (current.getPos().x < chosen.getPos().x)
            {
                chosen.setWall(Direction.LEFT, false);
                current.setWall(Direction.RIGHT, false);
            }
            
            if (current.getPos().y > chosen.getPos().y)
            {
                chosen.setWall(Direction.UP, false);
                current.setWall(Direction.DOWN, false);
            }
            
            if (current.getPos().y < chosen.getPos().y)
            {
                current.setWall(Direction.UP, false);
                chosen.setWall(Direction.DOWN, false);
            }
            
            // 4.
            current = chosen;
            current.setVisited(true);
            
            
        } else
        {
            // 2.
            if (!stack.isEmpty())
            {
                // 1.
                // 2.
                current = stack.poll();
            }
        }
    }
    
    @Override
    public void render()
    {
        glColor3f(0, 1, 0);
        
        int x = (int) java.lang.Math.floor(current.getPos().x * cellSize.x);
        int y = (int) java.lang.Math.floor(current.getPos().y * cellSize.y);
        
        
        Shapes.drawSquare(new Vector2i(x, y), cellSize);
        glColor3f(1, 1, 1);
        
        for (Cell c : cells)
        {
            c.render();
        }
    }
}

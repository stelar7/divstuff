package mazeGenetation.generator;

import mazeGenetation.generator.Cell.*;
import org.joml.*;
import renderer.*;

import java.util.*;
import java.util.stream.*;

import static org.lwjgl.opengl.GL11.*;

public class RecurciveBacktrackingMazeGenerator extends MazeGenerator
{
    
    Cell current;
    Random      random = new Random();
    Queue<Cell> stack  = new ArrayDeque<>();
    
    public RecurciveBacktrackingMazeGenerator(Vector2i maze, Vector2i cellCount)
    {
        super(maze, cellCount);
        
        // 1.
        current = cells.get(0);
        current.visited = true;
    }
    
    @SuppressWarnings("ResultOfMethodCallIgnored")
    List<Cell> getNeighbours(Cell cell)
    {
        List<Cell> list = new ArrayList<>();
        
        cells.stream().filter(c -> c.getPos().x == cell.getPos().x + 1 && c.getPos().y == cell.getPos().y).collect(Collectors.toCollection(() -> list));
        cells.stream().filter(c -> c.getPos().x == cell.getPos().x - 1 && c.getPos().y == cell.getPos().y).collect(Collectors.toCollection(() -> list));
        cells.stream().filter(c -> c.getPos().x == cell.getPos().x && c.getPos().y == cell.getPos().y + 1).collect(Collectors.toCollection(() -> list));
        cells.stream().filter(c -> c.getPos().x == cell.getPos().x && c.getPos().y == cell.getPos().y - 1).collect(Collectors.toCollection(() -> list));
        
        return list;
    }
    
    
    // 2.
    @Override
    public void nextStep()
    {
        // 1.
        
        if (finished || cells.stream().allMatch(c -> c.visited))
        {
            finished = true;
            return;
        }
        
        // 2.
        List<Cell> unvisited = getNeighbours(current).stream().filter(c -> !c.visited).collect(Collectors.toList());
        if (unvisited.size() > 0)
        {
            int choice = random.nextInt(unvisited.size());
            
            
            Cell chosen = unvisited.get(choice);
            stack.add(current);
            
            
            // 3.
            if (current.pos.x > chosen.pos.x)
            {
                current.setWall(Direction.LEFT, false);
                chosen.setWall(Direction.RIGHT, false);
            }
            
            if (current.pos.x < chosen.pos.x)
            {
                chosen.setWall(Direction.LEFT, false);
                current.setWall(Direction.RIGHT, false);
            }
            
            if (current.pos.y > chosen.pos.y)
            {
                chosen.setWall(Direction.UP, false);
                current.setWall(Direction.DOWN, false);
            }
            
            if (current.pos.y < chosen.pos.y)
            {
                current.setWall(Direction.UP, false);
                chosen.setWall(Direction.DOWN, false);
            }
            
            // 4.
            current = chosen;
            current.visited = true;
            
            
        } else
        {
            // 2.
            if (stack.size() > 0)
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
        
        int x = (int) java.lang.Math.floor(current.pos.x * cellSize.x);
        int y = (int) java.lang.Math.floor(current.pos.y * cellSize.y);
        
        
        Shapes.drawSquare(new Vector2i(x, y), cellSize);
        glColor3f(1, 1, 1);
        
        for (Cell c : cells)
        {
            c.render(mazeSize, cellSize);
        }
    }
}

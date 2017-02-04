package maze.genetation.generator;

import lombok.*;
import org.joml.*;

import java.util.*;

@Getter
@ToString
public abstract class MazeGenerator
{
    
    protected Vector2i   mazeSize;
    protected Vector2i   cellCount;
    protected List<Cell> cells;
    protected Vector2f   cellSize;
    protected boolean    finished;
    
    public MazeGenerator(Vector2i mazeSize, Vector2i cellCount)
    {
        this.mazeSize = mazeSize;
        this.cellCount = cellCount;
        this.cellSize = new Vector2f((float) mazeSize.x / (float) cellCount.x, (float) mazeSize.y / (float) cellCount.y);
        this.cells = new ArrayList<>();
        
        for (int i = 0; i < cellCount.x; i++)
        {
            for (int j = 0; j < cellCount.y; j++)
            {
                this.cells.add(new Cell(i, j, cellSize));
            }
        }
    }
    
    
    public abstract void nextStep();
    
    public abstract void render();
    
    public boolean isFinished()
    {
        return finished;
    }
}

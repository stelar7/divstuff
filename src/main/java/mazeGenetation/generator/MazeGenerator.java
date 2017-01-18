package mazeGenetation.generator;

import lombok.*;
import org.joml.*;

import java.util.*;

@Getter
@ToString
public abstract class MazeGenerator
{
    
    Vector2i   mazeSize;
    Vector2i   cellCount;
    List<Cell> cells;
    Vector2f   cellSize;
    boolean    finished;
    
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
                this.cells.add(new Cell(i, j));
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

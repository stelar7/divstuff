package maze.genetation.generator;

import org.joml.*;

import java.util.*;

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
    
    public Vector2i getMazeSize()
    {
        return mazeSize;
    }
    
    public void setMazeSize(Vector2i mazeSize)
    {
        this.mazeSize = mazeSize;
    }
    
    public Vector2i getCellCount()
    {
        return cellCount;
    }
    
    public void setCellCount(Vector2i cellCount)
    {
        this.cellCount = cellCount;
    }
    
    public List<Cell> getCells()
    {
        return cells;
    }
    
    public void setCells(List<Cell> cells)
    {
        this.cells = cells;
    }
    
    public Vector2f getCellSize()
    {
        return cellSize;
    }
    
    public void setCellSize(Vector2f cellSize)
    {
        this.cellSize = cellSize;
    }
    
    public void setFinished(boolean finished)
    {
        this.finished = finished;
    }
    
    public abstract void nextStep();
    
    public abstract void render();
    
    public boolean isFinished()
    {
        return finished;
    }
}

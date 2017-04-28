package pathfinding.solver;

import org.joml.Math;
import org.joml.*;

public abstract class PathfinderSolver
{
    private Vector2f cellSize;
    private Vector2i mazeSize;
    
    private HeuristicType heuristicType = HeuristicType.EUCLIDEAN;
    
    public PathfinderSolver(Vector2i mazeSize, Vector2i cellCount)
    {
        this.mazeSize = mazeSize;
        this.cellSize = new Vector2f((float) mazeSize.x / (float) cellCount.x, (float) mazeSize.y / (float) cellCount.y);
    }
    
    public Vector2f getCellSize()
    {
        return cellSize;
    }
    
    public Vector2i getMazeSize()
    {
        return mazeSize;
    }
    
    public HeuristicType getHeuristicType()
    {
        return heuristicType;
    }
    
    public void setHeuristicType(final HeuristicType heuristicType)
    {
        this.heuristicType = heuristicType;
    }
    
    public abstract void nextStep();
    
    public abstract void render();
    
    public float heuristic(final Node a, final Node b)
    {
        float da = Math.abs((float) (a.getPos().x - b.getPos().x));
        float db = Math.abs((float) (a.getPos().y - b.getPos().y));
        
        
        switch (heuristicType)
        {
            case MANHATTAN:
                return da + db;
            case EUCLIDEAN:
                return (float) a.getPos().distance(b.getPos());
            case EUCLIDEAN_SQUARED:
                return (float) a.getPos().distanceSquared(b.getPos());
            case DIAGONAL:
                return da + db + Math.min(da, db);
            case DIJKSTRA:
                return 0;
            default:
                return 1;
        }
    }
}

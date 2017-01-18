package pathfinding.solver;

import com.sun.istack.internal.logging.*;
import org.joml.Math;
import org.joml.*;

public abstract class PathfinderSolver
{
	Vector2f cellSize;
	Vector2i mazeSize;
	
	Logger logger = Logger.getLogger(PathfinderSolver.class);
	
	HeuristicType heuristicType = HeuristicType.EUCLIDEAN;
	
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
		float da = Math.abs(a.pos.x - b.pos.x);
		float db = Math.abs(a.pos.y - b.pos.y);
		
		
		switch (heuristicType)
		{
			case MANHATTAN:
				return da + db;
			case EUCLIDEAN:
				return (float) a.pos.distance(b.pos);
			case EUCLIDEAN_SQUARED:
				return (float) a.pos.distanceSquared(b.pos);
			case DIAGONAL:
				return da + db + Math.min(da, db);
			case DIJKSTRA:
				return 0;
			default:
				return 1;
		}
	}
}

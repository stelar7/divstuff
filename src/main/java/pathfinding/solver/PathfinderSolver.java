package pathfinding.solver;

import org.joml.*;

public abstract class PathfinderSolver
{
	Vector2f cellSize;
	Vector2i mazeSize;
	
	public PathfinderSolver(Vector2i mazeSize, Vector2i cellCount)
	{
		this.mazeSize = mazeSize;
		this.cellSize = new Vector2f((float) mazeSize.x / (float) cellCount.x, (float) mazeSize.y / (float) cellCount.y);
	}
	
	public abstract void nextStep();
	
	public abstract void render();
	
	public abstract float heuristic(Node a, Node b);
}

package pathfinding.solver;

import org.joml.Math;
import org.joml.*;

import java.util.*;

public class AStarSolver extends PathfinderSolver
{
	
	public HeuristicType heuristicType = HeuristicType.MANHATTAN;
	List<Node> openSet   = new ArrayList<>();
	List<Node> closedSet = new ArrayList<>();
	List<Node> path      = new ArrayList<>();
	List<Node> grid      = new ArrayList<>();
	float      w         = cellSize.x;
	float      h         = cellSize.y;
	Node start;
	Node current;
	Node end;
	boolean doneSearching = false;
	
	public AStarSolver(final Vector2i mazeSize, final Vector2i cellCount)
	{
		super(mazeSize, cellCount);
		
		for (int x = 0; x < cellCount.x; x++)
		{
			for (int y = 0; y < cellCount.y; y++)
			{
				grid.add(new Node(x, y, true));
			}
		}
		grid.forEach(n -> n.allowDiagonal = true);
		grid.forEach(n -> n.addNeighbors(grid));
		
		start = getNodeAt(grid, 0, 0);
		start.isWall = false;
		
		end = getNodeAt(grid, cellCount.x - 1, cellCount.y - 1);
		end.isWall = false;
		
		openSet.add(start);
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
	
	@Override
	public void nextStep()
	{
		
		if (doneSearching)
		{
			return;
		}
		
		if (openSet.size() > 0)
		{
			int largestFIndex = 0;
			for (int i = 0; i < openSet.size(); i++)
			{
				if (openSet.get(i).f < openSet.get(largestFIndex).f)
				{
					largestFIndex = i;
				}
			}
			
			current = openSet.get(largestFIndex);
			
			if (current.equals(end))
			{
				System.out.println("We found the end!");
				doneSearching = true;
			}
			
			openSet.remove(largestFIndex);
			closedSet.add(current);
			
			
			for (final Node neighbor : current.neighbors)
			{
				if (!closedSet.contains(neighbor) && !neighbor.isWall)
				{
					
					float g = current.g + heuristic(neighbor, current);
					
					boolean isNewPath = false;
					if (openSet.contains(neighbor))
					{
						if (g < neighbor.g)
						{
							neighbor.g = g;
							isNewPath = true;
						}
					} else
					{
						neighbor.g = g;
						isNewPath = true;
						openSet.add(neighbor);
					}
					
					if (isNewPath)
					{
						
						neighbor.h = heuristic(neighbor, end);
						neighbor.f = neighbor.g + neighbor.h;
						neighbor.previous = current;
					}
				}
			}
		} else
		{
			System.out.println("No path found!");
			doneSearching = true;
		}
	}
	
	@Override
	public void render()
	{
		
		if (current != null)
		{
			path.clear();
			Node temp = current;
			path.add(temp);
			while (temp.previous != null)
			{
				path.add(temp.previous);
				temp = temp.previous;
			}
		}
		
		
		for (final Node node : closedSet)
		{
			node.setColor(1, 0, 0);
		}
		
		for (final Node node : openSet)
		{
			node.setColor(0, 1, 0);
		}
		
		for (final Node node : path)
		{
			node.setColor(0, 0, 1);
		}
		
		if (doneSearching)
		{
			for (final Node node : path)
			{
				node.setColor(1, 0, 0.75f);
			}
		}
		
		for (final Node node : grid)
		{
			node.render(mazeSize, cellSize);
		}
		
	}
	
	@Override
	public float heuristic(final Node a, final Node b)
	{
		// A large value means more likely to travel the path
		
		
		float da = Math.abs(a.pos.x - b.pos.x);
		float db = Math.abs(a.pos.y - b.pos.y);
		
		float dx1 = b.pos.x - end.pos.x;
		float dy1 = b.pos.y - end.pos.y;
		float dx2 = start.pos.x - end.pos.x;
		float dy2 = start.pos.y - end.pos.y;
		
		float cross = Math.abs(dx1 * dy2 - dx2 * dy1) * 0.001f;
		
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
			case TIE_BREAK_CROSS:
				return (da + db) + Math.min(da, db) + cross;
			case DIJKSTRA:
				return 0;
			default:
				return 1;
		}
	}
}

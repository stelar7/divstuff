package pathfinding.solver;

import org.joml.*;

import java.util.*;

public class DijkstraSolver extends PathfinderSolver
{
	
	List<Node> openSet   = new ArrayList<>();
	List<Node> closedSet = new ArrayList<>();
	List<Node> path      = new ArrayList<>();
	List<Node> grid      = new ArrayList<>();
	
	float w = cellSize.x;
	float h = cellSize.y;
	
	Node start;
	Node current;
	Node end;
	
	boolean doneSearching = false;
	
	public DijkstraSolver(final Vector2i mazeSize, final Vector2i cellCount)
	{
		super(mazeSize, cellCount);
		
		for (int x = 0; x < cellCount.x; x++)
		{
			for (int y = 0; y < cellCount.y; y++)
			{
				grid.add(new Node(x, y, true));
			}
		}
		grid.forEach(n -> n.allowDiagonal = false);
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
		return 0; // Dijkstra's algorithm is a case of A* where h(x) is constant
	}
}

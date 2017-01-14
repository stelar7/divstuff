package mazesolver;

import mazeGenetation.generator.*;
import org.joml.*;
import pathfinding.solver.*;

import java.util.*;

public class MazeGeneratorSolver extends PathfinderSolver
{
	
	List<NodeCell> openSet   = new ArrayList<>();
	List<NodeCell> closedSet = new ArrayList<>();
	List<NodeCell> path      = new ArrayList<>();
	List<NodeCell> grid      = new ArrayList<>();
	
	float w = getCellSize().x;
	float h = getCellSize().y;
	
	NodeCell start;
	NodeCell current;
	NodeCell end;
	
	boolean doneSearching = false;
	
	public MazeGeneratorSolver(final Vector2i mazeSize, final Vector2i cellCount, List<Cell> maze)
	{
		super(mazeSize, cellCount);
		setHeuristicType(HeuristicType.MANHATTAN); // Manhattan distance is best for 4 directions, Euclidean for 8
		
		for (int x = 0; x < cellCount.x; x++)
		{
			for (int y = 0; y < cellCount.y; y++)
			{
				grid.add(new NodeCell(x, y, NodeCell.getCellAt(maze, x, y)));
			}
		}
		grid.forEach(n -> n.setAllowDiagonal(false));
		grid.forEach(n -> n.addNodeCellNeighbors(grid));
		
		start = NodeCell.getNodeCellAt(grid, 0, 0);
		start.setWall(false);
		
		end = NodeCell.getNodeCellAt(grid, cellCount.x - 1, cellCount.y - 1);
		end.setWall(false);
		
		openSet.add(start);
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
				if (openSet.get(i).getF() < openSet.get(largestFIndex).getF())
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
			
			
			for (final NodeCell neighbor : current.neighbors)
			{
				if (!closedSet.contains(neighbor) && !neighbor.cell.hasWall(current.cell)/*!neighbor.isWall*/)
				{
					
					float g = current.getG() + heuristic(neighbor, current);
					
					boolean isNewPath = false;
					if (openSet.contains(neighbor))
					{
						if (g < neighbor.getG())
						{
							neighbor.setG(g);
							isNewPath = true;
						}
					} else
					{
						neighbor.setG(g);
						isNewPath = true;
						openSet.add(neighbor);
					}
					
					if (isNewPath)
					{
						
						neighbor.setH(heuristic(neighbor, end));
						neighbor.setF(neighbor.getG() + neighbor.getH());
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
			NodeCell temp = current;
			path.add(temp);
			while (temp.previous != null)
			{
				path.add(temp.previous);
				temp = temp.previous;
			}
		}
		
		for (final Node node : closedSet)
		{
			node.setColor(Node.closedColor);
		}
		
		for (final Node node : openSet)
		{
			node.setColor(Node.openColor);
		}
		
		for (final Node node : path)
		{
			node.setColor(Node.pathColor);
		}
		
		if (doneSearching)
		{
			for (final Node node : path)
			{
				node.setColor(Node.finalColor);
			}
		}
		
		for (final Node node : grid)
		{
			node.render(getMazeSize(), getCellSize());
		}
	}
	
}

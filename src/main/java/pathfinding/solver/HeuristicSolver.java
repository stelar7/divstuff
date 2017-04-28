package pathfinding.solver;

import org.joml.*;

import java.util.*;

public class HeuristicSolver extends PathfinderSolver
{
    
    private List<Node> openSet   = new ArrayList<>();
    private List<Node> closedSet = new ArrayList<>();
    private List<Node> path      = new ArrayList<>();
    private List<Node> grid      = new ArrayList<>();
    private Node current;
    private Node end;
    private boolean doneSearching = false;
    
    
    public HeuristicSolver(final Vector2i mazeSize, final Vector2i cellCount)
    {
        super(mazeSize, cellCount);
        
        for (int x = 0; x < cellCount.x; x++)
        {
            for (int y = 0; y < cellCount.y; y++)
            {
                grid.add(new Node(x, y, true));
            }
        }
        grid.forEach(n -> n.setAllowDiagonal(true));
        grid.forEach(n -> n.addNeighbors(grid));
        
        Node start = Node.getNodeAt(grid, 0, 0);
        start.setWall(false);
        
        end = Node.getNodeAt(grid, cellCount.x - 1, cellCount.y - 1);
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
        
        if (!openSet.isEmpty())
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
            
            
            for (final Node neighbor : current.getNeighbors())
            {
                if (!closedSet.contains(neighbor) && !neighbor.isWall())
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
                        neighbor.setPrevious(current);
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
            while (temp.getPrevious() != null)
            {
                path.add(temp.getPrevious());
                temp = temp.getPrevious();
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
            node.render(getCellSize());
        }
        
    }
    
}

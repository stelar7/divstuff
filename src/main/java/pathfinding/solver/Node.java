package pathfinding.solver;

import org.joml.*;
import renderer.*;

import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static pathfinding.solver.DijkstraSolver.*;

public class Node
{
	
	private static final Random random = new Random();
	float f;
	float g;
	float h;
	float      wallChance = 0.1f;
	List<Node> neighbors  = new ArrayList<>();
	Node previous;
	boolean isWall        = false;
	boolean allowDiagonal = false;
	Vector2i pos;
	Vector3f color = new Vector3f(1, 1, 1);
	
	public Node(int x, int y, boolean useWall)
	{
		pos = new Vector2i(x, y);
		
		if (useWall)
		{
			isWall = random.nextFloat() < wallChance;
		}
		
	}
	
	public void render(final Vector2i mazeSize, final Vector2f size)
	{
		int x = (int) java.lang.Math.floor(pos.x * size.x);
		int y = (int) java.lang.Math.floor(pos.y * size.y);
		
		glColor3f(0, 0, 0);
		if (!isWall)
		{
			glColor3f(color.x, color.y, color.z);
		}
		Shapes.drawSquare(new Vector2i(x, y), size);
	}
	
	public void addNeighbors(List<Node> nodes)
	{
		neighbors.add(getNodeAt(nodes, pos.x + 1, pos.y));
		neighbors.add(getNodeAt(nodes, pos.x - 1, pos.y));
		neighbors.add(getNodeAt(nodes, pos.x, pos.y + 1));
		neighbors.add(getNodeAt(nodes, pos.x, pos.y - 1));
		if (allowDiagonal)
		{
			neighbors.add(getNodeAt(nodes, pos.x + 1, pos.y + 1));
			neighbors.add(getNodeAt(nodes, pos.x - 1, pos.y - 1));
			neighbors.add(getNodeAt(nodes, pos.x + 1, pos.y - 1));
			neighbors.add(getNodeAt(nodes, pos.x - 1, pos.y + 1));
		}
		
		neighbors.removeIf(Objects::isNull);
	}
	
	public void setColor(final float x, final float y, final float z)
	{
		color.set(x, y, z);
	}
}

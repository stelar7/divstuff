package travelingSalesman.solver;

import org.joml.*;

import java.util.*;


public abstract class TravelingSalesmanSolver
{
	protected int bestDistance = Integer.MAX_VALUE;
	
	long    triedpermutations;
	float   percent;
	boolean solved;
	
	List<Vector2f> cities;
	int            WIDTH;
	int            HEIGHT;
	
	public TravelingSalesmanSolver(final List<Vector2f> cities, final int w, final int h)
	{
		this.cities = cities;
		this.WIDTH = w;
		this.HEIGHT = h;
	}
	
	public abstract void nextStep();
	
	public abstract void render();
	
	public long getPermutationsDone()
	{
		return triedpermutations;
	}
	
	public void setPermutationsDone(final long triedpermutations)
	{
		this.triedpermutations = triedpermutations;
	}
	
	protected long factorial(int num)
	{
		return num == 0 ? 1 : num * factorial(num - 1);
	}
	
	public boolean isSolved()
	{
		return solved;
	}
	
	public void setSolved(final boolean solved)
	{
		this.solved = solved;
	}
	
	public float getPercent()
	{
		return percent;
	}
	
	public void setPercent(final float percent)
	{
		this.percent = percent;
	}
	
	public List<Vector2f> getCities()
	{
		return cities;
	}
}
package mazeGenetation;

import mazeGenetation.generator.*;
import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MazeGeneratorDisplay
{
	private final Object lock = new Object();
	MazeGenerator generator;
	int horizontalCellCount = 150;
	int verticalCellCount   = 100;
	int offsetX             = 80;
	int offsetY             = 20;
	private long window;
	private int      WIDTH  = 800;
	private int      HEIGHT = 600;
	private Vector2f cursor = new Vector2f();
	
	public static void main(String[] args)
	{
		new MazeGeneratorDisplay().run();
	}
	
	public void run()
	{
		try
		{
			init();
			
			new Thread(this::loop).start();
			
			while (!glfwWindowShouldClose(window))
			{
				glfwWaitEvents();
			}
			
			
			synchronized (lock)
			{
				glfwFreeCallbacks(window);
				glfwDestroyWindow(window);
			}
		} finally
		{
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
	}
	
	private void init()
	{
		GLFWErrorCallback.createPrint(System.err).set();
		
		if (!glfwInit())
		{
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
		window = glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", NULL, NULL);
		if (window == NULL)
		{
			throw new RuntimeException("Failed to create the GLFW window");
		}
		
		glfwSetCursorPosCallback(window, (window, x, y) -> cursor.set((float) x, (float) y));
		glfwSetMouseButtonCallback(window, (window, button, action, mods) ->
		{
		});
		
		
		glfwSetFramebufferSizeCallback(window, (window, w, h) ->
		                               {
			                               if (w > 0 && h > 0)
			                               {
				                               WIDTH = w;
				                               HEIGHT = h;
			                               }
		                               }
		                              );
		
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		
		glfwSetWindowPos(window, (vidmode.width() - WIDTH) / 2, (vidmode.height() - HEIGHT) / 2);
		glfwShowWindow(window);
		
	}
	
	private void loop()
	{
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		initPostGL();
		
		int updatesPerSecond = 1000;
		int skipInterval     = 1000 / updatesPerSecond;
		int maxFramesSkipped = 1000 * 2000;
		
		long  timer = System.currentTimeMillis();
		int   loops;
		float interp;
		
		long fpstimer = System.currentTimeMillis();
		int  ups, fps = ups = 0;
		
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glEnable(GL_DEPTH_TEST);
		glViewport(0, 0, WIDTH, HEIGHT);
		
		while (!glfwWindowShouldClose(window))
		{
			
			if (System.currentTimeMillis() > fpstimer + 1000)
			{
				fpstimer = System.currentTimeMillis();
				System.out.format("fps: %d  ups: %d%n", fps, ups);
				fps = ups = 0;
			}
			
			loops = 0;
			while (System.currentTimeMillis() > timer && loops < maxFramesSkipped)
			{
				update();
				loops++;
				ups++;
				timer += skipInterval;
				
			}
			interp = (float) (System.currentTimeMillis() + skipInterval - timer) / (float) skipInterval;
			
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			render(interp);
			fps++;
			
			synchronized (lock)
			{
				if (!glfwWindowShouldClose(window))
				{
					glfwSwapBuffers(window);
				}
			}
		}
	}
	
	private void initPostGL()
	{
		
		Vector2i mazeSize  = new Vector2i(WIDTH - offsetX, HEIGHT - offsetY);
		Vector2i cellCount = new Vector2i(horizontalCellCount, verticalCellCount);
		
		generator = new RecurciveBacktrackingMazeGenerator(mazeSize, cellCount);
	}
	
	private void update()
	{
		generator.nextStep();
	}
	
	private void render(final float interp)
	{
		glTranslatef(offsetX / 2, offsetY, 0);
		generator.render();
		glTranslatef(-offsetX / 2, -offsetY, 0);
	}
	
}
	

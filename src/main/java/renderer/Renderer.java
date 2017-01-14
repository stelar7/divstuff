package renderer;

import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Renderer
{
	private final Object lock = new Object();
	int swapinterval = 1;
	private long window;
	private int      WIDTH  = 800;
	private int      HEIGHT = 600;
	private Vector2f cursor = new Vector2f();
	
	public static void main(String[] args)
	{
		new Renderer().run();
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
		glfwSwapInterval(swapinterval); // is there a better loop than this?
		
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		initPostGL();
		
		
		int tps              = 60;
		int skipInterval     = 1000 / tps;
		int maxFramesSkipped = 5;
		
		long timer = System.currentTimeMillis();
		int  loops;
		
		
		long fpstimer = System.currentTimeMillis();
		int  ups, fps = ups = 0;
		
		glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
		glEnable(GL_DEPTH_TEST);
		
		while (!glfwWindowShouldClose(window))
		{
			
			if (System.currentTimeMillis() > fpstimer + 1000)
			{
				
				fpstimer = System.currentTimeMillis();
				System.out.println("FPS:" + fps);
				//System.out.println("UPS:" + ups);
				fps = ups = 0;
			}
			
			loops = 0;
			while (System.currentTimeMillis() > timer && loops < maxFramesSkipped)
			{
				update();
				timer += skipInterval;
				loops++;
				ups++;
				
			}
			
			float interp = (System.currentTimeMillis() + skipInterval - timer) / skipInterval;
			
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			glViewport(0, 0, WIDTH, HEIGHT);
			
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
	}
	
	private void update()
	{
	}
	
	private void render(final float interp)
	{
		Shapes.drawTriangle(new Vector2f(WIDTH / 2, HEIGHT / 2), 50);
		glTranslatef(WIDTH / 2, HEIGHT / 2, 0);
		glRotatef(0.01f, 0, 0, 1);
		glTranslatef(-WIDTH / 2, -HEIGHT / 2, 0);
	}
	
	
}

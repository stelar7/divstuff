package mazesolver;

import mazeGenetation.generator.*;
import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import pathfinding.solver.*;

import java.text.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MazeGenPathfinder
{
    private final Object lock = new Object();
    boolean doUpdates = false;
    PathfinderSolver solver;
    MazeGenerator    generator;
    Vector2i mazeSize  = new Vector2i(1780, 940);
    Vector2i cellCount = new Vector2i(80 * 2, 80 * 2);
    private long window;
    private int      WIDTH  = 1820;
    private int      HEIGHT = 980;
    private Vector2f cursor = new Vector2f();
    
    
    public static void main(String[] args)
    {
        new MazeGenPathfinder().run();
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
        
        glfwSetKeyCallback(window, (window, key, scan, action, mod) ->
        {
            if (action == GLFW_RELEASE && key == GLFW_KEY_S)
            {
                doUpdates = !doUpdates;
            }
        });
        
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
        
        
        long  timer = System.currentTimeMillis();
        int   loops;
        float interp;
        
        int updatesPerSecond = 1000;
        int skipInterval     = 1000 / updatesPerSecond;
        int maxFramesSkipped = 1000 * 2000;
        
        DecimalFormat nf = new DecimalFormat("###.##%");
        
        long fpstimer = System.currentTimeMillis();
        int  ups, fps = ups = 0;
        
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glEnable(GL_DEPTH_TEST);
        
        while (!glfwWindowShouldClose(window))
        {
            if (System.currentTimeMillis() > fpstimer + 1000)
            {
                fpstimer = System.currentTimeMillis();
                //System.out.println("FPS:" + fps);
                System.out.println("UPS:" + ups);
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
            interp = (float) (System.currentTimeMillis() + skipInterval - timer) / (float) skipInterval;
            
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
        generator = new RecurciveBacktrackingMazeGenerator(mazeSize, cellCount);
    }
    
    private void update()
    {
        if (doUpdates)
        {
            if (!generator.isFinished())
            {
                generator.nextStep();
            } else
            {
                if (solver == null)
                {
                    solver = new MazeGeneratorSolver(mazeSize, cellCount, generator.getCells());
                    doUpdates = false;
                } else
                {
                    solver.nextStep();
                }
            }
        }
    }
    
    private void render(final float interp)
    {
        int xoffset = 25;
        int yoffset = 30;
        
        glViewport(0, 0, WIDTH, HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glTranslatef(xoffset, yoffset, 0);
        
        if (!generator.isFinished())
        {
            generator.render();
        } else
        {
            if (solver != null)
            {
                solver.render();
            }
        }
        
        glTranslatef(-xoffset, -yoffset, 0);
        
    }
    
}

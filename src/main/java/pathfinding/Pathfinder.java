package pathfinding;

import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import pathfinding.solver.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Pathfinder
{
    private final Object lock = new Object();
    private PathfinderSolver solver;
    private Vector2i mazeSize  = new Vector2i(750, 750);
    private Vector2i cellCount = new Vector2i(75, 75);
    private long window;
    private int      WIDTH  = 800;
    private int      HEIGHT = 800;
    private Vector2f cursor = new Vector2f();
    
    public static void main(String[] args)
    {
        new Pathfinder().run();
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
        
        glfwSetCursorPosCallback(window, (windowPtr, x, y) -> cursor.set((float) x, (float) y));
        
        glfwSetFramebufferSizeCallback(window, (windowPtr, w, h) ->
                                       {
                                           if (w > 0 && h > 0)
                                           {
                                               WIDTH = w;
                                               HEIGHT = h;
                                           }
                                       }
                                      );
        
        glfwSetKeyCallback(window, (windowPtr, key, code, action, mods) ->
        {
            if (key == GLFW_KEY_H && action == GLFW_RELEASE)
            {
                HeuristicType lastType = solver.getHeuristicType();
                HeuristicType nextType = HeuristicType.values()[(lastType.ordinal() + 1) % HeuristicType.values().length];
                
                solver = new HeuristicSolver(mazeSize, cellCount);
                solver.setHeuristicType(nextType);
                
                System.out.format("Swapped to : %s%n", solver.getHeuristicType());
            }
            
            if (key == GLFW_KEY_KP_ADD && action == GLFW_RELEASE)
            {
                cellCount.add(1, 1);
                solver = new HeuristicSolver(mazeSize, cellCount);
                System.out.println(cellCount);
            }
            if (key == GLFW_KEY_KP_SUBTRACT && action == GLFW_RELEASE)
            {
                cellCount.sub(1, 1);
                solver = new HeuristicSolver(mazeSize, cellCount);
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
        
        
        long timer = System.currentTimeMillis();
        int  loops;
        
        int updatesPerSecond = 100;
        int skipInterval     = 1000 / updatesPerSecond;
        int maxFramesSkipped = 1000 * 2000;
        
        long fpstimer = System.currentTimeMillis();
        int  ups      = 0;
        int  fps      = 0;
        
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glEnable(GL_DEPTH_TEST);
        
        while (!glfwWindowShouldClose(window))
        {
            if (System.currentTimeMillis() > fpstimer + 1000)
            {
                fpstimer = System.currentTimeMillis();
                System.out.println("FPS:" + fps);
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
            render();
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
        solver = new HeuristicSolver(mazeSize, cellCount);
    }
    
    private void update()
    {
        solver.nextStep();
    }
    
    private void render()
    {
        glViewport(0, 0, WIDTH, HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glTranslatef(25, 25, 0);
        
        solver.render();
        
        glTranslatef(-25, -25, 0);
    }
    
}

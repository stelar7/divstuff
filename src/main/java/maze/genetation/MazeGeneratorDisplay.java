package maze.genetation;

import maze.genetation.generator.*;
import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class MazeGeneratorDisplay
{
    private MazeGenerator generator;
    private static final int HORIZONTAL_CELL_COUNT = 150;
    private static final int VERTICAL_CELL_COUNT   = 100;
    private static final int OFFSET_X              = 80;
    private static final int OFFSET_Y              = 20;
    
    private int width  = 800;
    private int height = 600;
    
    private final Vector2f cursor = new Vector2f();
    private final Object   lock   = new Object();
    
    private long window;
    
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
        
        window = glfwCreateWindow(width, height, "Maze Generator", NULL, NULL);
        if (window == NULL)
        {
            throw new RuntimeException("Failed to create the GLFW window");
        }
        
        glfwSetCursorPosCallback(window, (windowPtr, x, y) -> cursor.set((float) x, (float) y));
        
        
        glfwSetFramebufferSizeCallback(window, (windowPtr, w, h) ->
                                       {
                                           if (w > 0 && h > 0)
                                           {
                                               width = w;
                                               height = h;
                                           }
                                       }
                                      );
        
        
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        glfwShowWindow(window);
        
    }
    
    private void loop()
    {
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        initPostGL();
        
        int   updatesPerSecond = 60;
        float skipInterval     = 1000f / updatesPerSecond;
        int   maxFramesSkipped = 5;
        
        int   loops;
        float interp;
        
        double timer    = System.currentTimeMillis();
        long   fpstimer = System.currentTimeMillis();
        
        int ups = 0;
        int fps = 0;
        
        glOrtho(0, width, height, 0, 1, -1);
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, width, height);
        
        while (!glfwWindowShouldClose(window))
        {
            
            if (System.currentTimeMillis() > fpstimer + 1000)
            {
                System.out.format("fps: %d  ups: %d%n", fps, ups);
                fpstimer = System.currentTimeMillis();
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
            
            interp = (System.currentTimeMillis() + skipInterval - (float) timer) / skipInterval;
            
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glViewport(0,0,width,height);
            
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
        
        Vector2i mazeSize  = new Vector2i(width - OFFSET_X, height - OFFSET_Y);
        Vector2i cellCount = new Vector2i(HORIZONTAL_CELL_COUNT, VERTICAL_CELL_COUNT);
        
        generator = new RecurciveBacktrackingMazeGenerator(mazeSize, cellCount);
    }
    
    private void update()
    {
        generator.nextStep();
    }
    
    private void render(final float interp)
    {
        glTranslatef(OFFSET_X / 2f, OFFSET_Y, 0);
        generator.render();
        glTranslatef(-OFFSET_X / 2f, -OFFSET_Y, 0);
    }
    
}
    

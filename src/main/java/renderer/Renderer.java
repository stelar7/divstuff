package renderer;

import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public abstract class Renderer
{
    protected int width  = 800;
    protected int height = 600;
    
    protected final Vector2f cursor = new Vector2f();
    private final   Object   lock   = new Object();
    
    private long    window;
    private boolean shouldClose;
    
    public void run()
    {
        try
        {
            init();
            
            new Thread(this::loop).start();
            
            while (!shouldClose)
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
    
    public void setWindowSize(Vector2i size)
    {
        this.width = size.x;
        this.height = size.y;
        glfwSetWindowSize(window, width, height);
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
        
        window = glfwCreateWindow(width, height, "Basic Renderer!", NULL, NULL);
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
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - width) / 2);
        
        glfwShowWindow(window);
    }
    
    private void loop()
    {
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        initPostGL();
        
        int   updatesPerSecond = 60;
        int   maxFramesSkipped = 20;
        float skipInterval     = 1000f / updatesPerSecond;
        
        int ups = 0;
        int fps = 0;
        
        int loops;
        
        double timer    = System.currentTimeMillis();
        long   fpstimer = System.currentTimeMillis();
        
        glOrtho(0, width, height, 0, 1, -1);
        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, width, height);
        
        while (!shouldClose)
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
            
            render();
            fps++;
            
            synchronized (lock)
            {
                shouldClose = glfwWindowShouldClose(window);
                
                if (!shouldClose)
                {
                    glfwSwapBuffers(window);
                }
            }
        }
    }
    
    public abstract void initPostGL();
    
    public abstract void update();
    
    public abstract void render();
}

package travelingSalesman;

import org.joml.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import renderer.*;
import travelingSalesman.solver.*;

import java.text.*;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class TravelingSalesman
{
    private final Object lock = new Object();
    List<Vector2f> cities = new ArrayList<>();
    Random         random = new Random();
    TravelingSalesmanSolver solver;
    private long window;
    private int      WIDTH  = 800;
    private int      HEIGHT = 600;
    private Vector2f cursor = new Vector2f();
    
    public static void main(String[] args)
    {
        new TravelingSalesman().run();
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
            if (action == GLFW_RELEASE && button == GLFW_MOUSE_BUTTON_LEFT)
            {
                if (cursor.y > HEIGHT / 2 || cursor.y < 0)
                {
                    return;
                }
                
                cities.add(new Vector2f(cursor).mul(1, 2f));
                solver = new LexiographicSolver(cities, WIDTH, HEIGHT);
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
        
        int updatesPerSecond = 100000;
        int skipInterval     = 1000 / updatesPerSecond;
        int maxFramesSkipped = 1000 * 2000;
        
        long  timer = System.currentTimeMillis();
        int   loops;
        float interp;
        
        long fpstimer = System.currentTimeMillis();
        int  ups, fps = ups = 0;
        
        DecimalFormat nf = new DecimalFormat("###.##%");
        
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glEnable(GL_DEPTH_TEST);
        
        while (!glfwWindowShouldClose(window))
        {
            if (System.currentTimeMillis() > fpstimer + 1000)
            {
                fpstimer = System.currentTimeMillis();
                System.out.format("fps: %d  ups: %d%n", fps, ups);
                System.out.format("cities: %d %npercent done: %s%n", solver.getCities().size(), nf.format(solver.getPercent()));
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
        int totalCities = 20;
        for (int i = 0; i < totalCities; i++)
        {
            cities.add(new Vector2f(random.nextInt(WIDTH), random.nextInt(HEIGHT)));
        }
        
        solver = new LexiographicSolver(cities, WIDTH, HEIGHT);
    }
    
    private void update()
    {
        if (solver.getCities().size() > 2)
        {
            solver.nextStep();
        }
    }
    
    private void render(final float interp)
    {
        glViewport(0, 0, WIDTH, HEIGHT);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        glColor3f(1, 0, 0);
        Shapes.drawLine(new Vector2f(0, HEIGHT / 2), new Vector2f(WIDTH, HEIGHT / 2), 3);
        
        solver.render();
    }
    
}

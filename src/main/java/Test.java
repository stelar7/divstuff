import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Test
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Parsing anime database");
        
        Map<Integer, String> names = new HashMap<>();
        
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Paths.get("C:\\Users\\stelar7\\Desktop\\anime-titles.xml").toFile());
        doc.getDocumentElement().normalize();
        final NodeList animes = doc.getElementsByTagName("anime");
        
        for (int i = 0; i < animes.getLength(); i++)
        {
            final Node    node = animes.item(i);
            final Element el   = (Element) node;
            int           aid  = Integer.parseInt(el.getAttribute("aid"));
            
            NodeList titles = el.getElementsByTagName("title");
            
            for (int j = 0; j < titles.getLength(); j++)
            {
                final Node    titleNode = titles.item(j);
                final Element titleEl   = (Element) titleNode;
                
                if (titleEl.hasAttribute("xml:lang") && titleEl.getAttribute("type").equals("main") && titleEl.getAttribute("xml:lang").equals("x-jat"))
                {
                    names.put(aid, titleEl.getFirstChild().getNodeValue());
                }
            }
            
            if (!names.containsKey(aid))
            {
                for (int j = 0; j < titles.getLength(); j++)
                {
                    final Node    titleNode = titles.item(j);
                    final Element titleEl   = (Element) titleNode;
                    
                    if (titleEl.hasAttribute("xml:lang") && titleEl.getAttribute("type").equals("official") && titleEl.getAttribute("xml:lang").equals("en"))
                    {
                        names.put(aid, titleEl.getFirstChild().getNodeValue());
                    }
                }
            }
            
            if (!names.containsKey(aid))
            {
                for (int j = 0; j < titles.getLength(); j++)
                {
                    final Node    titleNode = titles.item(j);
                    final Element titleEl   = (Element) titleNode;
                    
                    if (titleEl.hasAttribute("xml:lang") && !titleEl.getAttribute("type").equals("short") && titleEl.getAttribute("xml:lang").equals("en"))
                    {
                        names.put(aid, titleEl.getFirstChild().getNodeValue());
                    }
                }
            }
        }
        
        System.out.println("Parsing downloaded anime");
        
        List<Integer> ids = new ArrayList<>();
        
        Path start = Paths.get("S:\\anime");
        Files.walkFileTree(start, new SimpleFileVisitor<>()
        {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
            {
                if (dir.toString().equals("S:\\anime"))
                {
                    return FileVisitResult.CONTINUE;
                }
                
                String id = dir.toString().substring(dir.toString().lastIndexOf('\\') + 1);
                ids.add(Integer.parseInt(id));
                return FileVisitResult.SKIP_SUBTREE;
            }
        });
        
        ids.sort(Comparator.comparingInt(i -> i));
        
        System.out.println("Outputting missing anime");
        
        for (int i = 0; i <= 400; i++)
        {
            if (!ids.contains(i))
            {
                if (names.containsKey(i))
                {
                    System.out.println(i + " => " + names.get(i));
                }
            }
        }
    }
}

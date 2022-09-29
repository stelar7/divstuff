import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class Test
{
    public static void main(String[] args) throws Exception
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        Path                      path       = Paths.get("C:\\Users\\stelar7\\Desktop\\cards.json");
        String                    text       = String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
        List<Map<String, String>> cards      = new Gson().fromJson(text, new TypeToken<List<Map<String, String>>>() {}.getType());
        Path                      outputPath = path.resolveSibling("digimon").resolve("data");
        Files.createDirectories(outputPath);
        
        String[] typeLookup      = {"", "Digitama", "Digimon", "Tamer", "Option"};
        String[] attributeLookup = {"", "Data", "Vaccine", "Virus", "Free", "Variable", "Unknown", ""};
        String[] colorLookup     = {"", "Red", "", "Blue", "Yellow", "Black", "Green", "Purple", "White", "", "Rainbow", "Yellow Purple", "Purple Yellow", "Blue Green", "Green Blue", "Yellow Blue", "Red Blue", "Red Yellow", "Green Red", "Blue Red", "Blue Black", "Blue Yellow", "Black Red", "Red Black", "Purple Green", "Yellow Green", "Green Yellow", "Red Green", "Yellow Red", "Purple Black", "Black Purple", "Red Purple", "Purple Red", "Green Black", "Yellow Black"};
        String[] stageLookup     = {"Baby", "In Training", "Rookie", "Champion", "Ultimate", "Mega", "Undefined/Unknown", "", "Hybrid", "Armor"};
        
        for (Map<String, String> card : cards)
        {
            JsonArray colors = new JsonArray();
            String[]  items  = getIfNotNull(card.get("color"), colorLookup).split(" ");
            for (String item : items)
            {
                colors.add(item);
            }
            
            JsonObject out = new JsonObject();
            out.addProperty("card_number", card.get("cardid"));
            out.addProperty("card_type", getIfNotNull(card.get("cardtype"), typeLookup));
            out.addProperty("name", card.get("name") != null ? card.get("name").trim() : null);
            out.add("color", colors);
            out.addProperty("cost", card.get("cost"));
            out.addProperty("level", card.get("level"));
            out.addProperty("power", card.get("dp"));
            out.addProperty("type", card.get("dtype") != null ? card.get("dtype").trim() : null);
            out.addProperty("attribute", getIfNotNull(card.get("attr"), attributeLookup));
            out.addProperty("stage", getIfNotNull(card.get("stage"), stageLookup));
            out.addProperty("main_effect", card.get("maineffect"));
            out.addProperty("inherited_effect", card.get("sourceeffect"));
            out.addProperty("security_effect", card.get("securityeffect"));
            
            JsonArray evolves = new JsonArray();
            JsonObject evolveOption = new JsonObject();
            JsonObject evolveRequirement = new JsonObject();
            evolveRequirement.addProperty("color", getIfNotNull(card.get("ecostcolor"), colorLookup));
            evolveRequirement.addProperty("level", card.get("elvfrom"));
            evolveOption.addProperty("cost", card.get("ecost"));
            evolveOption.add("requirements", evolveRequirement);
            if (!evolveOption.get("cost").isJsonNull() && !evolveOption.get("cost").getAsString().isEmpty()) {
                evolves.add(evolveOption);
            }
            
            JsonObject evolveOption2 = new JsonObject();
            JsonObject evolveRequirement2 = new JsonObject();
            evolveRequirement2.addProperty("color", getIfNotNull(card.get("ecostcolor2"), colorLookup));
            evolveRequirement2.addProperty("level", card.get("elvfrom2"));
            evolveOption2.addProperty("cost", card.get("ecost2"));
            evolveOption2.add("requirements", evolveRequirement2);
            if (!evolveOption2.get("cost").isJsonNull() && !evolveOption2.get("cost").getAsString().isEmpty()) {
                evolves.add(evolveOption2);
            }
            
            if (evolves.size() > 0) {
                out.add("digivolves_from", evolves);
            }
            
            Files.writeString(outputPath.resolve(card.get("cardid") + ".json"), gson.toJson(out));
        }
    }
    
    private static String getIfNotNull(String key, String[] lookup)
    {
        int index = key == null ? 0 : Integer.parseInt(key);
        return lookup[index];
    }
}
package wpt;

import com.sun.javafx.collections.MappingChange.Map;

import java.util.List;

public class SubtestResult
{
    public String    name;
    public RunStatus status;
    public String    message;
    public String    expected;
    public List<Map> known_intermittent;
}

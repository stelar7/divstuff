package wpt;

import java.util.*;

public class TestResult
{
    public String              test;
    public String              subsuite;
    public List<SubtestResult> subtests;
    public RunStatus           status;
    public String              message;
    public float               duration;
    public String              expected;
    public List<Map>           known_intermittent;
    public Map<String, String> screenshots;
}

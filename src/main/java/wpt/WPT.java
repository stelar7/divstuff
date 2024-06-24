package wpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.util.*;

public class WPT
{
    public static void main(String[] args) throws IOException, SQLException
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path path = Paths.get(args[0]);
        
        System.out.println("Reading file: " + path);
        
        String                 text              = String.join("\n", Files.readAllLines(path, StandardCharsets.UTF_8));
        WPTRun                 data              = new ObjectMapper().readValue(text, WPTRun.class);
        Map<String, TestScore> scorePerSubfolder = new HashMap<>();
        
        System.out.println("Processing " + data.results.size() + " results");
        
        for (TestResult result : data.results)
        {
            String[] parts = result.test.split("/");
            
            for (int i = 1; i <= parts.length; i++)
            {
                String    subfolder = String.join("/", Arrays.copyOf(parts, i));
                TestScore score     = scorePerSubfolder.computeIfAbsent(subfolder, k -> new TestScore());
                
                if (result.subtests.isEmpty())
                {
                    switch (result.status)
                    {
                        case PASS:
                            score.passing++;
                            break;
                        case ERROR:
                            score.error++;
                            break;
                        case TIMEOUT:
                            score.timeout++;
                            break;
                        case NOTRUN:
                            score.notrun++;
                            break;
                        case PRECONDITION_FAILED:
                            score.precondition++;
                            break;
                        case FAIL:
                            score.failing++;
                            break;
                        case SKIP:
                            score.skipped++;
                            break;
                        case CRASH:
                            score.crashing++;
                            break;
                        default:
                            throw new RuntimeException("Unknown status: " + result.status);
                        
                    }
                } else
                {
                    for (SubtestResult subtest : result.subtests)
                    {
                        switch (subtest.status)
                        {
                            case PASS:
                                score.passing++;
                                break;
                            case ERROR:
                                score.error++;
                                break;
                            case TIMEOUT:
                                score.timeout++;
                                break;
                            case NOTRUN:
                                score.notrun++;
                                break;
                            case PRECONDITION_FAILED:
                                score.precondition++;
                                break;
                            case FAIL:
                                score.failing++;
                                break;
                            case SKIP:
                                score.skipped++;
                                break;
                            case CRASH:
                                score.crashing++;
                                break;
                            default:
                                throw new RuntimeException("Unknown status: " + subtest.status);
                        }
                    }
                }
            }
        }
        
        LocalDateTime runStartedAt   = LocalDateTime.ofEpochSecond(data.time_start / 1000, 0, ZoneOffset.UTC);
        LocalDateTime runEndedAt     = LocalDateTime.ofEpochSecond(data.time_end / 1000, 0, ZoneOffset.UTC);
        String        serenityCommit = data.run_info.revision;
        
        System.out.println("Writing to database");
        
        MySQL sql = new MySQL("192.168.100.75", "3306", "wpt_results", "stelar7", "steffen1");
        sql.open();
        
        try (PreparedStatement statement = sql.getConnection().prepareStatement(
                "INSERT IGNORE INTO `runs` (`serenity_commit`, `path`, `run_start`, `run_end`, `passing`, `error`, `failing`, `crashing`, `timeout`, `notrun`, `precondition`, `skipped`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"))
        {
            int index = 0;
            for (Map.Entry<String, TestScore> entry : scorePerSubfolder.entrySet())
            {
                statement.setString(1, serenityCommit);
                statement.setString(2, entry.getKey());
                statement.setTimestamp(3, Timestamp.valueOf(runStartedAt));
                statement.setTimestamp(4, Timestamp.valueOf(runEndedAt));
                statement.setInt(5, entry.getValue().passing);
                statement.setInt(6, entry.getValue().error);
                statement.setInt(7, entry.getValue().failing);
                statement.setInt(8, entry.getValue().crashing);
                statement.setInt(9, entry.getValue().timeout);
                statement.setInt(10, entry.getValue().notrun);
                statement.setInt(11, entry.getValue().precondition);
                statement.setInt(12, entry.getValue().skipped);
                statement.addBatch();
                
                index++;
                if (index % 5500 == 0)
                {
                    System.out.println("Generated batch " + index);
                }
            }
            
            System.out.println("Executing batch");
            statement.executeBatch();
        }
    }
}

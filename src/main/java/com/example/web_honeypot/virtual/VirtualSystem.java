package com.example.web_honeypot.virtual;

import lombok.Data;
import java.util.Map;
import java.util.HashMap;

@Data
public class VirtualSystem {
    private final String osName;
    private final String osVersion;
    private final Map<String, String> fileSystem;
    private final Map<Integer, String> processes;
    private final Map<String, String[]> users;
    
    public VirtualSystem() {
        this.osName = "Ubuntu";
        this.osVersion = "20.04";
        this.fileSystem = new HashMap<>();
        this.processes = new HashMap<>();
        this.users = new HashMap<>();
    }
    
    public VirtualSystem(String osName, String osVersion,
                        Map<String, String> fileSystem,
                        Map<Integer, String> processes,
                        Map<String, String[]> users) {
        this.osName = osName;
        this.osVersion = osVersion;
        this.fileSystem = fileSystem;
        this.processes = processes;
        this.users = users;
    }
    
    public String executeCommand(String command) {
        if (command.startsWith("ls")) {
            return simulateLsCommand(command);
        } else if (command.startsWith("cat")) {
            return simulateCatCommand(command);
        } else if (command.startsWith("ps")) {
            return simulatePsCommand();
        }
        return "Command not found: " + command;
    }
    
    private String simulateLsCommand(String command) {
        StringBuilder result = new StringBuilder();
        fileSystem.keySet().stream()
            .filter(path -> !path.contains("shadow"))
            .forEach(path -> result.append(path).append("\n"));
        return result.toString();
    }
    
    private String simulateCatCommand(String command) {
        String file = command.substring(4).trim();
        return fileSystem.getOrDefault(file, "File not found: " + file);
    }
    
    private String simulatePsCommand() {
        StringBuilder result = new StringBuilder("PID\tCOMMAND\n");
        processes.forEach((pid, process) -> 
            result.append(pid).append("\t").append(process).append("\n"));
        return result.toString();
    }
} 
package com.github.zxxz_ru.command;

import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.ApplicationCloser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


@Component
public class Dispatcher {
    private final ProjectCommand projectCommand;
    private final TaskCommand taskCommand;
    private final UserCommand userCommand;
    private final Messenger messenger;
    private final AppState appState;
    private final ApplicationCloser closer;

    @Autowired
    Dispatcher(ProjectCommand pc, TaskCommand tc, UserCommand uc, Messenger ms, AppState appst, ApplicationCloser closer) {
        this.projectCommand = pc;
        this.taskCommand = tc;
        this.userCommand = uc;
        this.messenger = ms;
        this.appState = appst;
        this.closer = closer;
    }


    /**
     * Maps commands recognized by double dash (--), makes dashes obligatory for parameters at
     * Application start time.
     *
     * @param args String[]
     * @return HashMap  which is mapping command to it's value as String, numerical values may require parsing.
     */
    public HashMap<String, String> getArgsMap(String... args) {
        HashMap<String, String> map = new HashMap<>();
        String command = "";
        for (int i = 0; i < args.length; ++i) {
            String str = args[i];
            if (str.contains("--")) {
                command = str;
                // Put default empty string in case of value is not required or missing
                map.put(command, "");
                continue;
            }
            if (i % 2 != 0 && !command.equals("") && !str.equals("")) {
                map.put(command, str);
                // ignore double arguments separated with space
                command = "";
            }
        }
        return map;
    }

    public void processCommandMap(Map<String, String> map) {
        if (map.containsKey("--filepath")) {
            // even if it's empty parameter --filepath File System is about to be used with default file
            appState.setMode(AppState.AppMode.FILESYSTEM);
            String path = map.get("--filepath");
            if (!path.equals("")) {
                appState.setPath(path);
            }
        }
        if (map.containsKey("--database")) {
            appState.setMode(AppState.AppMode.DATABASE);
            // do db stuff
        }
    }

    /**
     * Used to dispatch user input when Application is started.
     *
     * @param args String Array
     */
    public void dispatch(String... args) {
        if (args.length == 0) {
            messenger.printAdvice();
            return;
        }
        String command;
        // One argument it's either help or quit.
        if (args.length == 1) {
            command = args[0];
            if (command.equals("-h") || command.equals("--help") || command.equals("help")) {
                messenger.printHelp();
                closer.closeApp(0);
            }
            // no short for exit command.
            // keep it for consistency, although it's kinda stupid here.
            else if (command.equals("quit") || command.equals("--quit")) {
                closer.closeApp(0);
            }
        } else if (args.length > 1) {
            Map<String, String> map = getArgsMap(args);
            processCommandMap(map);
            messenger.printAdvice();
        }
    }

    //    \w{2,}\s*=\s*\w++
    // aa=bb
    //aa = bb
    //id=1 firstname  = Slavka
    // --{1,2}\w++
    // search for command with dashes

    public void dispatch() {
        Scanner scan = new Scanner(System.in);
        String str = scan.nextLine();
        if(str.equals("")) {
            return;
        }else{
            String command;
        Scanner sc = new Scanner(str).useDelimiter("\\s++");
        // search for dash commands
        command = sc.findInLine("^\\p{Blank}*?-{1,}+\\w++");
            System.out.println("Command line 122:" + command);
            if (command.equals("-h") || command.equals("--help")) {
                messenger.printHelp();
                sc.close();
                scan.close();
                closer.closeApp(0);
                return;
            }
            // no short for exit command.
            // keep it for consistency, although it's kinda stupid here.
            else if (command.equals("--quit")) {
                sc.close();
                scan.close();
                closer.closeApp(0);
                return;
            }
            sc.close();
            System.out.println("Got to line 138");
            System.out.println(str);
        Scanner sc2 = new Scanner(str).useDelimiter("\\s++");
        command = sc2.findInLine("^\\p{Blank}*?\\w++").trim();
        System.out.println("Command " + command);
        if(command.equals("user")){
            userCommand.execute(str);
        }else if(command.equals("task")){
            taskCommand.execute(str);
        }else if(command.equals("project")){
            projectCommand.execute(str);
        }else if(command.equals("help")){
            messenger.printHelp();
            sc2.close();
            closer.closeApp(0);
            return;
        }else if(command.equals("quit")){
            sc2.close();
            scan.close();
            closer.closeApp(0);
            return;
        }
        // sc.close();
        }
    }

}
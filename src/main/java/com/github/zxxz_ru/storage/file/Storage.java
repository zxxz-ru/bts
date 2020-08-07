package com.github.zxxz_ru.storage.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.zxxz_ru.AppState;
import com.github.zxxz_ru.command.Messenger;
import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Storage Class is utility class for all File Repositories Classes.
 * It should load all ArrayLists of Objects  from File System.
 * It should keep cached data at all the time and immediately before application exits
 * Storage Class will write data back to File System.
 * It it must have methods to return each ArrayList of Entities and method to replace ArrayList of
 * Entity when List has changed.
 *
 * @param <T>
 */
@Component
public class Storage<T> {
    enum Mode {USER, TASK, PROJECT}

    ;
    /**
     * Path to Storage File. Path is either default value or can be set from
     * Application Parameters.
     */
    private File file;
    private final Messenger messenger;
    private final AppState state;
    private final StorageFileCreator creator;
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<List> data = new ArrayList<>();

    @Autowired
    public Storage(AppState state, Messenger messenger, StorageFileCreator creator) {
        this.creator = creator;
        this.state = state;
        this.messenger = messenger;
        file = creator.createStorageFile();
        readData();
    }

    // Since I do not want use map must keep inner Lists order constant
    // data[0] - return ArrayList of Projects
    // data[1] - return ArrayList of Tasks
    // data[2] - return ArrayList of Users


    public void writeData() {
        try {
            mapper.writeValue(file, data);
        } catch (IOException e) {
            messenger.printError("Error writing data to: " + file.getAbsolutePath());
            System.exit(1);
        }
    }


    private void readData() {
        if (!file.exists()) {
            messenger.printError("Cannot read Storage File. File not exists.");
            return;
        }
        if (file.length() > 0) {
            try {
                List<List> d = mapper.readValue(file, new TypeReference<List<List>>() {
                });
                this.data.clear();
                this.data.addAll(d);
            } catch (IOException e) {
                e.printStackTrace();
                messenger.printError("Error reading Data from: " + file.getPath());
                System.exit(1);
            }
        }
    }

    // Methods to get specific List from
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        if (data.size() == 3) {
            return (List<User>) data.get(2);
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Task> getTasks() {
        if (data.size() == 3) {
            return (List<Task>) data.get(1);
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")
    public List<Project> getProjects() {
        if (data.size() == 3) {
            return (List<Project>) data.get(0);
        }
        return new ArrayList<>();
    }

    // Methods to set specific List of Entities
    public void setProjects(List<Project> projects) {
        this.data.set(0, projects);
    }

    public void setTasks(List<Task> tasks) {
        this.data.set(1, tasks);
    }

    public void setUsers(List<User> users) {
        this.data.set(2, users);
    }


    /*
    public void store(List<T> list){
        final String storageDir = "~/opt/storage/epam/";
        final String userFile = storageDir+"users";
        final String taskFile = storageDir+"tasks";
        final String projectFile = storageDir+"projects";
        try(
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            ){
         out.writeObject(list);
      }  catch (IOException i) {
         i.printStackTrace();
      }
    }

    @SuppressWarnings("unchecked")
    public List<T> get(Mode mode){
        List<T> list = new ArrayList<>();
        try (
                FileInputStream fileIn = new FileInputStream(path);
                ObjectInputStream in = new ObjectInputStream(fileIn)){
             list = (List<T>) in.readObject();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Employee class not found");
            c.printStackTrace();
        }
        return list;
    }

     */
}

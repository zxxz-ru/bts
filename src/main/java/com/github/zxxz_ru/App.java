/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.github.zxxz_ru;
import com.github.zxxz_ru.command.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements CommandLineRunner {
    @Autowired
    Dispatcher disp;
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
    @Override
    public void run(String... args){
        disp.dispatch(args);
    }
}

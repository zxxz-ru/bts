package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Component
public class Util<T> {
    private final PrintStream out = System.out;
    private final PrintStream err = System.err;

     public <T>void print(List<T> list){
        if (list.size() == 0){
            out.println("No Result.");
            return;
        }
        for (T ent : list){
            out.print(ent.toString());
        }
        System.exit(0);
    }
    public void printMessage(String s){
        out.println(s);
        System.exit(0);
    }
    public void printError(String s){
        err.println(s);
        System.exit(1);
    }
    private String parseSingleParameter(String pair) throws NoSuchElementException {
        int index = pair.lastIndexOf("=");
        if (index < 0) throw new NoSuchElementException("Incorrect usage [ = ] is missing.");
        String param = pair.substring(index);
        if (param.length() == 0) throw new NoSuchElementException("No Parameter after equality Character.");
        return param;
    }

    public String getParameter(String parameter, String... args) throws NoSuchElementException {
         // Check if there is equality sign
        if(!Arrays.stream(args).anyMatch(s -> Pattern.matches(".*(=){1}.*",s)) && args.length == 2){
            return args[1];
        }
        String rex = parameter + "(=){1}.*";
        return Arrays.stream(args).filter((v) -> Pattern.matches(rex, v))
                .map(this::parseSingleParameter)
                    .findFirst().get();
                //.reduce("", (a,b)->a+b);
    }
}
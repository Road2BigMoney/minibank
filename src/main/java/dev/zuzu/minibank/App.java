package dev.zuzu.minibank;

import dev.zuzu.minibank.config.AppConfig;
import dev.zuzu.minibank.console.ConsoleListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        ConsoleListener consoleListener = context.getBean(ConsoleListener.class);
        consoleListener.start();
        context.close();
    }
}

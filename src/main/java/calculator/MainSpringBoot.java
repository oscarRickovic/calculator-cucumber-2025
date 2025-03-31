package calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "calculator")  // Scanne tous les sous-packages de "calculator"
public class MainSpringBoot {

    public static void main(String[] args) {
        SpringApplication.run(MainSpringBoot.class, args);
    }
}


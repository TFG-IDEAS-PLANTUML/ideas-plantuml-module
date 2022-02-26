import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("es.us.isa.ideas")
public class PlantUml4IdeasModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlantUml4IdeasModuleApplication.class, args);
    }
}

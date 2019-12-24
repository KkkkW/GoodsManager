package ez.manager;

import ez.manager.Util.RunnableThreadWebCount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ManagerApplication {

    public static void main(String[] args) {

//        RunnableThreadWebCount runnableThreadWebCount = new RunnableThreadWebCount();
//        runnableThreadWebCount.run();
        SpringApplication.run(ManagerApplication.class, args);
    }
}

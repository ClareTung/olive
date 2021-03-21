package cn.olive.forest;

import com.thebeastshop.forest.springboot.annotation.ForestScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// forest扫描远程接口所在的包名
@ForestScan(basePackages = "cn.olive.forest.client")
@SpringBootApplication
public class ForestClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForestClientApplication.class, args);
    }
}

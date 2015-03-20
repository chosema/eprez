package sk.tuke.kpi.eprez.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan("sk.tuke.kpi.eprez.web.rest")
public class RestConfig extends WebMvcConfigurerAdapter {

}

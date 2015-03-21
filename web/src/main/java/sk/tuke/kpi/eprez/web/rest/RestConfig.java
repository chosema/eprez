package sk.tuke.kpi.eprez.web.rest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@ComponentScan("sk.tuke.kpi.eprez.web.rest")
public class RestConfig extends WebMvcConfigurerAdapter {

}

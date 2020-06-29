package test;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApplicationEventListner {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("onApplicationEvent");
        final String uri = "http://localhost:8080/filereap";
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        log.info(result);
    }
}
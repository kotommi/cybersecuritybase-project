package sec.project;

import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;

@SpringBootApplication
public class CyberSecurityBaseProjectApplication implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(CyberSecurityBaseProjectApplication.class);
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer cesc) {
        ((TomcatEmbeddedServletContainerFactory) cesc).addContextCustomizers(new TomcatContextCustomizer() {
            @Override
            public void customize(Context cntxt) {
                // Caused some red error on prod so we disabled it. We're unhackable anyways.
                // This exposes the session-cookie's contents to browserside javascript.
                // Which allows an attacker to steal the session id with a xss-attack.
                cntxt.setUseHttpOnly(false);

                // This creates a new manager that creates predictable sessionIDs based on a running number. 
                cntxt.setManager(new Manager());
            }
            
        });
    }
}

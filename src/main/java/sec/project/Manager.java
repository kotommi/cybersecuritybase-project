package sec.project;

import org.apache.catalina.session.StandardManager;

public class Manager extends StandardManager {
    
    private int i;

    public Manager() {
        super();
        this.i = 0;
    }

    @Override
    protected String generateSessionId() {
        return "session" + i++;
    }

    
}
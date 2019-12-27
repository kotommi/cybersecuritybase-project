package sec.project.config;

import java.util.Arrays;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import sec.project.domain.Account;
import sec.project.repository.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SecurityConfiguration sec;
    @Autowired
    private AccountRepository ar;
    private PasswordEncoder bcrypt;

    @PostConstruct
    public void init() {
        Account a = new Account();
        a.setUsername("admin");
        a.setPassword("$2a$06$rtacOjuBuSlhnqMO2GKxW.Bs8J6KI0kYjw/gtF0bfErYgFyNTZRDm");
        ar.save(a);
        this.bcrypt = sec.passwordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account a = ar.findByUsername(username);
        if (a == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                a.getPassword(),
                true,
                true,
                true,
                true,
                username == "admin" ? Arrays.asList(new SimpleGrantedAuthority("ADMIN")) : Arrays.asList(new SimpleGrantedAuthority("USER")));
    }

    public void save(Account a) {
        a.setPassword(bcrypt.encode(a.getPassword()));
        ar.save(a);
    }
}

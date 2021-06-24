package railway.application.system.service.userDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import railway.application.system.models.Credentials;
import railway.application.system.repository.CredentialsRepository;

import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(credentialsRepository.findAll().parallelStream().filter(p -> p.getUsername().equals(username)).count() == 0)
            throw new UsernameNotFoundException("UserName Not Found");
        Credentials credentials = credentialsRepository.findAll().parallelStream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).get(0);
        Credentials updateCredentials = new Credentials(credentials.getUsername(), credentials.getPassword(),credentials.getUser_id(),credentials.getRoles());
        return new User(updateCredentials.getUsername(),updateCredentials.getPassword(),updateCredentials.getAuthorities());
    }
}
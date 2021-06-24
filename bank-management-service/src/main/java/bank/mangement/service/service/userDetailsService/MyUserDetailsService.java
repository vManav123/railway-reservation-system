package bank.mangement.service.service.userDetailsService;

import bank.mangement.service.model.bank.Credentials;
import bank.mangement.service.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(credentialsRepository.findAll().parallelStream().noneMatch(p -> p.getUsername().equals(username)))
            throw new UsernameNotFoundException("UserName Not Found");
        Credentials credentials = credentialsRepository.findAll().parallelStream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).get(0);
        Credentials updateCredentials = new Credentials(credentials.getUsername(), credentials.getPassword(),credentials.getUser_id(),credentials.getRoles());
        return new User(updateCredentials.getUsername(),updateCredentials.getPassword(),updateCredentials.getAuthorities());
    }
}
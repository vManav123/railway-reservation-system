package railway.reservation.system.service.userDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import railway.reservation.system.model.controllerBody.Credentials;
import railway.reservation.system.repository.CredentialsRepository;


import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new User("railway-admin", "jaosda@42121",
                new ArrayList<>());
    }
//    @Autowired
//    private CredentialsRepository credentialsRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        if( credentialsRepository.findAll().stream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).isEmpty())
//            throw new UsernameNotFoundException("UserName Not Found");
//        Credentials credentials = credentialsRepository.findAll().stream().filter(p->p.getUsername().equals(username)).collect(Collectors.toList()).get(0);
//        return new User(credentials.getUsername(),credentials.getPassword(),credentials.getAuthorities());
//    }
}
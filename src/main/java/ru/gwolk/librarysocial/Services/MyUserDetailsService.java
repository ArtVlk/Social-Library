package ru.gwolk.librarysocial.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.CRUDRepositories.UserRepository;
import ru.gwolk.librarysocial.Entities.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User userFromDB = userRepository.findByName(username).getFirst();
            return new org.springframework.security.core.userdetails.User(
                            userFromDB.getName(),
                            userFromDB.getPassword(),
                            mapRoles(userFromDB));
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Collection<GrantedAuthority> mapRoles(User user)
    {
        List<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return collect;
    }
}

package ru.gwolk.librarysocial.AppBackend.CommonServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.PhoneNumberRepository;
import ru.gwolk.librarysocial.AppBackend.Entities.PhoneNumber;
import ru.gwolk.librarysocial.AppBackend.Entities.User;
import ru.gwolk.librarysocial.AppBackend.CRUDRepositories.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PhoneNumberRepository phoneNumberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MyUserDetailsService(UserRepository userRepository,
                                PhoneNumberRepository phoneNumberRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.passwordEncoder = passwordEncoder;
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
    public boolean addUser(User user) {
        try {
            User userFromDb = userRepository.findByName(user.getName()).getFirst();
            return false;
        }
        catch (NoSuchElementException e) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

            /*PhoneNumber pN = user.getPhoneNumber();
            pN.setUser(user);
            phoneNumberRepository.save(pN);*/

            return true;
        }
    }
}
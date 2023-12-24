package me.drpuc.lda.security.service;

import lombok.AllArgsConstructor;
import me.drpuc.lda.entity.User;
import me.drpuc.lda.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class LdaUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String callsign) throws UsernameNotFoundException {
        User user = userRepository.findByCallsign(callsign).orElseThrow(
                () -> new UsernameNotFoundException("Callsign not found"));

        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getCallsign(),
                user.getPassword(),
                authorities
        );
    }
}

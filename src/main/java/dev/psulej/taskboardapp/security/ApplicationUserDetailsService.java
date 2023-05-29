package dev.psulej.taskboardapp.security;
import dev.psulej.taskboardapp.user.domain.User;
import dev.psulej.taskboardapp.user.repository.UserRepository;
import dev.psulej.taskboardapp.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByLoginIgnoreCase(username)
                .map(this::mapToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }

    private ApplicationUserDetails mapToUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.role().name()));
        return ApplicationUserDetails.builder()
                .id(user.id())
                .email(user.email())
                .login(user.login())
                .name(user.name())
                .password(user.password())
                .authorities(authorities)
                .build();
    }
}

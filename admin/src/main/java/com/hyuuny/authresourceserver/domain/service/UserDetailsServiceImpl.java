package com.hyuuny.authresourceserver.domain.service;

import com.hyuuny.authresourceserver.application.dto.UserAdapter;
import com.hyuuny.authresourceserver.domain.entity.Privilege;
import com.hyuuny.authresourceserver.domain.entity.User;
import com.hyuuny.authresourceserver.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User existingUser = userRepository.findByUserAccountUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username + "을 찾을 수 없습니다.")
        );
        List<Privilege.PrivilegeType> privilegeTypes = existingUser.getPrivilegeTypes();
        return new UserAdapter(existingUser, privilegeTypes);
    }

}

package dev.psulej.taskboard.board.mapper;


import dev.psulej.taskboard.user.api.User;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.domain.UserRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMap() {
        UserEntity entity = UserEntity.builder()
                .id(UUID.fromString("0dd72a53-17be-4b35-ac87-d2cde83dc9d2"))
                .login("johndoe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .name("John Doe")
                .imageId(UUID.fromString("3c9eef1b-d27b-40bf-a504-e997ac877a6e"))
                .build();

        User user = userMapper.mapUser(entity);

        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("johndoe");
        assertThat(user.imageId().toString()).isEqualTo("3c9eef1b-d27b-40bf-a504-e997ac877a6e");
    }


    @Test
    void shouldMapUserWithoutImageIdSet() {
        UserEntity entity = UserEntity.builder()
                .id(UUID.fromString("0dd72a53-17be-4b35-ac87-d2cde83dc9d2"))
                .login("johndoe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .name("John Doe")
                .imageId(null)
                .build();

        User user = userMapper.mapUser(entity);

        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("johndoe");
        assertThat(user.imageId()).isNull();
    }
}
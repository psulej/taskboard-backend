package dev.psulej.taskboard.board.mapper;


import dev.psulej.taskboard.board.api.User;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.domain.UserRole;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = new UserMapper();

    @Test
    void shouldMapUser() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.fromString("0dd72a53-17be-4b35-ac87-d2cde83dc9d2"))
                .login("johndoe")
                .name("John Doe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .imageId(UUID.fromString("3c9eef1b-d27b-40bf-a504-e997ac877a6e"))
                .build();

        // when
        User user = userMapper.mapUser(userEntity);

        // then
        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("johndoe");
        assertThat(user.name()).isEqualTo("John Doe");
        assertThat(user.imageId().toString()).isEqualTo("3c9eef1b-d27b-40bf-a504-e997ac877a6e");
    }

    @Test
    void shouldMapUserWithoutImageIdSet() {
        UserEntity userEntity = UserEntity.builder()
                .id(UUID.fromString("0dd72a53-17be-4b35-ac87-d2cde83dc9d2"))
                .login("johndoe")
                .email("johndoe@localhost")
                .role(UserRole.USER)
                .name("John Doe")
                .build();

        User user = userMapper.mapUser(userEntity);

        assertThat(user).isNotNull();
        assertThat(user.login()).isEqualTo("johndoe");
        assertThat(user.name()).isEqualTo("John Doe");
        assertThat(user.imageId()).isNull();
    }
}
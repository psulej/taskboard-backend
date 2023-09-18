package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.user.api.User;
import dev.psulej.taskboard.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User mapUser(UserEntity user) {
        return User.builder()
                .id(user.id())
                .login(user.login())
                .name(user.name())
                .imageId(user.imageId())
                .avatarColor(user.avatarColor())
                .build();
    }

}

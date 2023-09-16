package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.user.api.UserView;
import dev.psulej.taskboard.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserView mapUser(UserEntity user) {
        return UserView.builder()
                .id(user.id())
                .login(user.login())
                .name(user.name())
                .imageId(user.imageId())
                .avatarColor(user.avatarColor())
                .build();
    }
}

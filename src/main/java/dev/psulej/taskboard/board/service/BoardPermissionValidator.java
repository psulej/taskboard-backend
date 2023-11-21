package dev.psulej.taskboard.board.service;

import dev.psulej.taskboard.board.domain.BoardEntity;
import dev.psulej.taskboard.board.domain.BoardUser;
import dev.psulej.taskboard.board.domain.BoardUserRole;
import dev.psulej.taskboard.board.exception.BoardNotFoundException;
import dev.psulej.taskboard.board.repository.BoardRepository;
import dev.psulej.taskboard.user.domain.UserEntity;
import dev.psulej.taskboard.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BoardPermissionValidator {
    private final UserService userService;
    private final BoardRepository boardRepository;

    private BoardUserRole getLoggedUserRole(UUID boardId) {
        BoardEntity boardEntity = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(boardId));
        UserEntity loggedUser = userService.getLoggedUser();
        List<BoardUser> boardUsers = boardEntity.users();
        return boardUsers.stream()
                .filter(user -> user.user().id().equals(loggedUser.id()))
                .map(BoardUser::role)
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("User with this role not allowed"));
    }

    public void validateBoardModifying(UUID boardId) {
        BoardUserRole userRole = getLoggedUserRole(boardId);
        if (!isRoleAllowedToModifyBoard(userRole)) {
            throw new AccessDeniedException("Role not allowed for editing");
        }
    }

    private boolean isRoleAllowedToModifyBoard(BoardUserRole userRole) {
        return Objects.equals(BoardUserRole.BOARD_ADMINISTRATOR, userRole);
    }
}

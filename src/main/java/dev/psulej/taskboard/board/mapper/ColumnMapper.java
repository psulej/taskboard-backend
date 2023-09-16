package dev.psulej.taskboard.board.mapper;
import dev.psulej.taskboard.board.api.Column;
import dev.psulej.taskboard.board.domain.ColumnEntity;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ColumnMapper {

    private final TaskMapper taskMapper;

    public Column mapColumn(ColumnEntity column) {
        return Column.builder()
                .id(column.id())
                .name(column.name())
                .tasks(CollectionUtils.emptyIfNull(column.tasks()).stream()
                        .map(taskMapper::mapTask
                        ).toList())
                .build();
    }
}

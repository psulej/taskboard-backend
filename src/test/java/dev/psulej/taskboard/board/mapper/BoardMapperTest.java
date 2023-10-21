package dev.psulej.taskboard.board.mapper;

import static org.junit.jupiter.api.Assertions.*;

class BoardMapperTest {

        UserMapper userMapper = new UserMapper();
        BoardMapper boardMapper = new BoardMapper(new ColumnMapper(new TaskMapper(userMapper)), userMapper);


}
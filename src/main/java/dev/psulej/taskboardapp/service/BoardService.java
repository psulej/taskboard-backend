package dev.psulej.taskboardapp.service;

import dev.psulej.taskboardapp.api.AvailableBoard;
import dev.psulej.taskboardapp.api.UpdateColumn;
import dev.psulej.taskboardapp.model.Board;
import dev.psulej.taskboardapp.model.Column;
import dev.psulej.taskboardapp.repository.BoardRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BoardService {

    private final MongoTemplate mongoTemplate;
    private final BoardRepository boardRepository;

    public BoardService(MongoTemplate mongoTemplate, BoardRepository boardRepository) {
        this.mongoTemplate = mongoTemplate;
        this.boardRepository = boardRepository;
    }

    public List<AvailableBoard> getAvailableBoards(){
        Query query = new Query();
        // Wyszukiwanie boardow tylko dla usera
        // Criteria criteria = Criteria.where("users").elemMatch(Criteria.where("id").is(UUID.randomUUID()));
        Criteria criteria = new Criteria();
        query.addCriteria(criteria);
        return mongoTemplate.find(query, AvailableBoard.class, "boards");
    }

    public Board getBoard(UUID id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Board was not found"));
    }

    public void updateColumns(UUID id,List<UpdateColumn> columns) {
        Board board = getBoard(id);

        // TODO map columns
        columns.forEach(column ->{

        });

        System.out.println("boardId: " + id);
    }
}

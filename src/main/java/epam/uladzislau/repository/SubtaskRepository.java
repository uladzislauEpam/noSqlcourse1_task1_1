package epam.uladzislau.repository;

import epam.uladzislau.model.Subtask;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubtaskRepository extends MongoRepository<Subtask, String> {

}

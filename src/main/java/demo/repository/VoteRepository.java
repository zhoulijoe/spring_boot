package demo.repository;

import demo.domain.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VoteRepository extends CrudRepository<Vote, Long> {

    @Query(value = "select v.* from Option o, Vote v where o.POLL_ID = ?1 and o.OPTION_ID = v.OPTION_ID",
            nativeQuery = true)
    public Iterable<Vote> findByPoll(Long pollId);
}

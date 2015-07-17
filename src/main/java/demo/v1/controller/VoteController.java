package demo.v1.controller;

import demo.domain.Vote;
import demo.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController("voteControllerV1")
@RequestMapping("/v1/")
public class VoteController {

    @Autowired
    private VoteRepository voteRepository;

    @RequestMapping(value = "polls/{pollId}/votes", method = RequestMethod.POST)
    public ResponseEntity<?> createVote(@PathVariable Long pollId, @RequestBody Vote vote) {
        Vote newVote = voteRepository.save(vote);

        URI voteUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newVote.getId())
                .toUri();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(voteUri);

        return new ResponseEntity<>(httpHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "polls/{pollId}/votes", method = RequestMethod.GET)
    public ResponseEntity<Iterable<Vote>> getAllVotes(@PathVariable Long pollId) {
        return new ResponseEntity<>(voteRepository.findByPoll(pollId), HttpStatus.OK);
    }
}

package demo.controller;

import demo.domain.Poll;
import demo.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @RequestMapping(value="polls", method= RequestMethod.GET)
    public ResponseEntity<Iterable<Poll>> getAllPolls() {
        return new ResponseEntity<Iterable<Poll>>(pollRepository.findAll(), HttpStatus.OK);
    }

}

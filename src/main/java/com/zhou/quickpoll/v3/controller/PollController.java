package com.zhou.quickpoll.v3.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.zhou.quickpoll.domain.Poll;
import com.zhou.quickpoll.dto.error.ErrorDetail;
import com.zhou.quickpoll.exception.ResourceNotFoundException;
import com.zhou.quickpoll.repository.PollRepository;
import com.zhou.quickpoll.v1.controller.ComputeResultController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController("pollControllerV3")
@RequestMapping("/oauth2/v3/")
@Api(value="polls", description="Polls API")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @RequestMapping(value = "polls", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves all the polls", response=Poll.class,
            responseContainer="List")
    public ResponseEntity<Page<Poll>> getAllPolls(Pageable pageable) {
        Page<Poll> polls = pollRepository.findAll(pageable);

        for (Poll poll : polls) {
            updatePollResourceWithLinks(poll, pageable);
        }

        return new ResponseEntity<>(polls, HttpStatus.OK);
    }

    @RequestMapping(value = "polls/{pollId}", method = RequestMethod.GET)
    @ApiOperation(value = "Retrieves a Poll associated with the pollId", response=Poll.class)
    public ResponseEntity<Poll> getPoll(@PathVariable Long pollId) {
        Poll poll = verifyPoll(pollId);

        updatePollResourceWithLinks(poll, null);

        return new ResponseEntity<>(poll, HttpStatus.OK);
    }

    @RequestMapping(value = "polls", method = RequestMethod.POST)
    @ApiOperation(value = "Creates a new Poll",
            notes="The newly created poll Id will be sent in the location response header",
            response = Void.class)
    @ApiResponses(value = {@ApiResponse(code=201, message="Poll Created Successfully",
            response=Void.class),
            @ApiResponse(code=500, message="Error creating Poll", response=ErrorDetail.class) } )
    public ResponseEntity<Void> createPoll(@Valid @RequestBody Poll poll) {
        Poll newPoll = pollRepository.save(poll);

        URI pollUri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newPoll.getPollId())
                .toUri();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(pollUri);

        return new ResponseEntity<>(responseHeaders, HttpStatus.CREATED);
    }

    @RequestMapping(value = "polls/{pollId}", method = RequestMethod.PUT)
    public ResponseEntity<?> updatePoll(@RequestBody Poll poll, @PathVariable Long pollId) {
        verifyPoll(pollId);

        pollRepository.save(poll);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "polls/{pollId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deletePoll(@PathVariable Long pollId) {
        verifyPoll(pollId);

        pollRepository.delete(pollId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected Poll verifyPoll(Long pollId)
        throws ResourceNotFoundException {
        Poll poll = pollRepository.findOne(pollId);
        if (poll == null) {
            throw new ResourceNotFoundException("Poll with id " + pollId + " not found");
        }

        return poll;
    }

    private void updatePollResourceWithLinks(Poll poll, Pageable pageable) {
        poll.add(linkTo(methodOn(PollController.class).getAllPolls(pageable)).slash(poll.
                getPollId()).withSelfRel());
        poll.add(linkTo(methodOn(VoteController.class).getAllVotes(poll.getPollId())).
                withRel("votes"));
        poll.add(linkTo(methodOn(ComputeResultController.class).computeResult(poll.
                getPollId())).withRel("compute-result"));
    }
}

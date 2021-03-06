package com.zhou.quickpoll.v3.controller;

import com.zhou.quickpoll.dto.OptionCount;
import com.zhou.quickpoll.dto.VoteResult;
import com.zhou.quickpoll.domain.Vote;
import com.zhou.quickpoll.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("computeResultControllerV3")
@RequestMapping("/v3/")
public class ComputeResultController {

    @Autowired
    private VoteRepository voteRepository;

    @RequestMapping(value="/computeresult", method= RequestMethod.GET)
    public ResponseEntity<VoteResult> computeResult(@RequestParam Long pollId) {
        Iterable<Vote> votes = voteRepository.findByPoll(pollId);

        VoteResult voteResult = new VoteResult();
        int totalVotes = 0;
        Map<Long, OptionCount> optionCounts = new HashMap<>();

        for (Vote vote : votes) {
            totalVotes++;

            OptionCount optionCount = optionCounts.get(vote.getOption().getId());
            if (optionCount == null) {
                optionCount = new OptionCount();
                optionCount.setOptionId(vote.getOption().getId());

                optionCounts.put(vote.getOption().getId(), optionCount);
            }

            optionCount.setCount(optionCount.getCount() + 1);
        }

        voteResult.setTotalVotes(totalVotes);
        voteResult.setResults(optionCounts.values());

        return new ResponseEntity<>(voteResult, HttpStatus.OK);
    }
}

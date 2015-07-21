package com.zhou.quickpoll.repository;

import com.zhou.quickpoll.domain.Poll;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PollRepository extends PagingAndSortingRepository<Poll, Long> {

}

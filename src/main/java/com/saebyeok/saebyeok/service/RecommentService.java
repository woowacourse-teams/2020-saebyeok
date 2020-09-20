package com.saebyeok.saebyeok.service;

import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.RecommentRepository;
import com.saebyeok.saebyeok.dto.RecommentCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class RecommentService {
    private final RecommentRepository recommentRepository;

    public Long createRecomment(Member member, RecommentCreateRequest recommentCreateRequest) {
        return 1L;
    }
}

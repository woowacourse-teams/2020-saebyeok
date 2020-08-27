package com.saebyeok.saebyeok.util;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Member;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class NicknameGenerator {
    public static final String WRITER_NICKNAME = "작성자";
    public static final String NICKNAME_PREFIX = "익명#";
    private static final List<Integer> TOTAL_NUMBERS = IntStream.rangeClosed(1, 1000)
            .boxed()
            .collect(Collectors.toList());

    public String generate(Member member, Article article) {
        if (article.isWrittenBy(member)) {
            return WRITER_NICKNAME;
        }
        return article.loadExistingNickname(member)
                .orElse(createNewNickname(article));
    }

    private String createNewNickname(Article article) {
        List<String> allNicknames = article.getAllNicknames();
        List<String> filteredNicknames = allNicknames.stream()
                .filter(s -> s.contains(NICKNAME_PREFIX))
                .collect(Collectors.toList());
        List<Integer> numbers = filteredNicknames.stream()
                .map(nickname -> Integer.parseInt(nickname.replace(NICKNAME_PREFIX, "")))
                .collect(Collectors.toList());
        List<Integer> filteredNumbers = TOTAL_NUMBERS.stream()
                .filter(integer -> !numbers.contains(integer))
                .collect(Collectors.toList());
        Collections.shuffle(filteredNumbers);
        return NICKNAME_PREFIX + filteredNumbers.get(0);
    }
}

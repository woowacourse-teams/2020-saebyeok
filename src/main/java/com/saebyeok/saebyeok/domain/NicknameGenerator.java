package com.saebyeok.saebyeok.domain;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class NicknameGenerator {

    static final String WRITER_NICKNAME = "작성자";
    private static final String NICKNAME_PREFIX = "익명#";
    private static final List<Integer> TOTAL_NUMBERS = IntStream.rangeClosed(1, 1000)
            .boxed()
            .collect(Collectors.toList());

    public String generate(Member member, Article article, List<Comment> comments) {
        if (article.isWrittenBy(member)) {
            return WRITER_NICKNAME;
        }
        return loadExistingNickname(member, comments)
                .orElseGet(() -> createNewNickname(comments));
    }

    private String createNewNickname(List<Comment> comments) {
        List<String> allNicknames = getAllNicknames(comments);
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

    private Optional<String> loadExistingNickname(Member member, List<Comment> comments) {
        return comments.stream()
                .filter(comment -> comment.isWrittenBy(member))
                .findFirst()
                .map(Comment::getNickname);
    }

    private List<String> getAllNicknames(List<Comment> comments) {
        return comments.stream()
                .map(Comment::getNickname)
                .collect(Collectors.toList());
    }
}

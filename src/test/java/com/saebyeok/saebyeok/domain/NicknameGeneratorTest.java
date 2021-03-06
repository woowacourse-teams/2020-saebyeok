package com.saebyeok.saebyeok.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.saebyeok.saebyeok.domain.NicknameGenerator.WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class NicknameGeneratorTest {

    private NicknameGenerator nicknameGenerator;
    private Member me;
    private Member other1;
    private Member other2;
    private Article myArticle;
    private Article othersArticle;

    @BeforeEach
    void setUp() {
        nicknameGenerator = new NicknameGenerator();
        me = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        other1 = new Member(2L, "987654321", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        other2 = new Member(3L, "132435465", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        myArticle = new Article(1L, "내용", me, LocalDateTime.now(), true, false, new ArrayList<>());
        othersArticle = new Article(2L, "내용", other1, LocalDateTime.now(), true, false, new ArrayList<>());
    }

    @DisplayName("본인의 글에 댓글 단 경우, 작성자임을 나타내는 지정된 닉네임이 부여돼야 한다.")
    @Test
    void writerNicknameTest() {
        String nickname = nicknameGenerator.generate(me, myArticle, new ArrayList<>());

        assertThat(nickname).isEqualTo(WRITER_NICKNAME);
    }

    @DisplayName("타인의 글에 댓글을 여러 개 단 경우, 내 닉네임들은 모두 같아야 한다.")
    @Test
    void severalCommentsByOneMemberTest() {
        List<Comment> comments = new ArrayList<>();
        String myNickname1 = nicknameGenerator.generate(me, othersArticle, comments);
        comments.add(new Comment("댓글내용", me, myNickname1, othersArticle, null));
        String othersNickname = nicknameGenerator.generate(
                other2, new Article(2L, "게시물내용", other1, LocalDateTime.now(), true, false, new ArrayList<>()), comments);
        comments.add(new Comment("댓글내용", other2, othersNickname, othersArticle, null));
        String myNickname2 = nicknameGenerator.generate(me, new Article(2L, "게시물내용", other1,
                LocalDateTime.now(), true, false, new ArrayList<>()), comments);

        assertAll (
                () -> assertThat(myNickname1).isNotEqualTo(othersNickname),
                () -> assertThat(myNickname1).isEqualTo(myNickname2)
        );
    }
}

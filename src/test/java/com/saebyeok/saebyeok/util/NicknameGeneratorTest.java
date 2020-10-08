package com.saebyeok.saebyeok.util;

import com.saebyeok.saebyeok.domain.Article;
import com.saebyeok.saebyeok.domain.Comment;
import com.saebyeok.saebyeok.domain.Member;
import com.saebyeok.saebyeok.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static com.saebyeok.saebyeok.util.NicknameGenerator.WRITER_NICKNAME;
import static org.assertj.core.api.Assertions.assertThat;

public class NicknameGeneratorTest {

    private NicknameGenerator nicknameGenerator;
    private Member me;
    private Member other;
    private Article myArticle;
    private Article othersArticle;

    @BeforeEach
    void setUp() {
        nicknameGenerator = new NicknameGenerator();
        me = new Member(1L, "123456789", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        other = new Member(2L, "987654321", "naver", LocalDateTime.now(), false, Role.USER, new ArrayList<>());
        myArticle = new Article(1L, "내용", me, LocalDateTime.now(), true, false, new ArrayList<>(), new ArrayList<>());
        othersArticle = new Article(2L, "내용", other, LocalDateTime.now(), true, false, new ArrayList<>(), new ArrayList<>());
    }

    @DisplayName("본인의 글에 댓글 단 경우, 작성자임을 나타내는 지정된 닉네임이 부여돼야 한다.")
    @Test
    void writerNicknameTest() {
        String nickname = nicknameGenerator.generate(me, myArticle);

        assertThat(nickname).isEqualTo(WRITER_NICKNAME);
    }

    @DisplayName("타인의 글에 댓글을 여러개 단 경우, 내 닉네임들은 모두 같아야 한다.")
    @Test
    void severalCommentsByOneMemberTest() {
        String myNickname1 = nicknameGenerator.generate(me, othersArticle);
        Comment comment = new Comment("댓글내용", me, myNickname1, othersArticle, null);
        String myNickname2 = nicknameGenerator.generate(
                me, new Article(2L, "게시물내용", other, LocalDateTime.now(), true, false, Arrays.asList(comment), new ArrayList<>()));

        assertThat(myNickname1).isEqualTo(myNickname2);
    }
}

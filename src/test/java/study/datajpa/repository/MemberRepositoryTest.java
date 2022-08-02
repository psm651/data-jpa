package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;


    @Test
    public void testMember() {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검사
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("AAA", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        assertThat(memberRepository.findByUsernameAndAgeGreaterThan("AAA",10).get(0)).isEqualTo(memberB);
    }

    @Test
    public void findUser() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 20);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        assertThat(memberRepository.findUser("AAA",10).get(0)).isEqualTo(memberA);
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("test");
        teamRepository.save(team);
        Member memberA = new Member("AAA", 10, team);
        memberRepository.save(memberA);

        assertThat(memberRepository.findMemberDto().get(0).getTeamName()).isEqualTo("test");
        assertThat(memberRepository.findMemberDto().get(0).getUsername()).isEqualTo("AAA");
        assertThat(memberRepository.findMemberDto().get(0).getAge()).isEqualTo(10);
    }
    @Test
    public void findByNames() {
        Member memberA = new Member("AAA", 10);
        Member memberB = new Member("BBB", 10);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        assertThat(memberRepository.findByNames(Arrays.asList("AAA", "BBB")).size()).isEqualTo(2);
    }
}
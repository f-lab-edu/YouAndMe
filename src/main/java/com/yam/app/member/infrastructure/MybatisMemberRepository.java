package com.yam.app.member.infrastructure;

import com.yam.app.member.domain.Member;
import com.yam.app.member.domain.MemberReader;
import com.yam.app.member.domain.MemberRepository;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisMemberRepository implements MemberRepository, MemberReader {

    private final SqlSessionTemplate template;

    private static final String SAVE_FQCN = "com.yam.app.member.domain.MemberRepository.save";
    private static final String UPDATE_FQCN = "com.yam.app.member.domain.MemberRepository.update";

    public MybatisMemberRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return template.getMapper(MemberReader.class).findByNickname(nickname);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return template.getMapper(MemberReader.class).findById(memberId);
    }

    @Override
    public void save(Member entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem saving the object : %s", entity));
        }
    }

    @Override
    public void update(Member entity) {
        int result = template.update(UPDATE_FQCN, entity);
        if (result != 1) {
            throw new RuntimeException(
                String.format("There was a problem updating the object : %s", entity));
        }
    }
}

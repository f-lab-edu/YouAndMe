package com.yam.app.member.infrastructure;

import com.yam.app.member.domain.Member;
import com.yam.app.member.domain.MemberReader;
import com.yam.app.member.domain.MemberRepository;
import java.util.Optional;
import org.mybatis.spring.SqlSessionTemplate;

public final class MybatisMemberRepository implements MemberRepository, MemberReader {

    private static final String SAVE_FQCN = "com.yam.app.member.domain.MemberRepository.save";
    private static final String UPDATE_FQCN = "com.yam.app.member.domain.MemberRepository.update";

    private final SqlSessionTemplate template;

    public MybatisMemberRepository(SqlSessionTemplate template) {
        this.template = template;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return template.getMapper(MemberReader.class).existsByNickname(nickname);
    }

    @Override
    public Optional<Member> findById(Long memberId) {
        return template.getMapper(MemberReader.class).findById(memberId);
    }

    @Override
    public Long save(Member entity) {
        int result = template.insert(SAVE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were saved than expected. : %s", entity));
        }
        return entity.getId();
    }

    @Override
    public void update(Member entity) {
        int result = template.update(UPDATE_FQCN, entity);
        if (result != 1) {
            throw new IllegalStateException(String.format(
                "Unintentionally, more records were updated than expected. : %s", entity));
        }
    }
}

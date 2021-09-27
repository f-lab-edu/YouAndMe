package com.yam.app.member.infrastructure;

import com.yam.app.member.domain.MemberReader;
import com.yam.app.member.domain.MemberRepository;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemberModuleConfiguration {

    @Bean
    public MemberReader memberReader(SqlSessionTemplate template) {
        return new MybatisMemberRepository(template);
    }

    @Bean
    public MemberRepository memberRepository(SqlSessionTemplate template) {
        return new MybatisMemberRepository(template);
    }
}

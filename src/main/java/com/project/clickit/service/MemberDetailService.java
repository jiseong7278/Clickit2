package com.project.clickit.service;

import com.project.clickit.entity.MemberEntity;
import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.common.ObjectNotFoundException;
import com.project.clickit.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberDetailService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberRepository.findById(id);
        if(memberEntity == null) {
            throw new ObjectNotFoundException(ErrorCode.MEMBER_NOT_FOUND);
        }
        return User.builder()
                .username(memberEntity.getId())
                .password(memberEntity.getPassword())
                .roles(memberEntity.getType())
                .build();
    }
}

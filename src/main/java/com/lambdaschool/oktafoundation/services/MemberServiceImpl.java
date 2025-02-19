package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional
@Service(value = "memberService")
public class MemberServiceImpl implements MemberService {
    /**
     * Connects this service to the Member table.
     */
    @Autowired
    private MemberRepository memberrepos;

    @Autowired
    private ReactionService reactionService;

    @Autowired
    private ClubService clubService;

    @Override
    public List<Member> findAll() {
        List<Member> list = new ArrayList<>();
        memberrepos.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Override
    public Member findMemberById(long id) throws ResourceNotFoundException {
        return memberrepos.findById(id).orElseThrow(() -> new ResourceNotFoundException("Member id " + id + " not found!"));
    }

    @Override
    public Member findMemberByMemberId(String memberid) throws ResourceNotFoundException {
        return memberrepos.findMemberByMemberid(memberid).orElseThrow(() -> new ResourceNotFoundException("Member with memberid" + memberid + " not found"));
    }

    @Override
    public void deleteAll(){
        memberrepos.deleteAll();
    }


    @Override
    public Member save(Member member) {
//        Member newMember = new Member();
//
//        if (member.getMember_table_id() != 0) {
//           memberrepos.findById(member.getMember_table_id())
//                    .orElseThrow(() -> new ResourceNotFoundException("Member id " + member.getMember_table_id() + " not found!!"));
//            newMember.setMember_table_id(member.getMember_table_id());
//        }
//
////         if the memberid is already in the database, return it, no new member is created.
//            Optional<Member> temp = memberrepos.findByMemberid(member.getMemberid());
//            if (temp.isPresent()) {
//                return temp.get();
//            }
//        System.out.println(member.getMemberid());
//
//        newMember.setMemberid(member.getMemberid());
//
//        newMember.getReactions().clear();
//        for (MemberReactions mr : member.getReactions()) {
//            Reaction addReaction = reactionService.findReactionById(mr.getReaction().getReactionid());
//            newMember.getReactions().add(new MemberReactions(newMember, addReaction, mr.getClubactivity()));
//        }
//        newMember.getClubs().clear();
//        for (ClubMembers clubMembers : member.getClubs()) {
//            Club addClub = clubService.findClubById(clubMembers.getClub().getClubid());
//            newMember.getClubs().add(new ClubMembers(addClub, newMember));
//        }
//
//        return memberrepos.save(newMember);
        return memberrepos.save(member);
    }
}

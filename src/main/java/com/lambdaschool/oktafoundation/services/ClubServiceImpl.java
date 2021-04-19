package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.ClubActivityRepository;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
import com.lambdaschool.oktafoundation.repository.ClubUsersRepository;
import com.lambdaschool.oktafoundation.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "clubService")
public class ClubServiceImpl implements ClubService{

    @Autowired
    private ClubRepository clubrepos;

    @Autowired
    private ClubActivityRepository clubactivityrepos;

    @Autowired
    private ClubUsersRepository clubUsersrepo;

    @Autowired
    private RoleRepository rolerepos;

    @Override
    public List<Club> findAll() {
        List<Club> clubList = new ArrayList<>();

        clubrepos.findAll()
                .iterator()
                .forEachRemaining(clubList::add);
        return clubList;
    }

    @Override
    public Club findClubById(Long clubid) throws
            ResourceNotFoundException
    {
        return clubrepos.findById(clubid)
            .orElseThrow(() -> new ResourceNotFoundException("Club id" + clubid + "not found!"));
    }

    @Transactional
    @Override
    public Club save(Club club) {

        Club newClub = new Club();

        if(club.getClubid() != 0)
        {
            clubrepos.findById(club.getClubid())
                    .orElseThrow(() -> new ResourceNotFoundException("Club id" + club.getClubid() + "not found!"));
            newClub.setClubid(club.getClubid());
        }

        newClub.setClubname(club.getClubname());

//      TODO : Did I handle this many to many relationship correctly?
        newClub.getActivities()
                .clear();
        for (ClubActivity ca: club.getActivities())
        {
//
            Activity newActivity = new Activity();
            newActivity.setActivityname(ca.getActivity().getActivityname());

            ClubActivity newclubActivity = new ClubActivity();
            newclubActivity.setActivity(newActivity);
            newclubActivity.setClub(newClub);

            newClub.getActivities().add(newclubActivity);
        }

        newClub.getUsers()
                .clear();
        for (ClubUsers cu: club.getUsers())
        {
            User newUser = new User();
            newUser.setUsername(cu.getUser().getUsername());
//            newUser.setUseremails(cu.getUser().getUseremails());
            newUser.getRoles()
                    .clear();
            for(UserRoles ur: cu.getUser().getRoles())
            {
                Role assignRole = rolerepos.findByNameIgnoreCase(ur.getRole().getName());
                if(assignRole == null)
                {
                    throw new ResourceNotFoundException("Role " + ur.getRole().getName() + "not found");
                }
                UserRoles newUserRoles = new UserRoles();
                newUserRoles.setRole(assignRole);
                newUserRoles.setUser(newUser);
                newUser.getRoles().add(newUserRoles);
            }
            ClubUsers newclubusers =
            newClub.getUsers().add(newclubusers);
        }

        return clubrepos.save(newClub);
    }

    @Transactional
    @Override
    public void update(Club club, long clubid) {
        Club updateClub = clubrepos.findById(clubid)
                .orElseThrow(() -> new EntityNotFoundException("Club Id" + clubid + "not found."));

        if(club.getClubname() != null)
        {
            updateClub.setClubname(club.getClubname().toLowerCase());
        }
        if(club.getActivities().size() > 0)
        {
            updateClub.getActivities().clear();
            for(ClubActivity ca: club.getActivities())
            {
                ClubActivity newClubActivity = clubactivityrepos.findById(ca.getClubactivityid())
                        .orElseThrow(() -> new EntityNotFoundException("Club Activity" + ca.getClubactivityid() + "Not found!"));
                updateClub.getActivities().add(newClubActivity);
            }
        }
        if(club.getUsers().size() > 0)
        {
            updateClub.getUsers().clear();
            for(ClubUsers cu: club.getUsers())
            {
                ClubUsers newClubUser = clubUsersrepo.findById(cu.getUser().getUserid())
                        .orElseThrow(() -> new EntityNotFoundException("Club User" + cu.getUser().getUserid() + "Not found!"));
                updateClub.getUsers().add(newClubUser);
            }
        }
    }

    @Override
    public void delete(long clubid) {
        clubrepos.findById(clubid)
                .orElseThrow(() -> new ResourceNotFoundException("Club id" + clubid + "not found!"));
        clubrepos.deleteById(clubid);
    }
}

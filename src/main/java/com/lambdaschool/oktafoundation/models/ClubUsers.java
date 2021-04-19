package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

// TODO Why is our clubusers table named clubdirectors when we also have ydp's ?
@Entity
@Table(name = "clubdirectors")
public class ClubUsers extends Auditable implements Serializable
{
    @Id
    @ManyToOne
    @JoinColumn(name = "clubid")
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Club club;

    @Id
    @ManyToOne
    @JoinColumn(name = "userid")
    @JsonIgnoreProperties(value = "clubs", allowSetters = true)
    private User user;

    public ClubUsers(
            User user,
            Club club
    )
    {
        this.user = user;
        this.club = club;
    }

    public ClubUsers() {

    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

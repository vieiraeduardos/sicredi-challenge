package com.example.sicredi_challenge.entities;

import com.example.sicredi_challenge.entities.enums.VoteChoice;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_votes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"agenda_id", "associate_id"})
})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @Column(name = "associate_id", nullable = false)
    private String associateId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VoteChoice choice;

    @Column(name = "voted_at", nullable = false)
    private LocalDateTime votedAt = LocalDateTime.now();

    public Vote() {}

    public Vote(Agenda agenda, String associateId, VoteChoice choice) {
        this.agenda = agenda;
        this.associateId = associateId;
        this.choice = choice;
        this.votedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public String getAssociateId() {
        return associateId;
    }

    public VoteChoice getChoice() {
        return choice;
    }

    public LocalDateTime getVotedAt() {
        return votedAt;
    }
}
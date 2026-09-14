package com.example.sicredi_challenge.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_agendas")
public class Agenda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String description;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "opened_at", nullable = true)
    private LocalDateTime openedAt;

    @Column(name = "expired_at", nullable = true)
    private LocalDateTime expiredAt;

    @Column(name = "voting_period", nullable = true)
    private Integer votingPeriod;

    public Agenda() {}

    public Agenda(String title, String description) {
        this.title = title;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    public void open(Integer votingPeriod) {
        int period = (votingPeriod != null && votingPeriod > 0) ? votingPeriod : 1;
        LocalDateTime now = LocalDateTime.now();
        this.openedAt = now;
        this.votingPeriod = period;
        this.expiredAt = now.plusMinutes(period);
    }

    public boolean isOpen() {
        if (openedAt == null || expiredAt == null) {
            return false;
        }
        return LocalDateTime.now().isBefore(expiredAt);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public Integer getVotingPeriod() {
        return votingPeriod;
    }

    public void setVotingPeriod(Integer votingPeriod) {
        this.votingPeriod = votingPeriod;
    }
}

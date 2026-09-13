package com.example.sicredi_challenge.repositories;

import com.example.sicredi_challenge.entities.Vote;
import com.example.sicredi_challenge.entities.enums.VoteChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    boolean existsByAgendaIdAndAssociateId(Long agendaId, String associateId);
    long countByAgendaIdAndChoice(Long agendaId, VoteChoice choice);
    long countByAgendaId(Long agendaId);
}
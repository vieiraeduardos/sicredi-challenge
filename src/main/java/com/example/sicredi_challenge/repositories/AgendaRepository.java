package com.example.sicredi_challenge.repositories;

import com.example.sicredi_challenge.entities.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaRepository extends JpaRepository<Agenda, Long> { }
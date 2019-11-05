package com.lomasz.api.team.model.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;
import java.util.List;

/**
 * Main entity
 *
 * */
@Data
@Entity
public class Team implements Serializable {

    private static final long serialVersionUID = 5994238256683599906L;

    private static final String SEQUENCE_NAME = "team_seq";

    /**
     * System identifier
     */
    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 25, nullable = false)
    private String acronym;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private List<Player> players;

    /**
     * Budget is natural number (in EUR currency)
     */
    @Column(nullable = false)
    private Long budget;

}

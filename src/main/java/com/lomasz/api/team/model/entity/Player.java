package com.lomasz.api.team.model.entity;

import com.lomasz.api.team.model.enums.Position;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.io.Serializable;

@Data
@Entity
public class Player implements Serializable {

    private static final long serialVersionUID = 5994238252283599906L;

    private static final String SEQUENCE_NAME = "player_seq";

    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 22, nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

}

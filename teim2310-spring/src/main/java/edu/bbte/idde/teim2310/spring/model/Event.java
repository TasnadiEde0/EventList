package edu.bbte.idde.teim2310.spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_Event")
public class Event extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String location;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime endDate;

    private Integer reminderTime; // days before a reminder for the event should be sent, 0 means no reminder

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}

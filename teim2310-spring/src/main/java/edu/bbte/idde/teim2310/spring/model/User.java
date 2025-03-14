package edu.bbte.idde.teim2310.spring.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "db_User")
public class User extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private String email;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Event> events = new ArrayList<>();

    public User(String name, String email) {
        super();

        this.name = name;
        this.email = email;
    }

}

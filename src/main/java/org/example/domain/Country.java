package org.example.domain;
import jakarta.persistence.*;

import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "country",schema = "movie")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id")
    private Short Id;

    @Column(name = "country")
    private String title;

    @Column(name="last_update")
    @UpdateTimestamp
    private LocalDateTime last_update;

    public Short getId() {
        return Id;
    }

    public void setId(Short id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getLast_update() {
        return last_update;
    }

    public void setLast_update(LocalDateTime last_update) {
        this.last_update = last_update;
    }
}

package com.example.helloworld.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "profileView")
@NamedQueries({
        @NamedQuery(
                name = "com.example.helloworld.core.ProfileView.findAll",
                query = "SELECT p FROM ProfileView p"
        )
})
public class ProfileView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JoinColumn(name = "viewedBy", nullable = false)
    @OneToOne
    private Person viewedBy;

    @JoinColumn(name = "visitedPerson")
    @OneToOne
    private Person visitedPerson;

    @Column(name = "visitedAt", nullable = false)
    private Timestamp visitedAt;

    public ProfileView() {}

    public ProfileView(Person viewedBy, Person visitedPerson, Timestamp visitedAt) {
        this.viewedBy = viewedBy;
        this.visitedPerson = visitedPerson;
        this.visitedAt = visitedAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getVisitedAt() {
        return visitedAt;
    }

    public void setVisitedAt(Timestamp visitedAt) {
        this.visitedAt = visitedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProfileView)) return false;

        final ProfileView that = (ProfileView) o;

        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.viewedBy, that.viewedBy) &&
                Objects.equals(this.visitedPerson, that.visitedPerson) &&
                Objects.equals(this.visitedAt, that.visitedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, visitedPerson, viewedBy, visitedAt);
    }
}

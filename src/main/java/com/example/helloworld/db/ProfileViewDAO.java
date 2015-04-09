package com.example.helloworld.db;

import com.example.helloworld.core.Person;
import com.example.helloworld.core.ProfileView;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.util.List;

public class ProfileViewDAO extends AbstractDAO<ProfileView> {
    public ProfileViewDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<ProfileView> findById(Long id) {
        return Optional.fromNullable(get(id));
    }

    public ProfileView create(ProfileView profileView) {
        return persist(profileView);
    }

    public List<ProfileView> findAll() {
        return list(namedQuery("com.example.helloworld.core.ProfileView.findAll"));
    }

    public List<Person> getVisitorsFor(Person user) {
        Query query = currentSession().createQuery("select p from Person p, ProfileView pv " +
                "where p = pv.viewedBy " +
                "and pv.visitedPerson = :visitedPerson " +
                "and pv.visitedAt >= :lastTenDays " +
                "order by pv.visitedAt desc");
        query.setLong("visitedPerson", user.getId());
        query.setTimestamp("lastTenDays", new Timestamp(new DateTime().minusDays(10).getMillis()));
        query.setMaxResults(10);
        List<Person> visitors = query.list();
        return visitors;
    }

    public int removeOldProfileViews() {
        Query query = currentSession().createQuery("delete ProfileView where visitedAt < :lessThanTenDays");
        query.setParameter("lessThanTenDays", new Timestamp(new DateTime().minusDays(10).getMillis()));
        return query.executeUpdate();
    }
}

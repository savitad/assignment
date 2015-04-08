package com.example.helloworld.db;

import com.example.helloworld.core.Person;
import com.example.helloworld.core.ProfileView;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

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
}

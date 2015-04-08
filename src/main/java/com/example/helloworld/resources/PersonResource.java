package com.example.helloworld.resources;

import com.example.helloworld.core.Person;
import com.example.helloworld.core.ProfileView;
import com.example.helloworld.db.PersonDAO;
import com.example.helloworld.db.ProfileViewDAO;
import com.example.helloworld.views.PersonView;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import org.joda.time.DateTime;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.sql.Timestamp;

@Path("/people/{userId}/{personId}")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {

    private final PersonDAO peopleDAO;
    private final ProfileViewDAO profileViewDAO;

    public PersonResource(PersonDAO peopleDAO, ProfileViewDAO profileViewDAO) {
        this.peopleDAO = peopleDAO;
        this.profileViewDAO = profileViewDAO;
    }

    @GET
    @UnitOfWork
    public Person getPerson(@PathParam("userId") LongParam userId, @PathParam("personId") LongParam personId) {

        Person visitedPerson = findSafely(personId.get());
        Person viewedBy = findSafely(userId.get());
        recordVisit(visitedPerson, viewedBy);
        return visitedPerson;
    }

    private void recordVisit(Person visitedPerson, Person viewedBy) {
        ProfileView profileView = new ProfileView(viewedBy, visitedPerson, new Timestamp(new DateTime().getMillis()));
        profileViewDAO.create(profileView);
    }

    @GET
    @Path("/view_freemarker")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public PersonView getPersonViewFreemarker(@PathParam("personId") LongParam personId) {
        return new PersonView(PersonView.Template.FREEMARKER, findSafely(personId.get()));
    }

    @GET
    @Path("/view_mustache")
    @UnitOfWork
    @Produces(MediaType.TEXT_HTML)
    public PersonView getPersonViewMustache(@PathParam("personId") LongParam personId) {
        return new PersonView(PersonView.Template.MUSTACHE, findSafely(personId.get()));
    }

    private Person findSafely(long personId) {
        final Optional<Person> person = peopleDAO.findById(personId);
        if (!person.isPresent()) {
            throw new NotFoundException("No such user.");
        }
        return person.get();
    }
}

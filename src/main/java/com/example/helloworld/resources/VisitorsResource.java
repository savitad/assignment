package com.example.helloworld.resources;

import com.example.helloworld.core.Person;
import com.example.helloworld.core.ProfileView;
import com.example.helloworld.db.PersonDAO;
import com.example.helloworld.db.ProfileViewDAO;
import com.google.common.base.Optional;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/visitors")
@Produces(MediaType.APPLICATION_JSON)
public class VisitorsResource {
    private final Logger LOG = Logger.getLogger(VisitorsResource.class);

    private final PersonDAO peopleDAO;
    private final ProfileViewDAO profileViewDAO;


    public VisitorsResource(PersonDAO peopleDAO, ProfileViewDAO profileViewDAO) {
        this.peopleDAO = peopleDAO;
        this.profileViewDAO = profileViewDAO;
    }

    @GET
    @UnitOfWork
    @Path("/{userId}")
    public List<Person> visitorsOf(@PathParam("userId") LongParam userId) {

        Person user = findSafely(userId.get());
        List<Person> visitors = profileViewDAO.getVisitorsFor(user);
        LOG.info(String.format("Found %d visitors for user: %s.", visitors.size(), user.getFullName()));
        return visitors;
    }

    @GET
    @UnitOfWork
    public List<ProfileView> allVisitors() {
        return profileViewDAO.findAll();
    }

    private Person findSafely(long personId) {
        final Optional<Person> person = peopleDAO.findById(personId);
        if (!person.isPresent()) {
            throw new NotFoundException("No such user.");
        }
        return person.get();
    }
}

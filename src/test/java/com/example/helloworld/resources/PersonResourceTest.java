package com.example.helloworld.resources;

import com.example.helloworld.core.Person;
import com.example.helloworld.core.ProfileView;
import com.example.helloworld.db.PersonDAO;
import com.example.helloworld.db.ProfileViewDAO;
import com.google.common.base.Optional;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PersonResource}.
 */
public class PersonResourceTest {
    private static final PersonDAO DAO = mock(PersonDAO.class);
    private static final ProfileViewDAO profileViewDAO = mock(ProfileViewDAO.class);
    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder()
            .addResource(new PersonResource(DAO, profileViewDAO))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();
    private Person person;

    @Before
    public void setup() {
        person = new Person();
        person.setId(1L);
    }

    @After
    public void tearDown() {
        reset(DAO);
    }

    @Test
    public void getPersonSuccess() {
        when(DAO.findById(1L)).thenReturn(Optional.of(person));

        Person found = RULE.getJerseyTest().target("/people/1/1").request().get(Person.class);

        assertThat(found.getId()).isEqualTo(person.getId());
        verify(DAO, times(2)).findById(1L);
    }

    @Test
    public void getPersonNotFound() {
        when(DAO.findById(2L)).thenReturn(Optional.<Person>absent());
        final Response response = RULE.getJerseyTest().target("/people/2/2").request().get();

        assertThat(response.getStatusInfo().getStatusCode()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
        verify(DAO).findById(2L);
    }
}

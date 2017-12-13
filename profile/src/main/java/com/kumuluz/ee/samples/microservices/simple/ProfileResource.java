package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.metrics.producers.MetricRegistryProducer;
import com.kumuluz.ee.samples.microservices.simple.models.Profile;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;


import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/profiles")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class ProfileResource {

    private static final Logger LOG = LogManager.getLogger(ProfileResource.class.getName());

    @PersistenceContext
    private EntityManager em;

    /**
     * Vrne seznam vseh profilov
     * */

    @GET
    @Metered(name = "simple_meter")
    public Response getProfiles() {

        TypedQuery<Profile> query = em.createNamedQuery("Profile.findAll", Profile.class);

        List<Profile> profiles = query.getResultList();
        LOG.info("List of profiles: {}", profiles);
        return Response.ok(profiles).build();
    }

    /**
     * Pridobi posamezen profil glede na njegov id
     */

    @GET
    @Path("/{id}")
    public Response getProfile(@PathParam("id") Integer id) {
        LOG.trace("BLABLABLABLA");
        Profile p = em.find(Profile.class, id);

        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();

        }
        LOG.info("Profile search: {}", p);
        return Response.ok(p).build();
    }

    /**
     * Omogoča urejanje profila tako, da staremu profilu nastavi nove vrednosti
     */
    @POST
    @Path("/{id}")
    public Response editProfile(@PathParam("id") Integer id, Profile profile) {

        Profile p = em.find(Profile.class, id);

        if (p == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        p.setName(profile.getName());
        p.setSurname(profile.getSurname());
        p.setPhone(profile.getPhone());
        p.setEmail(profile.getEmail());
        p.setName(profile.getName());
        p.setAdress(profile.getAdress());
        p.setPostnumb(profile.getPostnumb());

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();
        LOG.trace("uspesno posodobljeno");
        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    /**
     * Doda nov profil (Profile p)
     */
    @POST
    public Response createProfile(Profile p) {

        p.setId(null);

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();

        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    /**
     * Vrne config info
     * */

    @Inject
    private ProfileProperties properties;

    @GET
    @Path("/config")
    public Response test() {
        String response =
                "{" +
                        "\"jndi-name\": \"%s\"," +
                        "\"connection-url\": %s," +
                        "\"username\": %s," +
                        "\"password\": %s," +
                        "\"max-pool-size\": %d" +
                        "}";

        response = String.format(
                response,
                properties.getJndiName(),
                properties.getConnectionUrl(),
                properties.getUsername(),
                properties.getPassword(),
                properties.getMaxPoolSize()
                );

        return Response.ok(response).build();
    }
}

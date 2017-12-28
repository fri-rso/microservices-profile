package com.kumuluz.ee.samples.microservices.simple;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.metrics.producers.MetricRegistryProducer;
import com.kumuluz.ee.samples.microservices.simple.models.Profile;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.Histogram;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.Timed;


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
    @Inject
    @Metric(name = "histogram_dodanih")
    Histogram histogram;

    @GET
    @Metered(name = "getProfiles_meter")
    public Response getProfiles() {
        LOG.trace("getProfiles ENTRY");
        TypedQuery<Profile> query = em.createNamedQuery("Profile.findAll", Profile.class);

        List<Profile> profiles = query.getResultList();
        histogram.update(profiles.size());
        LOG.info("Stevilo vseh profilov: {}", profiles.size());
        return Response.ok(profiles).build();
    }

    /**
     * Pridobi posamezen profil glede na njegov id
     */

    @GET
    @Path("/{id}")
    @Timed(name = "getProfile_timer")
    public Response getProfile(@PathParam("id") Integer id) {
        LOG.trace("getProfile ENTRY");
        Profile p = em.find(Profile.class, id);

        if (p == null) {
            return Response.status(Response.Status.NOT_FOUND).build();

        }
        LOG.info("Prikazan profil ID: {}", p.getId());
        return Response.ok(p).build();
    }

    /**
     * Omogoča urejanje profila tako, da staremu profilu nastavi nove vrednosti
     */
    @POST
    @Path("/{id}")
    public Response editProfile(@PathParam("id") Integer id, Profile profile) {
        LOG.trace("editProfile ENTRY");
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
        LOG.info("Urejen profil ID: {}", p.getId());
        return Response.status(Response.Status.CREATED).entity(p).build();
    }

    /**
     * Doda nov profil (Profile p)
     */
    @POST
    public Response createProfile(Profile p) {
        LOG.trace("createProfile ENTRY");
        p.setId(null);

        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();
        LOG.info("Create profile ID: {}", p.getId());
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
        LOG.trace("config ENTRY");
        String response =
                "{" +
                        "\"jndi-name\": \"%s\"," +
                        "\"connection-url\": \"%s\"," +
                        "\"username\": \"%s\"," +
                        "\"password\": \"%s\"," +
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
        LOG.trace("config uspesen EXIT");
        return Response.ok(response).build();
    }
}

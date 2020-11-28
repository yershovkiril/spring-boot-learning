package com.yershovkiril.learningspringboot.clientproxy;

import com.yershovkiril.learningspringboot.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public interface UserResourceV1 {
    @GET
    @Produces(APPLICATION_JSON)
    List<User> fetchUsers(@QueryParam("gender") String gender);

    @GET
    @Path("{userUid}")
    @Produces(APPLICATION_JSON)
    User fetchUser(@PathParam("userUid") UUID userUid);

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    void insertNewUser(User user);

    @PUT
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    void updateUser(User user);

    @DELETE
    @Path("{userUid}")
    @Produces(APPLICATION_JSON)
    void deleteUser(@PathParam("userUid") UUID userUid);
}

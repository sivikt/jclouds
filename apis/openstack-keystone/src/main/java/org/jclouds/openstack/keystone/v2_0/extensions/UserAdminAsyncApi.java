/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jclouds.openstack.keystone.v2_0.extensions;

import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jclouds.Fallbacks.FalseOnNotFoundOr404;
import org.jclouds.Fallbacks.NullOnNotFoundOr404;
import org.jclouds.openstack.keystone.v2_0.domain.User;
import org.jclouds.openstack.keystone.v2_0.filters.AuthenticateRequest;
import org.jclouds.openstack.keystone.v2_0.options.CreateUserOptions;
import org.jclouds.openstack.keystone.v2_0.options.UpdateUserOptions;
import org.jclouds.openstack.v2_0.ServiceType;
import org.jclouds.openstack.v2_0.services.Extension;
import org.jclouds.rest.annotations.Fallback;
import org.jclouds.rest.annotations.MapBinder;
import org.jclouds.rest.annotations.PayloadParam;
import org.jclouds.rest.annotations.RequestFilters;
import org.jclouds.rest.annotations.SelectJson;

import com.google.common.annotations.Beta;
import com.google.common.util.concurrent.ListenableFuture;

/**
 * Provides asynchronous access to Users Administration actions.
 * <p/>
 * 
 * @see org.jclouds.openstack.keystone.v2_0.extensions.UserAdminApi
 * @author Pedro Navarro
 */
@Beta
@Extension(of = ServiceType.IDENTITY, namespace = ExtensionNamespaces.OS_KSADM)
@RequestFilters(AuthenticateRequest.class)
public interface UserAdminAsyncApi {
   
   /**
    * Creates a new user
    * 
    * @return the new user
    */
   @Named("user:create")
   @POST
   @Path("/users")
   @SelectJson("user")
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Fallback(NullOnNotFoundOr404.class)
   ListenableFuture<? extends User> create(@PayloadParam("name") String name,
         @PayloadParam("password") String password);

   /**
    * Creates a new user
    * 
    * @return the new user
    */
   @Named("user:create")
   @POST
   @Path("/users")
   @SelectJson("user")
   @Consumes(MediaType.APPLICATION_JSON)
   @MapBinder(CreateUserOptions.class)
   @Fallback(NullOnNotFoundOr404.class)
   ListenableFuture<? extends User> create(@PayloadParam("name") String name,
         @PayloadParam("password") String password, CreateUserOptions options);

   /**
    * Deletes an user.
    * 
    * @return true if successful
    */
   @Named("user:delete")
   @DELETE
   @Path("/users/{id}")
   @Consumes
   @Fallback(FalseOnNotFoundOr404.class)
   ListenableFuture<Boolean> delete(@PathParam("id") String id);

   /**
    * Updates an user
    * 
    * @return the updated user
    */
   @Named("user:updateuser")
   @PUT
   @Path("/users/{id}")
   @SelectJson("user")
   @Consumes(MediaType.APPLICATION_JSON)
   @MapBinder(UpdateUserOptions.class)
   @Fallback(NullOnNotFoundOr404.class)
   ListenableFuture<? extends User> update(@PathParam("id") String id, UpdateUserOptions options);

}

/**
 * Copyright (C) 2009-2011 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robbins.flashcards.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.Options;
import org.fusesource.restygwt.client.RestService;
import org.robbins.flashcards.model.Tag;


@Path("/flashcardsapi/v1/tags")
public interface TagRestService extends RestService {

    @GET
    @Path("/flashcardsapi/v1/tags")
    @Consumes("application/json")
    @Produces("application/json")
    public void getTags(@HeaderParam("Authorization") String authHeader, 
    					@QueryParam("fields") String fields, 
    					MethodCallback<List<Tag>> callback);
    
    @GET
    @Path("/flashcardsapi/v1/tags/search")
    @Consumes("application/json")
    @Produces("application/json")
    @Options(expect={200,204,1223})
    public void getTagsSearch(	@HeaderParam("Authorization") String authHeader, 
    							@QueryParam("name") String name, 
    							MethodCallback<Tag> callback);

    @GET
    @Path("/flashcardsapi/v1/tags/{tagId}")
    @Consumes("application/json")
    @Produces("application/json")
    public void getTag(	@HeaderParam("Authorization") String authHeader, 
    					@PathParam("tagId") Long tagId, 
    					@QueryParam("fields") String fields, 
    					MethodCallback<Tag> callback);
    
    @POST
    @Path("/flashcardsapi/v1/tags")
    @Consumes("application/json")
    @Produces("application/json")
    public void postTags(	@HeaderParam("Authorization") String authHeader, 
    						Tag tag, 
    						MethodCallback<Tag> callback);

    @PUT
    @Path("/flashcardsapi/v1/tags/{tagId}")
    @Consumes("application/json")
    @Produces("application/json")
    @Options(expect={204,1223})
    public void putTag(@HeaderParam("Authorization") String authHeader, 
    					@PathParam("tagId") Long tagId,
    					Tag tag,
    					MethodCallback<java.lang.Void> callback);
    
    @DELETE
    @Path("/flashcardsapi/v1/tags/{tagId}")
    @Consumes("application/json")
    @Produces("application/json")
    public void deleteTags(	@HeaderParam("Authorization") String authHeader, 
    						@PathParam("tagId") Long tagId, 
    						MethodCallback<java.lang.Void> callback);
}
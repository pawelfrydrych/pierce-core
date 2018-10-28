package com.pierceecom.blog.controllers;

import com.pierceecom.blog.services.PostService;
import commands.PostCommand;
import dtos.PostDto;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("posts")
public class PostController {

	@Inject
	private PostService postService;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllPosts() {
		return postService.getAllPosts();
	}

	@GET
	@Path("/{postId}")
	public Response getPostById(@PathParam("postId") Long postId) {
		return postService.getPostById(postId);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPost(PostCommand postCommand){
		return postService.createPost(postCommand);
	}

	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePost(PostDto postDto){
		return postService.update(postDto);
	}

	@DELETE
	@Path("/{postId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deletePost(@PathParam("postId") Long postId) {
		return postService.deletePost(postId);
	}

}

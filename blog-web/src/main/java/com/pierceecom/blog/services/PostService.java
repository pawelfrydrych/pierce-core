package com.pierceecom.blog.services;

import commands.PostCommand;
import domain.Post;
import dtos.PostDto;
import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApplicationScoped
public class PostService {

	private HashMap<Long, Post> posts = new HashMap<>();
	private Long postSequence = 1L;

	public Response getAllPosts() {
		List<Post> result = new ArrayList<>(posts.values());
		return Response.ok(result).build();
	}

	public Response getPostById(Long id) {
		if (!posts.containsKey(id)) {
			return Response.status(204).type("application/json").entity(buildErrorResponse(204)).build();
		}

		return Response.ok(posts.get(id)).status(200).build();
	}

	public Response createPost(PostCommand postCommand) {
		if (postCommand.getContent() == null || postCommand.getTitle() == null) {
			return Response.status(405).type("application/json").entity(buildErrorResponse(405)).build();
		}

		Post post = new ModelMapper().map(postCommand, Post.class);
		post.setId(postSequence.toString());
		posts.put(postSequence, post);
		postSequence++;

		return Response.ok(post).status(201).build();
	}

	public Response update(PostDto postDto) {
		if (postDto.getContent() == null || postDto.getTitle() == null || postDto.getId() == null) {
			return Response.status(405).type("application/json").entity(buildErrorResponse(405)).build();
		}

		Post post = new ModelMapper().map(postDto, Post.class);
		posts.put(Long.parseLong(postDto.getId()), post);

		return Response.ok(post).status(201).build();
	}


	public Response deletePost(Long id) {
		if (!posts.containsKey(id)) {
			return Response.status(404).type("application/json").entity(buildErrorResponse(404)).build();
		}

		posts.remove(id);
		return Response.ok(posts).status(200).build();
	}

	private static String buildErrorResponse(int code) {
		switch (code) {
			case 204:
				return "{\n" +
						"  \"error\":\"No content\"\n" +
						"}";
			case 403:
				return "{\n" +
						"  \"error\":\"Forbidden!\"\n" +
						"}";
			case 404:
				return "{\n" +
						"  \"error\":\"Post not found\"\n" +
						"}";
			case 405:
				return "{\n" +
						"  \"error\":\"Invalid input\"\n" +
						"}";
		}
		return "{}";
	}

}

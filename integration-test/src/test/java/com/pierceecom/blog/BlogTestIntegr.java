package com.pierceecom.blog;

import java.util.List;

import static org.junit.Assert.*;

import domain.Post;
import dtos.PostDto;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.modelmapper.ModelMapper;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlogTestIntegr {

	private static final String API_URL = "http://localhost:8080/";
	private static final String POSTS_LIST_URI = API_URL + "blog-web/posts";
	private static final String POSTS_URI = POSTS_LIST_URI + "/";

	private static final Post POST_1 = new Post(null, "First title", "First content");
	private static final Post POST_2 = new Post(null, "Second title", "Second content");

	private Client client = ClientBuilder.newClient();

	public BlogTestIntegr() {
	}

	@Test
	public void test_1_BlogWithoutPosts() {
		Response response = getPosts();
		List posts = response.readEntity(List.class);
		assertEquals(0, posts.size());
	}

	@Test
	public void test_2_AddPost1() {
		Post post = new Post();
		post.setContent(POST_1.getContent());
		post.setTitle(POST_1.getTitle());

		Response response = createPost(post);
		Post responsePost = response.readEntity(Post.class);

		assertEquals(201, response.getStatus());
		assertNotNull(responsePost);
		assertNotNull(responsePost.getId());
		assertEquals(POST_1.getTitle(), responsePost.getTitle());
		assertEquals(POST_1.getContent(), responsePost.getContent());
	}

	@Test
	public void test_3_AddInvalidPost() {
		Post post = new Post();
		post.setContent(POST_1.getContent());

		Response response = createPost(post);
		assertEquals(405, response.getStatus());
	}

	@Test
	public void test_4_AddPost2() {
		Post post = new Post();
		post.setContent(POST_2.getContent());
		post.setTitle(POST_2.getTitle());

		Response response = createPost(post);
		Post responsePost = response.readEntity(Post.class);

		assertEquals(201, response.getStatus());
		assertNotNull(responsePost);
		assertNotNull(responsePost.getId());
		assertEquals(POST_2.getTitle(), responsePost.getTitle());
		assertEquals(POST_2.getContent(), responsePost.getContent());
	}

	@Test
	public void test_5_AddInvalidPost() {
		Post post = new Post();
		post.setTitle(POST_2.getTitle());

		Response response = createPost(post);
		assertEquals(405, response.getStatus());
	}

	@Test
	public void test_6_updatePost() {
		Response response = getOrDeletePostById(1L, "GET");
		Post post = response.readEntity(Post.class);

		PostDto postDto = new ModelMapper().map(post, PostDto.class);
		postDto.setTitle("Updated title");
		postDto.setContent("Updated content");

		Response updateResponse = updatePost(postDto);
		Post updatedPost = updateResponse.readEntity(Post.class);

		assertEquals(post.getId(), updatedPost.getId());
		assertNotEquals(post.getTitle(), updatedPost.getTitle());
		assertNotEquals(post.getContent() , updatedPost.getContent());

		updatePost(new ModelMapper().map(post, PostDto.class));
	}


	@Test
	public void test_7_GetPost() {
		Response response = getOrDeletePostById(1L, "GET");
		assertEquals(200, response.getStatus());

		Post post = response.readEntity(Post.class);

		assertNotNull(post);
		assertNotNull(post.getId());
		assertEquals(POST_1.getTitle(), post.getTitle());
		assertEquals(POST_1.getContent(), post.getContent());

		response = getOrDeletePostById(2L, "GET");
		assertEquals(200, response.getStatus());

		post = response.readEntity(Post.class);

		assertNotNull(post);
		assertNotNull(post.getId());
		assertEquals(POST_2.getTitle(), post.getTitle());
		assertEquals(POST_2.getContent(), post.getContent());

	}

	@Test
	public void test_8_GetAllPosts() {
		Response response = getPosts();
		List posts = response.readEntity(List.class);
		assertEquals(2, posts.size());
	}

	@Test
	public void test_9_DeletePosts() {
		assertEquals(200, getOrDeletePostById(1L, "DELETE").getStatus());
		assertEquals(204, getOrDeletePostById(1L, "GET").getStatus());
		assertEquals(200, getOrDeletePostById(2L, "DELETE").getStatus());
		assertEquals(204, getOrDeletePostById(2L, "GET").getStatus());
	}

	@Test
	public void test_10_GetAllPostsShouldNowBeEmpty() {
		Response response = getPosts();
		List posts = response.readEntity(List.class);
		assertEquals(0, posts.size());
	}


	private Response getOrDeletePostById(Long id, String requestType) {
		WebTarget webTarget = client
				.target(BlogTestIntegr.POSTS_URI);

		if (id != null) {
			webTarget = webTarget.path(String.valueOf(id));
		}

		Invocation.Builder builder = webTarget
				.request(MediaType.APPLICATION_JSON);

		if (requestType.equals("DELETE")) {
			return builder.delete();
		}

		return builder.get();
	}

	private Response getPosts() {
		return client
				.target(BlogTestIntegr.POSTS_LIST_URI)
				.request(MediaType.APPLICATION_JSON).get();
	}

	private Response createPost(Post post) {
		return client.
				target(POSTS_URI)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(post, MediaType.APPLICATION_JSON));
	}

	private Response updatePost(PostDto postDto) {
		return client.
				target(POSTS_URI)
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(postDto, MediaType.APPLICATION_JSON));
	}

}

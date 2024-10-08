package com.imwoo.threads.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.imwoo.threads.model.entity.UserEntity;
import com.imwoo.threads.model.post.request.PostCreateRequest;
import com.imwoo.threads.model.post.request.PostUpdateRequest;
import com.imwoo.threads.model.post.response.PostResponse;
import com.imwoo.threads.service.PostService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {

	private final PostService postService;

	@GetMapping
	public ResponseEntity<List<PostResponse>> getPosts() {
		log.info("GET /api/v1/posts");
		return ResponseEntity.ok(postService.getPosts());
	}

	@GetMapping("{postId}")
	public ResponseEntity<PostResponse> getPostByPostId(
		@PathVariable("postId") Long postId
	) {
		log.info("GET /api/v1/posts/{}", postId);
		return ResponseEntity.ok(postService.getPostByPostId(postId));
	}

	@PostMapping
	public ResponseEntity<PostResponse> createPost(
		@RequestBody PostCreateRequest postCreateRequest,
		Authentication authentication
	) {
		log.info("POST /api/v1/posts request body : {}", postCreateRequest);
		var post = postService.createPost(postCreateRequest, (UserEntity)authentication.getPrincipal());
		return ResponseEntity.ok(post);
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<PostResponse> updatePost(
		@PathVariable Long postId,
		@RequestBody PostUpdateRequest postUpdateRequest,
		Authentication authentication
	) {
		log.info("PATCH /api/v1/posts/{} request body : {}", postId, postUpdateRequest);
		var post = postService.updatePost(postId, postUpdateRequest, (UserEntity)authentication.getPrincipal());
		return ResponseEntity.ok(post);
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<Void> deletePost(
		@PathVariable Long postId,
		Authentication authentication
	) {
		log.info("DELETE /api/v1/posts/{}", postId);
		postService.deletePost(postId, (UserEntity)authentication.getPrincipal());
		// NO_CONTENT(204, HttpStatus.Series.SUCCESSFUL, "No Content")
		return ResponseEntity.noContent().build();
	}
}

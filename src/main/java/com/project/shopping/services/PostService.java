package com.project.shopping.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.project.shopping.entities.Post;
import com.project.shopping.entities.User;
import com.project.shopping.repos.PostRepository;
import com.project.shopping.requests.PostCreateRequest;
import com.project.shopping.requests.PostUpdateRequest;
import com.project.shopping.responses.ErrorResponse;
import com.project.shopping.responses.LikeResponse;
import com.project.shopping.responses.PostResponse;

@Service
public class PostService {

	private PostRepository postRepository;
	private LikeService likeService;
	private UserService userService;

	public PostService(PostRepository postRepository,
			UserService userService) {
		this.postRepository = postRepository;
		this.userService = userService;
	}

	@Autowired
	public void setLikeService(LikeService likeService) {
		this.likeService = likeService;
	}

	public List<PostResponse> getAllPosts(Optional<Long> userId) {
		List<Post> list;
		if (userId.isPresent()) {
			list = postRepository.findByUserId(userId.get());
		} else
			list = postRepository.findAll();
		return list.stream().map(p -> {
			List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null),
					Optional.of(p.getId()));
			return new PostResponse(p, likes);
		}).collect(Collectors.toList());
	}

	public Post getOnePostById(Long postId) {
		return postRepository.findById(postId).orElse(null);
	}

	public PostResponse getOnePostByIdWithLikes(Long postId) {
		Post post = postRepository.findById(postId).orElse(null);
		List<LikeResponse> likes = likeService.getAllLikesWithParam(Optional.ofNullable(null), Optional.of(postId));
		return new PostResponse(post, likes);
	}

	public ResponseEntity<?> createOnePost(PostCreateRequest newPostRequest) {
		// List<String> desiredFields = Arrays.asList("id");
		// User user = userService.getOneUserByIdWithFields(newPostRequest.getUserId(),
		// desiredFields);
		User user = userService.getOneUserById(newPostRequest.getUserId());

		if (user == null) {
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "User not found",
					"The user with the given ID does not exist.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
		}

		Post toSave = new Post();
		toSave.setId(newPostRequest.getId());
		toSave.setText(newPostRequest.getText());
		toSave.setTitle(newPostRequest.getTitle());
		toSave.setCreateDate(new Date());
		toSave.setUser(user);
		postRepository.save(toSave);
		toSave.setUser(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(toSave);
	}

	public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
		Optional<Post> post = postRepository.findById(postId);
		if (post.isPresent()) {
			Post toUpdate = post.get();
			toUpdate.setText(updatePost.getText());
			toUpdate.setTitle(updatePost.getTitle());
			postRepository.save(toUpdate);
			return toUpdate;
		}
		return null;
	}

	public void deleteOnePostById(Long postId) {
		postRepository.deleteById(postId);
	}

}

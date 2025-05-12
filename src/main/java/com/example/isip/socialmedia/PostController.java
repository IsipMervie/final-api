package com.example.isip.socialmedia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:5173") // Allow CORS requests from the frontend
public class PostController {

    private final PostRepository postRepository;

    // Constructor Injection
    @Autowired
    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // Get all posts
    @GetMapping
    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    // Create a new post
    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }

    // Bulk create posts
    @PostMapping("/bulk")
    public List<Post> createPosts(@RequestBody List<Post> posts) {
        return postRepository.saveAll(posts);
    }

    // Edit an existing post
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable Long id, @RequestBody Post postDetails) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));

        post.setAuthor(postDetails.getAuthor());
        post.setContent(postDetails.getContent());
        post.setProfilePictureUrl(postDetails.getProfilePictureUrl());

        return postRepository.save(post);
    }

    // Bulk update posts
    @PutMapping("/bulk")
    public List<Post> updatePosts(@RequestBody List<Post> postDetailsList) {
        List<Post> updatedPosts = new ArrayList<>();

        for (Post postDetails : postDetailsList) {
            Post post = postRepository.findById(postDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Post not found with ID: " + postDetails.getId()));

            post.setAuthor(postDetails.getAuthor());
            post.setContent(postDetails.getContent());
            post.setProfilePictureUrl(postDetails.getProfilePictureUrl());
            post.setLikes(postDetails.getLikes());
            post.setShares(postDetails.getShares());
            post.setComments(postDetails.getComments());

            updatedPosts.add(post);
        }

        return postRepository.saveAll(updatedPosts);
    }

    // Like a post
    @PutMapping("/{id}/like")
    public Post likePost(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setLikes(post.getLikes() + 1);
        return postRepository.save(post);
    }

    // Share a post
    @PutMapping("/{id}/share")
    public Post sharePost(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.setShares(post.getShares() + 1);
        return postRepository.save(post);
    }

    // Add a comment to a post
    @PutMapping("/{id}/comment")
    public Post commentOnPost(@PathVariable Long id, @RequestBody String comment) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        post.getComments().add(comment);
        return postRepository.save(post);
    }

    // Delete a post
    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        postRepository.delete(post);
        return "Post deleted successfully!";
    }

    // Bulk delete posts
    @DeleteMapping("/bulk")
    public String deletePosts(@RequestBody List<Long> ids) {
        List<Post> postsToDelete = postRepository.findAllById(ids);
        postRepository.deleteAll(postsToDelete);
        return "Posts deleted successfully!";
    }
}

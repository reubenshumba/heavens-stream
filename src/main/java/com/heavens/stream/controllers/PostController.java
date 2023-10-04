//package com.heavens.stream.controllers;
//
//import com.heavens.stream.dtos.MyUserDto;
//import com.heavens.stream.models.Post;
//import com.heavens.stream.response.Response;
//import com.heavens.stream.services.PostService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/v1/posts")
//public class PostController {
//
//    private final PostService postService;
//
//    @Autowired
//    public PostController(PostService postService) {
//        this.postService = postService;
//    }
//
//    @PostMapping
//    public ResponseEntity<Response<Post>> createPost(@RequestBody Post post, @AuthenticationPrincipal MyUserDto myUserDto) {
//        Long createdByUserId = myUserDto.getId();
//        Response<Post> response = postService.createPost(post, createdByUserId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @PutMapping("/{postId}")
//    public ResponseEntity<Response<Post>> updatePost(@PathVariable Long postId, @RequestBody Post updatedPost) {
//        updatedPost.setId(postId);
//        Response<Post> response = postService.updatePost(updatedPost);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @GetMapping("/{postId}")
//    public ResponseEntity<Response<Post>> getPost(@PathVariable Long postId) {
//        Response<Post> response = postService.getPostById(postId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @DeleteMapping("/{postId}")
//    public ResponseEntity<Response<String>> deletePost(@PathVariable Long postId) {
//        Response<String> response = postService.deletePostById(postId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//}

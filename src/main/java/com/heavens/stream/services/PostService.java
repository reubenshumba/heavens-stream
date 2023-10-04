//package com.heavens.stream.services;
//
//import com.heavens.stream.dtos.HeavenDto;
//import com.heavens.stream.dtos.MyUserDto;
//import com.heavens.stream.models.Heaven;
//import com.heavens.stream.models.Post;
//import com.heavens.stream.repositories.PostRepository;
//import com.heavens.stream.response.Response;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class PostService {
//
//    private final PostRepository postRepository;
//    private final HeavenService heavenService;
//    private static final Sort DATE_CREATED = Sort.by(Sort.Direction.DESC, "dateCreated");
//
//
//
//    public Response<Page<Post>> getAllPosts(int page, int size) {
//        PageRequest pageRequest = PageRequest.of(page, size, DATE_CREATED);
//        Page<Post> postPage = postRepository.findAll(pageRequest);
//        return Response.successfulResponse("Successful", postPage);
//    }
//
//
//    public Response<Page<HeavenDto>> getAllPosts(int pageSize, int page, MyUserDto authUser, boolean filterByUser) {
//        Pageable pageable = PageRequest.of(page, pageSize, DATE_CREATED);
//        Page<Heaven> allByActiveTrue = filterByUser ? heavenRepository.findDistinctByAuthoritiesInOrHeavenOwnAndActiveTrue(authUser.getAuthorities(), authUser.getId(), pageable) :
//                heavenRepository.findAllByActiveTrue(pageable);
//
//        List<HeavenDto> heavenDto = HeavenDto.toDTO(allByActiveTrue.getContent());
//        Page<HeavenDto> newPage = new PageImpl<>(heavenDto, pageable, allByActiveTrue.getTotalElements());
//        return Response.successfulResponse("Successful", newPage);
//    }
//
//    public Response<Post> getPostById(Long id) {
//        Optional<Post> optionalPost = postRepository.findById(id);
//        return optionalPost.map(post -> Response.successfulResponse("Successful", post))
//                .orElse(Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Not found"));
//    }
//
//    public Response<String> deletePostById(Long id) {
//        Optional<Post> optionalPost = postRepository.findById(id);
//        if (optionalPost.isEmpty()) {
//            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Not found");
//        }
//
//        Post post = optionalPost.get();
//        post.setActive(false);
//        postRepository.save(post);
//        return Response.successfulResponse("Successful");
//    }
//
//    public Response<Post> createPost(Post post, Long createdByUserId) {
//        // Set the createdBy user ID and set active to true
//        post.setCreatedBy(createdByUserId);
//        post.setActive(true);
//        Post savedPost = postRepository.save(post);
//        return Response.successfulResponse("Post created successfully", savedPost);
//    }
//
//    public Response<Post> updatePost(Post updatedPost) {
//        Optional<Post> optionalPost = postRepository.findById(updatedPost.getId());
//        if (optionalPost.isEmpty()) {
//            return Response.failedResponse(HttpStatus.NOT_FOUND.value(), "Post not found");
//        }
//
//        Post existingPost = optionalPost.get();
//        existingPost.setPostTitle(updatedPost.getPostTitle());
//        existingPost.setShortDescription(updatedPost.getShortDescription());
//        existingPost.setPostMessage(updatedPost.getPostMessage());
//        existingPost.setPostType(updatedPost.getPostType());
//        existingPost.setFeaturePost(updatedPost.getFeaturePost());
//        existingPost.setStringId(updatedPost.getStringId());
//        existingPost.setImageUrl(updatedPost.getImageUrl());
//
//        Post updatedPostEntity = postRepository.save(existingPost);
//        return Response.successfulResponse("Post updated successfully", updatedPostEntity);
//    }
//}

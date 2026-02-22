package com.example.hrmsbackend.controllers;

import com.example.hrmsbackend.dtos.request.PostCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.PostUpdateRequestDTO;
import com.example.hrmsbackend.dtos.response.ApiResponse;
import com.example.hrmsbackend.dtos.response.PostDetailDTO;
import com.example.hrmsbackend.dtos.response.PostDTO;
import com.example.hrmsbackend.services.PostService;
import com.example.hrmsbackend.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createPost(@Valid @ModelAttribute PostCreateRequestDTO request,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.createPost(request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> updatePost(@PathVariable Long id,
                                                          @Valid @RequestBody PostUpdateRequestDTO request,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.updatePost(id, request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.deletePost(id, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<String>> likePost(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.likePost(id, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<ApiResponse<String>> unlikePost(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.unlikePost(id, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<ApiResponse<String>> addComment(@PathVariable Long postId,
                                                          @Valid @RequestBody com.example.hrmsbackend.dtos.request.CommentCreateRequestDTO request,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.addComment(postId, request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 201));
    }

    @PutMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<String>> editComment(@PathVariable Long postId,
                                                           @PathVariable Long commentId,
                                                           @Valid @RequestBody com.example.hrmsbackend.dtos.request.CommentCreateRequestDTO request,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.editComment(postId, commentId, request, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable Long postId,
                                                             @PathVariable Long commentId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.deleteComment(postId, commentId, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<ApiResponse<String>> likeComment(@PathVariable Long postId,
                                                           @PathVariable Long commentId,
                                                           @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.likeComment(postId, commentId, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @DeleteMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<ApiResponse<String>> unlikeComment(@PathVariable Long postId,
                                                             @PathVariable Long commentId,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        String res = postService.unlikeComment(postId, commentId, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(res, res, 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDetailDTO>> getPost(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails userDetails) {
        PostDetailDTO dto = postService.getPostById(id, userDetails);
        return ResponseEntity.ok(ResponseUtil.success(dto, "Post fetched successfully", 200));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getAllPosts(@AuthenticationPrincipal UserDetails userDetails) {
        List<PostDTO> list = postService.getAllPosts(userDetails);
        return ResponseEntity.ok(ResponseUtil.success(list, "Posts fetched successfully", 200));
    }
}

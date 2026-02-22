package com.example.hrmsbackend.services;

import com.example.hrmsbackend.dtos.request.CommentCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.PostCreateRequestDTO;
import com.example.hrmsbackend.dtos.request.PostUpdateRequestDTO;
import com.example.hrmsbackend.dtos.response.PostDTO;
import com.example.hrmsbackend.dtos.response.PostDetailDTO;
import com.example.hrmsbackend.entities.*;
import com.example.hrmsbackend.exceptions.ResourceNotFoundException;
import com.example.hrmsbackend.mappers.EntityMapper;
import com.example.hrmsbackend.repos.*;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepo postRepo;
    private final TagRepo tagRepo;
    private final PostTagRepo postTagRepo;
    private final EmployeeRepo employeeRepo;
    private final CommentRepo commentRepo;
    private final MediaService mediaService;
    private final EntityMapper entityMapper;
    private final LikeRepo likeRepo;

    @Autowired
    public PostService(PostRepo postRepo, TagRepo tagRepo, PostTagRepo postTagRepo, EmployeeRepo employeeRepo, CommentRepo commentRepo, MediaService mediaService, EntityMapper entityMapper, LikeRepo likeRepo) {
        this.postRepo = postRepo;
        this.tagRepo = tagRepo;
        this.postTagRepo = postTagRepo;
        this.employeeRepo = employeeRepo;
        this.commentRepo = commentRepo;
        this.mediaService = mediaService;
        this.entityMapper = entityMapper;
        this.likeRepo = likeRepo;
    }

    @Transactional
    public String createPost(PostCreateRequestDTO request, UserDetails userDetails) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Title is required");
        }

        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new RuntimeException("Description is required");
        }

        Employee author = getCurrentEmployee(userDetails);

        Media media = saveMedia(request, userDetails);

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setDescription(request.getDescription());
        post.setAuthor(author);
        post.setMedia(media);

        Post saved = postRepo.save(post);

        saveTags(request, saved);

        return "Post created successfully";
    }

    private void saveTags(PostCreateRequestDTO request, Post saved) {
        List<PostTag> postTags = new ArrayList<>();
        if (request.getTags() != null) {
            for (String tagName : request.getTags()) {
                Tag tag = tagRepo.findByTag(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setTag(tagName);
                    tagRepo.save(tag);
                }
                PostTag pt = new PostTag();
                pt.setPost(saved);
                pt.setTag(tag);
                postTags.add(pt);
            }
            postTagRepo.saveAll(postTags);
            saved.setPostTags(postTags);
        }
    }

    private @Nullable Media saveMedia(PostCreateRequestDTO request, UserDetails userDetails) {
        MultipartFile mediaFile = request.getMedia();
        Media media = null;
        if (mediaFile != null && !mediaFile.isEmpty()) {
            media = mediaService.upload(mediaFile, "post", userDetails);
        }
        return media;
    }

    @Transactional
    public String updatePost(Long postId, PostUpdateRequestDTO request, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        if (post.getIsDeleted()) {
            throw new RuntimeException("Cannot update deleted post");
        }

        Employee current = getCurrentEmployee(userDetails);
        if (!post.getAuthor().getId().equals(current.getId())) {
            throw new RuntimeException("Unauthorized: only author can update the post");
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            post.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            post.setDescription(request.getDescription());
        }

        updateTags(request.getTags(), post);

        postRepo.save(post);
        return "Post updated successfully";
    }

    @Transactional
    private void updateTags(List<String> tags, Post post) {
        // remove existing mappings in a single bulk operation to avoid constraint/flush issues
        if (post.getId() != null) {
            postTagRepo.deleteByPostId(post.getId());
        }

        List<PostTag> newPostTags = new ArrayList<>();
        if (tags != null) {
            for (String tagName : tags) {
                Tag tag = tagRepo.findByTag(tagName);
                if (tag == null) {
                    tag = new Tag();
                    tag.setTag(tagName);
                    tagRepo.save(tag);
                }
                PostTag pt = new PostTag();
                pt.setPost(post);
                pt.setTag(tag);
                newPostTags.add(pt);
            }
            postTagRepo.saveAll(newPostTags);
            post.setPostTags(newPostTags);
        } else {
            post.setPostTags(newPostTags);
        }
    }

    @Transactional
    public String deletePost(Long postId, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        if (post.getIsDeleted()) {
            throw new RuntimeException("Post already deleted");
        }

        Employee current = getCurrentEmployee(userDetails);
        if (!post.getAuthor().getId().equals(current.getId())) {
            throw new RuntimeException("Unauthorized: only author can delete the post");
        }

        post.setIsDeleted(true);
        post.setDeletedBy(current);
        postRepo.save(post);
        return "Post deleted successfully";
    }

    @Transactional
    public String likePost(Long postId, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");
        if (post.getIsDeleted()) throw new RuntimeException("Cannot like deleted post");

        Employee current = getCurrentEmployee(userDetails);
        if (likeRepo.existsByPostIdAndEmployeeId(postId, current.getId())) {
            return "Post already liked";
        }

        Like like = new Like();
        like.setPost(post);
        like.setEmployee(current);
        likeRepo.save(like);
        return "Post liked successfully";
    }

    @Transactional
    public String unlikePost(Long postId, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        Employee current = getCurrentEmployee(userDetails);
        if (!likeRepo.existsByPostIdAndEmployeeId(postId, current.getId())) {
            return "Like not found";
        }

        likeRepo.deleteByPostIdAndEmployeeId(postId, current.getId());
        return "Like removed successfully";
    }

    @Transactional
    public String addComment(Long postId, CommentCreateRequestDTO request, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");
        if (post.getIsDeleted()) throw new RuntimeException("Cannot comment on deleted post");

        if (request.getText() == null || request.getText().isBlank()) {
            throw new RuntimeException("Comment text is required");
        }

        Employee current = getCurrentEmployee(userDetails);

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setPost(post);
        comment.setEmployee(current);
        if (request.getParentCommentId() != null) {
            Comment parent = commentRepo.findById(request.getParentCommentId()).orElse(null);
            if (parent == null) throw new ResourceNotFoundException("Parent comment not found");
            comment.setParentComment(parent);
            comment.setPost(null);
        }

        commentRepo.save(comment);
        return "Comment added successfully";
    }

    @Transactional
    public String editComment(Long postId, Long commentId, CommentCreateRequestDTO request, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (comment == null) throw new ResourceNotFoundException("Comment not found");

        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("Comment does not belong to post");
        }

        Employee current = getCurrentEmployee(userDetails);
        if (!comment.getEmployee().getId().equals(current.getId())) {
            throw new RuntimeException("Unauthorized: only author can edit the comment");
        }

        if (request.getText() == null || request.getText().isBlank()) {
            throw new RuntimeException("Comment text is required");
        }

        comment.setText(request.getText());
        commentRepo.save(comment);
        return "Comment updated successfully";
    }

    @Transactional
    public String deleteComment(Long postId, Long commentId, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (comment == null) throw new ResourceNotFoundException("Comment not found");

        if (!comment.getPost().getId().equals(postId)) {
            throw new RuntimeException("Comment does not belong to post");
        }

        Employee current = getCurrentEmployee(userDetails);
        if (!comment.getEmployee().getId().equals(current.getId())) {
            throw new RuntimeException("Unauthorized: only author can delete the comment");
        }

        commentRepo.delete(comment);
        return "Comment deleted successfully";
    }

    @Transactional
    public String likeComment(Long postId, Long commentId, UserDetails userDetails) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (comment == null) throw new ResourceNotFoundException("Comment not found");

        Employee current = getCurrentEmployee(userDetails);
        if (likeRepo.existsByCommentIdAndEmployeeId(commentId, current.getId())) {
            return "Comment already liked";
        }

        Like like = new Like();
        like.setComment(comment);
        like.setEmployee(current);
        likeRepo.save(like);
        return "Comment liked successfully";
    }

    @Transactional
    public String unlikeComment(Long postId, Long commentId, UserDetails userDetails) {
        Post post = postRepo.findById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");

        Comment comment = commentRepo.findById(commentId).orElse(null);
        if (comment == null) throw new ResourceNotFoundException("Comment not found");

        Employee current = getCurrentEmployee(userDetails);
        if (!likeRepo.existsByCommentIdAndEmployeeId(commentId, current.getId())) {
            return "Like not found";
        }

        likeRepo.deleteByCommentIdAndEmployeeId(commentId, current.getId());
        return "Comment like removed successfully";
    }

    public PostDetailDTO getPostById(Long postId, UserDetails userDetails) {
        Post post = postRepo.findActiveById(postId).orElse(null);
        if (post == null) throw new ResourceNotFoundException("Post not found");
        if (post.getIsDeleted()) throw new RuntimeException("Post is deleted");
        PostDetailDTO dto = entityMapper.toPostDetailDTO(post);
        if (userDetails != null) {
            Employee current = getCurrentEmployee(userDetails);
            dto.setIsLiked(likeRepo.existsByPostIdAndEmployeeId(postId, current.getId()));
        } else {
            dto.setIsLiked(false);
        }
        return dto;
    }

    public List<PostDTO> getAllPosts(UserDetails userDetails) {
        List<Post> posts = postRepo.findAllActiveOrderByCreatedAtDesc();
        List<PostDTO> dtos = new ArrayList<>();
        for (Post post : posts) {
            PostDTO dto = entityMapper.toPostDTO(post);
            if (userDetails != null) {
                Employee current = getCurrentEmployee(userDetails);
                dto.setIsLiked(likeRepo.existsByPostIdAndEmployeeId(post.getId(), current.getId()));
            } else {
                dto.setIsLiked(false);
            }
            dtos.add(dto);
        }
        return dtos;
    }

    private Employee getCurrentEmployee(UserDetails userDetails) {
        Employee emp = employeeRepo.findByEmail(userDetails.getUsername());
        if (emp == null) throw new ResourceNotFoundException("Employee not found");
        return emp;
    }
}

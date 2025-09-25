//package windeath44.server.memorial.domain.service;
//
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//import windeath44.server.memorial.domain.memorial.model.Memorial;
//import windeath44.server.memorial.domain.memorial.model.MemorialComment;
//import windeath44.server.memorial.domain.memorial.model.MemorialCommentLikesPrimaryKey;
//import windeath44.server.memorial.domain.memorial.repository.MemorialCommentLikesRepository;
//import windeath44.server.memorial.domain.memorial.repository.MemorialCommentRepository;
//import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
//import windeath44.server.memorial.domain.memorial.service.MemorialCommentLikesService;
//import windeath44.server.memorial.domain.memorial.service.MemorialCommentService;
//
//import java.util.HashSet;
//import java.util.Set;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//public class MemorialCommentLikesServiceTest {
//
//    @Autowired
//    private MemorialCommentLikesService memorialCommentLikesService;
//
//    @Autowired
//    private MemorialCommentService memorialCommentService;
//
//    @Autowired
//    private MemorialRepository memorialRepository;
//
//    @Autowired
//    private MemorialCommentRepository memorialCommentRepository;
//
//    @Autowired
//    private MemorialCommentLikesRepository memorialCommentLikesRepository;
//
//    @Test
//    void likeCommentSuccess() {
//        // Setup
//        Memorial memorial = new Memorial(1L);
//        memorialRepository.save(memorial);
//
//        MemorialComment comment = MemorialComment.of(memorial, "user1", "Test comment", null);
//        memorialCommentRepository.save(comment);
//
//        Long commentId = comment.getCommentId();
//        String userId = "user2";
//
//        // Initial likes count
//        Long initialLikes = comment.getLikesCount();
//
//        // Execute
//        memorialCommentLikesService.like(commentId, userId);
//
//        // Verify
//        MemorialComment updatedComment = memorialCommentService.findCommentById(commentId);
//        assertEquals(initialLikes + 1, updatedComment.getLikesCount(), "Likes count should be incremented by 1");
//
//        MemorialCommentLikesPrimaryKey key = updatedComment.likesKey(userId);
//        assertTrue(memorialCommentLikesRepository.existsById(key), "Like record should exist in repository");
//    }
//
//    @Test
//    void likeCommentTwiceNoEffect() {
//        // Setup
//        Memorial memorial = new Memorial(1L);
//        memorialRepository.save(memorial);
//
//        MemorialComment comment = MemorialComment.of(memorial, "user1", "Test comment", null);
//        memorialCommentRepository.save(comment);
//
//        Long commentId = comment.getCommentId();
//        String userId = "user2";
//
//        // Like once
//        memorialCommentLikesService.like(commentId, userId);
//        MemorialComment commentAfterFirstLike = memorialCommentService.findCommentById(commentId);
//        Long likesAfterFirstLike = commentAfterFirstLike.getLikesCount();
//
//        // Like again
//        memorialCommentLikesService.like(commentId, userId);
//
//        // Verify
//        MemorialComment commentAfterSecondLike = memorialCommentService.findCommentById(commentId);
//        assertEquals(likesAfterFirstLike, commentAfterSecondLike.getLikesCount(), "Likes count should not change when liking twice");
//    }
//
//    @Test
//    void unlikeCommentSuccess() {
//        // Setup
//        Memorial memorial = new Memorial(1L);
//        memorialRepository.save(memorial);
//
//        MemorialComment comment = MemorialComment.of(memorial, "user1", "Test comment", null);
//        memorialCommentRepository.save(comment);
//
//        Long commentId = comment.getCommentId();
//        String userId = "user2";
//
//        // Like first
//        memorialCommentLikesService.like(commentId, userId);
//        MemorialComment commentAfterLike = memorialCommentService.findCommentById(commentId);
//        Long likesAfterLike = commentAfterLike.getLikesCount();
//
//        // Unlike
//        memorialCommentLikesService.unlike(commentId, userId);
//
//        // Verify
//        MemorialComment commentAfterUnlike = memorialCommentService.findCommentById(commentId);
//        assertEquals(likesAfterLike - 1, commentAfterUnlike.getLikesCount(), "Likes count should be decremented by 1");
//
//        MemorialCommentLikesPrimaryKey key = commentAfterUnlike.likesKey(userId);
//        assertFalse(memorialCommentLikesRepository.existsById(key), "Like record should not exist in repository after unlike");
//    }
//
//    @Test
//    void unlikeNonExistingLikeNoEffect() {
//        // Setup
//        Memorial memorial = new Memorial(1L);
//        memorialRepository.save(memorial);
//
//        MemorialComment comment = MemorialComment.of(memorial, "user1", "Test comment", null);
//        memorialCommentRepository.save(comment);
//
//        Long commentId = comment.getCommentId();
//        String userId = "user2";
//
//        // Initial likes count
//        Long initialLikes = comment.getLikesCount();
//
//        // Unlike without liking first
//        memorialCommentLikesService.unlike(commentId, userId);
//
//        // Verify
//        MemorialComment commentAfterUnlike = memorialCommentService.findCommentById(commentId);
//        assertEquals(initialLikes, commentAfterUnlike.getLikesCount(), "Likes count should not change when unliking a non-existing like");
//    }
//
//    @Test
//    void getLikedCommentIdsSuccess() {
//        // Setup
//        Memorial memorial = new Memorial(1L);
//        memorialRepository.save(memorial);
//
//        // Create multiple comments
//        MemorialComment comment1 = MemorialComment.of(memorial, "user1", "Test comment 1", null);
//        memorialCommentRepository.save(comment1);
//
//        MemorialComment comment2 = MemorialComment.of(memorial, "user1", "Test comment 2", null);
//        memorialCommentRepository.save(comment2);
//
//        MemorialComment comment3 = MemorialComment.of(memorial, "user1", "Test comment 3", null);
//        memorialCommentRepository.save(comment3);
//
//        String userId = "user2";
//
//        // Like comment1 and comment3
//        memorialCommentLikesService.like(comment1.getCommentId(), userId);
//        memorialCommentLikesService.like(comment3.getCommentId(), userId);
//
//        // Create set of all comment IDs
//        Set<Long> allCommentIds = new HashSet<>();
//        allCommentIds.add(comment1.getCommentId());
//        allCommentIds.add(comment2.getCommentId());
//        allCommentIds.add(comment3.getCommentId());
//
//        // Execute
//        Set<Long> likedCommentIds = memorialCommentLikesService.getLikedCommentIds(userId, allCommentIds);
//
//        // Verify
//        assertEquals(2, likedCommentIds.size(), "Should return 2 liked comment IDs");
//        assertTrue(likedCommentIds.contains(comment1.getCommentId()), "Should contain comment1 ID");
//        assertTrue(likedCommentIds.contains(comment3.getCommentId()), "Should contain comment3 ID");
//        assertFalse(likedCommentIds.contains(comment2.getCommentId()), "Should not contain comment2 ID");
//    }
//
//    @Test
//    void getLikedCommentIdsWithEmptySet() {
//        // Setup
//        String userId = "user1";
//
//        // Execute with empty set
//        Set<Long> likedCommentIds = memorialCommentLikesService.getLikedCommentIds(userId, new HashSet<>());
//
//        // Verify
//        assertTrue(likedCommentIds.isEmpty(), "Should return empty set when input is empty");
//    }
//
//    @Test
//    void getLikedCommentIdsWithNullSet() {
//        // Setup
//        String userId = "user1";
//
//        // Execute with null set
//        Set<Long> likedCommentIds = memorialCommentLikesService.getLikedCommentIds(userId, null);
//
//        // Verify
//        assertTrue(likedCommentIds.isEmpty(), "Should return empty set when input is null");
//    }
//}

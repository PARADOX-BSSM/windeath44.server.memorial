package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Slice;
import org.springframework.test.context.ActiveProfiles;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommentRequestDto;
import windeath44.server.memorial.domain.memorial.dto.request.MemorialCommentUpdateRequestDto;
import windeath44.server.memorial.domain.memorial.exception.MemorialCommentNotFoundException;
import windeath44.server.memorial.domain.memorial.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.memorial.model.Memorial;
import windeath44.server.memorial.domain.memorial.model.MemorialComment;
import windeath44.server.memorial.domain.memorial.repository.MemorialCommentRepository;
import windeath44.server.memorial.domain.memorial.repository.MemorialRepository;
import windeath44.server.memorial.domain.memorial.service.MemorialCommentService;
import windeath44.server.memorial.domain.memorial.service.MemorialGetService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialCommentServiceTest {

    @Autowired
    private MemorialCommentService memorialCommentService;

    @Autowired
    private MemorialGetService memorialGetService;

    @Autowired
    private MemorialRepository memorialRepository;

    @Autowired
    private MemorialCommentRepository memorialCommentRepository;

    @Test
    void commentSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        String userId = "user1";
        String content = "Test comment";
        MemorialCommentRequestDto dto = new MemorialCommentRequestDto(content, null);

        // Execute
        memorialCommentService.comment(dto, userId, memorial.getMemorialId());

        // Verify
        List<MemorialComment> comments = memorialCommentRepository.findAll();
        assertEquals(1, comments.size(), "Should create one comment");
        MemorialComment savedComment = comments.get(0);
        assertEquals(content, savedComment.getContent(), "Content should match");
        assertEquals(userId, savedComment.getUserId(), "User ID should match");
        assertEquals(memorial.getMemorialId(), savedComment.getMemorialId(), "Memorial ID should match");
        assertNull(savedComment.getParentComment(), "Parent comment should be null");
    }

    @Test
    void commentWithParentSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        // Create parent comment
        MemorialComment parentComment = MemorialComment.of(memorial, "user1", "Parent comment", null);
        memorialCommentRepository.save(parentComment);

        String userId = "user2";
        String content = "Reply comment";
        MemorialCommentRequestDto dto = new MemorialCommentRequestDto(content, parentComment.getCommentId());

        // Execute
        memorialCommentService.comment(dto, userId, memorial.getMemorialId());

        // Verify
        List<MemorialComment> comments = memorialCommentRepository.findAll();
        assertEquals(2, comments.size(), "Should have two comments");
        
        Optional<MemorialComment> replyComment = comments.stream()
                .filter(c -> c.getCommentId() != parentComment.getCommentId())
                .findFirst();
        
        assertTrue(replyComment.isPresent(), "Reply comment should exist");
        assertEquals(content, replyComment.get().getContent(), "Content should match");
        assertEquals(userId, replyComment.get().getUserId(), "User ID should match");
        assertEquals(memorial.getMemorialId(), replyComment.get().getMemorialId(), "Memorial ID should match");
        assertNotNull(replyComment.get().getParentComment(), "Parent comment should not be null");
        assertEquals(parentComment.getCommentId(), replyComment.get().getParentCommentId(), "Parent comment ID should match");
    }

    @Test
    void commentWithNonExistingMemorialFails() {
        // Setup
        String userId = "user1";
        String content = "Test comment";
        MemorialCommentRequestDto dto = new MemorialCommentRequestDto(content, null);
        Long nonExistingMemorialId = 999L;

        // Execute & Verify
        assertThrows(MemorialNotFoundException.class, () -> 
            memorialCommentService.comment(dto, userId, nonExistingMemorialId),
            "Should throw MemorialNotFoundException for non-existing memorial"
        );
    }

    @Test
    void commentWithNonExistingParentCommentFails() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        String userId = "user1";
        String content = "Test comment";
        Long nonExistingParentId = 999L;
        MemorialCommentRequestDto dto = new MemorialCommentRequestDto(content, nonExistingParentId);

        // Execute & Verify
        assertThrows(MemorialCommentNotFoundException.class, () -> 
            memorialCommentService.comment(dto, userId, memorial.getMemorialId()),
            "Should throw MemorialCommentNotFoundException for non-existing parent comment"
        );
    }

    @Test
    void rewriteCommentSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        MemorialComment comment = MemorialComment.of(memorial, "user1", "Original content", null);
        memorialCommentRepository.save(comment);

        String newContent = "Updated content";
        MemorialCommentUpdateRequestDto dto = new MemorialCommentUpdateRequestDto(newContent);

        // Execute
        memorialCommentService.rewrite(comment.getCommentId(), dto);

        // Verify
        MemorialComment updatedComment = memorialCommentRepository.findById(comment.getCommentId())
                .orElseThrow();
        assertEquals(newContent, updatedComment.getContent(), "Content should be updated");
    }

    @Test
    void rewriteNonExistingCommentFails() {
        // Setup
        Long nonExistingCommentId = 999L;
        String newContent = "Updated content";
        MemorialCommentUpdateRequestDto dto = new MemorialCommentUpdateRequestDto(newContent);

        // Execute & Verify
        assertThrows(MemorialCommentNotFoundException.class, () -> 
            memorialCommentService.rewrite(nonExistingCommentId, dto),
            "Should throw MemorialCommentNotFoundException for non-existing comment"
        );
    }

    @Test
    void deleteCommentSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        MemorialComment comment = MemorialComment.of(memorial, "user1", "Test comment", null);
        memorialCommentRepository.save(comment);
        Long commentId = comment.getCommentId();

        // Execute
        memorialCommentService.delete(commentId);

        // Verify
        Optional<MemorialComment> deletedComment = memorialCommentRepository.findById(commentId);
        assertTrue(deletedComment.isEmpty(), "Comment should be deleted");
    }

    @Test
    void deleteNonExistingCommentFails() {
        // Setup
        Long nonExistingCommentId = 999L;

        // Execute & Verify
        assertThrows(MemorialCommentNotFoundException.class, () -> 
            memorialCommentService.delete(nonExistingCommentId),
            "Should throw MemorialCommentNotFoundException for non-existing comment"
        );
    }

    @Test
    void findCommentByIdSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        MemorialComment comment = MemorialComment.of(memorial, "user1", "Test comment", null);
        memorialCommentRepository.save(comment);
        Long commentId = comment.getCommentId();

        // Execute
        MemorialComment foundComment = memorialCommentService.findCommentById(commentId);

        // Verify
        assertNotNull(foundComment, "Should find the comment");
        assertEquals(commentId, foundComment.getCommentId(), "Comment ID should match");
        assertEquals("Test comment", foundComment.getContent(), "Content should match");
    }

    @Test
    void findNonExistingCommentByIdFails() {
        // Setup
        Long nonExistingCommentId = 999L;

        // Execute & Verify
        assertThrows(MemorialCommentNotFoundException.class, () -> 
            memorialCommentService.findCommentById(nonExistingCommentId),
            "Should throw MemorialCommentNotFoundException for non-existing comment"
        );
    }

    @Test
    void getRootCommentSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        // Create root comments
        MemorialComment rootComment1 = MemorialComment.of(memorial, "user1", "Root comment 1", null);
        memorialCommentRepository.save(rootComment1);

        MemorialComment rootComment2 = MemorialComment.of(memorial, "user1", "Root comment 2", null);
        memorialCommentRepository.save(rootComment2);

        // Create child comment
        MemorialComment childComment = MemorialComment.of(memorial, "user2", "Child comment", rootComment1);
        memorialCommentRepository.save(childComment);

        // Execute
        Slice<MemorialComment> rootComments = memorialCommentService.getRootComment(memorial.getMemorialId(), null, 10);

        // Verify
        assertNotNull(rootComments, "Should return a slice of root comments");
        assertEquals(2, rootComments.getContent().size(), "Should return 2 root comments");
        assertTrue(rootComments.getContent().stream()
                .allMatch(c -> c.getParentComment() == null), 
                "All returned comments should be root comments");
    }

    @Test
    void connectChildSuccess() {
        // Setup
        Memorial memorial = new Memorial(1L);
        memorialRepository.save(memorial);

        // Create root comments
        MemorialComment rootComment1 = MemorialComment.of(memorial, "user1", "Root comment 1", null);
        memorialCommentRepository.save(rootComment1);

        MemorialComment rootComment2 = MemorialComment.of(memorial, "user1", "Root comment 2", null);
        memorialCommentRepository.save(rootComment2);

        // Create child comments
        MemorialComment childComment1 = MemorialComment.of(memorial, "user2", "Child comment 1", rootComment1);
        memorialCommentRepository.save(childComment1);

        MemorialComment childComment2 = MemorialComment.of(memorial, "user2", "Child comment 2", rootComment1);
        memorialCommentRepository.save(childComment2);

        MemorialComment childComment3 = MemorialComment.of(memorial, "user2", "Child comment 3", rootComment2);
        memorialCommentRepository.save(childComment3);

        List<MemorialComment> rootComments = List.of(rootComment1, rootComment2);
        List<Long> rootIds = List.of(rootComment1.getCommentId(), rootComment2.getCommentId());

        // Execute
        memorialCommentService.connectChild(rootComments, rootIds);

        // Verify
        assertEquals(2, rootComment1.getChildren().size(), "Root comment 1 should have 2 children");
        assertEquals(1, rootComment2.getChildren().size(), "Root comment 2 should have 1 child");
    }
}
//package windeath44.server.memorial.domain.service;
//
//import jakarta.transaction.Transactional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.ActiveProfiles;
//import windeath44.server.memorial.domain.dto.response.MemorialTracingResponse;
//import windeath44.server.memorial.domain.model.MemorialTracing;
//import windeath44.server.memorial.domain.model.event.MemorialTracingEvent;
//import windeath44.server.memorial.domain.repository.MemorialTracingRepository;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//@Transactional
//public class MemorialTraceServiceTest {
//
//    @Autowired
//    private MemorialTraceService memorialTraceService;
//
//    @Autowired
//    private MemorialTracingRepository memorialTracingRepository;
//
//    @Test
//    void memorialTracingEventHandlerSavesTracing() {
//        // Setup
//        Long memorialId = 1L;
//        String userId = "user1";
//        MemorialTracingEvent event = new MemorialTracingEvent(memorialId, userId);
//
//        // Execute
//        memorialTraceService.memorialTracing(event);
//
//        // Verify
//        List<MemorialTracing> tracings = memorialTracingRepository.findAll();
//        assertFalse(tracings.isEmpty(), "Should save a tracing record");
//
//        MemorialTracing savedTracing = tracings.get(0);
//        assertEquals(memorialId, savedTracing.getMemorialId(), "Memorial ID should match");
//        assertEquals(userId, savedTracing.getUserId(), "User ID should match");
//    }
//
//    @Test
//    void findRecentByUserIdReturnsTracingResponses() {
//        // Setup
//        String userId = "user1";
//        Integer size = 5;
//
//        // Create some tracing records
//        MemorialTracing tracing1 = MemorialTracing.of(1L, userId);
//        MemorialTracing tracing2 = MemorialTracing.of(2L, userId);
//        MemorialTracing tracing3 = MemorialTracing.of(3L, userId);
//        memorialTracingRepository.save(tracing1);
//        memorialTracingRepository.save(tracing2);
//        memorialTracingRepository.save(tracing3);
//
//        // Execute
//        List<MemorialTracingResponse> responses = memorialTraceService.findRecentByUserId(userId, size);
//
//        // Verify
//        assertNotNull(responses, "Should return a list of responses");
//        assertEquals(3, responses.size(), "Should return 3 responses");
//    }
//
//    @Test
//    void findRecentByUserIdWithNoTracingsReturnsEmptyList() {
//        // Setup
//        String userId = "nonexistentUser";
//        Integer size = 5;
//
//        // Execute
//        List<MemorialTracingResponse> responses = memorialTraceService.findRecentByUserId(userId, size);
//
//        // Verify
//        assertNotNull(responses, "Should return a list of responses");
//        assertTrue(responses.isEmpty(), "List should be empty for user with no tracings");
//    }
//}
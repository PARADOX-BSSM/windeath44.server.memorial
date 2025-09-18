package windeath44.server.memorial.domain.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import windeath44.server.memorial.domain.dto.response.MemorialListResponseDto;
import windeath44.server.memorial.domain.dto.response.MemorialResponseDto;
import windeath44.server.memorial.domain.exception.MemorialNotFoundException;
import windeath44.server.memorial.domain.exception.UndefinedOrderByException;
import windeath44.server.memorial.domain.model.Memorial;
import windeath44.server.memorial.domain.model.event.MemorialTracingEvent;
import windeath44.server.memorial.domain.repository.MemorialRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MemorialGetServiceTest {

    @Autowired
    private MemorialGetService memorialGetService;

    @Autowired
    private MemorialRepository memorialRepository;

    @Mock
    private ApplicationEventPublisher mockEventPublisher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Inject the mock event publisher into the service
        ReflectionTestUtils.setField(memorialGetService, "applicationEventPublisher", mockEventPublisher);
    }

    @Test
    void findMemorialByIdSuccess() {
        // Setup
        Long memorialId = 1L;
        Memorial memorial = new Memorial(memorialId);
        memorialRepository.save(memorial);
        
        // Create a mock response DTO
        MemorialResponseDto expectedDto = new MemorialResponseDto(
            memorialId, 1L, Collections.emptyList(), 0L, 1L, 
            "Test content", "user1", LocalDateTime.now(), "merger1", LocalDateTime.now()
        );
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialById(memorialId)).thenReturn(expectedDto);
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute
        MemorialResponseDto result = memorialGetService.findMemorialById(memorialId, "user1");
        
        // Verify
        assertNotNull(result, "Should return a MemorialResponseDto");
        verify(mockEventPublisher, times(1)).publishEvent(any(MemorialTracingEvent.class));
    }
    
    @Test
    void findMemorialByIdWithNullUserIdDoesNotPublishEvent() {
        // Setup
        Long memorialId = 1L;
        Memorial memorial = new Memorial(memorialId);
        memorialRepository.save(memorial);
        
        // Create a mock response DTO
        MemorialResponseDto expectedDto = new MemorialResponseDto(
            memorialId, 1L, Collections.emptyList(), 0L, 1L, 
            "Test content", "user1", LocalDateTime.now(), "merger1", LocalDateTime.now()
        );
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialById(memorialId)).thenReturn(expectedDto);
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute
        MemorialResponseDto result = memorialGetService.findMemorialById(memorialId, null);
        
        // Verify
        assertNotNull(result, "Should return a MemorialResponseDto");
        verify(mockEventPublisher, never()).publishEvent(any(MemorialTracingEvent.class));
    }
    
    @Test
    void findMemorialByIdNotFound() {
        // Setup
        Long nonExistingMemorialId = 999L;
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialById(nonExistingMemorialId)).thenReturn(null);
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute & Verify
        assertThrows(MemorialNotFoundException.class, () -> 
            memorialGetService.findMemorialById(nonExistingMemorialId, "user1"),
            "Should throw MemorialNotFoundException for non-existing memorial"
        );
    }
    
    @Test
    void findMemorialsSuccess() {
        // Setup
        String orderBy = "recently-updated";
        Long page = 0L;
        
        // Create mock response DTOs
        List<MemorialListResponseDto> expectedDtos = Arrays.asList(
            new MemorialListResponseDto(1L, 1L),
            new MemorialListResponseDto(2L, 2L)
        );
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialsOrderByAndPage(orderBy, page, 10L)).thenReturn(expectedDtos);
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute
        List<MemorialListResponseDto> result = memorialGetService.findMemorials(orderBy, page);
        
        // Verify
        assertNotNull(result, "Should return a list of MemorialListResponseDto");
        assertEquals(2, result.size(), "Should return 2 memorials");
    }
    
    @Test
    void findMemorialsEmptyResult() {
        // Setup
        String orderBy = "recently-updated";
        Long page = 0L;
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialsOrderByAndPage(orderBy, page, 10L)).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute & Verify
        assertThrows(MemorialNotFoundException.class, () -> 
            memorialGetService.findMemorials(orderBy, page),
            "Should throw MemorialNotFoundException when result is empty"
        );
    }
    
    @Test
    void findMemorialsNullOrderBy() {
        // Setup
        String orderBy = null;
        Long page = 0L;
        
        // Execute & Verify
        assertThrows(UndefinedOrderByException.class, () -> 
            memorialGetService.findMemorials(orderBy, page),
            "Should throw UndefinedOrderByException when orderBy is null"
        );
    }
    
    @Test
    void findMemorialsEmptyOrderBy() {
        // Setup
        String orderBy = "";
        Long page = 0L;
        
        // Execute & Verify
        assertThrows(UndefinedOrderByException.class, () -> 
            memorialGetService.findMemorials(orderBy, page),
            "Should throw UndefinedOrderByException when orderBy is empty"
        );
    }
    
    @Test
    void findMemorialsInvalidOrderBy() {
        // Setup
        String orderBy = "invalid-order";
        Long page = 0L;
        
        // Execute & Verify
        assertThrows(UndefinedOrderByException.class, () -> 
            memorialGetService.findMemorials(orderBy, page),
            "Should throw UndefinedOrderByException when orderBy is invalid"
        );
    }
    
    @Test
    void findMemorialsFilteredSuccess() {
        // Setup
        String orderBy = "recently-updated";
        Long page = 0L;
        List<Long> characters = Arrays.asList(1L, 2L);
        
        // Create mock response DTOs
        List<MemorialListResponseDto> expectedDtos = Arrays.asList(
            new MemorialListResponseDto(1L, 1L),
            new MemorialListResponseDto(2L, 2L)
        );
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialsOrderByAndPageCharacterFiltered(orderBy, page, 10L, characters)).thenReturn(expectedDtos);
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute
        List<MemorialListResponseDto> result = memorialGetService.findMemorialsFiltered(orderBy, page, characters);
        
        // Verify
        assertNotNull(result, "Should return a list of MemorialListResponseDto");
        assertEquals(2, result.size(), "Should return 2 memorials");
    }
    
    @Test
    void findMemorialsFilteredEmptyResult() {
        // Setup
        String orderBy = "recently-updated";
        Long page = 0L;
        List<Long> characters = Arrays.asList(1L, 2L);
        
        // Mock the repository method
        MemorialRepository spyRepository = spy(memorialRepository);
        when(spyRepository.findMemorialsOrderByAndPageCharacterFiltered(orderBy, page, 10L, characters)).thenReturn(Collections.emptyList());
        ReflectionTestUtils.setField(memorialGetService, "memorialRepository", spyRepository);
        
        // Execute & Verify
        assertThrows(MemorialNotFoundException.class, () -> 
            memorialGetService.findMemorialsFiltered(orderBy, page, characters),
            "Should throw MemorialNotFoundException when result is empty"
        );
    }
    
    @Test
    void findByIdSuccess() {
        // Setup
        Long memorialId = 1L;
        Memorial memorial = new Memorial(memorialId);
        memorialRepository.save(memorial);
        
        // Execute
        Memorial result = memorialGetService.findById(memorialId);
        
        // Verify
        assertNotNull(result, "Should return a Memorial");
        assertEquals(memorialId, result.getMemorialId(), "Memorial ID should match");
    }
    
    @Test
    void findByIdNotFound() {
        // Setup
        Long nonExistingMemorialId = 999L;
        
        // Execute & Verify
        assertThrows(MemorialNotFoundException.class, () -> 
            memorialGetService.findById(nonExistingMemorialId),
            "Should throw MemorialNotFoundException for non-existing memorial"
        );
    }
}
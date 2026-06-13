package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.model.api.Activity;
import com.ninjaone.dundie_awards.model.entity.ActivityEntity;
import com.ninjaone.dundie_awards.model.mapper.ActivityMapper;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private ActivityMapper activityMapper;

    private ActivityService activityService;

    @BeforeEach
    void setUp() {
        activityService = new ActivityService(activityRepository, activityMapper);
    }

    @Test
    void findAll_mapsEntityPageToDtoPage() {
        ActivityEntity entity = ActivityEntity.builder().id(UUID.randomUUID()).event("Dundie Award").occurredAt(LocalDateTime.now()).build();
        Activity dto = Activity.builder().id(entity.getId()).event("Dundie Award").occurredAt(entity.getOccurredAt()).build();
        Pageable pageable = PageRequest.of(0, 25);
        Page<ActivityEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(activityRepository.findAll(pageable)).thenReturn(entityPage);
        when(activityMapper.toDto(entity)).thenReturn(dto);

        Page<Activity> result = activityService.findAll(pageable);

        assertThat(result.getContent()).containsExactly(dto);
        verify(activityRepository).findAll(pageable);
    }

    @Test
    void findById_existingId_returnsMappedActivity() {
        UUID id = UUID.randomUUID();
        ActivityEntity entity = ActivityEntity.builder().id(id).event("Best Salesperson").occurredAt(LocalDateTime.now()).build();
        Activity dto = Activity.builder().id(id).event("Best Salesperson").occurredAt(entity.getOccurredAt()).build();

        when(activityRepository.findById(id)).thenReturn(Optional.of(entity));
        when(activityMapper.toDto(entity)).thenReturn(dto);

        Activity result = activityService.findById(id);

        assertThat(result).isEqualTo(dto);
    }

    @Test
    void findById_unknownId_returnsNull() {
        UUID id = UUID.randomUUID();
        when(activityRepository.findById(id)).thenReturn(Optional.empty());

        Activity result = activityService.findById(id);

        assertThat(result).isNull();
    }

    @Test
    void create_savesEntityAndReturnsMappedActivity() {
        Activity dto = Activity.builder().event("World's Best Boss").occurredAt(LocalDateTime.now()).build();
        ActivityEntity entityToSave = ActivityEntity.builder().event("World's Best Boss").occurredAt(dto.getOccurredAt()).build();
        ActivityEntity savedEntity = ActivityEntity.builder().id(UUID.randomUUID()).event("World's Best Boss").occurredAt(dto.getOccurredAt()).build();
        Activity savedDto = Activity.builder().id(savedEntity.getId()).event("World's Best Boss").occurredAt(dto.getOccurredAt()).build();

        when(activityMapper.toEntity(dto)).thenReturn(entityToSave);
        when(activityRepository.save(entityToSave)).thenReturn(savedEntity);
        when(activityMapper.toDto(savedEntity)).thenReturn(savedDto);

        Activity result = activityService.create(dto);

        assertThat(result).isEqualTo(savedDto);
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void update_setsIdOnMappedEntityBeforeSaving() {
        UUID id = UUID.randomUUID();
        Activity dto = Activity.builder().id(id).event("Updated Event").occurredAt(LocalDateTime.now()).build();
        ActivityEntity mappedEntity = ActivityEntity.builder().event("Updated Event").occurredAt(dto.getOccurredAt()).build();
        ActivityEntity savedEntity = ActivityEntity.builder().id(id).event("Updated Event").occurredAt(dto.getOccurredAt()).build();
        Activity savedDto = Activity.builder().id(id).event("Updated Event").occurredAt(dto.getOccurredAt()).build();

        when(activityMapper.toEntity(dto)).thenReturn(mappedEntity);
        when(activityRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(activityMapper.toDto(savedEntity)).thenReturn(savedDto);

        Activity result = activityService.update(dto);

        assertThat(mappedEntity.getId()).isEqualTo(id);
        assertThat(result).isEqualTo(savedDto);
    }

    @Test
    void delete_delegatesToRepository() {
        UUID id = UUID.randomUUID();

        activityService.delete(id);

        verify(activityRepository).deleteById(id);
    }
}

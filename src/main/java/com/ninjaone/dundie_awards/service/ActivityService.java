package com.ninjaone.dundie_awards.service;

import com.ninjaone.dundie_awards.model.api.Activity;
import com.ninjaone.dundie_awards.model.entity.ActivityEntity;
import com.ninjaone.dundie_awards.model.mapper.ActivityMapper;
import com.ninjaone.dundie_awards.repository.ActivityRepository;
import io.micrometer.core.annotation.Timed;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final ActivityMapper activityMapper;

    public ActivityService(@NonNull ActivityRepository activityRepository,
                           @NonNull ActivityMapper activityMapper) {
        this.activityRepository = activityRepository;
        this.activityMapper = activityMapper;
    }

    @Transactional(readOnly = true)
    @Timed(value = "dundie.activity.service.find.all", histogram = true)
    public Page<Activity> findAll(@NonNull Pageable pageable) {
        Page<ActivityEntity> page = activityRepository.findAll(pageable);
        return page.map(activityMapper::toDto);
    }

    @Cacheable(value = "activities", key = "#id")
    @Transactional(readOnly = true)
    @Timed(value = "dundie.activity.service.find.id", histogram = true)
    public Activity findById(@NonNull UUID id) {
        ActivityEntity a = activityRepository.findById(id).orElse(null);
        return activityMapper.toDto(a);
    }

    @Transactional
    @Timed(value = "dundie.activity.service.create", histogram = true)
    public Activity create(@NonNull Activity activity) {
        ActivityEntity activityEntity = activityMapper.toEntity(activity);
        ActivityEntity savedActivity = activityRepository.save(activityEntity);
        return activityMapper.toDto(savedActivity);
    }

    @Caching(evict = { @CacheEvict(value = "activities", key = "#activity.id") })
    @Transactional
    @Timed(value = "dundie.activity.service.update", histogram = true)
    public Activity update(@NonNull Activity activity) {
        ActivityEntity activityEntity = activityMapper.toEntity(activity);
        activityEntity.setId(activity.getId());
        ActivityEntity savedActivity = activityRepository.save(activityEntity);
        return activityMapper.toDto(savedActivity);
    }

    @Caching(evict = { @CacheEvict(value = "activities", key = "#id") })
    @Transactional
    @Timed(value = "dundie.activity.service.delete", histogram = true)
    public void delete(@NonNull UUID id) {
        activityRepository.deleteById(id);
    }
}

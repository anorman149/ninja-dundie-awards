package com.ninjaone.dundie_awards.controller;

import com.ninjaone.dundie_awards.model.api.Activity;
import com.ninjaone.dundie_awards.model.api.PagedResponse;
import com.ninjaone.dundie_awards.service.ActivityService;
import com.ninjaone.dundie_awards.validator.ValidUUID;
import io.micrometer.core.annotation.Timed;
import lombok.experimental.NonFinal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/activities")
public class ActivityController extends Controller {
    private final ActivityService activityService;

    public ActivityController(@NonFinal ActivityService activityService) {
        this.activityService = activityService;
    }

    // get all activities
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.activity.find.all", histogram = true)
    public ResponseEntity<PagedResponse<Activity>> findAll(@PageableDefault(size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        //Validate Page
        Pageable validatedPageable = validatePageable(pageable);

        //Perform Service Call
        Page<Activity> paged = activityService.findAll(validatedPageable);

        //Transform and return
        return ResponseEntity.ok().body(PagedResponse.from(paged));
    }

    // get activity by id rest api
    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.activity.find.id", histogram = true)
    public ResponseEntity<Activity> findById(@PathVariable @ValidUUID UUID id) {
        Activity a = activityService.findById(id);
        if (a == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(a);
    }

    // create activity rest api
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.activity.create", histogram = true)
    public ResponseEntity<Activity> create(@RequestBody Activity activity) {
        Activity a = activityService.create(activity);
        return ResponseEntity.status(HttpStatus.CREATED).body(a);
    }

    // update activity rest api
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.activity.update", histogram = true)
    public ResponseEntity<Activity> update(@RequestBody Activity activity) {
        Activity a = activityService.update(activity);
        return ResponseEntity.ok().body(a);
    }

    // delete activity rest api
    @DeleteMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Timed(value = "dundie.activity.delete", histogram = true)
    public ResponseEntity<Void> delete(@PathVariable @ValidUUID UUID id) {
        activityService.delete(id);
        return ResponseEntity.ok().build();
    }
}

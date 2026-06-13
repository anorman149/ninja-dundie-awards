package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.api.Activity;
import com.ninjaone.dundie_awards.model.api.PagedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ActivityControllerITest extends BaseTest {

    @Test
    void create_persistsAndReturnsActivity() {
        Activity activity = Activity.builder()
                .occurredAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .event("Employee of the Month")
                .build();

        ResponseEntity<Activity> response = create(activity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getEvent()).isEqualTo("Employee of the Month");
        assertThat(response.getBody().getOccurredAt()).isEqualTo(activity.getOccurredAt());
    }

    @Test
    void findById_returnsCreatedActivity() {
        Activity created = create(Activity.builder()
                .occurredAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .event("Dundie Award Ceremony")
                .build()).getBody();

        ResponseEntity<Activity> response = restTemplate.exchange(
                baseUrl() + "/activities/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Activity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getEvent()).isEqualTo("Dundie Award Ceremony");
    }

    @Test
    void findById_unknownId_returnsNotFound() {
        ResponseEntity<Activity> response = restTemplate.exchange(
                baseUrl() + "/activities/" + UUID.randomUUID(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Activity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAll_returnsPagedActivities() {
        create(Activity.builder()
                .occurredAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .event("Annual Award Show")
                .build());

        ResponseEntity<PagedResponse<Activity>> response = restTemplate.exchange(
                baseUrl() + "/activities?page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                new ParameterizedTypeReference<PagedResponse<Activity>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).isNotEmpty();
        assertThat(response.getBody().page().size()).isEqualTo(10);
    }

    @Test
    void update_modifiesActivityFields() {
        Activity created = create(Activity.builder()
                .occurredAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .event("Initial Event")
                .build()).getBody();

        created.setEvent("Updated Event");

        ResponseEntity<Activity> response = restTemplate.exchange(
                baseUrl() + "/activities",
                HttpMethod.PUT,
                new HttpEntity<>(created, authHeaders()),
                Activity.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getEvent()).isEqualTo("Updated Event");
    }

    @Test
    void delete_removesActivity() {
        Activity created = create(Activity.builder()
                .occurredAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .event("Disposable Event")
                .build()).getBody();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl() + "/activities/" + created.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(authHeaders()),
                Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Activity> getResponse = restTemplate.exchange(
                baseUrl() + "/activities/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Activity.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void unauthenticatedRequest_isRejected() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/activities",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Activity> create(Activity activity) {
        return restTemplate.exchange(
                baseUrl() + "/activities",
                HttpMethod.POST,
                new HttpEntity<>(activity, authHeaders()),
                Activity.class);
    }
}

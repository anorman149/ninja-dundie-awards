package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.api.Organization;
import com.ninjaone.dundie_awards.model.api.PagedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OrganizationControllerITest extends BaseTest {

    @Test
    void create_persistsAndReturnsOrganization() {
        Organization organization = Organization.builder()
                .name("Wernham Hogg " + UUID.randomUUID())
                .build();

        ResponseEntity<Organization> response = create(organization);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(organization.getName());
    }

    @Test
    void findById_returnsCreatedOrganization() {
        Organization created = create(Organization.builder().name("Dunder Mifflin " + UUID.randomUUID()).build()).getBody();

        ResponseEntity<Organization> response = restTemplate.exchange(
                baseUrl() + "/organizations/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Organization.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getName()).isEqualTo(created.getName());
    }

    @Test
    void findById_unknownId_returnsNotFound() {
        ResponseEntity<Organization> response = restTemplate.exchange(
                baseUrl() + "/organizations/" + UUID.randomUUID(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Organization.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAll_returnsPagedOrganizations() {
        create(Organization.builder().name("Vance Refrigeration " + UUID.randomUUID()).build());

        ResponseEntity<PagedResponse<Organization>> response = restTemplate.exchange(
                baseUrl() + "/organizations?page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                new ParameterizedTypeReference<PagedResponse<Organization>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).isNotEmpty();
        assertThat(response.getBody().page().size()).isEqualTo(10);
    }

    @Test
    void update_modifiesOrganizationName() {
        Organization created = create(Organization.builder().name("Initial Org " + UUID.randomUUID()).build()).getBody();
        created.setName("Renamed Org " + UUID.randomUUID());

        ResponseEntity<Organization> response = restTemplate.exchange(
                baseUrl() + "/organizations",
                HttpMethod.PUT,
                new HttpEntity<>(created, authHeaders()),
                Organization.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getName()).isEqualTo(created.getName());
    }

    @Test
    void delete_removesOrganization() {
        Organization created = create(Organization.builder().name("Disposable Org " + UUID.randomUUID()).build()).getBody();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl() + "/organizations/" + created.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(authHeaders()),
                Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Organization> getResponse = restTemplate.exchange(
                baseUrl() + "/organizations/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Organization.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void unauthenticatedRequest_isRejected() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/organizations",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Organization> create(Organization organization) {
        return restTemplate.exchange(
                baseUrl() + "/organizations",
                HttpMethod.POST,
                new HttpEntity<>(organization, authHeaders()),
                Organization.class);
    }
}

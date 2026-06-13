package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.api.Employee;
import com.ninjaone.dundie_awards.model.api.PagedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerITest extends BaseTest {

    @Test
    void create_persistsAndReturnsEmployee() {
        Employee employee = Employee.builder()
                .firstName("Michael")
                .lastName("Scott")
                .dundieAwards(5)
                .build();

        ResponseEntity<Employee> response = create(employee);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Michael");
        assertThat(response.getBody().getLastName()).isEqualTo("Scott");
        assertThat(response.getBody().getDundieAwards()).isEqualTo(5);
    }

    @Test
    void findById_returnsCreatedEmployee() {
        Employee created = create(Employee.builder().firstName("Jim").lastName("Halpert").dundieAwards(1).build()).getBody();

        ResponseEntity<Employee> response = restTemplate.exchange(
                baseUrl() + "/employees/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getFirstName()).isEqualTo("Jim");
        assertThat(response.getBody().getLastName()).isEqualTo("Halpert");
    }

    @Test
    void findById_unknownId_returnsNotFound() {
        ResponseEntity<Employee> response = restTemplate.exchange(
                baseUrl() + "/employees/" + UUID.randomUUID(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findAll_returnsPagedEmployees() {
        create(Employee.builder().firstName("Pam").lastName("Beesly").dundieAwards(0).build());

        ResponseEntity<PagedResponse<Employee>> response = restTemplate.exchange(
                baseUrl() + "/employees?page=0&size=10",
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                new ParameterizedTypeReference<PagedResponse<Employee>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().content()).isNotEmpty();
        assertThat(response.getBody().page().size()).isEqualTo(10);
    }

    @Test
    void update_modifiesEmployeeFields() {
        Employee created = create(Employee.builder().firstName("Andy").lastName("Bernard").dundieAwards(2).build()).getBody();
        created.setDundieAwards(10);
        created.setLastName("Bernard-Hannon");

        ResponseEntity<Employee> response = restTemplate.exchange(
                baseUrl() + "/employees",
                HttpMethod.PUT,
                new HttpEntity<>(created, authHeaders()),
                Employee.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(created.getId());
        assertThat(response.getBody().getDundieAwards()).isEqualTo(10);
        assertThat(response.getBody().getLastName()).isEqualTo("Bernard-Hannon");
    }

    @Test
    void delete_removesEmployee() {
        Employee created = create(Employee.builder().firstName("Creed").lastName("Bratton").dundieAwards(0).build()).getBody();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl() + "/employees/" + created.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(authHeaders()),
                Void.class);
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        ResponseEntity<Employee> getResponse = restTemplate.exchange(
                baseUrl() + "/employees/" + created.getId(),
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                Employee.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void unauthenticatedRequest_isRejected() {
        ResponseEntity<String> response = restTemplate.exchange(
                baseUrl() + "/employees",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<Employee> create(Employee employee) {
        return restTemplate.exchange(
                baseUrl() + "/employees",
                HttpMethod.POST,
                new HttpEntity<>(employee, authHeaders()),
                Employee.class);
    }
}

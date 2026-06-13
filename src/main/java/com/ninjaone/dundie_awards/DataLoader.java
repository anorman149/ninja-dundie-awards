package com.ninjaone.dundie_awards;

import com.ninjaone.dundie_awards.model.entity.EmployeeEntity;
import com.ninjaone.dundie_awards.model.entity.OrganizationEntity;
import com.ninjaone.dundie_awards.repository.EmployeeRepository;
import com.ninjaone.dundie_awards.repository.OrganizationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final OrganizationRepository organizationRepository;

    public DataLoader(EmployeeRepository employeeRepository, OrganizationRepository organizationRepository) {
        this.employeeRepository = employeeRepository;
        this.organizationRepository = organizationRepository;
    }

    @Override
    public void run(String... args) {
        // uncomment to reseed data
        // employeeRepository.deleteAll();
        // organizationRepository.deleteAll();

        if (employeeRepository.count() == 0) {
            OrganizationEntity organizationEntityPikashu = OrganizationEntity.builder()
                    .name("Pikashu")
                    .build();

            organizationRepository.save(organizationEntityPikashu);
            employeeRepository.save(EmployeeEntity.builder().firstName("John").lastName("Doe").organization(organizationEntityPikashu).build());
            employeeRepository.save(EmployeeEntity.builder().firstName("Jane").lastName("Smith").organization(organizationEntityPikashu).build());
            employeeRepository.save(EmployeeEntity.builder().firstName("Creed").lastName("Braton").organization(organizationEntityPikashu).build());

            OrganizationEntity organizationEntitySquanchy = OrganizationEntity.builder()
                    .name("Squanchy")
                    .build();
            organizationRepository.save(organizationEntitySquanchy);

            employeeRepository.save(EmployeeEntity.builder().firstName("Michael").lastName("Michael").organization(organizationEntitySquanchy).build());
            employeeRepository.save(EmployeeEntity.builder().firstName("Dwight").lastName("Schrute").organization(organizationEntitySquanchy).build());
            employeeRepository.save(EmployeeEntity.builder().firstName("Jim").lastName("Halpert").organization(organizationEntitySquanchy).build());
            employeeRepository.save(EmployeeEntity.builder().firstName("Pam").lastName("Beesley").organization(organizationEntitySquanchy).build());
        }
    }
}

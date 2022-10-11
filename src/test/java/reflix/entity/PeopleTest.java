package reflix.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class PeopleTest {
    @Autowired
    EntityManager entityManager;

    @Test
    void createPeople(){
        var people = People.builder()
                .name("Clint Eastwood")
                .birthdate(LocalDate.of(1930,5,31))
                // .birthdate(new Date())
                .build();
        entityManager.persist(people);
        entityManager.flush();
        assertNotNull(people.getId());
    }

}
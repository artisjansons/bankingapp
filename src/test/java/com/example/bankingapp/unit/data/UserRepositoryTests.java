package com.example.bankingapp.unit.data;

import com.example.bankingapp.data.model.AppUser;
import com.example.bankingapp.data.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by Artis on 2/4/2017.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmail() throws Exception {
        AppUser user = new AppUser();
        user.setEmail("user@user");
        user.setPassword("qwerty");
        user.setName("User");

        testEntityManager.persist(user);
        AppUser actualUser = userRepository.findByEmail(user.getEmail()).get();
        assertThat(actualUser.getEmail()).isEqualTo(user.getEmail());
    }
}

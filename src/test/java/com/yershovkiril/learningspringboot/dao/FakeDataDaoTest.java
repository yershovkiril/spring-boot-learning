package com.yershovkiril.learningspringboot.dao;

import com.yershovkiril.learningspringboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FakeDataDaoTest {
    private FakeDataDao fakeDataDao;

    @BeforeEach
    void setUp() {
        fakeDataDao = new FakeDataDao();
    }

    @Test
    void shouldSelectAllUsers() {
        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(1);

        User user = users.get(0);

        assertThat(user.getAge()).isEqualTo(22);
        assertThat(user.getEmail()).isEqualTo("example@gmail.com");
        assertThat(user.getFirstName()).isEqualTo("Joe");
        assertThat(user.getLastName()).isEqualTo("Jones");
        assertThat(user.getGender()).isEqualTo(User.Gender.MALE);
        assertThat(user.getUserUid()).isNotNull();
    }

    @Test
    void shouldSelectUserByUserUid() {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(annaUserUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        fakeDataDao.insertUser(annaUserUid, anna);
        assertThat(fakeDataDao.selectAllUsers()).hasSize(2);

        Optional<User> annaOptional = fakeDataDao.selectUserByUserUid(annaUserUid);
        assertTrue(annaOptional.isPresent());
        assertThat(annaOptional.get()).isEqualToComparingFieldByField(anna);
    }

    @Test
    void shouldNotSelectUserByRandomUserUid() {
        Optional<User> user = fakeDataDao.selectUserByUserUid(UUID.randomUUID());
        assertFalse(user.isPresent());
    }

    @Test
    void shouldUpdateUser() {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        User newJoe = new User(joeUserUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        fakeDataDao.updateUser(newJoe);

        Optional<User> optionalUser = fakeDataDao.selectUserByUserUid(joeUserUid);
        assertTrue(optionalUser.isPresent());

        assertThat(fakeDataDao.selectAllUsers()).hasSize(1);
        assertThat(optionalUser.get()).isEqualToComparingFieldByField(newJoe);

    }

    @Test
    void deleteUserByUserUid() {
        UUID joeUserUid = fakeDataDao.selectAllUsers().get(0).getUserUid();
        fakeDataDao.deleteUserByUserUid(joeUserUid);
        assertFalse(fakeDataDao.selectUserByUserUid(joeUserUid).isPresent());
        assertThat(fakeDataDao.selectAllUsers()).isEmpty();
    }

    @Test
    void insertUser() {
        UUID userUid = UUID.randomUUID();
        User user = new User(userUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");
        fakeDataDao.insertUser(userUid, user);

        List<User> users = fakeDataDao.selectAllUsers();
        assertThat(users).hasSize(2);
        assertThat(fakeDataDao.selectUserByUserUid(userUid).get()).isEqualToComparingFieldByField(user);
    }
}
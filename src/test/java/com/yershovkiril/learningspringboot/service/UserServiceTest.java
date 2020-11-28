package com.yershovkiril.learningspringboot.service;

import com.yershovkiril.learningspringboot.dao.FakeDataDao;
import com.yershovkiril.learningspringboot.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @Mock
    private FakeDataDao fakeDataDao;
    private UserService userService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(fakeDataDao);
    }

    @Test
    void shouldGetAllUsers() {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(annaUserUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");

        ImmutableList<User> users = new ImmutableList.Builder<User>().add(anna).build();

        given(fakeDataDao.selectAllUsers()).willReturn(users);
        List<User> allUsers = userService.getAllUsers(Optional.empty());
        assertThat(allUsers).hasSize(1);

        User user = allUsers.get(0);
        assertUserFields(user);
    }

    @Test
    public void shouldGetAllUsersByGender() {
        UUID annaUserUid = UUID.randomUUID();
        User anna = new User(annaUserUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");

        UUID joeUserUid = UUID.randomUUID();
        User joe = new User(annaUserUid, "joe", "jones",
                User.Gender.MALE, 30, "jones@gmail.com");

        ImmutableList<User> users = new ImmutableList.Builder<User>()
                .add(anna)
                .add(joe)
                .build();

        given(fakeDataDao.selectAllUsers()).willReturn(users);

        List<User> filteredUsers = userService.getAllUsers(Optional.of("female"));
        assertThat(filteredUsers).hasSize(1);
        assertUserFields(filteredUsers.get(0));
    }

    @Test
    public void shouldThrowExceptionWhenGenderIsInvalid() {
        assertThatThrownBy(() -> userService.getAllUsers(Optional.of("SDFGdsgds")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Invalid gender");
    }

    @Test
    void ShouldGetUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");

        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));

        Optional<User> userOptional = userService.getUser(annaUid);
        assertTrue(userOptional.isPresent());

        User user = userOptional.get();
        assertUserFields(user);
    }

    @Test
    void ShouldUpdateUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");

        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.updateUser(anna)).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int updateResult = userService.updateUser(anna);

        verify(fakeDataDao).selectUserByUserUid(annaUid);
        verify(fakeDataDao).updateUser(captor.capture());

        User user = captor.getValue();
        assertUserFields(user);

        assertThat(updateResult).isEqualTo(1);
    }

    @Test
    void ShouldRemoveUser() {
        UUID annaUid = UUID.randomUUID();
        User anna = new User(annaUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");

        given(fakeDataDao.selectUserByUserUid(annaUid)).willReturn(Optional.of(anna));
        given(fakeDataDao.deleteUserByUserUid(annaUid)).willReturn(1);

        int deleteResult = userService.removeUser(annaUid);

        verify(fakeDataDao).selectUserByUserUid(annaUid);
        verify(fakeDataDao).deleteUserByUserUid(annaUid);

        assertThat(deleteResult).isEqualTo(1);
    }

    @Test
    void ShouldInsertUser() {
        UUID userUid = UUID.randomUUID();
        User anna = new User(userUid, "anna", "montana",
                User.Gender.FEMALE, 30, "anna@gmail.com");

        given(fakeDataDao.insertUser(any(UUID.class), any(User.class))).willReturn(1);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        int insertResult = userService.insertUser(anna);
        verify(fakeDataDao).insertUser(eq(userUid), captor.capture());

        User user = captor.getValue();
        assertUserFields(user);
        assertThat(insertResult).isEqualTo(1);
    }

    private void assertUserFields(User user) {
        assertThat(user.getAge()).isEqualTo(30);
        assertThat(user.getEmail()).isEqualTo("anna@gmail.com");
        assertThat(user.getFirstName()).isEqualTo("anna");
        assertThat(user.getLastName()).isEqualTo("montana");
        assertThat(user.getGender()).isEqualTo(User.Gender.FEMALE);
        assertThat(user.getUserUid()).isNotNull();
        assertThat(user.getUserUid()).isInstanceOf(UUID.class);

    }
}
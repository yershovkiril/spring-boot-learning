package com.yershovkiril.learningspringboot.it;

import com.yershovkiril.learningspringboot.clientproxy.UserResourceV1;
import com.yershovkiril.learningspringboot.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.UUID;

import static com.yershovkiril.learningspringboot.model.User.Gender.FEMALE;
import static com.yershovkiril.learningspringboot.model.User.Gender.MALE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class UserIT {

	@Autowired
	private UserResourceV1 userResourceV1;

	@Test
	public void shouldInsertUser() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones",
				MALE, 22, "example@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);
	}

	@Test
	public void shouldDeleteUser() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones",
				MALE, 22, "example@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		// Then
		User joe = userResourceV1.fetchUser(userUid);
		assertThat(joe).isEqualToComparingFieldByField(user);

		// When
		userResourceV1.deleteUser(userUid);

		// Then
		assertThatThrownBy(() -> userResourceV1.fetchUser(userUid))
				.isInstanceOf(NotFoundException.class);
	}

	@Test
	public void shouldUpdateUser() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones",
				MALE, 22, "example@gmail.com");

		// When
		userResourceV1.insertNewUser(user);

		User updateUser = new User(userUid, "Alex", "Jones",
				MALE, 55, "alex@gmail.com");
		userResourceV1.updateUser(updateUser);

		// Then
		user = userResourceV1.fetchUser(userUid);
		assertThat(user).isEqualToComparingFieldByField(updateUser);
	}

	@Test
	public void shouldFetchUsersByGender() {
		// Given
		UUID userUid = UUID.randomUUID();
		User user = new User(userUid, "Joe", "Jones",
				MALE, 22, "example@gmail.com");

		// When
		userResourceV1.insertNewUser(user);
		List<User> females = userResourceV1.fetchUsers(FEMALE.name());

		// Then
		assertThat(females).extracting("userUid").doesNotContain(userUid);
		assertThat(females).extracting("firstName").doesNotContain(user.getFirstName());
		assertThat(females).extracting("lastName").doesNotContain(user.getLastName());
		assertThat(females).extracting("gender").doesNotContain(user.getGender());
		assertThat(females).extracting("age").doesNotContain(user.getAge());
		assertThat(females).extracting("email").doesNotContain(user.getEmail());


		// When
		userResourceV1.insertNewUser(user);
		List<User> males = userResourceV1.fetchUsers(MALE.name());

		// Then
		assertThat(males).extracting("userUid").contains(userUid);
		assertThat(males).extracting("firstName").contains(user.getFirstName());
		assertThat(males).extracting("lastName").contains(user.getLastName());
		assertThat(males).extracting("gender").contains(user.getGender());
		assertThat(males).extracting("age").contains(user.getAge());
		assertThat(males).extracting("email").contains(user.getEmail());
	}
}

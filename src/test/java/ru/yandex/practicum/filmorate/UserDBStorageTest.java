package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDBStorage;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Import({UserDBStorage.class, UserRowMapper.class})
class UserDBStorageTest {
	private final UserDBStorage userDBStorage;
	private User testUser;
	private User friendUser;

	@BeforeEach
	void setUp() {
		testUser = new User();
		testUser.setEmail("user@mail.ru");
		testUser.setLogin("userLogin");
		testUser.setName("User Name");
		testUser.setBirthday(LocalDate.of(1990, 1, 1));

		friendUser = new User();
		friendUser.setEmail("friend@mail.ru");
		friendUser.setLogin("friendLogin");
		friendUser.setName("Friend Name");
		friendUser.setBirthday(LocalDate.of(1995, 5, 5));
	}

	@Test
	public void testFindUserById() {
		Optional<User> userOptional = userDBStorage.getUserById(1L);

		assertThat(userOptional)
				.isPresent()
				.hasValueSatisfying(user -> assertThat(user).hasFieldOrPropertyWithValue("id", 1L));
	}

	@Test
	void testGetAllUsers() {
		User user1 = userDBStorage.create(testUser);
		User user2 = userDBStorage.create(friendUser);

		List<User> users = userDBStorage.getAllUsers();

		assertThat(users)
				.extracting(User::getId)
				.contains(user1.getId(), user2.getId());
	}

	@Test
	void testCreateUser() {
		User createdUser = userDBStorage.create(testUser);
		Optional<User> foundUser = userDBStorage.getUserById(createdUser.getId());
		assertThat(foundUser)
				.isPresent()
				.hasValueSatisfying(user -> {
					assertThat(user.getId()).isEqualTo(createdUser.getId());
					assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
					assertThat(user.getLogin()).isEqualTo(testUser.getLogin());
				});
	}

	@Test
	void testUpdateUser() {
		User createdUser = userDBStorage.create(testUser);

		createdUser.setEmail("updated@mail.ru");
		createdUser.setName("Updated Name");

		userDBStorage.update(createdUser);

		Optional<User> foundUser = userDBStorage.getUserById(createdUser.getId());
		assertThat(foundUser)
				.isPresent()
				.hasValueSatisfying(user -> {
					assertThat(user.getEmail()).isEqualTo("updated@mail.ru");
					assertThat(user.getName()).isEqualTo("Updated Name");
				});
	}

	@Test
	void testAddFriend() {
		User user1 = userDBStorage.create(testUser);
		User user2 = userDBStorage.create(friendUser);

		userDBStorage.addFriend(user1.getId(), user2.getId());

		List<User> friends = userDBStorage.getFriendsByUserId(user1.getId());

		assertThat(friends)
				.hasSize(1)
				.extracting(User::getId)
				.containsExactly(user2.getId());
	}

	@Test
	void testDeleteFriend() {
		User user1 = userDBStorage.create(testUser);
		User user2 = userDBStorage.create(friendUser);

		userDBStorage.addFriend(user1.getId(), user2.getId());
		userDBStorage.deleteFriend(user1.getId(), user2.getId());

		assertThat(userDBStorage.getFriendsByUserId(user1.getId())).isEmpty();
	}

	@Test
	void testGetMutualFriends() {
		User user1 = userDBStorage.create(testUser);
		User user2 = userDBStorage.create(friendUser);
		User mutualFriend = new User();
		mutualFriend.setName("mutualName");
		mutualFriend.setEmail("mutual@mail.ru");
		mutualFriend.setLogin("mutualLogin");
		mutualFriend.setBirthday(LocalDate.of(2000, 1, 1));
		userDBStorage.create(mutualFriend);

		userDBStorage.addFriend(user1.getId(), mutualFriend.getId());
		userDBStorage.addFriend(user2.getId(), mutualFriend.getId());

		List<User> mutualFriends = userDBStorage.getMutualFriends(user1.getId(), user2.getId());

		assertThat(mutualFriends)
				.hasSize(1)
				.extracting(User::getId)
				.containsExactly(mutualFriend.getId());
	}

}

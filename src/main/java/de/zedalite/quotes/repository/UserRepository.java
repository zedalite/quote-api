package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.tables.Users;
import de.zedalite.quotes.data.jooq.tables.records.UsersRecord;
import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exceptions.UserNotFoundException;
import org.jooq.DSLContext;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * The UserRepository class is responsible for interacting with the user data in the database.
 * It provides methods for saving and retrieving user information.
 */
@Repository
public class UserRepository {
  private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;
  private static final String USER_NOT_FOUND = "User not found";
  private static final Users USERS = Users.USERS.as("Users");
  private final DSLContext dsl;

  public UserRepository(final DSLContext dsl) {
    this.dsl = dsl;
  }

  /**
   * Saves a user to the database.
   *
   * @param user the user details to be saved
   * @return the saved user
   * @throws UserNotFoundException if the user is not found in the database
   */
  @CachePut(value = "users", key = "#result.id()", unless = "#result == null")
  public User save(final UserRequest user) throws UserNotFoundException {
    final var savedUser = dsl.insertInto(USERS)
      .set(USERS.NAME, user.name())
      .set(USERS.PASSWORD, user.password())
      .returning()
      .fetchOneInto(UsersRecord.class);
    if (savedUser == null) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.userRecToUser(savedUser);
  }

  /**
   * Finds a user in the database by their name.
   *
   * @param name the name of the user to be found
   * @return the found user
   * @throws UserNotFoundException if the user is not found in the database
   */
  @Cacheable(value = "users", key = "#name", unless = "#result == null")
  public User findByName(final String name) throws UserNotFoundException {
    final var user = dsl.selectFrom(USERS)
      .where(USERS.NAME.eq(name))
      .fetchOneInto(UsersRecord.class);
    if (user == null) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.userRecToUser(user);
  }
}

package de.zedalite.quotes.repository;

import de.zedalite.quotes.data.jooq.users.tables.Users;
import de.zedalite.quotes.data.jooq.users.tables.records.UsersRecord;
import de.zedalite.quotes.data.mapper.UserMapper;
import de.zedalite.quotes.data.model.User;
import de.zedalite.quotes.data.model.UserRequest;
import de.zedalite.quotes.exception.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

  private static final Users USERS = Users.USERS_.as("Users");

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
    final Optional<UsersRecord> savedUser = dsl
      .insertInto(USERS)
      .set(USERS.NAME, user.name())
      .set(USERS.EMAIL, user.email())
      .set(USERS.CREATION_DATE, LocalDateTime.now())
      .set(USERS.DISPLAY_NAME, user.displayName())
      .returning()
      .fetchOptionalInto(UsersRecord.class);
    if (savedUser.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(savedUser.get());
  }

  public List<User> findAll() {
    final List<UsersRecord> users = dsl.selectFrom(USERS).fetchInto(UsersRecord.class);
    if (users.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUserList(users);
  }

  // TODO Cache result or better integrate in user cache

  public List<User> findAllByIds(final List<Integer> ids) throws UserNotFoundException {
    final List<UsersRecord> users = dsl.selectFrom(USERS).where(USERS.ID.in(ids)).fetchInto(UsersRecord.class);
    if (users.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUserList(users);
  }

  // TODO Cache result or better integrate in user cache -> otherwise sync problem when cacheput
  // -> custom implementiation of cache to search for name in exisiting cache
  @Cacheable(value = "users", key = "#name", unless = "#result == null")
  public User findByName(final String name) {
    final Optional<UsersRecord> user = dsl
      .selectFrom(USERS)
      .where(USERS.NAME.eq(name))
      .fetchOptionalInto(UsersRecord.class);
    if (user.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(user.get());
  }

  @Cacheable(value = "users", key = "#id", unless = "#result == null")
  public User findById(final Integer id) {
    final Optional<UsersRecord> user = dsl
      .selectFrom(USERS)
      .where(USERS.ID.eq(id))
      .fetchOptionalInto(UsersRecord.class);
    if (user.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(user.get());
  }

  @CachePut(value = "users", key = "#id", unless = "#result == null")
  public User update(final Integer id, final UserRequest user) throws UserNotFoundException {
    final Optional<UsersRecord> updatedUser = dsl
      .update(USERS)
      .set(USERS.NAME, user.name())
      .set(USERS.EMAIL, user.email())
      .set(USERS.DISPLAY_NAME, user.displayName())
      .where(USERS.ID.eq(id))
      .returning()
      .fetchOptionalInto(UsersRecord.class);
    if (updatedUser.isEmpty()) throw new UserNotFoundException(USER_NOT_FOUND);
    return USER_MAPPER.mapToUser(updatedUser.get());
  }

  public boolean isUsernameTaken(final String name) {
    return dsl.fetchExists(dsl.selectFrom(USERS).where(USERS.NAME.eq(name)));
  }

  public boolean isUsernameAvailable(final String name) {
    return !dsl.fetchExists(dsl.selectFrom(USERS).where(USERS.NAME.eq(name)));
  }

  public boolean doesUserNonExist(final Integer id) {
    return !dsl.fetchExists(dsl.selectFrom(USERS).where(USERS.ID.eq(id)));
  }
}

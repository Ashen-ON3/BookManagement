package bookmanagement;

import java.sql.SQLException;
import java.util.List;

/**
 * Interface defining CRUD operations for manageable entities.
 * Implements the OOP concept of Abstraction via interfaces.
 */
public interface Manageable<T> {

    /**
     * Adds a new entity to the database.
     * @param entity the entity to add
     * @return true if successful, false otherwise
     */
    boolean add(T entity) throws SQLException;

    /**
     * Updates an existing entity in the database.
     * @param entity the entity with updated values
     * @return true if successful, false otherwise
     */
    boolean update(T entity) throws SQLException;

    /**
     * Deletes an entity from the database by ID.
     * @param id the ID of the entity to delete
     * @return true if successful, false otherwise
     */
    boolean delete(int id) throws SQLException;

    /**
     * Retrieves all entities from the database.
     * @return list of all entities
     */
    List<T> getAll() throws SQLException;

    /**
     * Searches entities by a keyword.
     * @param keyword the search keyword
     * @return list of matching entities
     */
    List<T> search(String keyword) throws SQLException;
}

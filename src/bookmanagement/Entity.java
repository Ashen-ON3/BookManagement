package bookmanagement;

/**
 * Abstract base class representing a generic database entity.
 * Demonstrates OOP concept of Abstraction and Inheritance.
 */
public abstract class Entity {

    protected int id;

    public Entity() {}

    public Entity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the entity's data.
     * Each subclass must implement its own version.
     */
    @Override
    public abstract String toString();

    /**
     * Validates the entity's fields before database operations.
     * Each subclass must define its own validation rules.
     */
    public abstract boolean isValid();
}

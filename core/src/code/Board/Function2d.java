package code.Board;

/**
 * interface for any kind of Function2d
 */
public interface Function2d {

    double evaluate(Vector2d p);
    Vector2d gradient(Vector2d p);
}

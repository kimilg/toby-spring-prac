package springbook.learningtest.template;

/**
 * @author user
 */
public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}

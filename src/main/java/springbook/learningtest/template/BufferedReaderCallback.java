package springbook.learningtest.template;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author user
 */
public interface BufferedReaderCallback {
    Integer doSomethingWithReader(BufferedReader br) throws IOException;
}

package edu.gemini.aspen.gmp.data;

/**
 * A handler that will be invoked whenever a new Intermediate File event
 * arrives.
 */
public interface IntermediateFileEventHandler {

    /**
     * Invoked when a new intermediate file event arrives. The framework
     * invokes this method in a separate thread.
     * @param filename name of the intermediate file associated to this event
     * @param dataset dataset associated to the intermediate file
     * @param hint (optional) string to provide additional information about
     *        the file. Its use is instrument-dependent. 
     */
    void onIntermediateFileEvent(String filename, Dataset dataset, String hint);
}

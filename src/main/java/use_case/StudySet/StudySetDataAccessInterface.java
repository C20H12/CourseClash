/// File: StudySetDataAccessInterface.java
/// Description: Providing uploading and download set methods
/// Wrote by Daniel

package use_case.StudySet;

import org.json.JSONObject;
import entity.StudyDeck;
import use_case.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;

// Example: https://github.com/CSC207-2025F-UofT/NoteApplication/blob/main/src/main/java/use_case/note/NoteDataAccessInterface.java

public interface StudySetDataAccessInterface {

    // A simple connection test to API
    String testAPIConnection() throws DataAccessException;

    HashMap<String, Integer> getAllSetNameAndID() throws DataAccessException;

    StudyDeck getSetByName(String setName) throws DataAccessException;



}

// Manages file I/O operations in user's directory
// Uses JSONToDeckWorker, DeckToJSONWorker.
// Archie
package frameworks_and_drivers.DataAccess.DeckManagement.JSON;

// entities
import entity.DeckManagement.StudyCard;
import entity.DeckManagement.StudyDeck;

// vanilla java libs
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SysFileHandler {

    // Declarations
    private final String STORAGE_DIRECTORY = System.getProperty("user.home") + "/.CourseClash/local_storage";

    // Make directories if there's none existin
    public SysFileHandler() {
        new File(STORAGE_DIRECTORY).mkdirs();
    }

}

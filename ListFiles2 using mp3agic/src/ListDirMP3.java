// Application to work through all MP3 files and update as necessary. Plan is to update genre to allow categorisation
// of all Mike/Gail music to allow selection by genre (e.g. set all Gail to 'Pop' all Mike to 'Rock')
// This version uses the mp3agic libraries
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import com.mpatric.mp3agic.*;
public class ListDirMP3 {
    /**
     * List all the files and folders from a directory
     * @param directoryName to be listed
     * @throws IOException 
     * @throws TagException 
     * @throws InvalidDataException 
     * @throws UnsupportedTagException 
     * @throws NotSupportedException 
     */
	public static void main(String [] args) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
        File directory = new File("c:/mydocs/mike/MP3test");
        //get all the files from a directory into an array called flist
        File[] fList = directory.listFiles();
// Loop through every file in the directory
        for (File file : fList){
        	String extension = "";
        	String nameoffile = "";
        	String filelocation = "";
// Locate beginning of file extension (placement of final dot in file name)
			int i = file.getName().lastIndexOf('.');
    	    int x = file.getPath().lastIndexOf('\\');
    	    filelocation = file.getPath().substring(0, x+1);
			// If no dot in file name, value returned is -1
        	if (i > 0) {
// Get substring starting at 1 character after dot and extending to end of string
        	    extension = file.getName().substring(i+1);
        	    nameoffile = file.getName().substring(1,i);
        	}
// Converts dir+filename string to Path
            Path filename= Paths.get(file.getPath());
// read basic file attributes and ID3 tag of MP3 files - ignore other files
            if (extension.equalsIgnoreCase("mp3")) {
            	Mp3File mp3file = new Mp3File(file.getPath());
//            	if (mp3file.hasId3v1Tag()) {System.out.println("Id3V1");}
//            	if (mp3file.hasId3v2Tag()) {System.out.println("Id3v2");}
            	BasicFileAttributes attr = Files.readAttributes(filename, BasicFileAttributes.class);
            	ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            	ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            	System.out.println("The file " + file.getPath() + " with extension " + extension + " was created on " + attr.creationTime() + " and the genre tag is " + id3v1Tag.getGenre() + " with genre description " + id3v1Tag.getGenreDescription());
            	if (id3v1Tag.getGenre()==0) {
            		System.out.println("Do nothing");
            	}
            		else {
            			System.out.println("Changing genre to 0 (Blues)");
            			// genre tag successfully updates but genre description (which seems to be used by media players) does not reflect new value
            			id3v1Tag.setGenre((byte) 0);
            			id3v1Tag.setComment("Mike changed");
            			id3v2Tag.setArtist("Mikey");
            			id3v2Tag.setAlbumArtist("Pop star Mike");
            			String newfilename=filelocation+nameoffile+"X."+extension;
            			// save as new file name as cannot seem to update original
            			mp3file.save(newfilename);
            		}
            }
            else {
            	System.out.println("File is not an mp3, it is a " + extension);
            }
        }
    }
}
package Interface;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public interface Sistema <Elemento extends Objects>{

    public String createHash(String contra) throws NoSuchAlgorithmException;
    public void signUp() throws NoSuchAlgorithmException;

    public void login() throws NoSuchAlgorithmException;
    public void menu() throws NoSuchAlgorithmException, IOException;

    public void readFile() throws FileNotFoundException;
    public void writeFile() throws IOException;


}

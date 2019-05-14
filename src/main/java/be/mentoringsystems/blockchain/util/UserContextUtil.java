/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jellediet
 */
import be.mentoringsystems.blockchain.user.CAEnrollment;
import be.mentoringsystems.blockchain.user.UserContext;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hyperledger.fabric.sdk.exception.CryptoException;

/**
 *
 * @author Jelle Diet
 *
 */
public class UserContextUtil {

    /**
     * Serialize user
     *
     * @param userContext
     */
    public static void writeUserContext(UserContext userContext) {
        String directoryPath = "users/" + userContext.getAffiliation();
        String filePath = directoryPath + "/" + userContext.getName() + ".ser";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        try (FileOutputStream file = new FileOutputStream(filePath); ObjectOutputStream out = new ObjectOutputStream(file)) {

            // Method for serialization of object
            out.writeObject(userContext);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(UserContextUtil.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserContextUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Deserialize user
     *
     * @param affiliation
     * @param username
     * @return
     * @throws Exception
     */
    public static UserContext readUserContext(String affiliation, String username) throws Exception {
        String filePath = "users/" + affiliation + "/" + username + ".ser";
        File file = new File(filePath);
        if (file.exists()) {
            // Reading the object from a file
            FileInputStream fileStream = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileStream);

            // Method for deserialization of object
            UserContext uContext = (UserContext) in.readObject();

            in.close();
            fileStream.close();
            return uContext;
        }

        return null;
    }
}

//    /**
//     * Create enrollment from key and certificate files.
//     *
//     * @param folderPath
//     * @param keyFileName
//     * @param certFileName
//     * @return
//     * @throws IOException
//     * @throws NoSuchAlgorithmException
//     * @throws InvalidKeySpecException
//     * @throws CryptoException
//     */
//    public static CAEnrollment getEnrollment(String keyFolderPath, String keyFileName, String certFolderPath, String certFileName)
//            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException, CryptoException {
//        PrivateKey key = null;
//        String certificate = null;
//        InputStream isKey = null;
//        BufferedReader brKey = null;
//
//        try {
//
//            isKey = new FileInputStream(keyFolderPath + File.separator + keyFileName);
//            brKey = new BufferedReader(new InputStreamReader(isKey));
//            StringBuilder keyBuilder = new StringBuilder();
//
//            for (String line = brKey.readLine(); line != null; line = brKey.readLine()) {
//                if (!line.contains("PRIVATE")) {
//                    keyBuilder.append(line);
//                }
//            }
//
//            certificate = new String(Files.readAllBytes(Paths.get(certFolderPath, certFileName)));
//
//            byte[] encoded = DatatypeConverter.parseBase64Binary(keyBuilder.toString());
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
//            KeyFactory kf = KeyFactory.getInstance("ECDSA");
//            key = kf.generatePrivate(keySpec);
//        } finally {
//            isKey.close();
//            brKey.close();
//        }
//
//        CAEnrollment enrollment = new CAEnrollment(key, certificate);
//        return enrollment;
//    }
//
//    public static void cleanUp() {
//        String directoryPath = "users";
//        File directory = new File(directoryPath);
//        deleteDirectory(directory);
//    }
//
//    public static boolean deleteDirectory(File dir) {
//        if (dir.isDirectory()) {
//            File[] children = dir.listFiles();
//            for (File children1 : children) {
//                boolean success = deleteDirectory(children1);
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        // either file or an empty directory
//        Logger.getLogger(UserContextUtil.class.getName()).log(Level.INFO, "Deleting - " + dir.getName());
//        return dir.delete();
//    }
//
//}

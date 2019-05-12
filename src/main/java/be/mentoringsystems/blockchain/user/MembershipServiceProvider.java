/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.user;

import be.mentoringsystems.blockchain.config.Config;
import be.mentoringsystems.blockchain.util.UserContextUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;

/**
 *
 * @author jelle
 */
public class MembershipServiceProvider {

    public static UserContext enrollAdmin(final HFCAClient hfcaClient, final String mspId, final String affiliation) throws EnrollmentException, InvalidArgumentException {

        UserContext adminUserContext = null;

        adminUserContext = new UserContext();
        adminUserContext.setName(Config.ADMIN_NAME); // admin username
        adminUserContext.setAffiliation(affiliation); // affiliation
        adminUserContext.setMspId(mspId); // org1 mspid
        Enrollment adminEnrollment = hfcaClient.enroll(Config.ADMIN_NAME, Config.ADMIN_PASSWORD); //pass admin username and password, adminpw is the default for fabric
        adminUserContext.setEnrollment(adminEnrollment);
        UserContextUtil.writeUserContext(adminUserContext); // save admin context to local file system

        return adminUserContext;

    }

    public static String registerUser(final UserContext userContext, final HFCAClient hfcaClient, final UserContext adminUserContext) {
        String enrollmentSecret = "password";
        RegistrationRequest rr;
        try {
            rr = new RegistrationRequest(userContext.name, userContext.affiliation);
            enrollmentSecret = hfcaClient.register(rr, adminUserContext);

        } catch (Exception ex) {
            Logger.getLogger(MembershipServiceProvider.class.getName()).log(Level.SEVERE, ex.getMessage());
        }
        return enrollmentSecret;
    }

    public static String enrollUser(UserContext userContext, HFCAClient hfcaClient, String enrollmentSecret) {
        try {
            Enrollment enrollment = hfcaClient.enroll(userContext.getName(), enrollmentSecret);
            userContext.setEnrollment(enrollment);
            UserContextUtil.writeUserContext(userContext);
        } catch (EnrollmentException | InvalidArgumentException ex) {
            Logger.getLogger(MembershipServiceProvider.class.getName()).log(Level.SEVERE, ex.getMessage());

        }
        return "";
    }
}

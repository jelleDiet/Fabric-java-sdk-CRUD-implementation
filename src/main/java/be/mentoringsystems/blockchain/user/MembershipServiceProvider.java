
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.mentoringsystems.blockchain.user;

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
//Not used in the application, use for registering new users
public class MembershipServiceProvider {

    public static String registerUser(final FabricUserContext userContext, final HFCAClient hfcaClient, final FabricUserContext adminUserContext) {
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

    public static String enrollUser(FabricUserContext userContext, HFCAClient hfcaClient, String enrollmentSecret) {
        try {
            Enrollment enrollment = hfcaClient.enroll(userContext.getName(), enrollmentSecret);
            userContext.setEnrollment(enrollment);
        } catch (EnrollmentException | InvalidArgumentException ex) {
            Logger.getLogger(MembershipServiceProvider.class.getName()).log(Level.SEVERE, ex.getMessage());

        }
        return "success";
    }
}

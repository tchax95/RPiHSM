package ch.bfh.ti.project1.RPiHSM.CommandLine.Command;

import ch.bfh.ti.project1.RPiHSM.API.EncryptDecrypt;
import ch.bfh.ti.project1.RPiHSM.API.SerialHelper;
import com.beust.jcommander.Parameter;

import javax.naming.OperationNotSupportedException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static ch.bfh.ti.project1.RPiHSM.CommandLine.Utils.Constants.*;

/**
 * <h1>DecryptCommand</h1>
 * Parses the command line parameters and use the {@link EncryptDecrypt} to decrypt a file on the Raspberry Pi.
 */
public class DecryptCommand implements CommandI {

    private SerialHelper serialHelper;

    private String userPath;

    @Parameter(description = PARAMETERS_LIST)//JCommander requirements (all others parameters)
    private List<String> command = new ArrayList<>();

    @Parameter(names = {NAME_COMPLETE_VALUE, NAME_SHORT_VALUE}, description = NAME_VALUE, required = true)
    private String keySetName;

    @Parameter(names = {FILE_COMPLETE_VALUE, FILE_SHORT_VALUE}, description = FILE_DECRYPT_VALUE, required = true)
    private String filePath;

    /**
     * Saves the userPath and  an instance of the {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI} for future use.
     *
     * @param userPath     user home directory on the Raspberry Pi
     * @param serialHelper an instance of {@link ch.bfh.ti.project1.RPiHSM.API.SerialHelperI}
     */
    public DecryptCommand(String userPath, SerialHelper serialHelper) {
        this.userPath = userPath;
        this.serialHelper = serialHelper;
    }

    /**
     * Decrypts a file with the received parameters on the Raspberry Pi using {@link EncryptDecrypt}.
     *
     * @return a message that describes the result of the operation (error or success)
     */
    @Override
    public String execute() {
        EncryptDecrypt ed = new EncryptDecrypt(serialHelper, userPath, keySetName, filePath);
        try {
            if (ed.decrypt()) {
                return DECRYPT_SUCCESS;
            } else {
                return DECRYPT_ERROR;
            }
        } catch (OperationNotSupportedException e) {
            return UNSUPPORTED_OPERATION;
        } catch (FileNotFoundException e) {
            return FILE_NOT_FOUND;
        }
    }

    /**
     * Returns a message that contains the right command syntax.
     *
     * @return right command syntax
     */
    @Override
    public String print() {
        return DECRYPT_COMMAND;
    }
}

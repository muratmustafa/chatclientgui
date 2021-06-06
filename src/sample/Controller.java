package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Controller {

    private User mUser;

    @FXML
    private TextField user;

    @FXML
    private TextArea screen;

    @FXML
    private Button get;

    @FXML
    private TextField rec;

    @FXML
    private Button send;

    @FXML
    private Button check;

    @FXML
    void getKeys(ActionEvent ae) throws NoSuchAlgorithmException, IOException, InterruptedException {
        String uName = user.getText();

        mUser = new User(uName,8686);
        mUser.broadcastPublicKey();
        get.setDisable(true);
    }

    @FXML
    void sendMsg(ActionEvent ae) throws Exception {
        String recName = rec.getText();
        String msg = screen.getText();
        mUser.createMessage(msg, recName);
    }

    @FXML
    void displayAllMsgs(ActionEvent ae) throws Exception {

    }
}


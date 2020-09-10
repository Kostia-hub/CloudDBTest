
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    public TableView filesOnServerTable;

    @FXML
    VBox leftPanel, rightPanel;



    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void copyBtnAction(ActionEvent actionEvent) { //кнопка копирования файла
        LeftPanelController leftPC = (LeftPanelController) leftPanel.getProperties().get("ctrl"); //из левой панели достали ссылку на контроллер левой панели
        LeftPanelController rightPC = (LeftPanelController) rightPanel.getProperties().get("ctrl");

        if (leftPC.getSelectedFilename() == null && rightPC.getSelectedFilename() == null) { //если ни в левой ни вправой панели файл не выбран
            Alert alert = new Alert(Alert.AlertType.ERROR, "Ни один файл не был выбран", ButtonType.OK);
            alert.showAndWait();
            return;
        }

//        LeftPanelController srcPC = null, dstPC = null;
//        if (leftPC.getSelectedFilename() != null) {
//            srcPC = leftPC;
//            dstPC = rightPC;
//        }
//        if (rightPC.getSelectedFilename() != null) {
//            srcPC = rightPC;
//            dstPC = leftPC;
//        }
//
//        Path srcPath = Paths.get(srcPC.getCurrentPath(), srcPC.getSelectedFilename()); //мы берем папку из источника, добавляем имя файла
//        Path dstPath = Paths.get(dstPC.getCurrentPath()).resolve(srcPath.getFileName().toString());//копируем в папку точки назначения с этим именем файла
//
//        try {
//            Files.copy(srcPath, dstPath);
//            dstPC.updateList(Paths.get(dstPC.getCurrentPath())); //после добавления нового файла обновить лист
//        } catch (IOException e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось скопировать указанный файл", ButtonType.OK);
//            alert.showAndWait();
//        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();
        System.out.println("Network started22");
        Thread t = new Thread(() -> {
            try {
                while (true) {
                    System.out.println("New Thread");
                    //AbstractMessage am = Network.readObject();
                    Object am = Network.in;
//                    if (am instanceof FileMessage) {
//                        FileMessage fm = (FileMessage) am;
//                        Files.write(Paths.get("client_storage/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
//                        refreshLocalFilesList();
//                        continue;
//                    }
//                    if (am instanceof FileListRequest) {
//                        FileListRequest fileListRequest = (FileListRequest) am;
//                        refreshLocalFilesList();
//                        continue;
//                    }
//                    if (am instanceof AuthRequest) {
//                        AuthRequest authRequest = (AuthRequest) am;
//                        if (!authRequest.isAuthOk()) {
//                            showAuthWindow(authRequest.getAuthError());
//                            return;
//                        }
////                        refreshLocalFilesList();
////                        Network.sendMsg(new FileListRequest());
//                    }
                }
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
            }finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();
    }
}
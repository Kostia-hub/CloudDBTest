import common.AbstractMessage;
import common.FileMessage;
import common.FileRequest;
import common.FileDeleteRequest;
import common.FileListRequest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    ListView<String> filesRList;

    @FXML
    VBox leftPanel, rightPanel;

    public void btnExitAction(ActionEvent actionEvent) {
        Platform.exit();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();

//        Thread t = new Thread(() -> {                                //ВЫДАЕТ 2 ОШИБКИ!!!
//            try {
//                while (true) {
//                    AbstractMessage am = Network.readObject();
//                    if (am instanceof FileMessage) {
//                        FileMessage fm = (FileMessage) am;
//                        Files.write(Paths.get("client_storage/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
//                        continue;
//                    }
//                    if (am instanceof FileListRequest) {
//                        FileListRequest fileListRequest = (FileListRequest) am;
//                        continue;
//                    }
//                }
//            } catch (ClassNotFoundException | IOException e) {
//                e.printStackTrace();
//            } finally {
//                Network.stop();
//            }
//        });
//        t.setDaemon(true);
//        t.start();
    }
    public void pressOnDownloadBtn(ActionEvent actionEvent) throws IOException, ClassNotFoundException {      //скачиваем с сервака
        if (filesRList.getSelectionModel().getSelectedItem() != null && !filesRList.getSelectionModel().getSelectedItem().equals("")) {
            Network.sendMsg(new FileRequest(filesRList.getSelectionModel().getSelectedItem()));
            AbstractMessage am = Network.readObject();
            if (am instanceof FileMessage) {
                FileMessage fm = (FileMessage) am;
                Files.write(Paths.get("Client/ClientStorage/" + fm.getFileName()), fm.getData(), StandardOpenOption.CREATE);
            }
        }
    }

    public static void updateUI(Runnable runnable) { // Взяли из логинКонтроллера
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    public void refreshRemoteFilesList(List<String> serverFileList) { //Обновляем правое окно с файлами с сервака
        updateUI(() -> {
            filesRList.getItems().clear();
            filesRList.getItems().addAll(serverFileList);
        });
    }

    public void updatesRList() { //метод обновления правого окна
        Network.sendMsg(new FileListRequest());
        AbstractMessage am = null;
        try {
            am = Network.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Не удалось обновить лист");
            alert.showAndWait();
        }
        FileListRequest fileListRequest = (FileListRequest) am;
        System.out.println(fileListRequest.getRemoteFiles());
        refreshRemoteFilesList(fileListRequest.getRemoteFiles());
    }

    public void pressUpdatesRList(ActionEvent actionEvent) { //Кнпка обновления правого окна
        updatesRList();
    }

    public void pressOnDeleteBtn(ActionEvent actionEvent) throws InterruptedException {
        if (filesRList.getSelectionModel().getSelectedItem() != null && !filesRList.getSelectionModel().getSelectedItem().equals("")) {
            Network.sendMsg(new FileDeleteRequest(filesRList.getSelectionModel().getSelectedItem()));
            Alert alert = new Alert(Alert.AlertType.INFORMATION,  "Файл: " + filesRList.getSelectionModel().getSelectedItem() +" успешно удален с сервера");
            alert.showAndWait();
            Thread.sleep(1000);
            updatesRList();                //Не работает!!
        }
    }
}
package chatroom.implement.client.ui;

import chatroom.implement.client.Client;
import chatroom.protocol.entity.Chat;
import chatroom.protocol.entity.User;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Main implements Initializable {
    @FXML
    private ListView<User> lstUser;
    @FXML
    private Menu           mnuChat;
    @FXML
    private Label          lblChat;
    @FXML
    private WebView        webChat;
    @FXML
    private TextField      txtChat;
    private Integer        usrChat = null;
    private Integer        selChat = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        lstUser.setCellFactory(userListView -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(user == null ? "" : user.getName());
            }
        });
        lstUser.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstUser.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            final User selected = lstUser.getSelectionModel().getSelectedItem();
            switchToChat(selected == null ? null : selected.getUuid(), selChat);
        });

        webChat.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED)
                webChat.getEngine().executeScript("window.scrollTo(0, document.body.scrollHeight);");
        });
    }

    public void updateUserList(Integer sel, User[] userArray) {
        if (Objects.equals(sel, selChat))
            updateUserList(userArray);
    }

    public void updateUserList(User[] userArray) {
        lstUser.getItems().clear();
        lstUser.getItems().addAll(userArray);
    }

    public void updateChatList(Chat[] chats) {
        mnuChat.getItems().removeIf(item -> item instanceof RadioMenuItem);
        mnuChat.getItems().addAll(Arrays.stream(chats)
                                        .map(chat -> {
                                            final RadioMenuItem item = new RadioMenuItem(chat.getName());
                                            item.setOnAction(event -> switchToChat(usrChat, chat.getUuid()));
                                            item.setId(String.valueOf(chat.getUuid()));
                                            item.setSelected(Objects.equals(chat.getUuid(), selChat));
                                            return (MenuItem) item;
                                        })
                                        .collect(Collectors.toList()));
        if (Arrays.stream(chats).noneMatch(chat -> Objects.equals(chat.getUuid(), selChat)))
            actionExitChat();
    }

    public void updateHistory() {
        if (selChat == null && usrChat == null)
            return;
        final StringBuilder builder = new StringBuilder();
        for (final Pair<String, String> h : selChat != null
                                            ? Client.getInstance().getHistory().get(selChat)
                                            : Client.getInstance().getP2pChat().get(usrChat))
            builder.append("<span style=\"color: blue; text-decoration: underline;\">")
                   .append(h.getKey())
                   .append("</span><br/><span>&nbsp;&nbsp;&nbsp;&nbsp;")
                   .append(h.getValue())
                   .append("</span><br/><br/>");
        webChat.getEngine().loadContent(builder.toString());
    }

    public void switchToChat(Integer usr, Integer sel) {
        if (Objects.equals(usr, usrChat) && Objects.equals(sel, selChat))
            return;
        Platform.runLater(() -> {
            if (!Objects.equals(sel, selChat)) {
                if (sel == null)
                    updateUserList(null, Client.getInstance().getUsers().values().toArray(new User[0]));
                else
                    Client.getInstance().doWithSocket(socket -> socket.requestChatInfo(sel));
            }
            usrChat = usr;
            selChat = sel;
            if (sel != null) {
                final Chat chat = Client.getInstance().getMyChats().get(sel);
                if (chat != null) {
                    lblChat.setText("会话：" + Client.getInstance().getMyChats().get(sel).getName());
                    updateHistory();
                } else if (UI.confirm("您不再此会话中，是否向该会话的创建者请求加入？")) {
                    Client.getInstance().doWithSocket(socket -> socket.requestJoinChat(sel, Client.getInstance().getMyself()));
                }
            } else if (usr != null) {
                lblChat.setText("用户：" + Client.getInstance().getUsers().get(usr).getName());
                updateHistory();
            } else {
                lblChat.setText("");
                webChat.getEngine().loadContent("");
            }
        });
    }

    public void joinedChat(Chat chat, User user) {
        if (Objects.equals(chat.getUuid(), selChat))
            lstUser.getItems().add(user);
    }

    public void quitedChat(Chat chat, User user) {
        if (Objects.equals(chat.getUuid(), selChat)) {
            lstUser.getItems().remove(user);
            if (user.getUuid() == Client.getInstance().getMyself())
                actionExitChat();
        }
    }

    public void actionInitChat() {
        final ArrayList<User> list = new ArrayList<>(lstUser.getSelectionModel().getSelectedItems());
        list.removeIf(user -> user.getUuid() == Client.getInstance().getMyself());

        if (list.isEmpty()) {
            UI.error("请选择至少一个初始会话成员");
            return;
        }

        Client.getInstance().doWithSocket(socket -> socket.requestInitChat(
                UI.input("请输入会话名称："),
                list.stream().map(User::getUuid).toArray(Integer[]::new)));
    }

    public void actionQuitChat() {
        if (selChat != null)
            Client.getInstance().doWithSocket(socket -> socket.requestQuitChat(selChat, Client.getInstance().getMyself()));
    }

    public void actionExitChat() {
        mnuChat.getItems()
               .stream()
               .filter(item -> item instanceof RadioMenuItem)
               .forEach(item -> ((RadioMenuItem) item).setSelected(false));
        switchToChat(usrChat, null);
    }

    public void actionSendMessage() {
        if (selChat == null && usrChat == null)
            return;

        Client.getInstance().doWithSocket(socket -> socket.sendMessage(selChat != null ? selChat : usrChat,
                                                                       txtChat.getText()));
        txtChat.clear();
    }

    public void actionSendFile() {
        if (selChat == null && usrChat == null)
            return;
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择上传的文件");
        final File file = fileChooser.showOpenDialog(lblChat.getScene().getWindow());
        Client.getInstance().doWithSocket(socket -> socket.sendFileMsg(selChat != null ? selChat : usrChat,
                                                                       Files.readAllBytes(file.toPath())));
    }
}

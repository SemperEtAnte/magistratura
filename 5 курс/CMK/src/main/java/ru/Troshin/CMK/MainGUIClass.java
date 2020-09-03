package ru.Troshin.CMK;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.Troshin.CMK.Exceptions.IllegalKeyException;
import ru.Troshin.CMK.Exceptions.SymbolNotInAlphabetException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public final class MainGUIClass extends Application
{
    private static final Map<String, Method> methods = new HashMap<>(); //Карта методов для интерфейса
    private static final Methods methodsClass = new Methods(); //Объект класса методов для рефлекции
    private ComboBox cb;
    TextArea input, key;
    TextArea output;
    private Button crip, decript, close;
    private final Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
    private double xOffset;
    private double yOffset;

    public static void main(String... args) //Вход в прогрумму
    {
        for (Method m : Methods.class.getDeclaredMethods()) //Через рефлекцию находим функции, помеченные аннотацией как методы
        {
            if (m.isAnnotationPresent(ShifMethod.class))
            {
                ShifMethod sm = m.getAnnotation(ShifMethod.class);
                methods.put(sm.name(), m);
            }
        }
        launch(args); //Запускаем


    }

    protected Void closeAll()
    {
        Platform.exit();
        System.exit(0);
        return null;
    }

    @Override
    public void start(Stage stage) throws Exception //Метод JavaFX обозначающий начало работы
    {
        alert.setHeaderText("Ошибка");
        alert.setTitle("Ошибка");

        stage.initStyle(StageStyle.UNDECORATED);
        URL url = MainGUIClass.class.getClassLoader().getResource("interface.fxml");
        if (url == null)
        {
            this.showError("Не найден interface.fxml", this::closeAll);
            return;
        }
        Parent root = FXMLLoader.load(url);
        root.setOnMousePressed((event) ->
        {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged((event) ->
        {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        input = (TextArea) root.lookup("#inputText");
        output = (TextArea) root.lookup("#outputText");
        output.setWrapText(true);
        crip = (Button) root.lookup("#shifButton");
        decript = (Button) root.lookup("#deshifButton");
        close = (Button) root.lookup("#closeButton");
        key = (TextArea) root.lookup("#keyText");
        close.setOnAction((event) ->
        {
            stage.close();
            Platform.exit();
        });
        cb = (ComboBox) root.lookup("#MethodSelector");
        cb.setItems(FXCollections.observableArrayList(methods.keySet()));
        cb.promptTextProperty().setValue("Выберите метод.");
        crip.setOnAction((event) ->
        {
            criptDecript(true);
        });
        decript.setOnAction((event) ->
        {
            criptDecript(false);
        });

        stage.show();
    }

    public void criptDecript(boolean cript) //Функция отвечающая за вызов нужного методы шифрования cript - шифровать (true) или дешифровать (false)
    {

        Object o = cb.getSelectionModel().getSelectedItem();
        if (o == null)
        {
            cb.getStyleClass().add("errored");
            showError("Выберите метод шифрования.");
        }
        else
        {
            cb.getStyleClass().remove("errored");
            key.getStyleClass().remove("errored");
            input.getStyleClass().remove("errored");
            try
            {
                String req = input.getText();
                if (req != null && !req.isEmpty())
                {
                    String k = key.getText();
                    if (req != null)
                    {
                        req = req.replaceAll("[\\s+]", "").replaceAll("[.,\\/#!$%\\^&\\*;:{}=\\-_`~()\"\']", "").toLowerCase(); //Убираю пробелы и знаки препинания
                        String res = (String) methods.get(o.toString()).invoke(methodsClass, req, k, cript);
                        output.setText(res);
                    }
                }
                else
                {
                    input.getStyleClass().add("errored");
                    output.setText("Необходимо ввести исходную строку.");
                }

            }
            catch (IllegalAccessException | InvocationTargetException e)
            {
                if (e.getCause() instanceof IllegalKeyException)
                {

                    key.getStyleClass().add("errored");
                    showError(e.getCause().getMessage());
                }
                else if (e.getCause() instanceof SymbolNotInAlphabetException)
                {
                    input.getStyleClass().add("errored");
                    showError(e.getCause().getMessage());
                }
                else
                {
                    showError(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void showError(String message)
    {
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String message, Callable<Void> action)
    {
        showError(message);
        try
        {
            action.call();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

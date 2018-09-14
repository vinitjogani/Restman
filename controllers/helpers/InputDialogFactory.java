package controllers.helpers;

import javafx.scene.control.*;

import java.util.Collection;
import java.util.Optional;

/**
 * This is a factory class for easily asking for user input by way of dialogs. You can get
 * a string, an integer, a boolean, or a choice from a list of choices.
 */
public class InputDialogFactory {

    // Instance variables
    private String title, header, content;

    /**
     * Constructs the factory with dialog titles.
     *
     * @param title   The title of the dialog.
     * @param header  The header text of the dialog.
     * @param content The content of the dialog.
     */
    public InputDialogFactory(String title, String header, String content) {
        this.title = title;
        this.header = header;
        this.content = content;
    }

    /**
     * Sets a dialog's titles.
     *
     * @param dialog The dialog to set titles of.
     */
    private void setup(Dialog dialog) {
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
    }

    /**
     * Returns a boolean based on user confirmation.
     *
     * @return Returns whether the user clicked OK on the dialog.
     */
    public boolean getConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        setup(alert);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Returns a string based on user input from dialog.
     *
     * @return Returns the string entered by user on the dialog.
     */
    public String getString() {
        TextInputDialog dialog = new TextInputDialog();
        setup(dialog);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent() && !result.get().equals("")) return result.get();
        return null;
    }

    /**
     * Returns an integer based on user input.
     *
     * @return Returns the integer entered by user on the dialog.
     */
    public Integer getInteger() {
        Double val = getDouble();
        return val != null ? val.intValue() : null;
    }

    /**
     * Returns a double based on user input.
     *
     * @return Returns the double entered by user on the dialog.
     */
    public Double getDouble() {
        String dialogResult = getString();
        if (dialogResult != null && new StringHelper().isNumeric(dialogResult)) {
            return Double.parseDouble(dialogResult);
        }
        return null;
    }

    /**
     * Returns the users choice from a list of choices.
     *
     * @param choices The choices to display in combo-box.
     * @param <T>     The type of choices.
     * @return Returns the choice made by the user on the dialog.
     */
    public <T> T getChoice(Collection<T> choices) {
        if (choices.size() > 0) {
            ChoiceDialog<T> dialog = new ChoiceDialog<>(choices.iterator().next(), choices);
            setup(dialog);
            Optional<T> result = dialog.showAndWait();
            return result.orElse(null);
        }
        return null;
    }
}

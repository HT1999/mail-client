package csci2020.group3;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

// Custom List View class to display the emails headers (sender, subject, and date sent) in a single list view.

public class EmailListView {
    public static class EmailList {
        private String from;
        private String subject;
        private Date date;
        private int id;
        private String path;

        public String getName() {
            return this.from;
        }
        public String getSubject() { return this.subject; }
        public Date getDate() { return this.date; }
        public int getId() { return this.id; }
        public String getPath() {return this.path; }

        public EmailList(String name, String subject, Date date, int id, String path) {
            super();
            this.from = name;
            this.subject = subject;
            this.date = date;
            this.id = id;
            this.path = path;
        }
    }

    static public class EmailListCell extends ListCell<EmailList> {
        private VBox content;
        private Text name;
        private Text subject;
        private Text date;

        public EmailListCell() {
            super();
            name = new Text();
            name.setFont(Font.font("Open Sans", FontWeight.EXTRA_BOLD, 12));
            name.setFill(Color.WHITE);
            subject = new Text();
            subject.setFont(Font.font("Open Sans", FontWeight.EXTRA_LIGHT, 12));
            subject.setFill(Color.WHITE);
            date = new Text();
            date.setFont(Font.font("Open Sans", FontWeight.EXTRA_LIGHT, 12));
            date.setFill(Color.WHITE);


            content = new VBox(name, subject, date);
            content.setSpacing(5);
            content.setPadding(new Insets(20, 10, 20, 10));
            content.setStyle("fx-border-style: solid inside;" +
                                "-fx-border-radius: 2;" +
                                "-fx-border-color: #424242;");
            //content = new HBox(vBox);
            //content.setSpacing(2);
        }

        @Override
        // updates current list box
        public void updateItem(EmailList item, boolean empty) {

            super.updateItem(item, empty);

            // if null, set the cells content to null
            if (item == null) {
                setGraphic(null);
            }

            // make sure item isn't empty
            else if (!empty) {
                name.setText(item.getName());
                subject.setText(item.getSubject());
                DateFormat dateFormat = new SimpleDateFormat("dd-M-yy");
                date.setText(dateFormat.format(item.getDate()));
                setGraphic(content);
            }
        }

        @Override
        public void updateSelected(boolean selected) {
            super.updateSelected(selected);
            //String style =
            content.setStyle(selected ? "-fx-background-color: #757575;" : "-fx-background-color: #212121;"
                                + "-fx-border-color: #424242");
        }
    }

}

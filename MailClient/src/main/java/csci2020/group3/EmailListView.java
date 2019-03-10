package csci2020.group3;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
        private HBox content;
        private Text name;
        private Text subject;
        private Text date;

        public EmailListCell() {
            super();
            name = new Text();
            subject = new Text();
            date = new Text();
            VBox vBox = new VBox(name, subject, date);
            content = new HBox(new Label("[temp]"), vBox);
            content.setSpacing(5);
        }

        @Override
        // updates current list box
        public void updateItem(EmailList item, boolean empty) {
            super.updateItem(item, empty);
            // make sure item isn't empty
            if (item != null && !empty) {
                name.setText(item.getName());
                subject.setText(item.getSubject());
                DateFormat dateFormat = new SimpleDateFormat("dd-M-yy");
                date.setText(dateFormat.format(item.getDate()));
                setGraphic(content);
            }
        }
    }

}

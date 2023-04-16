import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private static final LocalTime WORKING_TIME = LocalTime.of(8,30);
    private static JTextField outgoing;
    private static int sum;

    public static void main(String[] args) {
        go();
    }

    private static void addText(String text) {

        StringTokenizer st = new StringTokenizer(text, ";:,. ");

        LocalDate date = LocalDate.now();

        int arrivalHours = Integer.parseInt(st.nextToken()),
                arrivalMinutes = Integer.parseInt(st.nextToken()),
                departureHours = Integer.parseInt(st.nextToken()),
                departureMinutes = Integer.parseInt(st.nextToken());

        LocalTime arrival = LocalTime.of(arrivalHours, arrivalMinutes);
        LocalTime departure = LocalTime.of(departureHours, departureMinutes);
        DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd.MM");
        DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm");

        int different = (departure.getMinute() - arrival.getMinute()) - WORKING_TIME.getMinute();

        File file = Path.of("C:\\Users\\Frost\\Desktop","report.txt").toFile();

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true))) {

            String line, last = "0";

            while (null != (line = bufferedReader.readLine())) {
                last = line;
            }

            st = new StringTokenizer(last,";:,. ");
            while (st.hasMoreTokens()) {
                last = st.nextToken();
            }

            int value = Integer.parseInt(last);

            sum = different + value;

            String newLine = String.format("%s%15s%15s%15d%15d\n",
                    date.format(formatterDate),arrival.format(formatterTime),
                    departure.format(formatterTime),different,sum);

            bufferedWriter.write(newLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void go() {

        JFrame frame = new JFrame("Учет часов");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(400, 200);
        frame.setVisible(true);

    }
    public static class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
            try {
                String text = outgoing.getText();
                addText(text);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            outgoing.setText("Кол-во переработанных минут: " + sum);
//            outgoing.requestFocus();
        }
    }

}
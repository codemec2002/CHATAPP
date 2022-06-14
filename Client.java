import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

// import javax.sound.sampled.SourceDataLine;


public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    // Declare Components
    private JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);


    public Client () {
        try {
            System.out.println("Sending request to server");
            socket = new Socket("127.0.0.1",7777); // get connection
            System.out.println("Connection established");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();
            // startWriting();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("Key released " + e.getKeyCode());
                if (e.getKeyCode() == 10) {
                    // System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me : "+ contentToSend + "\n");
                    out.println((contentToSend));
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
                
            }

        });
    }

    private void createGUI() {
        // gui code

        this.setTitle("Client Messager [END]");
        this.setSize(500, 500);
        this.setLocationRelativeTo(null); //make it at center
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // on click cross, it will exit
        // coding for all components

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        
        heading.setIcon(new ImageIcon("mlogo.jpg"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // set frame Layout
        this.setLayout(new BorderLayout());

        // adding components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);





        this.setVisible(true);
    }

    public void startReading() {
        Runnable r1=()->{ //lamda expression
            System.out.println("Reader started");
            try {
                while (!socket.isClosed()) { // to read again and again
                        String message = br.readLine();
                        if (message.equals("exit")) {
                            System.out.println("Server has exited\n");
                            JOptionPane.showMessageDialog(this, "Server Terminated");
                            messageInput.setEnabled(false);
                            socket.close();
                            break;
                        }

                        // System.out.println("Server : " + message);
                        messageArea.append("Server : " + message + "\n");
                    
                }
            } catch (Exception e) {
                //TODO: handle exception
                e.printStackTrace();
            }
        };

        new Thread(r1).start();
    }

    // public void startWriting() {
    //     Runnable r2 = ()-> { // thread implementation
    //         System.out.println("Writer started");
    //         try {
    //             while (!socket.isClosed()) {
    //                     BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
    //                     String content = br1.readLine();
    //                     out.println(content);
    //                     out.flush();
    //                     if (content.equals("exit")) {
    //                         socket.close();
    //                         break;
    //                     }
    //             }
    //         } catch (Exception e) {
    //             //TODO: handle exception
    //             e.printStackTrace();
    //         }
    //     };

    //     new Thread(r2).start();
    // }
    public static void main(String[] args) {
        System.out.println("this is client... ");
        new Client();
    }
}

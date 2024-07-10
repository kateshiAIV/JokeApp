package pack;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.PrintWriter;


public class ContactForm extends JFrame {



    private JTextField name_field;
    private JTextField email_field;

    private JRadioButton bMale;
    private JRadioButton bFemale;
    
    
    private JCheckBox nsfw;
    private JCheckBox religious;
    private JCheckBox political;
    private JCheckBox sexist;
    private JCheckBox explicit;
    
    
    
    
    private JCheckBox programming;
    private JCheckBox miscellaneous;
    private JCheckBox dark;
    private JCheckBox pun;
    private JCheckBox spooky;
    private JCheckBox christmas;
    

    


    

    private JCheckBox check;
    private JCheckBox check_Programming;

    public ContactForm(){
        super("Contact Form");
        super.setBounds(200,100, 450,900);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = super.getContentPane();
        container.setLayout(new GridLayout(25,5,5,10));

        JLabel name = new JLabel("Enter your full name:");
        name_field = new JTextField("...");
        JLabel email = new JLabel("Enter your email:");
        email_field = new JTextField("...");


        container.add(name);
        container.add(name_field);
        container.add(email);
        container.add(email_field);


        bMale = new JRadioButton("Male");
        bFemale = new JRadioButton("Female");
        
        JLabel Blacklist = new JLabel("Mark your BlackList Jokes: ");
        nsfw = new JCheckBox("nsfw");
        religious = new JCheckBox("religious");
        political = new JCheckBox("political");
        sexist = new JCheckBox("sexist");
        explicit = new JCheckBox("explicit");
       
        
        container.add(Blacklist);
        container.add(nsfw);
        container.add(religious);        
        container.add(political);
        container.add(sexist);
        container.add(explicit);
        
        
        
        
        JLabel WhiteList = new JLabel("Mark your Favorite Topics: ");
        programming = new JCheckBox("programming");
        miscellaneous = new JCheckBox("miscellaneous");
        dark = new JCheckBox("dark");
        pun = new JCheckBox("pun");
        spooky = new JCheckBox("spooky");
        christmas = new JCheckBox("christmas");
       
        
        container.add(WhiteList);
        container.add(programming);
        container.add(miscellaneous);        
        container.add(dark);
        container.add(pun);
        container.add(spooky);
        container.add(christmas);
        
        
        
        
        
        check = new JCheckBox("Are you 18+?", false);
        //check_Programming = new JCheckBox("Programming", false);
        JButton sendButton = new JButton("Confirm");

        bMale.setSelected(true);


        JLabel sex = new JLabel("What is your gender: ");
        container.add(sex);
        container.add(bMale);
        container.add(bFemale);

        ButtonGroup sexGroup = new ButtonGroup();
        sexGroup.add(bMale);
        sexGroup.add(bFemale);


        container.add(check);
        //container.add(check_Programming);
        container.add(sendButton);


        sendButton.addActionListener(new sendButtonEvenManager());



    }


    private String getJoke() {
        String joke = "Could not fetch joke!";
        try {
        	String sUrl ="https://v2.jokeapi.dev/joke/";
            
            
            if(programming.isSelected() || miscellaneous.isSelected() || dark.isSelected()  || pun.isSelected() || spooky.isSelected()  || christmas.isSelected()) {
            	
            	if(programming.isSelected()) {
            		sUrl+="Programming,";
            	}
            	if(miscellaneous.isSelected()) {
            		sUrl+="Miscellaneous,";
            	}            	
            	if(dark.isSelected()) {
            		sUrl+="Dark,";
            	}
            	
            	if(pun.isSelected()) {
            		sUrl+="Pun,";
            	}
            	if(spooky.isSelected()) {
            		sUrl+="Spooky,";
            	}
            	if(christmas.isSelected()) {
            		sUrl+="Christmas,";
            	}
            	
            	sUrl = sUrl.replaceAll(",\\s*$", "");
            }
            else{
            	sUrl+="Any";
            };
            
            if(nsfw.isSelected() || religious.isSelected() || political.isSelected()  || sexist.isSelected() || explicit.isSelected()) {
            	
        		sUrl+="?blacklistFlags=";
            	
            	if(nsfw.isSelected() && check.isSelected()) {
            		sUrl+="nsfw,";
            	}
            	if(religious.isSelected() && check.isSelected()) {
            		sUrl+="religious,";
            	}            	
            	if(political.isSelected() && check.isSelected()) {
            		sUrl+="political,";
            	}
            	
            	if(sexist.isSelected() && check.isSelected() && bFemale.isSelected()) {
            		sUrl+="sexist,";
            	}
            	if(explicit.isSelected() && check.isSelected()) {
            		sUrl+="explicit,";
            	}
           	
            	sUrl = sUrl.replaceAll(",\\s*$", "");
            }

            
            URL url = new URL(sUrl);
            
            
            
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }

            conn.disconnect();

            // Simple JSON parsing to extract the joke
            String jsonString = output.toString();
            System.out.println("Received JSON: " + jsonString); // Debugging print
            try (PrintWriter pw = new PrintWriter("JSONExample.json")) {
                System.out.println("Successfully wrote JSON to file");
                pw.write(jsonString);
            } catch (Exception e) {
                System.err.println("Failed to write JSON to file: " + e.getMessage());
                e.printStackTrace();
            }

            // Parsing JSON response manually
            joke = parseJoke(jsonString);

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
        return joke;
    }

    private static String parseJoke(String jsonString) {
        String joke = "Could not fetch joke!";
        try {
            int typeIndex = jsonString.indexOf("\"type\":");
            if (typeIndex != -1) {
                int typeStart = jsonString.indexOf("\"", typeIndex + 7) + 1;
                int typeEnd = jsonString.indexOf("\"", typeStart);
                String type = jsonString.substring(typeStart, typeEnd);
                if (type.equals("single")) {
                    int jokeIndex = jsonString.indexOf("\"joke\":");
                    if (jokeIndex != -1) {
                        int jokeStart = jsonString.indexOf("\"", jokeIndex + 7) + 1;
                        int jokeEnd = jsonString.indexOf("\"", jokeStart);
                        joke = jsonString.substring(jokeStart, jokeEnd);
                    }
                } else if (type.equals("twopart")) {
                    int setupIndex = jsonString.indexOf("\"setup\":");
                    int deliveryIndex = jsonString.indexOf("\"delivery\":");
                    if (setupIndex != -1 && deliveryIndex != -1) {
                        int setupStart = jsonString.indexOf("\"", setupIndex + 8) + 1;
                        int setupEnd = jsonString.indexOf("\"", setupStart);
                        String setup = jsonString.substring(setupStart, setupEnd);

                        int deliveryStart = jsonString.indexOf("\"", deliveryIndex + 11) + 1;
                        int deliveryEnd = jsonString.indexOf("\"", deliveryStart);
                        String delivery = jsonString.substring(deliveryStart, deliveryEnd);

                        joke = setup + " " + delivery;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing joke: " + e.getMessage());
            e.printStackTrace();
        }
        return joke;
    }


    class sendButtonEvenManager implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            String name = name_field.getText();
            String email = email_field.getText();


            String sex = "Male";
            if(!bMale.isSelected()){
                sex = "Female";
            }

            Boolean aged = check.isSelected();

            String joke = getJoke();



            JOptionPane.showMessageDialog(null, "\nJoke:  " + joke,"Hello, " + name + ", Thereis your joke!", JOptionPane.PLAIN_MESSAGE);


        }

    }

}

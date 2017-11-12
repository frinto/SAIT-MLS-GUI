import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.event.*;

import saitMLS.exceptions.clientale.InvalidPhoneNumberException;
import saitMLS.exceptions.clientale.InvalidPostalCodeException;
import saitMLS.exceptions.property.InvalidLegalDescriptionException;
import saitMLS.exceptions.property.InvalidNumberOfBathroomsException;
import saitMLS.persistence.clientale.ClientBroker;
import saitMLS.persistence.property.CommercialPropertyBroker;
import saitMLS.problemDomain.clientale.Client;
import saitMLS.persistence.property.ResidentialPropertyBroker;
import saitMLS.problemDomain.property.CommercialProperty;
import saitMLS.problemDomain.property.ResidentialProperty;

/*
 * Created by JFormDesigner on Wed Feb 22 15:17:23 MST 2017
 */

/**
 * This is the JavaGUI class which contains 99.999% of logic
 * and is the bulk of the program. Upon instantiation this class
 * will create a GUI window to which a person can manage clients,
 * residential and commercial property
 *
 * @author Huy Le
 */
public class JavaGUI extends JFrame
{
    /**
     * button group for radio buttons on the client panel
     */
    private ButtonGroup groupForClients = new ButtonGroup();

    /**
     * button group for radio buttons on the residential panel
     */
    private ButtonGroup groupForResidential = new ButtonGroup();

    /**
     * button group for radio buttons on the commercial panel
     */
    private ButtonGroup groupForCommercial = new ButtonGroup();

    /**
     * List model for the clients
     */
    private DefaultListModel<String> listModel = new DefaultListModel<String>();

    /**
     * list model for residential
     */
    private DefaultListModel<String> listModelResidential = new DefaultListModel<String>();

    /**
     * list model for commercial
     */
    private DefaultListModel<String> listModelCommercial = new DefaultListModel<String>();

    /**
     * client broker object to work with the backend library and datas
     */
    private ClientBroker clientBroker = ClientBroker.getBroker();

    /**
     * client object to hold information about clients
     */
    private Client client;

    /**
     * residential property broker object user to work with backend library and data
     */
    private ResidentialPropertyBroker residentialBroker = ResidentialPropertyBroker.getBroker();

    /**
     * contains information about the residential property
     */
    private ResidentialProperty residentialProperty = new ResidentialProperty();

    /**
     * contains information about the residential property and is needed to get the save and delete function to work
     */
    private ResidentialProperty residentialProperty2;

    /**
     * commercial property broker object used to work with backend library and data
     */
    private CommercialPropertyBroker commercialBroker = CommercialPropertyBroker.getBroker();

    /**
     * contains information about commercial property
     */
    private CommercialProperty commercialProperty = new CommercialProperty();

    /**
     * contains information about commercial property is needed to get save and delete functionality to work
     */
    private CommercialProperty commercialProperty2 = new CommercialProperty();

    /**
     * URL object used to point to the music file
     */
    private URL url = JavaGUI.class.getResource("natureOfDayLight.wav");

    /**
     * AudioClip object used to play the music at the url
     */
    private AudioClip clip = Applet.newAudioClip(url);

    /**
     * constructor responsible for initialising components, gui and everything else.
     */
    public JavaGUI()
    {
        super("SAIT MLS Inventory Manager BY HUY LE");
        initComponents();

        Image img = new ImageIcon(this.getClass().getResource("/pic.jpg")).getImage();
        image.setIcon(new ImageIcon(img));
    }


    /**
     * radio buttons on the clients panel
     */
    private void radioButtonGroupClients()
    {
        groupForClients.add(radioClientID2);
        groupForClients.add(radioLastName2);
        groupForClients.add(radioClientType2);
    }

    /**
     * radio buttons on the residential panel
     */
    private void radioButtonGroupResidential()
    {
        groupForResidential.add(radioResLegalDescription);
        groupForResidential.add(radioResPrice);
        groupForResidential.add(radioResPropertyID);
        groupForResidential.add(radioResQuadrantOfCity);
    }

    /**
     * radio buttons on the commercial panel
     */
    private void radioButtonGroupCommercial()
    {
        groupForCommercial.add(radioComDescription);
        groupForCommercial.add(radioComID);
        groupForCommercial.add(radioComPropertyPrice);
        groupForCommercial.add(radioComQuadrant);
    }

    /**
     * the main button at the top
     */
    private void mainButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        panel3.removeAll();
        panel3.add(mainPanel);
        mainPanel.setVisible(true);
        clientPanel.setVisible(false);
        residentialPanel.setVisible(false);
        commercialPanel.setVisible(false);
    }

    /**
     * the client button at the top
     */
    private void clientButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        panel3.removeAll();
        panel3.add(clientPanel);

        mainPanel.setVisible(false);
        clientPanel.setVisible(true);
        residentialPanel.setVisible(false);
        commercialPanel.setVisible(false);
    }

    /**
     * the residential button at the top
     */
    private void residentialButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        panel3.removeAll();
        panel3.add(residentialPanel);

        mainPanel.setVisible(false);
        clientPanel.setVisible(false);
        residentialPanel.setVisible(true);
        commercialPanel.setVisible(false);
    }

    /**
     * the commercial button at the top
     */
    private void commercialButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        panel3.removeAll();
        panel3.add(commercialPanel);

        mainPanel.setVisible(false);
        clientPanel.setVisible(false);
        residentialPanel.setVisible(false);
        commercialPanel.setVisible(true);
    }

    /**
     * the search button in the clients panel
     */
    private void searchClientButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        String searchTextBox = textFieldSearchClient.getText();

        String validType = "x";

        if (radioClientID2.isSelected())
        {
            validType = "id";
        } else if (radioLastName2.isSelected())
        {
            validType = "name";
        } else
        {
            validType = "type";
        }

        searchListClient.setModel(listModel);


        if (searchTextBox.equals(""))
        {
            JOptionPane.showMessageDialog(null, "you must fill in the Search Parameters");
        }

        if ((radioClientID2.isSelected() == false) && (radioLastName2.isSelected() == false) && (radioClientType2.isSelected() == false))
        {
            JOptionPane.showMessageDialog(null, "you must select the type of search to be performed");
        }

        Object[] clientsPeople = clientBroker.search(searchTextBox, validType).toArray();

        String toSplit;
        String splitted[];

        String stringId;
        String name;
        String address;
        String postalCode;
        String phoneNum;
        String clientTypeString;
        String status;

        String stringIdCleaned;
        String nameCleaned;
        String addressCleaned;
        String postalCodeCleaned;
        String phoneNumCleaned;
        String clientTypeStringCleaned;
        String statusCleaned;

        String finalToBeShownInClientListBox;


        for (int i = 0; i < clientsPeople.length; i++)
        {

            toSplit = clientsPeople[i].toString();
            splitted = toSplit.split("[:\\n]");

            stringId = splitted[1];
            name = splitted[3];
            address = splitted[5];
            postalCode = splitted[7];
            phoneNum = splitted[9];
            clientTypeString = splitted[11].toUpperCase();
            status = splitted[12];

            //used to remove leading and trailing whitespace
            stringIdCleaned = stringId.trim();
            nameCleaned = name.trim();
            addressCleaned = address.trim();
            postalCodeCleaned = postalCode.trim();
            phoneNumCleaned = phoneNum.trim();
            clientTypeStringCleaned = clientTypeString.trim();
            statusCleaned = status.trim();

            finalToBeShownInClientListBox = stringIdCleaned + " " + nameCleaned + " " + clientTypeStringCleaned;

            listModel.addElement(finalToBeShownInClientListBox);

        }

    }

    /**
     * the clear button in the clients panel
     */
    private void clearSearchClientButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        listModel.removeAllElements();
    }

    /**
     * the selection action in the client list box
     */
    private void searchListClientValueChanged(ListSelectionEvent e)
    {
        // TODO add your code here

        String stringId;
        String name;
        String address;
        String postalCode;
        String phoneNum;
        String clientTypeString;
        String status;

        String stringIdCleaned;
        String nameCleaned;
        String addressCleaned;
        String postalCodeCleaned;
        String phoneNumCleaned;
        String clientTypeStringCleaned;
        String statusCleaned;

        String[] nameCleanedSplitted;
        String firstname;
        String lastname;

        String selectedToBeSplit;

        int selectedIndex = searchListClient.getSelectedIndex();
        if (selectedIndex != -1)
        {
            selectedToBeSplit = listModel.get(selectedIndex);

            String[] splitted1 = selectedToBeSplit.split("[\\s]");

            String idOfSelectedPersonString = splitted1[0];

            String allInfoAboutSelectedClient = clientBroker.search(idOfSelectedPersonString, "id").toString();

            String[] splitted2 = allInfoAboutSelectedClient.split("[:\\n]");

            stringId = splitted2[1];
            name = splitted2[3];
            address = splitted2[5];
            postalCode = splitted2[7];
            phoneNum = splitted2[9];
            clientTypeString = splitted2[11].toUpperCase();
            status = splitted2[12];

            //used to remove leading and trailing whitespace
            stringIdCleaned = stringId.trim();
            nameCleaned = name.trim();
            addressCleaned = address.trim();
            postalCodeCleaned = postalCode.trim();
            phoneNumCleaned = phoneNum.trim();
            clientTypeStringCleaned = clientTypeString.trim();
            statusCleaned = status.trim();

            nameCleanedSplitted = nameCleaned.split("[\\s]");
            firstname = nameCleanedSplitted[0];
            lastname = nameCleanedSplitted[1];

            clientIDTextField2.setText(stringIdCleaned);
            firstNameTextField2.setText(firstname);
            lastNameTextField2.setText(lastname);
            addressTextField2.setText(addressCleaned);
            postalCodeTextField2.setText(postalCodeCleaned);
            phoneNumClientTextField.setText(phoneNumCleaned);
            textFieldForClientType.setText(clientTypeStringCleaned);

        }


    }

    /**
     * the save button in the clients panel
     */
    private void saveClientButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        Long id = Long.parseLong(clientIDTextField2.getText());
        char clientType = textFieldForClientType.getText().charAt(0);
        try
        {
            client = new Client(id, firstNameTextField2.getText(),
                    lastNameTextField2.getText(),
                    addressTextField2.getText(),
                    postalCodeTextField2.getText(),
                    phoneNumClientTextField.getText(), clientType);

            if (clientBroker.persist(client))
            {
                JOptionPane.showMessageDialog(null, "Success!!");
            } else
            {
                JOptionPane.showMessageDialog(null, "error changes were not saved make sure input is correct");
            }

        } catch (InvalidPhoneNumberException e1)
        {
            JOptionPane.showMessageDialog(null, "Phone number is not valid in format 000-000-0000");
        } catch (InvalidPostalCodeException e1)
        {
            JOptionPane.showMessageDialog(null, "Postal code not valid in format A0A 0A0");
        }

        clientBroker.closeBroker();

    }

    /**
     * the delete button in the clients panel
     */
    private void deleteClientButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        Long id = Long.parseLong(clientIDTextField2.getText());
        char clientType = textFieldForClientType.getText().charAt(0);
        try
        {
            client = new Client(id, firstNameTextField2.getText(),
                    lastNameTextField2.getText(),
                    addressTextField2.getText(),
                    postalCodeTextField2.getText(),
                    phoneNumClientTextField.getText(), clientType);

            if (clientBroker.remove(client))
            {
                JOptionPane.showMessageDialog(null, "Success!!");
            } else
            {
                JOptionPane.showMessageDialog(null, "error delete were not saved make sure input is correct");
            }

        } catch (InvalidPhoneNumberException e1)
        {
            JOptionPane.showMessageDialog(null, "Phone number is not valid in format 000-000-0000");
        } catch (InvalidPostalCodeException e1)
        {
            JOptionPane.showMessageDialog(null, "Postal code not valid in format A0A 0A0");
        }

        clientBroker.closeBroker();

    }

    /**
     * the clear button in the clients panel
     */
    private void clearClientInfoButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        clientIDTextField2.setText("");
        firstNameTextField2.setText("");
        lastNameTextField2.setText("");
        addressTextField2.setText("");
        postalCodeTextField2.setText("");
        phoneNumClientTextField.setText("");
        textFieldForClientType.setText("");
    }

    /**
     * the search button in the residential panel
     */
    private void searchButtonResActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        String searchTextBox = textFieldSearchRes.getText();

        String validType = "x";

        if (radioResLegalDescription.isSelected())
        {
            validType = "legal description";
        } else if (radioResPropertyID.isSelected())
        {
            validType = "id";
        } else if (radioResPrice.isSelected())
        {
            validType = "price";
        } else
        {
            validType = "quadrant";
        }

        searchListResidential.setModel(listModelResidential);


        if (searchTextBox.equals(""))
        {
            JOptionPane.showMessageDialog(null, "you must fill in the Search Parameters");
        }

        if ((radioResLegalDescription.isSelected() == false) && (radioResPropertyID.isSelected() == false) && (radioResPrice.isSelected() == false) && (radioResQuadrantOfCity.isSelected() == false))
        {
            JOptionPane.showMessageDialog(null, "you must select the type of search to be performed");
        }


        Object[] resPropertyArray = residentialBroker.search(searchTextBox, validType).toArray();

        String toSplit;
        String splitted[];

        Long Id;
        String legalDescription;
        String address;
        String quadrant;
        double askingPrice;
        String comments;
        double area;
        double numBathroom;
        int numBedRoom;
        char garageType;

        String finalToBeShownInClientListBox;

        for (int i = 0; i < resPropertyArray.length; i++)
        {
            residentialProperty = (ResidentialProperty) resPropertyArray[i];

            Id = residentialProperty.getId();
            legalDescription = residentialProperty.getLegalDescription();
            address = residentialProperty.getAddress();
            quadrant = residentialProperty.getQuadrant();
            askingPrice = residentialProperty.getAskingPrice();
            comments = residentialProperty.getComments();
            area = residentialProperty.getArea();
            numBathroom = residentialProperty.getBathrooms();
            numBedRoom = residentialProperty.getBedrooms();
            garageType = residentialProperty.getGarage();

            listModelResidential.addElement(Id + " " + legalDescription + " " + quadrant + " " + askingPrice);
        }

    }

    /**
     * the clear button in the residential panel
     */
    private void clearButtonResActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        listModelResidential.clear();
    }

    /**
     * the listbox selection action in the residential panel
     */
    private void searchListResidentialValueChanged(ListSelectionEvent e)
    {
        // TODO add your code here

        Long Id;
        String legalDescription;
        String address;
        String quadrant;
        double askingPrice;
        String comments;
        double area;
        double numBathroom;
        int numBedRoom;
        char garageType;
        String zone;

        String selectedItem;
        String id;

        int selectedIndex = searchListResidential.getSelectedIndex();
        if (selectedIndex != -1)
        {
            selectedItem = listModelResidential.get(selectedIndex);

            String[] theIdToSearchFor = selectedItem.split("\\s");
            id = theIdToSearchFor[0];


            Object[] resPropertyArray = residentialBroker.search(id, "id").toArray();

            for (int i = 0; i < resPropertyArray.length; i++)
            {
                residentialProperty = (ResidentialProperty) resPropertyArray[i];

                Id = residentialProperty.getId();
                legalDescription = residentialProperty.getLegalDescription();
                address = residentialProperty.getAddress();
                quadrant = residentialProperty.getQuadrant();
                askingPrice = residentialProperty.getAskingPrice();
                comments = residentialProperty.getComments();
                area = residentialProperty.getArea();
                numBathroom = residentialProperty.getBathrooms();
                numBedRoom = residentialProperty.getBedrooms();
                garageType = residentialProperty.getGarage();
                zone = residentialProperty.getZone();


                resPropertyIDTextField.setText(Long.toString(Id));
                resDescriptionTextField.setText(legalDescription);
                resAddressTextField.setText(address);
                resCityTextField.setText(quadrant);
                resZoningTextField.setText(zone);
                resAskingPriceTextField.setText(Double.toString(askingPrice));
                resSqrFtTextField.setText(Double.toString(area));
                resNumBathroomTextField.setText(Double.toString(numBathroom));
                resNumBedTextField.setText(Integer.toString(numBedRoom));
                textFieldForResGarageType.setText(Character.toString(garageType));
                resCommentsTextField.setText(comments);
            }


        }

    }

    /**
     * the save button in the residential panel
     */
    private void resSaveButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        Long id = Long.parseLong(resPropertyIDTextField.getText());
        double askingPrice = Double.parseDouble(resAskingPriceTextField.getText());
        double area = Double.parseDouble(resSqrFtTextField.getText());
        double numBath = Double.parseDouble(resNumBathroomTextField.getText());
        int numBed = Integer.parseInt(resNumBedTextField.getText());
        char garType = textFieldForResGarageType.getText().charAt(0);

        try
        {
            residentialProperty2 = new ResidentialProperty(id, resDescriptionTextField.getText(),
                    resAddressTextField.getText(),
                    resCityTextField.getText(),
                    resZoningTextField.getText(),
                    askingPrice,
                    resCommentsTextField.getText(),
                    area,
                    numBath,
                    numBed,
                    garType);

            if (residentialBroker.persist(residentialProperty2))
            {
                JOptionPane.showMessageDialog(null, "Success!!");
            } else
            {
                JOptionPane.showMessageDialog(null, "error changes were not saved make sure input is correct");
            }

        } catch (InvalidLegalDescriptionException e1)
        {
            JOptionPane.showMessageDialog(null, "invalid legal description");
        } catch (InvalidNumberOfBathroomsException e1)
        {
            JOptionPane.showMessageDialog(null, "invalid number of bathrooms");
        }

        residentialBroker.closeBroker();
    }

    /**
     * the delete button in the residential panel
     */
    private void resDeleteButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        Long id = Long.parseLong(resPropertyIDTextField.getText());
        double askingPrice = Double.parseDouble(resAskingPriceTextField.getText());
        double area = Double.parseDouble(resSqrFtTextField.getText());
        double numBath = Double.parseDouble(resNumBathroomTextField.getText());
        int numBed = Integer.parseInt(resNumBedTextField.getText());
        char garType = textFieldForResGarageType.getText().charAt(0);

        try
        {
            residentialProperty2 = new ResidentialProperty(id, resDescriptionTextField.getText(),
                    resAddressTextField.getText(),
                    resCityTextField.getText(),
                    resZoningTextField.getText(),
                    askingPrice,
                    resCommentsTextField.getText(),
                    area,
                    numBath,
                    numBed,
                    garType);

            if (residentialBroker.remove(residentialProperty2))
            {
                JOptionPane.showMessageDialog(null, "Success!!");
            } else
            {
                JOptionPane.showMessageDialog(null, "error changes were not deleted make sure input is correct");
            }

        } catch (InvalidLegalDescriptionException e1)
        {
            JOptionPane.showMessageDialog(null, "invalid legal description");
        } catch (InvalidNumberOfBathroomsException e1)
        {
            JOptionPane.showMessageDialog(null, "invalid number of bathrooms");
        }

        residentialBroker.closeBroker();

    }

    /**
     * the clear button in the residential panel
     */
    private void resClearButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        resPropertyIDTextField.setText("");
        resDescriptionTextField.setText("");
        resAddressTextField.setText("");
        resCityTextField.setText("");
        resZoningTextField.setText("");
        resAskingPriceTextField.setText("");
        resSqrFtTextField.setText("");
        resNumBathroomTextField.setText("");
        resNumBedTextField.setText("");
        textFieldForResGarageType.setText("");
        resCommentsTextField.setText("");
    }

    /**
     * play music button on the main panel
     */
    private void playMusicButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        clip.loop();
    }

    /**
     * stop music button on the main panel
     */
    private void stopMusicButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        clip.stop();
    }

    /**
     * the search button in the commercial panel
     */
    private void comSearchButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        // TODO add your code here

        String searchTextBox = comSearchTextField.getText();

        String validType = "x";

        if (radioComDescription.isSelected())
        {
            validType = "legal description";
        } else if (radioComID.isSelected())
        {
            validType = "id";
        } else if (radioComPropertyPrice.isSelected())
        {
            validType = "price";
        } else
        {
            validType = "quadrant";
        }

        comSearchList.setModel(listModelCommercial);


        if (searchTextBox.equals(""))
        {
            JOptionPane.showMessageDialog(null, "you must fill in the Search Parameters");
        }

        if ((radioComID.isSelected() == false) && (radioComPropertyPrice.isSelected() == false) && (radioComDescription.isSelected() == false) && (radioComQuadrant.isSelected() == false))
        {
            JOptionPane.showMessageDialog(null, "you must select the type of search to be performed");
        }


        Object[] commercialPropertyArray = commercialBroker.search(searchTextBox, validType).toArray();

        long Id;
        String legalDescription;
        String address;
        String quadrant;
        String comZoning;
        double askingPrice;
        String comPropertyType;
        int numFloors;
        String comments;

        String finalToBeShownInClientListBox;

        for (int i = 0; i < commercialPropertyArray.length; i++)
        {
            commercialProperty = (CommercialProperty) commercialPropertyArray[i];

            Id = commercialProperty.getId();
            legalDescription = commercialProperty.getLegalDescription();
            address = commercialProperty.getAddress();
            quadrant = commercialProperty.getQuadrant();
            comZoning = commercialProperty.getZone();
            askingPrice = commercialProperty.getAskingPrice();
            comPropertyType = commercialProperty.getType();
            numFloors = commercialProperty.getNoFloors();
            comments = commercialProperty.getComments();


            listModelCommercial.addElement(Id + " " + legalDescription + " " + quadrant + " " + askingPrice);
        }
    }

    /**
     * the clear button in the commercial panel
     */
    private void comClearSearchButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here
        listModelCommercial.clear();
    }

    /**
     * the list box selection action in the commercial panel
     */
    private void comSearchListValueChanged(ListSelectionEvent e)
    {
        // TODO add your code here

        long Id;
        String legalDescription;
        String address;
        String quadrant;
        String comZoning;
        double askingPrice;
        String comPropertyType;
        int numFloors;
        String comments;

        String selectedItem;
        String id;

        int selectedIndex = comSearchList.getSelectedIndex();
        if (selectedIndex != -1)
        {
            selectedItem = listModelCommercial.get(selectedIndex);

            String[] theIdToSearchFor = selectedItem.split("\\s");
            id = theIdToSearchFor[0];


            Object[] comPropertyArray = commercialBroker.search(id, "id").toArray();

            for (int i = 0; i < comPropertyArray.length; i++)
            {
                commercialProperty = (CommercialProperty) comPropertyArray[i];

                Id = commercialProperty.getId();
                legalDescription = commercialProperty.getLegalDescription();
                address = commercialProperty.getAddress();
                quadrant = commercialProperty.getQuadrant();
                askingPrice = commercialProperty.getAskingPrice();
                comments = commercialProperty.getComments();
                numFloors = commercialProperty.getNoFloors();
                comPropertyType = commercialProperty.getType();
                comZoning = commercialProperty.getZone();


                comPropertyIDTextField.setText(Long.toString(Id));
                comLegalDescriptionTextField.setText(legalDescription);
                comAddressTextField.setText(address.toString());
                comQuadrantTextField.setText(quadrant);
                comZoningTextField.setText(comZoning);
                comAskingPriceTextField.setText(Double.toString(askingPrice));
                comCommentsTextField.setText(comments);
                comNumFloorsTextField.setText(Integer.toString(numFloors));
                textFieldForComPropertyType.setText(comPropertyType);
            }
        }

    }

    /**
     * the save button in the commercial panel
     */
    private void comSaveButtonActionPerformed(ActionEvent e)
    {
        // TODO add your code here

        Long id = Long.parseLong(comPropertyIDTextField.getText());
        double askingPrice = Double.parseDouble(comAskingPriceTextField.getText());
        int numFloors = Integer.parseInt(comNumFloorsTextField.getText());

        try
        {
            commercialProperty2 = new CommercialProperty(id, comLegalDescriptionTextField.getText(),
                    comAddressTextField.getText(),
                    comQuadrantTextField.getText(),
                    comZoningTextField.getText(),
                    askingPrice,
                    comCommentsTextField.getText(),
                    textFieldForComPropertyType.getText(),
                    numFloors);

            if (commercialBroker.persist(commercialProperty2))
            {
                JOptionPane.showMessageDialog(null, "Success!!");
            } else
            {
                JOptionPane.showMessageDialog(null, "error changes were not saved make sure input is correct");
            }

        } catch (InvalidLegalDescriptionException e1)
        {
            JOptionPane.showMessageDialog(null,"invalid legal description format try again.");
        }

        commercialBroker.closeBroker();


    }

    /**
     * the delete button in the commercial panel
     */
    private void comDeleteButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
        Long id = Long.parseLong(comPropertyIDTextField.getText());
        double askingPrice = Double.parseDouble(comAskingPriceTextField.getText());
        int numFloors = Integer.parseInt(comNumFloorsTextField.getText());

        try
        {
            commercialProperty2 = new CommercialProperty(id, comLegalDescriptionTextField.getText(),
                    comAddressTextField.getText(),
                    comQuadrantTextField.getText(),
                    comZoningTextField.getText(),
                    askingPrice,
                    comCommentsTextField.getText(),
                    textFieldForComPropertyType.getText(),
                    numFloors);

            if (commercialBroker.remove(commercialProperty2))
            {
                JOptionPane.showMessageDialog(null, "Success!!");
            } else
            {
                JOptionPane.showMessageDialog(null, "error changes were not deleted make sure input is correct");
            }

        } catch (InvalidLegalDescriptionException e1)
        {
            JOptionPane.showMessageDialog(null,"invalid legal description format try again.");
        }

        commercialBroker.closeBroker();
    }

    /**
     * the clear button in the commercial panel
     */
    private void comClearInfoButtonActionPerformed(ActionEvent e) {
        // TODO add your code here

        comPropertyIDTextField.setText("");
        comLegalDescriptionTextField.setText("");
        comAddressTextField.setText("");
        comQuadrantTextField.setText("");
        comZoningTextField.setText("");
        comAskingPriceTextField.setText("");
        comCommentsTextField.setText("");
        comNumFloorsTextField.setText("");
        textFieldForComPropertyType.setText("");
    }

    /**
     * initialises buttons, gui, layout everything awesome
     */
    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner non-commercial license
        panel1 = new JPanel();
        panel2 = new JPanel();
        mainButton = new JButton();
        clientButton = new JButton();
        residentialButton = new JButton();
        commercialButton = new JButton();
        panel3 = new JPanel();
        mainPanel = new JPanel();
        label23 = new JLabel();
        image = new JLabel();
        playMusicButton = new JButton();
        stopMusicButton = new JButton();
        clientPanel = new JPanel();
        searchClientLabel2 = new JLabel();
        label12 = new JLabel();
        radioClientID2 = new JRadioButton();
        radioLastName2 = new JRadioButton();
        radioClientType2 = new JRadioButton();
        label13 = new JLabel();
        textFieldSearchClient = new JTextField();
        searchClientButton = new JButton();
        clearSearchClientButton = new JButton();
        scrollPane2 = new JScrollPane();
        searchListClient = new JList();
        label14 = new JLabel();
        label15 = new JLabel();
        clientIDTextField2 = new JTextField();
        label16 = new JLabel();
        firstNameTextField2 = new JTextField();
        label17 = new JLabel();
        lastNameTextField2 = new JTextField();
        label18 = new JLabel();
        addressTextField2 = new JTextField();
        label19 = new JLabel();
        postalCodeTextField2 = new JTextField();
        label20 = new JLabel();
        phoneNumClientTextField = new JTextField();
        label21 = new JLabel();
        saveClientButton = new JButton();
        deleteClientButton = new JButton();
        clearClientInfoButton = new JButton();
        label22 = new JLabel();
        textFieldForClientType = new JTextField();
        residentialPanel = new JPanel();
        label25 = new JLabel();
        label27 = new JLabel();
        label28 = new JLabel();
        radioResPropertyID = new JRadioButton();
        radioResLegalDescription = new JRadioButton();
        radioResQuadrantOfCity = new JRadioButton();
        radioResPrice = new JRadioButton();
        label29 = new JLabel();
        textFieldSearchRes = new JTextField();
        searchButtonRes = new JButton();
        clearButtonRes = new JButton();
        scrollPane3 = new JScrollPane();
        searchListResidential = new JList();
        label30 = new JLabel();
        label31 = new JLabel();
        resPropertyIDTextField = new JTextField();
        label32 = new JLabel();
        resDescriptionTextField = new JTextField();
        label33 = new JLabel();
        resAddressTextField = new JTextField();
        label34 = new JLabel();
        resCityTextField = new JTextField();
        label35 = new JLabel();
        resZoningTextField = new JTextField();
        label36 = new JLabel();
        resAskingPriceTextField = new JTextField();
        label37 = new JLabel();
        resSqrFtTextField = new JTextField();
        label38 = new JLabel();
        resNumBathroomTextField = new JTextField();
        label39 = new JLabel();
        resNumBedTextField = new JTextField();
        label40 = new JLabel();
        label41 = new JLabel();
        resCommentsTextField = new JTextField();
        resSaveButton = new JButton();
        resDeleteButton = new JButton();
        resClearButton = new JButton();
        textFieldForResGarageType = new JTextField();
        commercialPanel = new JPanel();
        label26 = new JLabel();
        label42 = new JLabel();
        radioComID = new JRadioButton();
        label43 = new JLabel();
        radioComDescription = new JRadioButton();
        radioComQuadrant = new JRadioButton();
        radioComPropertyPrice = new JRadioButton();
        label44 = new JLabel();
        comSearchButton = new JButton();
        comClearSearchButton = new JButton();
        comSearchTextField = new JTextField();
        scrollPane4 = new JScrollPane();
        comSearchList = new JList();
        label45 = new JLabel();
        label46 = new JLabel();
        comPropertyIDTextField = new JTextField();
        label47 = new JLabel();
        comLegalDescriptionTextField = new JTextField();
        label48 = new JLabel();
        comAddressTextField = new JTextField();
        label49 = new JLabel();
        comQuadrantTextField = new JTextField();
        label50 = new JLabel();
        comZoningTextField = new JTextField();
        label51 = new JLabel();
        comAskingPriceTextField = new JTextField();
        label52 = new JLabel();
        label53 = new JLabel();
        comNumFloorsTextField = new JTextField();
        label54 = new JLabel();
        comCommentsTextField = new JTextField();
        comSaveButton = new JButton();
        comDeleteButton = new JButton();
        comClearInfoButton = new JButton();
        textFieldForComPropertyType = new JTextField();

        //======== this ========
        setBackground(new Color(24, 28, 24));
        Container contentPane = getContentPane();

        //======== panel1 ========
        {
            panel1.setBackground(new Color(24, 28, 24));

            //======== panel2 ========
            {
                panel2.setBackground(new Color(24, 28, 24));

                //---- mainButton ----
                mainButton.setText("Main");
                mainButton.setBackground(new Color(32, 36, 32));
                mainButton.setForeground(new Color(220, 220, 220));
                mainButton.addActionListener(e -> mainButtonActionPerformed(e));

                //---- clientButton ----
                clientButton.setText("Client");
                clientButton.setBackground(new Color(32, 36, 32));
                clientButton.setForeground(new Color(220, 220, 220));
                clientButton.addActionListener(e -> clientButtonActionPerformed(e));

                //---- residentialButton ----
                residentialButton.setText("Residential");
                residentialButton.setBackground(new Color(32, 36, 32));
                residentialButton.setForeground(new Color(220, 220, 220));
                residentialButton.addActionListener(e -> residentialButtonActionPerformed(e));

                //---- commercialButton ----
                commercialButton.setText("Commercial");
                commercialButton.setBackground(new Color(32, 36, 32));
                commercialButton.setForeground(new Color(220, 220, 220));
                commercialButton.addActionListener(e -> commercialButtonActionPerformed(e));

                GroupLayout panel2Layout = new GroupLayout(panel2);
                panel2.setLayout(panel2Layout);
                panel2Layout.setHorizontalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(mainButton, GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(clientButton, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(residentialButton, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(commercialButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                );
                panel2Layout.setVerticalGroup(
                    panel2Layout.createParallelGroup()
                        .addGroup(panel2Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(clientButton)
                                .addComponent(mainButton)
                                .addComponent(residentialButton)
                                .addComponent(commercialButton))
                            .addContainerGap())
                );
            }

            //======== panel3 ========
            {
                panel3.setLayout(new CardLayout());

                //======== mainPanel ========
                {
                    mainPanel.setBackground(new Color(24, 28, 24));

                    //---- label23 ----
                    label23.setText("Assignment 3 By Huy, Le");
                    label23.setBackground(new Color(32, 36, 32));
                    label23.setForeground(new Color(220, 220, 220));

                    //---- playMusicButton ----
                    playMusicButton.setText("Play Music");
                    playMusicButton.addActionListener(e -> playMusicButtonActionPerformed(e));

                    //---- stopMusicButton ----
                    stopMusicButton.setText("Stop Music");
                    stopMusicButton.addActionListener(e -> stopMusicButtonActionPerformed(e));

                    GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
                    mainPanel.setLayout(mainPanelLayout);
                    mainPanelLayout.setHorizontalGroup(
                        mainPanelLayout.createParallelGroup()
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGroup(mainPanelLayout.createParallelGroup()
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(image, GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE))
                                    .addGroup(mainPanelLayout.createSequentialGroup()
                                        .addGap(240, 240, 240)
                                        .addComponent(label23)
                                        .addGap(0, 266, Short.MAX_VALUE)))
                                .addContainerGap())
                            .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(playMusicButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(stopMusicButton)
                                .addContainerGap(427, Short.MAX_VALUE))
                    );
                    mainPanelLayout.setVerticalGroup(
                        mainPanelLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                                .addComponent(image, GroupLayout.PREFERRED_SIZE, 415, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label23)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(playMusicButton)
                                    .addComponent(stopMusicButton))
                                .addGap(17, 17, 17))
                    );
                }
                panel3.add(mainPanel, "card1");

                //======== clientPanel ========
                {

                    //---- searchClientLabel2 ----
                    searchClientLabel2.setText("Search Clients");
                    searchClientLabel2.setFont(searchClientLabel2.getFont().deriveFont(searchClientLabel2.getFont().getStyle() | Font.BOLD));

                    //---- label12 ----
                    label12.setText("Select Type Of Search To Be Performed:");

                    //---- radioClientID2 ----
                    radioClientID2.setText("Client ID");

                    //---- radioLastName2 ----
                    radioLastName2.setText("Last Name");

                    //---- radioClientType2 ----
                    radioClientType2.setText("Client Type");

                    //---- label13 ----
                    label13.setText("Enter Search Parameters Below:");

                    //---- searchClientButton ----
                    searchClientButton.setText("Search");
                    searchClientButton.setBackground(new Color(32, 36, 32));
                    searchClientButton.setForeground(new Color(220, 220, 220));
                    searchClientButton.addActionListener(e -> searchClientButtonActionPerformed(e));

                    //---- clearSearchClientButton ----
                    clearSearchClientButton.setText("Clear");
                    clearSearchClientButton.setBackground(new Color(32, 36, 32));
                    clearSearchClientButton.setForeground(new Color(220, 220, 220));
                    clearSearchClientButton.addActionListener(e -> clearSearchClientButtonActionPerformed(e));

                    //======== scrollPane2 ========
                    {

                        //---- searchListClient ----
                        searchListClient.setSelectedIndex(-1);
                        searchListClient.addListSelectionListener(e -> searchListClientValueChanged(e));
                        scrollPane2.setViewportView(searchListClient);
                    }

                    //---- label14 ----
                    label14.setText("Client Information");
                    label14.setFont(label14.getFont().deriveFont(label14.getFont().getStyle() | Font.BOLD));

                    //---- label15 ----
                    label15.setText("Client ID");

                    //---- clientIDTextField2 ----
                    clientIDTextField2.setEditable(false);

                    //---- label16 ----
                    label16.setText("First Name");

                    //---- label17 ----
                    label17.setText("Last Name");

                    //---- label18 ----
                    label18.setText("Address");

                    //---- label19 ----
                    label19.setText("Postal Code");

                    //---- label20 ----
                    label20.setText("Phone Number:");

                    //---- label21 ----
                    label21.setText("Client Type");

                    //---- saveClientButton ----
                    saveClientButton.setText("Save");
                    saveClientButton.setBackground(new Color(32, 36, 32));
                    saveClientButton.setForeground(new Color(220, 220, 220));
                    saveClientButton.addActionListener(e -> saveClientButtonActionPerformed(e));

                    //---- deleteClientButton ----
                    deleteClientButton.setText("Delete");
                    deleteClientButton.setBackground(new Color(32, 36, 32));
                    deleteClientButton.setForeground(new Color(220, 220, 220));
                    deleteClientButton.addActionListener(e -> deleteClientButtonActionPerformed(e));

                    //---- clearClientInfoButton ----
                    clearClientInfoButton.setText("Clear");
                    clearClientInfoButton.setBackground(new Color(32, 36, 32));
                    clearClientInfoButton.setForeground(new Color(220, 220, 220));
                    clearClientInfoButton.addActionListener(e -> clearClientInfoButtonActionPerformed(e));

                    //---- label22 ----
                    label22.setText("Client Management Screen");
                    label22.setFont(new Font("Segoe UI", Font.PLAIN, 20));

                    GroupLayout clientPanelLayout = new GroupLayout(clientPanel);
                    clientPanel.setLayout(clientPanelLayout);
                    clientPanelLayout.setHorizontalGroup(
                        clientPanelLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, clientPanelLayout.createSequentialGroup()
                                .addGroup(clientPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(clientPanelLayout.createSequentialGroup()
                                        .addGap(30, 30, 30)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addGap(73, 73, 73)
                                                .addComponent(searchClientLabel2))
                                            .addComponent(label12)
                                            .addComponent(radioClientID2)
                                            .addComponent(radioClientType2)
                                            .addComponent(label13)
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addComponent(textFieldSearchClient, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
                                                .addGap(6, 6, 6)
                                                .addComponent(searchClientButton)
                                                .addGap(6, 6, 6)
                                                .addComponent(clearSearchClientButton))
                                            .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 275, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(radioLastName2))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addComponent(label14, GroupLayout.Alignment.TRAILING)
                                            .addGroup(GroupLayout.Alignment.TRAILING, clientPanelLayout.createSequentialGroup()
                                                .addComponent(label15)
                                                .addGap(18, 18, 18)
                                                .addComponent(clientIDTextField2, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
                                            .addGroup(GroupLayout.Alignment.TRAILING, clientPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addGroup(clientPanelLayout.createSequentialGroup()
                                                    .addComponent(label17)
                                                    .addGap(6, 6, 6)
                                                    .addComponent(lastNameTextField2, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(clientPanelLayout.createSequentialGroup()
                                                    .addComponent(label18)
                                                    .addGap(20, 20, 20)
                                                    .addComponent(addressTextField2, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(clientPanelLayout.createSequentialGroup()
                                                    .addComponent(label19)
                                                    .addGap(6, 6, 6)
                                                    .addComponent(postalCodeTextField2, GroupLayout.PREFERRED_SIZE, 171, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(clientPanelLayout.createSequentialGroup()
                                                    .addComponent(saveClientButton)
                                                    .addGap(12, 12, 12)
                                                    .addComponent(deleteClientButton)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(clearClientInfoButton))
                                                .addGroup(clientPanelLayout.createSequentialGroup()
                                                    .addComponent(label20)
                                                    .addGap(6, 6, 6)
                                                    .addComponent(phoneNumClientTextField, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(clientPanelLayout.createSequentialGroup()
                                                    .addComponent(label21)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(textFieldForClientType)))
                                            .addGroup(GroupLayout.Alignment.TRAILING, clientPanelLayout.createSequentialGroup()
                                                .addComponent(label16)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(firstNameTextField2, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(clientPanelLayout.createSequentialGroup()
                                        .addContainerGap(188, Short.MAX_VALUE)
                                        .addComponent(label22)
                                        .addGap(195, 195, 195)))
                                .addGap(26, 26, 26))
                    );
                    clientPanelLayout.setVerticalGroup(
                        clientPanelLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, clientPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label22)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                                .addGroup(clientPanelLayout.createParallelGroup()
                                    .addGroup(clientPanelLayout.createSequentialGroup()
                                        .addComponent(label14)
                                        .addGap(12, 12, 12)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addComponent(label15)
                                            .addComponent(clientIDTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(36, 36, 36)
                                        .addGroup(clientPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(firstNameTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label16))
                                        .addGap(48, 48, 48)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(label17))
                                            .addComponent(lastNameTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(26, 26, 26)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(label18))
                                            .addComponent(addressTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(35, 35, 35)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(label19))
                                            .addComponent(postalCodeTextField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(36, 36, 36)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(label20))
                                            .addComponent(phoneNumClientTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(clientPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label21)
                                            .addComponent(textFieldForClientType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(19, 19, 19)
                                        .addGroup(clientPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(saveClientButton)
                                            .addComponent(deleteClientButton)
                                            .addComponent(clearClientInfoButton)))
                                    .addGroup(clientPanelLayout.createSequentialGroup()
                                        .addComponent(searchClientLabel2)
                                        .addGap(12, 12, 12)
                                        .addComponent(label12)
                                        .addGap(6, 6, 6)
                                        .addComponent(radioClientID2)
                                        .addGap(6, 6, 6)
                                        .addComponent(radioLastName2)
                                        .addGap(6, 6, 6)
                                        .addComponent(radioClientType2)
                                        .addGap(6, 6, 6)
                                        .addComponent(label13)
                                        .addGap(12, 12, 12)
                                        .addGroup(clientPanelLayout.createParallelGroup()
                                            .addGroup(clientPanelLayout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addComponent(textFieldSearchClient, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                            .addComponent(searchClientButton)
                                            .addComponent(clearSearchClientButton))
                                        .addGap(6, 6, 6)
                                        .addComponent(scrollPane2, GroupLayout.PREFERRED_SIZE, 212, GroupLayout.PREFERRED_SIZE)))
                                .addGap(6, 6, 6))
                    );
                }
                panel3.add(clientPanel, "card2");

                //======== residentialPanel ========
                {

                    //---- label25 ----
                    label25.setText("Residential Property Management");
                    label25.setFont(new Font("Segoe UI", Font.PLAIN, 20));

                    //---- label27 ----
                    label27.setText("Search Residential Property");

                    //---- label28 ----
                    label28.setText("Select Type Of Search To Perform");

                    //---- radioResPropertyID ----
                    radioResPropertyID.setText("Residential Property ID");

                    //---- radioResLegalDescription ----
                    radioResLegalDescription.setText("Residential Property Legal Description");

                    //---- radioResQuadrantOfCity ----
                    radioResQuadrantOfCity.setText("Quadrant Of City");

                    //---- radioResPrice ----
                    radioResPrice.setText("Residential Property Price");

                    //---- label29 ----
                    label29.setText("Enter Search Paremeter Below:");

                    //---- searchButtonRes ----
                    searchButtonRes.setText("Search");
                    searchButtonRes.setBackground(new Color(32, 36, 32));
                    searchButtonRes.setForeground(new Color(220, 220, 220));
                    searchButtonRes.addActionListener(e -> searchButtonResActionPerformed(e));

                    //---- clearButtonRes ----
                    clearButtonRes.setText("Clear");
                    clearButtonRes.setBackground(new Color(32, 36, 32));
                    clearButtonRes.setForeground(new Color(220, 220, 220));
                    clearButtonRes.addActionListener(e -> clearButtonResActionPerformed(e));

                    //======== scrollPane3 ========
                    {

                        //---- searchListResidential ----
                        searchListResidential.addListSelectionListener(e -> searchListResidentialValueChanged(e));
                        scrollPane3.setViewportView(searchListResidential);
                    }

                    //---- label30 ----
                    label30.setText("Residential Property Information");

                    //---- label31 ----
                    label31.setText("Residential Property ID");

                    //---- resPropertyIDTextField ----
                    resPropertyIDTextField.setEditable(false);

                    //---- label32 ----
                    label32.setText("Property Description");

                    //---- label33 ----
                    label33.setText("Property Address");

                    //---- label34 ----
                    label34.setText("City Of Quandrant");

                    //---- label35 ----
                    label35.setText("Zoning Of Property");

                    //---- label36 ----
                    label36.setText("Property Asking Price");

                    //---- label37 ----
                    label37.setText("Building Square Footage");

                    //---- label38 ----
                    label38.setText("No Of Bathrooms");

                    //---- label39 ----
                    label39.setText("No Of Bed Rooms");

                    //---- label40 ----
                    label40.setText("Garage Type");

                    //---- label41 ----
                    label41.setText("Comments of property");

                    //---- resSaveButton ----
                    resSaveButton.setText("Save");
                    resSaveButton.setBackground(new Color(32, 36, 32));
                    resSaveButton.setForeground(new Color(220, 220, 220));
                    resSaveButton.addActionListener(e -> resSaveButtonActionPerformed(e));

                    //---- resDeleteButton ----
                    resDeleteButton.setText("Delete");
                    resDeleteButton.setBackground(new Color(32, 36, 32));
                    resDeleteButton.setForeground(new Color(220, 220, 220));
                    resDeleteButton.addActionListener(e -> resDeleteButtonActionPerformed(e));

                    //---- resClearButton ----
                    resClearButton.setText("Clear");
                    resClearButton.setBackground(new Color(32, 36, 32));
                    resClearButton.setForeground(new Color(220, 220, 220));
                    resClearButton.addActionListener(e -> resClearButtonActionPerformed(e));

                    GroupLayout residentialPanelLayout = new GroupLayout(residentialPanel);
                    residentialPanel.setLayout(residentialPanelLayout);
                    residentialPanelLayout.setHorizontalGroup(
                        residentialPanelLayout.createParallelGroup()
                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                .addGroup(residentialPanelLayout.createParallelGroup()
                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                        .addGroup(residentialPanelLayout.createParallelGroup()
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addGap(157, 157, 157)
                                                .addComponent(label25))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(radioResLegalDescription))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(radioResQuadrantOfCity))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(radioResPrice))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(label29)))
                                        .addGap(0, 183, Short.MAX_VALUE))
                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(residentialPanelLayout.createParallelGroup()
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addComponent(label28)
                                                .addGap(168, 168, 168)
                                                .addComponent(label31)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(resPropertyIDTextField, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addComponent(radioResPropertyID)
                                                .addGap(194, 194, 194)
                                                .addGroup(residentialPanelLayout.createParallelGroup()
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addComponent(label33)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addComponent(resAddressTextField, GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE))
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addComponent(label32)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(resDescriptionTextField, GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addGroup(residentialPanelLayout.createParallelGroup()
                                                            .addComponent(label34)
                                                            .addComponent(label35)
                                                            .addComponent(label36))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(residentialPanelLayout.createParallelGroup()
                                                            .addComponent(resZoningTextField, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                                            .addComponent(resCityTextField, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                                                            .addComponent(resAskingPriceTextField, GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)))))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addComponent(textFieldSearchRes, GroupLayout.PREFERRED_SIZE, 149, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(searchButtonRes)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(clearButtonRes)
                                                .addGap(30, 30, 30)
                                                .addComponent(label37)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(resSqrFtTextField, GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE))
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addComponent(scrollPane3, GroupLayout.PREFERRED_SIZE, 283, GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addGroup(residentialPanelLayout.createParallelGroup()
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addComponent(label38)
                                                            .addComponent(label39))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(residentialPanelLayout.createParallelGroup()
                                                            .addComponent(resNumBedTextField, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                                                            .addComponent(resNumBathroomTextField, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)))
                                                    .addGroup(GroupLayout.Alignment.TRAILING, residentialPanelLayout.createSequentialGroup()
                                                        .addGap(0, 127, Short.MAX_VALUE)
                                                        .addComponent(resCommentsTextField, GroupLayout.PREFERRED_SIZE, 191, GroupLayout.PREFERRED_SIZE))
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addGroup(residentialPanelLayout.createParallelGroup()
                                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                                .addComponent(resSaveButton)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(resDeleteButton)
                                                                .addGap(27, 27, 27)
                                                                .addComponent(resClearButton))
                                                            .addComponent(label41))
                                                        .addGap(0, 90, Short.MAX_VALUE))
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addComponent(label40)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(textFieldForResGarageType, GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)))))))
                                .addContainerGap())
                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(label27)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                                .addComponent(label30)
                                .addGap(78, 78, 78))
                    );
                    residentialPanelLayout.setVerticalGroup(
                        residentialPanelLayout.createParallelGroup()
                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label25)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label27)
                                    .addComponent(label30))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                            .addGroup(residentialPanelLayout.createSequentialGroup()
                                                                .addGroup(residentialPanelLayout.createParallelGroup()
                                                                    .addComponent(label28)
                                                                    .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(label31)
                                                                        .addComponent(resPropertyIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(radioResPropertyID))
                                                            .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(label32)
                                                                .addComponent(resDescriptionTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(radioResLegalDescription))
                                                    .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(label33)
                                                        .addComponent(resAddressTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(radioResQuadrantOfCity))
                                            .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(label34)
                                                .addComponent(resCityTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(radioResPrice))
                                    .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label35)
                                        .addComponent(resZoningTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(residentialPanelLayout.createParallelGroup()
                                    .addComponent(label29)
                                    .addComponent(resAskingPriceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label36))
                                .addGroup(residentialPanelLayout.createParallelGroup()
                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(textFieldSearchRes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(searchButtonRes)
                                            .addComponent(clearButtonRes))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane3, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                                        .addContainerGap())
                                    .addGroup(residentialPanelLayout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(resSqrFtTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label37))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label38)
                                            .addComponent(resNumBathroomTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(resNumBedTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(label39))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label40)
                                            .addComponent(textFieldForResGarageType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label41)
                                            .addComponent(resCommentsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                                        .addGroup(residentialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(resSaveButton)
                                            .addComponent(resDeleteButton)
                                            .addComponent(resClearButton))
                                        .addGap(36, 36, 36))))
                    );
                }
                panel3.add(residentialPanel, "card3");

                //======== commercialPanel ========
                {

                    //---- label26 ----
                    label26.setText("Commercial Property Management");
                    label26.setFont(new Font("Segoe UI", Font.PLAIN, 20));

                    //---- label42 ----
                    label42.setText("Search Commercial Property");

                    //---- radioComID ----
                    radioComID.setText("Commercial Property ID");

                    //---- label43 ----
                    label43.setText("Select Search Type To Be Performed");

                    //---- radioComDescription ----
                    radioComDescription.setText("Commercial Property Legal Description");

                    //---- radioComQuadrant ----
                    radioComQuadrant.setText("Quadrant Of City");

                    //---- radioComPropertyPrice ----
                    radioComPropertyPrice.setText("Commercial Property Price");

                    //---- label44 ----
                    label44.setText("Enter Search Parameter Below");

                    //---- comSearchButton ----
                    comSearchButton.setText("Search");
                    comSearchButton.setBackground(new Color(32, 36, 32));
                    comSearchButton.setForeground(new Color(220, 220, 220));
                    comSearchButton.addActionListener(e -> comSearchButtonActionPerformed(e));

                    //---- comClearSearchButton ----
                    comClearSearchButton.setText("Clear");
                    comClearSearchButton.setBackground(new Color(32, 36, 32));
                    comClearSearchButton.setForeground(new Color(220, 220, 220));
                    comClearSearchButton.addActionListener(e -> comClearSearchButtonActionPerformed(e));

                    //======== scrollPane4 ========
                    {

                        //---- comSearchList ----
                        comSearchList.addListSelectionListener(e -> comSearchListValueChanged(e));
                        scrollPane4.setViewportView(comSearchList);
                    }

                    //---- label45 ----
                    label45.setText("Commercial Property Information");

                    //---- label46 ----
                    label46.setText("Commercial Property ID");

                    //---- comPropertyIDTextField ----
                    comPropertyIDTextField.setEditable(false);

                    //---- label47 ----
                    label47.setText("Property Legal Description");

                    //---- label48 ----
                    label48.setText("Property Address");

                    //---- label49 ----
                    label49.setText("City Quadrant");

                    //---- label50 ----
                    label50.setText("Zoning Property");

                    //---- label51 ----
                    label51.setText("Property Asking Price");

                    //---- label52 ----
                    label52.setText("Property Type");

                    //---- label53 ----
                    label53.setText("No Of Floors");

                    //---- label54 ----
                    label54.setText("Comments");

                    //---- comSaveButton ----
                    comSaveButton.setText("Save");
                    comSaveButton.setBackground(new Color(32, 36, 32));
                    comSaveButton.setForeground(new Color(220, 220, 220));
                    comSaveButton.addActionListener(e -> comSaveButtonActionPerformed(e));

                    //---- comDeleteButton ----
                    comDeleteButton.setText("Delete");
                    comDeleteButton.setBackground(new Color(32, 36, 32));
                    comDeleteButton.setForeground(new Color(220, 220, 220));
                    comDeleteButton.addActionListener(e -> comDeleteButtonActionPerformed(e));

                    //---- comClearInfoButton ----
                    comClearInfoButton.setText("Clear");
                    comClearInfoButton.setBackground(new Color(32, 36, 32));
                    comClearInfoButton.setForeground(new Color(220, 220, 220));
                    comClearInfoButton.addActionListener(e -> comClearInfoButtonActionPerformed(e));

                    GroupLayout commercialPanelLayout = new GroupLayout(commercialPanel);
                    commercialPanel.setLayout(commercialPanelLayout);
                    commercialPanelLayout.setHorizontalGroup(
                        commercialPanelLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.TRAILING, commercialPanelLayout.createSequentialGroup()
                                .addContainerGap(172, Short.MAX_VALUE)
                                .addComponent(label26)
                                .addGap(165, 165, 165))
                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                .addGroup(commercialPanelLayout.createParallelGroup()
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addGap(97, 97, 97)
                                        .addComponent(label42))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(label43))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(radioComID))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(radioComDescription))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(radioComQuadrant))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(radioComPropertyPrice))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(comSearchTextField, GroupLayout.Alignment.LEADING)
                                            .addComponent(label44, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comSearchButton)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comClearSearchButton))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(scrollPane4, GroupLayout.PREFERRED_SIZE, 302, GroupLayout.PREFERRED_SIZE)))
                                .addGroup(commercialPanelLayout.createParallelGroup()
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                                        .addComponent(label45)
                                        .addGap(75, 75, 75))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addGap(34, 34, 34)
                                        .addGroup(commercialPanelLayout.createParallelGroup()
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(comSaveButton)
                                                .addGap(41, 41, 41)
                                                .addComponent(comDeleteButton)
                                                .addGap(46, 46, 46)
                                                .addComponent(comClearInfoButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label54)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comCommentsTextField, GroupLayout.DEFAULT_SIZE, 236, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label53)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(comNumFloorsTextField, GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label52)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(textFieldForComPropertyType, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label50)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comZoningTextField, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label49)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comQuadrantTextField, GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label48)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comAddressTextField, GroupLayout.DEFAULT_SIZE, 205, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label47)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comLegalDescriptionTextField, GroupLayout.DEFAULT_SIZE, 156, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label46)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comPropertyIDTextField, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                                .addComponent(label51)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(comAskingPriceTextField, GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)))
                                        .addContainerGap())))
                    );
                    commercialPanelLayout.setVerticalGroup(
                        commercialPanelLayout.createParallelGroup()
                            .addGroup(commercialPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(label26)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label42)
                                    .addComponent(label45))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label43)
                                    .addComponent(label46)
                                    .addComponent(comPropertyIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(radioComID)
                                    .addComponent(label47)
                                    .addComponent(comLegalDescriptionTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(radioComDescription)
                                    .addComponent(label48)
                                    .addComponent(comAddressTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(radioComQuadrant)
                                    .addComponent(label49)
                                    .addComponent(comQuadrantTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(radioComPropertyPrice)
                                    .addComponent(label50)
                                    .addComponent(comZoningTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label44)
                                    .addComponent(label51)
                                    .addComponent(comAskingPriceTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(commercialPanelLayout.createParallelGroup()
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(comSearchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                            .addComponent(comSearchButton)
                                            .addComponent(comClearSearchButton))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(scrollPane4, GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                                    .addGroup(commercialPanelLayout.createSequentialGroup()
                                        .addGap(42, 42, 42)
                                        .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label52)
                                            .addComponent(textFieldForComPropertyType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(20, 20, 20)
                                        .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label53)
                                            .addComponent(comNumFloorsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(label54)
                                            .addComponent(comCommentsTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                        .addGap(28, 28, 28)
                                        .addGroup(commercialPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                            .addComponent(comSaveButton)
                                            .addComponent(comDeleteButton)
                                            .addComponent(comClearInfoButton))
                                        .addContainerGap(28, Short.MAX_VALUE))))
                    );
                }
                panel3.add(commercialPanel, "card4");
            }

            GroupLayout panel1Layout = new GroupLayout(panel1);
            panel1.setLayout(panel1Layout);
            panel1Layout.setHorizontalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(panel3, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)
            );
            panel1Layout.setVerticalGroup(
                panel1Layout.createParallelGroup()
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panel3, GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
            );
        }

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addComponent(panel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        radioButtonGroupClients();
        radioButtonGroupResidential();
        radioButtonGroupCommercial();

        clip.loop();
    }

    /**
     * Variable declarations
     */
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner non-commercial license
    private JPanel panel1;
    private JPanel panel2;
    private JButton mainButton;
    private JButton clientButton;
    private JButton residentialButton;
    private JButton commercialButton;
    private JPanel panel3;
    private JPanel mainPanel;
    private JLabel label23;
    private JLabel image;
    private JButton playMusicButton;
    private JButton stopMusicButton;
    private JPanel clientPanel;
    private JLabel searchClientLabel2;
    private JLabel label12;
    private JRadioButton radioClientID2;
    private JRadioButton radioLastName2;
    private JRadioButton radioClientType2;
    private JLabel label13;
    private JTextField textFieldSearchClient;
    private JButton searchClientButton;
    private JButton clearSearchClientButton;
    private JScrollPane scrollPane2;
    private JList searchListClient;
    private JLabel label14;
    private JLabel label15;
    private JTextField clientIDTextField2;
    private JLabel label16;
    private JTextField firstNameTextField2;
    private JLabel label17;
    private JTextField lastNameTextField2;
    private JLabel label18;
    private JTextField addressTextField2;
    private JLabel label19;
    private JTextField postalCodeTextField2;
    private JLabel label20;
    private JTextField phoneNumClientTextField;
    private JLabel label21;
    private JButton saveClientButton;
    private JButton deleteClientButton;
    private JButton clearClientInfoButton;
    private JLabel label22;
    private JTextField textFieldForClientType;
    private JPanel residentialPanel;
    private JLabel label25;
    private JLabel label27;
    private JLabel label28;
    private JRadioButton radioResPropertyID;
    private JRadioButton radioResLegalDescription;
    private JRadioButton radioResQuadrantOfCity;
    private JRadioButton radioResPrice;
    private JLabel label29;
    private JTextField textFieldSearchRes;
    private JButton searchButtonRes;
    private JButton clearButtonRes;
    private JScrollPane scrollPane3;
    private JList searchListResidential;
    private JLabel label30;
    private JLabel label31;
    private JTextField resPropertyIDTextField;
    private JLabel label32;
    private JTextField resDescriptionTextField;
    private JLabel label33;
    private JTextField resAddressTextField;
    private JLabel label34;
    private JTextField resCityTextField;
    private JLabel label35;
    private JTextField resZoningTextField;
    private JLabel label36;
    private JTextField resAskingPriceTextField;
    private JLabel label37;
    private JTextField resSqrFtTextField;
    private JLabel label38;
    private JTextField resNumBathroomTextField;
    private JLabel label39;
    private JTextField resNumBedTextField;
    private JLabel label40;
    private JLabel label41;
    private JTextField resCommentsTextField;
    private JButton resSaveButton;
    private JButton resDeleteButton;
    private JButton resClearButton;
    private JTextField textFieldForResGarageType;
    private JPanel commercialPanel;
    private JLabel label26;
    private JLabel label42;
    private JRadioButton radioComID;
    private JLabel label43;
    private JRadioButton radioComDescription;
    private JRadioButton radioComQuadrant;
    private JRadioButton radioComPropertyPrice;
    private JLabel label44;
    private JButton comSearchButton;
    private JButton comClearSearchButton;
    private JTextField comSearchTextField;
    private JScrollPane scrollPane4;
    private JList comSearchList;
    private JLabel label45;
    private JLabel label46;
    private JTextField comPropertyIDTextField;
    private JLabel label47;
    private JTextField comLegalDescriptionTextField;
    private JLabel label48;
    private JTextField comAddressTextField;
    private JLabel label49;
    private JTextField comQuadrantTextField;
    private JLabel label50;
    private JTextField comZoningTextField;
    private JLabel label51;
    private JTextField comAskingPriceTextField;
    private JLabel label52;
    private JLabel label53;
    private JTextField comNumFloorsTextField;
    private JLabel label54;
    private JTextField comCommentsTextField;
    private JButton comSaveButton;
    private JButton comDeleteButton;
    private JButton comClearInfoButton;
    private JTextField textFieldForComPropertyType;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

}

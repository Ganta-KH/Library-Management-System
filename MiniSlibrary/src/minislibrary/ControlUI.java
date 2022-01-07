package minislibrary;

import com.toedter.calendar.JDateChooser;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ControlUI {
    
    public void setBookJTableR(String searchResult[], JTable BookjTable) {
            try {

                DefaultTableModel d = (DefaultTableModel) BookjTable.getModel();
                d.setRowCount(0);
                String[] searchResultRow = null;
                
                for (int i = 0; i<searchResult.length ; i++) {
                    searchResultRow = searchResult[i].split(":::");
                    Vector v2 = new Vector();

                    v2.add(Integer.parseInt(searchResultRow[0]));
                    v2.add(Integer.parseInt(searchResultRow[1]));
                    v2.add(searchResultRow[2]);
                    v2.add(searchResultRow[3]);
                    v2.add(Long.parseLong(searchResultRow[4]));
                    v2.add(searchResultRow[5]);
                    v2.add(searchResultRow[6]);
                    v2.add(searchResultRow[7]);
                    v2.add(Long.parseLong(searchResultRow[8]));
                    v2.add(searchResultRow[9]);
                    v2.add(Integer.parseInt(searchResultRow[10]));
                    
                    d.addRow(v2);
                }
                
            } catch (Exception e) {
                System.out.println("error in setBookJTable...");
            }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void setFeildBookNull(JTextField idbookCatalogue, JTextField bookTitle, JTextField bookAutor, JTextField bookISBN, JTextField bookCategory, JDateChooser bookPurchase, JTextField bookOrderNumber, JComboBox bookStatus, JComboBox bookCat, JComboBox bookState, JTextField bookValue, JSpinner expCopy){
        idbookCatalogue.setText("");
        bookTitle.setText("");
        bookAutor.setText("");
        bookISBN.setText("");
        bookCategory.setText("");
        bookPurchase.setDate(null);
        bookOrderNumber.setText("");
        bookStatus.setSelectedIndex(0);
        bookCat.setSelectedItem(null);
        bookState.setSelectedIndex(0);
        bookValue.setText("");
        expCopy.setValue(1);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getBookId(JTable BookjTable) {
        DefaultTableModel model = (DefaultTableModel)BookjTable.getModel();
        int selectedRowIndex = BookjTable.getSelectedRow();
        
        return Integer.parseInt(model.getValueAt(selectedRowIndex, 1).toString());
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] idExpsCatalogue(String string) {
        return string.split(" , |\\ ,|\\, |\\,");
    }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchStatuType(JComboBox searchStatuType) {
        if (searchStatuType.getSelectedIndex() == 1){
            return "on the shelf";
        } else if (searchStatuType.getSelectedIndex() == 2) {
            return "loaned";
        } else if (searchStatuType.getSelectedIndex() == 3) {
            return "late";
        } else if (searchStatuType.getSelectedIndex() == 4) {
            return "reserved";
        } else if (searchStatuType.getSelectedIndex() == 5) {
            return "under construction";
        }
        return "";
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void addAllToComboBoxR(JComboBox categ, String Cat) {
        String[] category = Cat.split(":::");
        
        categ.removeAllItems();

        category = Cat.split(":::");
        for (int i= 1; i< category.length; i++) {
            categ.addItem(category[i]);
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    //---------------------------------------------------- END BOOK -----------------------------------------------------------//
    //------------------------------------------------- START DOCUMENT --------------------------------------------------------//
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setDocJTableR(String searchResult[], JTable documentjTable) {
            try {

                DefaultTableModel d = (DefaultTableModel) documentjTable.getModel();
                d.setRowCount(0);
                String[] searchResultRow = null;
                
                for (int i = 0; i<searchResult.length ; i++) {
                    searchResultRow = searchResult[i].split(":::");
                    Vector v2 = new Vector();

                    v2.add(Integer.parseInt(searchResultRow[0]));
                    v2.add(Integer.parseInt(searchResultRow[1]));
                    v2.add(searchResultRow[2]);
                    v2.add(searchResultRow[3]);
                    v2.add(Long.parseLong(searchResultRow[4]));
                    v2.add(searchResultRow[5]);
                    v2.add(searchResultRow[6]);
                    v2.add(searchResultRow[7]);
                    v2.add(searchResultRow[8]);
                    v2.add(Long.parseLong(searchResultRow[9]));
                    v2.add(searchResultRow[10]);
                    v2.add(Integer.parseInt(searchResultRow[11]));
                    
                    d.addRow(v2);
                }
                
            } catch (Exception e) {
                System.out.println("error in setDocJTable...");
            }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getDocId(JTable documentjTable) {
        DefaultTableModel model = (DefaultTableModel)documentjTable.getModel();
        int selectedRowIndex = documentjTable.getSelectedRow();
        
        return Integer.parseInt(model.getValueAt(selectedRowIndex, 1).toString());
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setFeildDocumentNull(JTextField idDocumentCatalogue, JTextField documentTitle, JTextField documentEditor, JTextField documentID, JTextField documentCategory, JDateChooser documentPucliaction, JDateChooser documentPurchase, JTextField documentOrderNumber, JComboBox documentCat , JComboBox documentStatus, JComboBox documentState, JTextField docValue, JSpinner nbrDocCopy){
        idDocumentCatalogue.setText("");
        documentTitle.setText("");
        documentEditor.setText("");
        documentID.setText("");
        documentCategory.setText("");
        documentPucliaction.setDate(null);
        documentPurchase.setDate(null);
        documentOrderNumber.setText("");
        documentCat.setSelectedItem(null);
        documentStatus.setSelectedIndex(0);
        documentState.setSelectedIndex(0);
        docValue.setText("");
        nbrDocCopy.setValue(1);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    //---------------------------------------------------- END DOCUMENT -----------------------------------------------------------//
    //----------------------------------------------------- START USER -----------------------------------------------------------//
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public void setUsersJTableR(String searchResult[], JTable UserjTable) {
            try {

                DefaultTableModel d = (DefaultTableModel) UserjTable.getModel();
                d.setRowCount(0);
                String[] searchResultRow = null;
                
                for (int i = 0; i<searchResult.length ; i++) {
                    searchResultRow = searchResult[i].split(":::");
                    Vector v2 = new Vector();

                    v2.add(Integer.parseInt(searchResultRow[0]));
                    v2.add(Long.parseLong(searchResultRow[1]));
                    v2.add(searchResultRow[2]);
                    v2.add(searchResultRow[3]);
                    v2.add(searchResultRow[4]);
                    v2.add(nullString(searchResultRow[5]));
                    v2.add(Integer.parseInt(searchResultRow[6]));
                    v2.add(Integer.parseInt(searchResultRow[7]));
                    v2.add(Integer.parseInt(searchResultRow[8]));
                    v2.add(Integer.parseInt(searchResultRow[9]));
                    v2.add(Integer.parseInt(searchResultRow[10]));
                    
                    d.addRow(v2);
                }
                
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("error in setUsersJTableR...");
            }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String nullString(String stringBan) {
        if (stringBan.equals("null")) {
            return "";
        }
        return stringBan;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public long getUserId(JTable userJTable) {
        DefaultTableModel model = (DefaultTableModel)userJTable.getModel();
        int selectedRowIndex = userJTable.getSelectedRow();
        
        return Long.parseLong(model.getValueAt(selectedRowIndex, 1).toString());
    }
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void setFeildUserNull(JTextField userID, JTextField userFirstName, JTextField userLastName, JComboBox userTypeF){
        userID.setText("");
        userFirstName.setText("");
        userLastName.setText("");
        userTypeF.setSelectedIndex(0);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String userState(String string, JComboBox userState) {
        if (string.equals("")) {
            if (userState.getSelectedIndex() == 0) {
                return "";
            } else if (userState.getSelectedIndex() == 1) {
                return " WHERE ban = 'YES'";
            } else if (userState.getSelectedIndex() == 2) {
                return " WHERE bookLoaned > 0 OR docLoaned > 0";
            } else if (userState.getSelectedIndex() == 3) {
                return " WHERE bookLate > 0 OR docLate > 0";
            }
        } else {
            if (userState.getSelectedIndex() == 0) {
                return "";
            } else if (userState.getSelectedIndex() == 1) {
                return " AND ban = 'YES'";
            } else if (userState.getSelectedIndex() == 2) {
                return " AND (bookLoaned > 0 OR docLoaned > 0)";
            } else if (userState.getSelectedIndex() == 3) {
                return " AND (bookLate > 0 OR docLate > 0)";
            }
        }
        
        return "";
    }

    public String punishUserState (String string, JComboBox userState) {
        if (string.equals("")) {
            if (userState.getSelectedIndex() == 0) {
                return "";
            } else if (userState.getSelectedIndex() == 1) {
                return " WHERE ban = 'YES'";
            } else if (userState.getSelectedIndex() == 2) {
                return " WHERE bookLate > 0 OR docLate > 0";
            } else if (userState.getSelectedIndex() == 3) {
                return " WHERE penaltyValue > 0";
            }
        } else {
            if (userState.getSelectedIndex() == 0) {
                return "";
            } else if (userState.getSelectedIndex() == 1) {
                return " AND ban = 'YES'";
            } else if (userState.getSelectedIndex() == 2) {
                return " AND (bookLate > 0 OR docLate > 0)";
            } else if (userState.getSelectedIndex() == 3) {
                return " WHERE penaltyValue > 0";
            }
        }
        return "";
    }
    
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    //----------------------------------------------------- START LOAN -----------------------------------------------------------//
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLoanBookDocJTableR(String searchResult[], JTable loanBookDocjTable) {
        try {

            DefaultTableModel d = (DefaultTableModel) loanBookDocjTable.getModel();
            d.setRowCount(0);
            String[] searchResultRow = null;
                
            for (int i = 0; i<searchResult.length ; i++) {
                searchResultRow = searchResult[i].split(":::");
                Vector v2 = new Vector();
                
                v2.add(Integer.parseInt(searchResultRow[0]));
                v2.add(Integer.parseInt(searchResultRow[1]));
                v2.add(searchResultRow[2]);
                v2.add(searchResultRow[3]);
                v2.add(searchResultRow[4]);
                v2.add(Long.parseLong(searchResultRow[5]));
                    
                d.addRow(v2);
            }
                
        } catch (Exception e) {
            System.out.println("error in setLoanBookDocJTableR...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getUserType(JTable userJTable) {
        DefaultTableModel model = (DefaultTableModel)userJTable.getModel();
        int selectedRowIndex = userJTable.getSelectedRow();
        
        return model.getValueAt(selectedRowIndex, 4).toString();
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLabelLoanBookNull(JLabel bkLoanUserID, JLabel bkLoanFirstName, JLabel bkLoanLastName, JLabel bkLoanTitle, JLabel bkLoanAutor, JLabel bkLoanIdCata, JLabel bkLoanDate, JDateChooser bkLoanReDate){
        bkLoanUserID.setText("");
        bkLoanFirstName.setText("");
        bkLoanLastName.setText("");
        bkLoanTitle.setText("");
        bkLoanAutor.setText("");
        bkLoanIdCata.setText("");
        bkLoanDate.setText("");
        bkLoanReDate.setDate(null);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLabelLoanDocNull(JLabel dcLoanUserID, JLabel dcLoanFirstName, JLabel dcLoanLastName, JLabel dcLoanTitle, JLabel dcLoanEditor, JLabel dcLoanIdCata, JLabel dcLoanDate, JDateChooser bkLoanReDate){
        dcLoanUserID.setText("");
        dcLoanFirstName.setText("");
        dcLoanLastName.setText("");
        dcLoanTitle.setText("");
        dcLoanEditor.setText("");
        dcLoanIdCata.setText("");
        dcLoanDate.setText("");
        bkLoanReDate.setDate(null);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    //----------------------------------------------------- START RETURN -----------------------------------------------------------//
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setReturnBookDocJTableR(String searchResult[], JTable returnBookDocjTable) {
        try {

            DefaultTableModel d = (DefaultTableModel) returnBookDocjTable.getModel();
            d.setRowCount(0);
            String[] searchResultRow = null;
                
            for (int i = 0; i<searchResult.length ; i++) {
                searchResultRow = searchResult[i].split(":::");
                Vector v2 = new Vector();
                
                v2.add(Integer.parseInt(searchResultRow[0]));
                v2.add(Integer.parseInt(searchResultRow[1]));
                v2.add(searchResultRow[2]);
                v2.add(searchResultRow[3]);
                v2.add(searchResultRow[4]);
                v2.add(searchResultRow[5]);
                v2.add(searchResultRow[6]);
                v2.add(Long.parseLong(searchResultRow[7]));
                v2.add(searchResultRow[8]);
                v2.add(searchResultRow[9]);
                    
                d.addRow(v2);
            }
                
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println("error in setReturnBookDocJTableR...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public boolean checkIdJTableR(JLabel idUser, JTable returnBookJTable) {
        for (int i = 0; i<returnBookJTable.getRowCount() ; i++) {
            if (returnBookJTable.getModel().getValueAt(i, 1).equals(Long.parseLong(idUser.getText()))) {
                return true;
            }
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getStateIndex(String bookState, JComboBox returnBookState) {
        for (int i = 0; i< returnBookState.getItemCount(); i++) {
            if (returnBookState.getItemAt(i).toString().equals(bookState)) {
                return i;
            }
        }
        return -1;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLabelReturnBookNull(JLabel bkreturnUserID, JLabel bkreturnFirstName, JLabel bkreturnLastName, JLabel bkreturnTitle, JLabel bkreturnAutor, JLabel bkreturnIdCata, JComboBox returnBookState, JLabel bkreturnUserType, JDateChooser bkRDate){
        bkreturnUserID.setText("");
        bkreturnFirstName.setText("");
        bkreturnLastName.setText("");
        bkreturnTitle.setText("");
        bkreturnAutor.setText("");
        bkreturnIdCata.setText("");
        returnBookState.setSelectedItem(null);
        bkRDate.setDate(null);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setBookNull(JLabel bkreturnTitle, JLabel bkreturnAutor, JLabel bkreturnIdCata, JComboBox returnBookState, JDateChooser bkRDate) {
        bkreturnTitle.setText("");
        bkreturnAutor.setText("");
        bkreturnIdCata.setText("");
        returnBookState.setSelectedItem(null);
        bkRDate.setDate(null);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLabelReturnDocNull(JLabel dcreturnUserID, JLabel dcreturnFirstName, JLabel dcreturnLastName, JLabel dcreturnTitle, JLabel dcreturnEditor, JLabel dcreturnIdCata, JComboBox returnDocState, JLabel dcreturnUserType, JDateChooser dcRDate){
        dcreturnUserID.setText("");
        dcreturnFirstName.setText("");
        dcreturnLastName.setText("");
        dcreturnTitle.setText("");
        dcreturnEditor.setText("");
        dcreturnIdCata.setText("");
        returnDocState.setSelectedItem(null);
        dcreturnUserType.setText("");
        dcRDate.setDate(null);
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setDocNull(JLabel dcreturnTitle, JLabel dcreturnEditor, JLabel dcreturnIdCata, JComboBox returnDocState, JDateChooser dcRDate) {
        dcreturnTitle.setText("");
        dcreturnEditor.setText("");
        dcreturnIdCata.setText("");
        returnDocState.setSelectedItem(null);
        dcRDate.setDate(null);
    }
    
    
    //--------------------------------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setPunishmantJTableR(String searchResult[], JTable punishmentjTable) {
        try {

            DefaultTableModel d = (DefaultTableModel) punishmentjTable.getModel();
            d.setRowCount(0);
            String[] searchResultRow = null;
                
            for (int i = 0; i<searchResult.length ; i++) {
                searchResultRow = searchResult[i].split(":::");
                Vector v2 = new Vector();
                
                v2.add(Integer.parseInt(searchResultRow[0]));
                v2.add(Long.parseLong(searchResultRow[1]));
                v2.add(searchResultRow[2]);
                v2.add(searchResultRow[3]);
                v2.add(searchResultRow[4]);
                v2.add(nullString(searchResultRow[5]));
                v2.add(searchResultRow[6]);
                v2.add(searchResultRow[7]);
                v2.add(Integer.parseInt(searchResultRow[8]));
                    
                d.addRow(v2);
            }
                
        } catch (Exception e) {
            System.out.println("error in setPunishmanetJTableR...");
        
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void setFeildUserPunishmentNull(JLabel userIdPuni, JLabel firstNamePuni, JLabel lastNamePuni, JDateChooser banStart, JDateChooser banEnd, JTextField penaltyValue){
        userIdPuni.setText("");
        firstNamePuni.setText("");
        lastNamePuni.setText("");
        banStart.setDate(null);
        banEnd.setDate(null);
        penaltyValue.setText("");
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public void setAdminsJTableR(String searchResult[], JTable UserjTable) {
            try {

                DefaultTableModel d = (DefaultTableModel) UserjTable.getModel();
                d.setRowCount(0);
                String[] searchResultRow = null;
                
                for (int i = 0; i<searchResult.length ; i++) {
                    searchResultRow = searchResult[i].split(":::");
                    Vector v2 = new Vector();

                    v2.add(Integer.parseInt(searchResultRow[0]));
                    v2.add(Long.parseLong(searchResultRow[1]));
                    v2.add(searchResultRow[2]);
                    v2.add(searchResultRow[3]);
                    v2.add(searchResultRow[4]);
                    v2.add(searchResultRow[5]);
                    
                    d.addRow(v2);
                }
                
            } catch (Exception e) {
                System.out.println("error in setAdminsJTableR...");
            }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public long getAdminId(JTable adminJTable) {
        DefaultTableModel model = (DefaultTableModel)adminJTable.getModel();
        int selectedRowIndex = adminJTable.getSelectedRow();
        
        return Long.parseLong(model.getValueAt(selectedRowIndex, 1).toString());
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getAdminType(JTable adminJTable) {
        DefaultTableModel model = (DefaultTableModel)adminJTable.getModel();
        int selectedRowIndex = adminJTable.getSelectedRow();
        
        return model.getValueAt(selectedRowIndex, 5).toString();
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean canEditAdmin(String adminType) {
        if(adminType.equals("Trainee")) {
            return true;
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//

    public void setFeildAdminNull(JTextField adminID, JTextField adminUsername, JTextField adminFirstName, JTextField adminLastName, JPasswordField adminPassword, JPasswordField adminPasswordC, JComboBox adminType){
        adminID.setText("");
        adminUsername.setText("");
        adminFirstName.setText("");
        adminLastName.setText("");
        adminPassword.setText("");
        adminPasswordC.setText("");
        adminType.setSelectedItem(null);
    }
}

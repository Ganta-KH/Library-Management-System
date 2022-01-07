package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Document {
    int id_document;
    String title;
    String editor;
    long ID;
    String category;
    String status;
    String documentState;
    Date publicationDate;
    Date purchaseDate;
    long orderNumber;
    int docValue;
            
    //-----------------------------------------------------------------------------------------------------------------------//
    
    public Document() {
        this.id_document = 0;
        this.title = "";
        this.editor = "";
        this.ID = 0;
        this.category = "";
        this.status = "";
        this.documentState = "";
        this.publicationDate = null;
        this.purchaseDate = null;
        this.orderNumber = 0;
        this.docValue = 0;
    }
    
                 //****************************************//
    
    public Document(int  id_document, String title, String editor, long ID, String category, String status, String documentState, Date publicationDate, Date purchaseDate,long orderNumber, int docValue) {
        this.id_document = id_document;
        this.title = title;
        this.editor = editor;
        this.ID = ID;
        this.category = category;
        this.status = status;
        this.documentState = documentState;
        this.publicationDate = publicationDate;
        this.purchaseDate = purchaseDate;
        this.orderNumber = orderNumber;
        this.docValue = docValue;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------//
    
    public boolean titleIsThere(String t, Connection connection) {
        try
        {
            String mysql_query = "SELECT title FROM document";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (t.equalsIgnoreCase(rs.getString("title")))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in titleIsThere...");
        }
        return false;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------//
    
    public boolean idIsThere(long ID, Connection connection) {
        try
        {
            String mysql_query = "SELECT id FROM document";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (ID == rs.getLong("id"))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in is isbnIsThere...");
        }
        return false;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------//
    
    public boolean orderNumberIsThere(long order, Connection connection) {
        try
        {
            String mysql_query = "SELECT orderNumber FROM document";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                if (order == rs.getLong("orderNumber"))
                {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e)
        {
            System.err.println("error in orderNumberIsThere...");
        }
        return false;
    }
    
    //-----------------------------------------------------------------------------------------------------------------------//
    
     public boolean isIdDocumenCataloguetExist(int idDocument, Connection connection) {
        String mysql_query = "SELECT * FROM document";
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if(idDocument == rs.getInt("id_document")) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("error in isIdDocumenCataloguetExist...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isThereDocument(Document document, Connection connection) {
        try{
            String mysql_query = "SELECT * FROM document";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
              

            if (titleIsThere(document.title, connection) || idIsThere(document.ID, connection) || orderNumberIsThere(document.orderNumber, connection)) {
                return true;
            }
            return false;
            
        } catch(SQLException e) {
            System.out.println("error in isThereDocument...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isDocLoaned(int idDoc, Connection connection) {
        try {
            String mysql_query = "SELECT id_docDatabase FROM loaneddoc";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (idDoc == rs.getInt("id_docDatabase")) {
                    st.close();
                    return true;
                }
            }
            st.close();
        } catch (SQLException e){
            System.err.println("error in is isDocLoaned...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String docOldStatu(int idDoc,Connection connection) {
        String oldStatu = null;
        try {
            String mysql_query = "SELECT * FROM document WHERE id_documentDatabase = "+idDoc;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (idDoc == rs.getInt("id_documentDatabase")) {
                    oldStatu = rs.getString("statu");
                }
            }
            st.close();
        } catch (SQLException e){
            System.err.println("error in is docOldStatu...");
        }
        return oldStatu;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isDocStatuLanORLate(String oldStatu, String newStatu,Connection connection) {
        if ((oldStatu.equals("loaned") && newStatu.equals("loaned")) || (oldStatu.equals("late") && newStatu.equals("late"))) {
            return true;
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void addDocumentToDataBase(Document document, Connection connection) {
        try{  
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String inPublicationDate = format1.format(document.publicationDate);
            String inPurchaseDate = format1.format(document.purchaseDate);
            
            String mysql_query = "INSERT INTO `document`(`id_document`, `title`, `editor`, `id`, `category`, `statu`, `publicationDate`, `purchaseDate`, `orderNumber`, `documentState`, docValue) VALUES ("+document.id_document+", '"+document.title+"','"+document.editor+"',"+document.ID+",'"+document.category+"','"+document.status+"','"+inPublicationDate+"','"+inPurchaseDate+"',"+document.orderNumber+",'"+document.documentState+"', "+document.docValue+")";
            
            Statement st = connection.createStatement(); // create the java statement
            System.out.println(mysql_query);
            
            st.executeUpdate(mysql_query);

            st.close();               
        } catch (SQLException e)
        {
            System.out.println("error in addDocumentToDataBase...");
        } 
    }

    //--------------------------------------------------------------------------------------------------------------------//
    
    public void editDocument(Document document, int idDocument, Connection connection) {
        try{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String inPublicationDate = format1.format(document.publicationDate);
            String inPurchaseDate = format1.format(document.purchaseDate);
            
            String mysql_query = "UPDATE `document` SET `id_document`="+document.id_document+", `title`='"+document.title+"',`editor`='"+document.editor+"',`id`="+document.ID+",`category`='"+document.category+"',`statu`='"+document.status+"',`publicationDate`='"+inPublicationDate+"',`purchaseDate`='"+inPurchaseDate+"',`orderNumber`="+document.orderNumber+",`documentState`= '"+document.documentState+"', docValue="+document.docValue+" WHERE id_document="+idDocument+"";
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (SQLException e) {
            System.out.println("error in editDocument...");
        }
    }

    //--------------------------------------------------------------------------------------------------------------------//
    
    public void deleteDocument(int idDocument, Connection connection) {
        try{
            String mysql_query = "DELETE FROM `document` WHERE id_document = "+idDocument;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close(); 
        } catch (SQLException e) {
            System.out.println("error in deleteDocument");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalDocuments(Connection connection) {
        String mysql_query = "SELECT * FROM Document";
        int tDocuments = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tDocuments++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalDocuments...");
        }
        return tDocuments;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getTitle(long ID, Connection connection) {
        String mysql_query = "SELECT * FROM document WHERE id = "+ID;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            rs.next();
            return rs.getString("title");
        } catch (Exception e) {
           System.out.println("error in getTitle...");
        }
        return null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String addAllToComboBoxResult(Connection connection) {
        String mysql_query = "SELECT COUNT(id_documentDatabase), category FROM document GROUP BY category ORDER BY COUNT(id_documentDatabase) DESC;";
        String result = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);

            while (rs.next()) {
                result += ":::"+rs.getString("category");
            }
            st.close();
        } catch (SQLException ex) {
            System.out.println("can't add element in combobox Doc...");
        }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchDocumentCatalogueR(String string, int searchIndex, String searchStatuType) {
        
            if (string.equals("")) {
                return "SELECT * FROM document WHERE statu LIKE '%"+searchStatuType+"%'";
            } else {
                if (searchIndex == 0) {
                    return "SELECT * FROM document WHERE title LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 1){
                    return "SELECT * FROM document WHERE editor LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 2) {
                    return "SELECT * FROM document WHERE id LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM document WHERE category LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 4) {
                    return "SELECT * FROM document WHERE id_document LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 5) {
                    return "SELECT * FROM document WHERE orderNumber LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 6) {
                    return "SELECT * FROM document WHERE publicationDate LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 7) {
                    return "SELECT * FROM document WHERE purchaseDate LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                } else if (searchIndex == 8) {
                    return "SELECT * FROM document WHERE documentState LIKE '%"+string+"%' AND statu LIKE '%"+searchStatuType+"%'";
                }
            }
            return "SELECT * FROM document WHERE statu LIKE '%"+searchStatuType+"%'";
        }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchDocCatalogueResult(String mysql_query, Connection connection) {
        String[] result = null;
        try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(mysql_query);
                
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(mysql_query);
                
                int numberOfDoc = 0;
                int k =0;
                int i = 0;
                while(rs2.next()){
                    k++;
                }
                st2.close();
                result = new String[k];
                
                while(rs.next() && i<k) {
                    numberOfDoc++;
                    result[i] = numberOfDoc+":::"+rs.getInt("id_document")+":::"+rs.getString("title")+":::"+rs.getString("editor")+":::"+rs.getLong("id")+":::"+rs.getString("category")+":::"
                            +rs.getString("statu")+":::"+rs.getString("publicationDate")+":::"+rs.getString("purchaseDate")+":::"+rs.getLong("orderNumber")+":::"+rs.getString("documentState")+":::"+rs.getInt("docValue");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchDocCatalogueResult...");
            }
        return result;
    }
}

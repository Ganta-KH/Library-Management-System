package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReturnDoc extends User {
    
    Date returnDate;
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public ReturnDoc() {
        this.returnDate = null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public ReturnDoc(Date returnDate) {
        this.returnDate = returnDate;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void returnDocInDatabase(int dcreturnIdCata, long dcreturnUserID, String returnDocState, String dcreturnUserType, Connection connection) {
        try{
            String mysql_query = "DELETE FROM `loaneddoc` WHERE id_docDatabase = "+getDocDatabaseIdR(dcreturnIdCata, connection)+" AND id_userDatabase = "+getUserDatabaseIdR(dcreturnUserID, connection);
            String mysql_query2 = "DELETE FROM `latedoc` WHERE id_docDatabase = "+getDocDatabaseIdR(dcreturnIdCata, connection);
            String mysql_query3 = "UPDATE `document` SET `statu`= 'on the shelf' , documentState = '"+returnDocState+"' WHERE id_documentDatabase = "+getDocDatabaseIdR(dcreturnIdCata, connection);
            String mysql_query4 = "UPDATE `users` SET `copyCanLoan`= "+canLoanBR(dcreturnUserID, dcreturnUserType, connection)+", `docLoaned`= "+(getNumberOfDocLoaned(getUserDatabaseIdR(dcreturnUserID, connection), connection) - 1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(dcreturnUserID, connection);
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            if (isDocInLate(dcreturnIdCata, connection)) {
                st.executeUpdate("UPDATE `users` SET `docLate`= "+(getNumberOfDocLate(getUserDatabaseIdR(dcreturnUserID, connection), connection) - 1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(dcreturnUserID, connection));
                System.out.println("UPDATE `users` SET `docLate`= "+(getNumberOfDocLate(getUserDatabaseIdR(dcreturnUserID, connection), connection) - 1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(dcreturnUserID, connection));
            }
            st.executeUpdate(mysql_query2);
            System.out.println(mysql_query2);
            st.executeUpdate(mysql_query3);
            System.out.println(mysql_query3);
            st.executeUpdate(mysql_query4);
            System.out.println(mysql_query4);
            st.close();    
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in returnDocInDatabase...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean isDocInLate(int dcreturnIdCata, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM latedoc WHERE id_docDatabase = "+getDocDatabaseIdR(dcreturnIdCata, connection);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                if (getDocDatabaseIdR(dcreturnIdCata, connection) == rs.getInt("id_docDatabase")) {
                    st.close();
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in isDocInLate...");
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String getDocStateR(int dcreturnIdCata, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM document WHERE id_document = "+dcreturnIdCata;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if(rs.next()) {
                return rs.getString("documentState");
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in getDocState...");
        }
        return null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getDocValueR(int dcreturnIdCata, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM document WHERE id_document = "+dcreturnIdCata;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if(rs.next()) {
                return rs.getInt("docValue");
            }
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in getDocValue...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int penaltyR(int oldIndex, int newIndex, int dcreturnIdCata, Connection connection) {
        int p = 0;
        if (newIndex - oldIndex == 2) {
            p = (int) (getDocValueR(dcreturnIdCata, connection) * 0.5);
        } else if (newIndex - oldIndex == 3){
            p = (int) (getDocValueR(dcreturnIdCata, connection) * 0.6);
        } else if (newIndex - oldIndex == 4) {
            p = (int) (getDocValueR(dcreturnIdCata, connection) * 0.7);
        }
        return p;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setpenaltyR(long dcreturnUserID, int penalty, Connection connection) {
        try {
            int p = getUserPenaltyValueR(dcreturnUserID, connection) + penalty;
            String mysql_query = "UPDATE `users` SET `penaltyValue`= '"+p+"' WHERE id_user = "+dcreturnUserID;
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in setpenalty...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void editDocReturnDate(ReturnDoc returnDoc, int dcreturnIdCata, Connection connection) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String RDate = format1.format(returnDoc.returnDate);
            
            String mysql_query = "UPDATE `loaneddoc` SET `returnDate`= '"+RDate+"' WHERE id_docDatabase = "+getDocDatabaseIdR(dcreturnIdCata, connection);
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.close();
        } catch (Exception e) {
            System.out.println(e.toString());
            System.out.println("error in editDocReturnDate...");
        }
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int canLoanBR(long dcreturnUserID,String dcreturnUserType, Connection connection){
        if (canLoan(dcreturnUserID, connection) + 1 >= getNumberOfCopyCanLoan(dcreturnUserType)) {
            return getNumberOfCopyCanLoan(dcreturnUserType);
        }
        return canLoan(dcreturnUserID, connection) +1;
    }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String searchReturnDocUserCatalogueR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM `users` WHERE docLoaned > 0";
            } else {
                if (searchIndex == 1) {
                    return "SELECT * FROM users WHERE firstName LIKE '%"+string+"%' AND docLoaned > 0";
                } else if (searchIndex == 2){
                    return "SELECT * FROM users WHERE lastName LIKE '%"+string+"%' AND docLoaned > 0";
                } else if (searchIndex == 0) {
                    return "SELECT * FROM users WHERE id_user LIKE '%"+string+"%' AND docLoaned > 0";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM users WHERE userType LIKE '%"+string+"%' AND docLoaned > 0";
                }
            }
            return "SELECT * FROM `users` WHERE docLoaned > 0";
        }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchReturnDocCatalogueResult(String mysql_query, Connection connection) {
        String[] result = null;
        try {
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(mysql_query);
                
                Statement st2 = connection.createStatement();
                ResultSet rs2 = st2.executeQuery(mysql_query);
                
                int numberOfDocs = 0;
                int k =0;
                int i = 0;
                while(rs2.next()){
                    k++;
                }
                st2.close();
                result = new String[k];
                while(rs.next() && i<k) {
                    numberOfDocs++;
                    result[i] = numberOfDocs+":::"+rs.getInt("id_document")+":::"+rs.getString("title")+":::"+rs.getString("editor")+":::"+rs.getString("category")+":::"+rs.getString("statu")+":::"+rs.getString("documentState")+":::"+rs.getLong("id")+":::"+rs.getString("loadDate")+":::"+rs.getString("returnDate");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.err.println(e.toString());
                System.out.println("error in searchReturnDocCatalogueResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public String searchReturnDocCatalogueR(String string,long userID ,int searchIndex) {
            if (string.equals("")) {
                return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase   WHERE users.id_userDatabase = "+userID;
            } else {
                if (searchIndex == 0) {
                    return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND title LIKE '%"+string+"%'";
                } else if (searchIndex == 1){
                    return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND editor LIKE '%"+string+"%'";
                } else if (searchIndex == 2) {
                    return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND id_document LIKE '%"+string+"%'";
                } else if (searchIndex == 3) {
                    return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND id LIKE '%"+string+"%'";
                } else if (searchIndex == 4) {
                    return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase  WHERE users.id_userDatabase = "+userID+" AND category LIKE '%"+string+"%'";
                }
            }
            return "SELECT document.id_document, document.title, document.editor, document.category, document.statu, document.documentState, document.id , loaneddoc.loadDate, loaneddoc.returnDate FROM loaneddoc JOIN document ON loaneddoc.id_docDatabase = document.id_documentDatabase JOIN users ON loaneddoc.id_userDatabase = users.id_userDatabase WHERE users.id_userDatabase = "+userID;
        }
    
}

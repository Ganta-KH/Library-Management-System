package libraryserver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanDoc extends User{
    long id_user;
    int id_doc;
    Date loanDate;
    Date returnDate;
    
    public LoanDoc() {
        this.id_user = 0;
        this.id_doc = 0;
        this.loanDate = null;
        this.returnDate = null;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public LoanDoc(Date returnDate) {
        this.returnDate = returnDate;
    }
    

    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getNumberOfLoanDays(String userType) {
        if (userType.equalsIgnoreCase("occasional user")) {
            return 15;
        }
        return 30;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int canLoanBR(String userType, int databaseIdUser, Connection connection){
        if (canLoanR(userType, databaseIdUser, connection) - 1 > 0) {
            return canLoanR(userType, databaseIdUser, connection) - 1;
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void loanDocInDatabaseR(int dcLoanIdCata, long dcLoanUserID, String dcLoanDate, LoanDoc loanDoc, int databaseIdUser, String userType, Connection connection) {
        try{
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String RDate = format1.format(loanDoc.returnDate);
            
            String mysql_query = "INSERT INTO `loaneddoc`(`id_docDatabase`, `id_userDatabase`, `loadDate`, `returnDate`) VALUES ('"+getDocDatabaseIdR(dcLoanIdCata, connection)+"', '"+getUserDatabaseIdR(dcLoanUserID, connection)+"', '"+dcLoanDate+"', '"+RDate+"')";
            String mysql_query2 = "UPDATE `document` SET `statu`= 'loaned' WHERE id_documentDatabase = "+getDocDatabaseIdR(dcLoanIdCata, connection);
            String mysql_query3 = "UPDATE `users` SET `copyCanLoan`= "+canLoanBR(userType, databaseIdUser, connection)+", `docLoaned`= "+(getNumberOfDocLoaned(getUserDatabaseIdR(dcLoanUserID, connection), connection)+1)+" WHERE id_userDatabase = "+getUserDatabaseIdR(dcLoanUserID, connection);
            Statement st = connection.createStatement();
            st.executeUpdate(mysql_query);
            System.out.println(mysql_query);
            st.executeUpdate(mysql_query2);
            System.out.println(mysql_query2);
            st.executeUpdate(mysql_query3);
            System.out.println(mysql_query3);
            st.close();    
        } catch (Exception e) {
            System.out.println("error in loanDocInDatabase...");
        }
    }
        
    //--------------------------------------------------------------------------------------------------------------------//
    
    public String[] searchLoanDocCatalogueResult(String mysql_query, Connection connection) {
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
                    result[i] = numberOfDocs+":::"+rs.getInt("id_document")+":::"+rs.getString("title")+":::"+rs.getString("editor")+":::"+rs.getString("category")+":::"+rs.getLong("id");
                    i++;
                }  
                st.close();
            } catch (SQLException e) {
                System.out.println("error in searchLoanDocCatalogueResult...");
            }
        return result;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
        
    public String searchLoanDocCatalogueR(String string, int searchIndex) {
        
            if (string.equals("")) {
                return "SELECT * FROM document WHERE statu = 'on the shelf' ";
            } else {
                if (searchIndex == 0) {
                    return "SELECT * FROM document WHERE title LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 1){
                    return "SELECT * FROM document WHERE editor LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 2) {
                    return "SELECT * FROM document WHERE id_documet LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 3) {
                    return "SELECT * FROM document WHERE id LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                } else if (searchIndex == 4) {
                    return "SELECT * FROM document WHERE category LIKE '%"+string+"%' AND statu = 'on the shelf' ";
                }
            }
            return "SELECT * FROM document WHERE statu = 'on the shelf' ";
        }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public boolean checkLateDoc(int idDoc, Connection connection) {
        try {
            String mysql_query = "SELECT * FROM latedoc WHERE id_docDatabase = "+idDoc;
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            if (rs.next()) {
                if (idDoc == rs.getInt("id_docDatabase")) {
                    st.close();
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("error in checkLateDoc...");
            System.err.println(e.toString());
        }
        return false;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int getNumberOfLateDoc(int idUser, Connection connection) {
        try {
            String mysql_query = "SELECT COUNT(id_userDatabase), id_userDatabase FROM latedoc GROUP BY id_userDatabase ORDER BY COUNT(id_userDatabase) DESC";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                if (rs.getInt("id_userDatabase") == idUser) {  
                    return rs.getInt(1);
                }
            }
            st.close();
        } catch (Exception e) {
            System.out.println("error in getNumberOfLateDoc...");
        }
        return 0;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalLoandedDocs(Connection connection) {
        String mysql_query = "SELECT * FROM loaneddoc";
        int tLDocs = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tLDocs++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalLoanedDocs...");
        }
        return tLDocs;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public int totalLateDocs(Connection connection) {
        String mysql_query = "SELECT * FROM latedoc";
        int tLDocs = 0;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            while (rs.next()) {
                tLDocs++;
            }
            st.close();
        } catch(SQLException e) {
            System.out.println("error in totalLateDocs...");
        }
        return tLDocs;
    }
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void setLateDoc(Connection connection) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date returnD, today;
      
        try {
            today = format1.parse(format1.format(new Date()));
            
            String mysql_query = "SELECT * FROM `loaneddoc`";
            Statement st = connection.createStatement();
            Statement st2 = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            
            while (rs.next()) {
                returnD = format1.parse(rs.getString("returnDate"));
                if(today.compareTo(returnD) > 0) {                    
                    if (!checkLateDoc(rs.getInt("id_docDatabase"), connection)) {
                        st2.executeUpdate("INSERT INTO `latedoc`(`id_docDatabase`, `id_userDatabase`) VALUES ("+rs.getInt("id_docDatabase")+","+rs.getInt("id_userDatabase")+");");
                        st2.executeUpdate("UPDATE `document` SET `statu` = 'late' WHERE id_documentDatabase = "+rs.getInt("id_docDatabase"));
                        st2.executeUpdate("UPDATE `users` SET `docLate`= "+getNumberOfLateDoc(rs.getInt("id_userDatabase"), connection)+" WHERE id_userDatabase = "+rs.getInt("id_userDatabase"));
                    }
                }
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println("error in setLateDoc...");
        }
    }
}


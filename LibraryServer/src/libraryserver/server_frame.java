package libraryserver;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class server_frame extends javax.swing.JFrame 
{
    ArrayList<String> admins = new ArrayList();
    public static String name = "";
    public static int id_adminDatabase;
    public static String adminType = "";
    public static ArrayList<Integer> onlineAdminId = new ArrayList<Integer>();
    public static int onlineAdmin;
    public boolean ison;
    public ServerSocket serverSock;
    
    
    
    public class Threads implements Runnable{
    
    public Connection connection;
    
    //--------------------------------------------------------------------------------------------------------------------//
    
    public void connectToDataBase() {
        
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver"); //mysql connector

            String company_db = "jdbc:mysql://localhost:3306/slibrary?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(company_db,"root","");
            
        } catch (ClassNotFoundException | SQLException e)
            {
                System.out.println("Something wrong can't connect to data base!");
                System.out.println(e.getMessage());
            }
    }
    
    //-------------------------------------------------------------------------------------------------------------------//
    
    public boolean isAdminOnline(int AdminId) {
        for (int i : onlineAdminId) {
            if (i == AdminId) {
                return true;
            }
        }
        return false;
    }
    
    //-------------------------------------------------------------------------------------------------------------------//
    
   public boolean onladmin = false;
    
    public boolean correctPassword(String user, String pass) {
        
        try {
            String mysql_query = "SELECT * FROM admin";

            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(mysql_query);
            String password;
            String username;
            
            while (rs.next()) { 
                password = rs.getString("pass");
                username = rs.getString("username");
                
                if (password.equalsIgnoreCase(pass) && username.equalsIgnoreCase(user)) {

                    name = rs.getString("username");
                    id_adminDatabase = rs.getInt("id_adminDatabase");
                    adminType = rs.getString("adminType");
                    
                    serverState.append("the admin " +name + " Got connected. \n");
                    if(!isAdminOnline(id_adminDatabase))
                    {
                        onlineAdminId.add(id_adminDatabase);
                        onlineAdmin++;
                        admins.add(name);
                    }
                    st.close();
                    return true;
                } 
            }
            st.close();
            return false;
            
        } catch (SQLException e) {
            System.out.println("error in correctPassword server pass");
            //System.err.println(e.getMessage());
        }
        return false;   
    }
    
    //-------------------------------------------------------------------------------------------------------------------//
 
    Socket client;
    
    Threads(Socket client) {this.client=client;}
    public void run() {
        
        try {
            connectToDataBase();
            
            ObjectOutputStream Obout= new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream Obin = new ObjectInputStream(client.getInputStream());
            
            String passt = "";
            String usert = "";
            String[] RecievedTextS ;
            String Recieved = "";
            
            Recieved= (String) Obin.readObject();
            RecievedTextS= Recieved.split(" ");
            System.out.println(RecievedTextS[0]+" "+RecievedTextS[1]);
            
                if(RecievedTextS[0].equals("login"))
                {
                    if(correctPassword(RecievedTextS[1], RecievedTextS[2]))
                    {
                        Obout.writeObject("YES "+name+" "+id_adminDatabase+" "+adminType);
                    }
                    else
                    {
                        if (!onladmin)
                        {
                            Obout.writeObject("NO");
                        }
                    }
                }
                else if(RecievedTextS[0].equals("main"))
                {
                    Book bk = new Book();
                    Document dc = new Document();
                    User usr = new User();
                    Admin ad = new Admin();
                    LoanBook lnBk = new LoanBook();
                    LoanDoc lnDc = new LoanDoc();
                    ReturnBook rnBk = new ReturnBook();
                    ReturnDoc rnDc = new ReturnDoc();
                    Punishment pu = new Punishment();
                    
                    String Sread;
                    
                    Date[] dateR;
                    
                    Long[] longR;
                    
                    int[] intR;
                    
                    while(RecievedTextS[0].equals("main") && ison)
                    {
                        switch (RecievedTextS[1]) {
                            case "home":
                                Obout.writeObject(bk.totalBooks(connection)+" "+dc.totalDocuments(connection)+" "+usr.totalUsers(connection)+" "+ad.totalAdmins(connection)+" "+lnBk.totalLoandedBooks(connection)+" "+lnDc.totalLoandedDocs(connection)+" "+onlineAdmin);
                                break;
                            case "setLateBook":
                                lnBk.setLateBook(connection);
                                break;
                            case "setLateDoc":
                                lnDc.setLateDoc(connection);
                                break;
                            case "endBan":
                                pu.endBan(connection);
                                break;
                            case "activeUser":
                                Obout.writeObject(usr.nbrActiveUser(connection));
                                break;
                            case "nbrLateUser":
                                Obout.writeObject(pu.nbrLateUser(connection));
                                break;
                            case "logOut":
                                {
                                    intR = new int[1];
                                    intR[0] = (int)Obin.readObject();
                                    int k=0;
                                    for(int i : onlineAdminId) {
                                        if(i == intR[0]) {
                                            onlineAdminId.remove(k);
                                            onlineAdmin--;
                                            break;
                                        }
                                        k++;
                                    }
                                    Sread = (String)Obin.readObject();
                                    admins.remove(Sread);
                                    serverState.append("the admin " +name + " Deconnected. \n");
                                }
                                break;
                            case "Book":
                                switch (RecievedTextS[2]) {
                                    case "setBookJTable":
                                        {
                                            intR = new int[1];
                                            String[] getting = new String[2];
                                            getting[0] = (String)Obin.readObject();
                                            intR[0] = (int)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();

                                            Obout.writeObject(bk.searchBookCatalogueResult(bk.searchBookCatalogueR(getting[0], intR[0], getting[1]), connection));
                                            break;                                    
                                        }
                                    case "addAllToComboBox":
                                        Obout.writeObject(bk.addAllToComboBoxResult(connection));
                                        break;
                                    case "isIdBookExist":
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        if (bk.isIdBookExist(intR[0] , connection)) {
                                            Obout.writeObject("yes");                                    
                                        } else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "isbnIsThere":
                                        longR = new Long[1];
                                        longR[0] = (Long)Obin.readObject();
                                        if (bk.isbnIsThere(longR[0] , connection)) {
                                            Obout.writeObject("yes");

                                        } else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "isBookLoaned":
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        if (bk.isBookLoaned(usr.getBookDatabaseIdR(intR[0], connection), connection)) {
                                            Obout.writeObject(true);
                                        } else {
                                            Obout.writeObject(false);
                                        }
                                        break;
                                    case "isBookStatuLanORLate":
                                        {
                                            intR = new int[1];
                                            intR[0] = (int)Obin.readObject();
                                            String newStatu = (String)Obin.readObject();
                                            Obout.writeObject(bk.isBookStatuLanORLate(bk.bookOldStatu(usr.getBookDatabaseIdR(intR[0], connection), connection), newStatu, connection));
                                            Obout.writeObject(bk.bookOldStatu(usr.getBookDatabaseIdR(intR[0], connection), connection));
                                            break;
                                        }
                                    case "editBook":
                                        {
                                            longR = new Long[2];         
                                            dateR = new Date[2];
                                            intR = new int[3];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            intR[2] = (int)Obin.readObject();
                                            
                                            Book book = new Book(intR[0], getting[0],  getting[1], longR[0], getting[2], getting[3], getting[4], dateR[0], longR[1], intR[1]);
                                            book.editBook(book, intR[2], connection);
                                            break;                                    
                                        }
                                    case "addBookToDataBase":
                                        {
                                            longR = new Long[2]; 
                                            dateR = new Date[2];
                                            intR = new int[2];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            Book book = new Book(intR[0], bk.getTitle(longR[0] , connection), getting[0], longR[0], getting[1], getting[2], getting[3], dateR[0], longR[1], intR[1]);
                                            book.addBookToDataBase(book , connection);
                                            break;                                    
                                        }
                                    case "addBookToDataBase2":
                                        {
                                            longR = new Long[2];
                                            dateR = new Date[2];
                                            intR = new int[2];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            Book book = new Book(intR[0], getting[0],  getting[1], longR[0], getting[2], getting[3], getting[4], dateR[0], longR[1], intR[1]);
                                            book.addBookToDataBase(book , connection);
                                            break;                                    
                                        }
                                    case "deleteBook":
                                        {
                                            longR = new Long[2]; 
                                            dateR = new Date[2];
                                            intR = new int[3];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            intR[2] = (int)Obin.readObject();

                                            Book book = new Book(intR[0], getting[0],  getting[1], longR[0], getting[2], getting[3], getting[4], dateR[0], longR[1], intR[1]);
                                            book.deleteBook(intR[0], connection);
                                            break;
                                        }
                                    default:
                                        break;
                                }
                                break;
                            case "Document":
                                switch (RecievedTextS[2]) {
                                    case "setDocumentJTable":
                                        {
                                            intR = new int[1];
                                            String[] getting = new String[2];
                                            getting[0] = (String)Obin.readObject();
                                            intR[0] = (int)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();

                                            Obout.writeObject(dc.searchDocCatalogueResult(dc.searchDocumentCatalogueR(getting[0], intR[0], getting[1]), connection));
                                            break;                                    
                                        }
                                    case "addAllToComboBox":
                                        Obout.writeObject(dc.addAllToComboBoxResult(connection));
                                        break;
                                    case "isIdDocumenCataloguetExist":
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        if (dc.isIdDocumenCataloguetExist(intR[0] , connection)) {
                                            Obout.writeObject("yes");                                    
                                        } else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "idIsThere":
                                        longR = new Long[1];
                                        longR[0] = (Long)Obin.readObject();
                                        if (dc.idIsThere(longR[0] , connection)) {
                                            Obout.writeObject("yes");

                                        } else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "isDocLoaned":
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        if (dc.isDocLoaned(usr.getDocDatabaseIdR(intR[0], connection), connection)) {
                                            Obout.writeObject(true);
                                        } else {
                                            Obout.writeObject(false);
                                        }
                                        break;
                                    case "isDocStatuLoanORLate":
                                        {
                                            intR = new int[1];
                                            intR[0] = (int)Obin.readObject();
                                            String newStatu = (String)Obin.readObject();
                                            Obout.writeObject(dc.isDocStatuLanORLate(dc.docOldStatu(usr.getDocDatabaseIdR(intR[0], connection), connection), newStatu, connection));
                                            Obout.writeObject(dc.docOldStatu(usr.getDocDatabaseIdR(intR[0], connection), connection));
                                            break;
                                        }
                                    case "deleteDocument":
                                        {
                                            longR = new Long[2];         
                                            dateR = new Date[2];
                                            intR = new int[3];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            dateR[1] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            intR[2] = (int)Obin.readObject();

                                            Document document = new Document(intR[0], getting[0], getting[1], longR[0], getting[2], getting[3], getting[4], dateR[0], dateR[1], longR[1], intR[1]);
                                            document.deleteDocument(intR[2], connection);
                                            break;                                    
                                        }
                                    case "editDocument":
                                        {
                                            longR = new Long[2]; 
                                            dateR = new Date[2];
                                            intR = new int[3];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            dateR[1] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            intR[2] = (int)Obin.readObject();

                                            Document document = new Document(intR[0], getting[0], getting[1], longR[0], getting[2], getting[3], getting[4], dateR[0], dateR[1], longR[1], intR[1]);
                                            document.editDocument(document, intR[2], connection);
                                            break;                                    
                                        }
                                    case "addDocumentToDataBase":
                                        {
                                            longR = new Long[2];         
                                            dateR = new Date[2];
                                            intR = new int[2];
                                            String[] getting = new String[5];
                                            intR[0] = (int)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            dateR[1] = (Date)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            
                                            Document document = new Document(intR[0], getting[0], getting[1], longR[0], getting[2], getting[3], getting[4], dateR[0], dateR[1], longR[1], intR[1]);
                                            document.addDocumentToDataBase(document, connection);
                                            break;                                    
                                        }
                                    case "addDocumentToDataBase2":
                                        {
                                            longR = new Long[3]; 
                                            dateR = new Date[2];
                                            intR = new int[2];
                                            String[] getting = new String[4];
                                            intR[0] = (int)Obin.readObject();
                                            longR[0] = (Long)Obin.readObject();
                                            getting[0] = (String)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            dateR[1] = (Date)Obin.readObject();
                                            longR[2] = (Long)Obin.readObject();
                                            intR[1] = (int)Obin.readObject();
                                            Document document = new Document(intR[0], dc.getTitle(longR[0] , connection), getting[0],longR[1], getting[1], getting[2], getting[3], dateR[0], dateR[1], longR[2], intR[1]);
                                            document.addDocumentToDataBase(document, connection);
                                            break;
                                        }
                                    default:
                                        break;
                                }
                                break;
                            case "User":
                                switch (RecievedTextS[2]) {
                                    case "setUserJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        String userState =new String();    
                                        intR[0] = (int)Obin.readObject();
                                        userState = (String)Obin.readObject();
                                        Obout.writeObject(usr.searchUsersCatalogueResult(usr.searchUserCatalogueR(Sread, intR[0])+userState, connection));
                                        break;
                                    case "isUserExist":
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        if (dc.isIdDocumenCataloguetExist(intR[0] , connection)) {
                                            Obout.writeObject("yes");
                                        }
                                        else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "isIdUserExist":
                                        longR = new Long[1];
                                        longR[0] = (Long)Obin.readObject();
                                        if (usr.isIdUserExist(longR[0] , connection)) {
                                            Obout.writeObject("yes");
                                        }
                                        else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "isUserExist2":
                                        {
                                            longR = new Long[1]; 
                                            longR[0] = (Long)Obin.readObject();
                                            String[] getting = new String[3];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            User user = new User(longR[0], getting[0], getting[1], getting[2]);
                                            if (user.isUserExist(user, connection)) {
                                                Obout.writeObject("yes");
                                            }
                                            else {
                                                Obout.writeObject("no");
                                            }
                                            break;                                    
                                        }
                                    case "isUserLoaned":
                                        {
                                            longR = new Long[1]; 
                                            longR[0] = (Long)Obin.readObject();
                                            Obout.writeObject(usr.isUserLoaned(usr.getUserDatabaseIdR(longR[0], connection), connection));
                                            break;
                                        }
                                    case "addUserToDataBase":
                                        {
                                            longR = new Long[1]; 
                                            longR[0] = (Long)Obin.readObject();
                                            String[] getting = new String[3];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            User user = new User(longR[0], getting[0], getting[1], getting[2]);
                                            user.addUserToDataBase(user, connection);
                                            break;
                                        }
                                    case "deleteUser":
                                        {
                                            longR = new Long[2];         
                                            longR[0] = (Long)Obin.readObject();
                                            String[] getting = new String[3];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            User user = new User(longR[0], getting[0], getting[1], getting[2]);
                                            usr.deleteUser(longR[1], connection);
                                            break;                                    
                                        }
                                    case "editUser":
                                        {
                                            longR = new Long[2]; 
                                            longR[0] = (Long)Obin.readObject();
                                            String[] getting = new String[3];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            longR[1] = (Long)Obin.readObject();
                                            User user = new User(longR[0], getting[0], getting[1], getting[2]);
                                            
                                            user.editUserR(user, longR[1], usr.getUserDatabaseIdR(longR[1], connection), connection);
                                            break;
                                        }
                                    default:
                                        break;
                                }
                                break;
                            case "Admin":
                                switch (RecievedTextS[2]) {
                                    case "setAdminJTable":
                                        Obout.writeObject(ad.searchAdminsCatalogueResult("SELECT * FROM admin", connection));
                                        break;
                                    case "setAdminJTable2":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        Obout.writeObject(ad.searchAdminsCatalogueResult(ad.searchAdminCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "isIDExist":
                                        longR = new Long[1];
                                        longR[0] = (Long)Obin.readObject();
                                        if (ad.isIDExist(longR[0] , connection)) {
                                            Obout.writeObject("yes");
                                        }
                                        else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "isAdminExist":
                                        {
                                            longR = new Long[1];
                                            longR[0] = (long)Obin.readObject();
                                            String[] getting = new String[5];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            Admin admin = new Admin(longR[0], getting[0], getting[1], getting[2], getting[3], getting[4]);
                                            if (admin.isAdminExist(admin, connection)) {
                                                Obout.writeObject("yes");
                                            }
                                            else {
                                                Obout.writeObject("no");
                                            }
                                            break;
                                        }
                                    case "deleteAdmin":
                                        {
                                            longR = new Long[2];
                                            longR[1] = (long)Obin.readObject();
                                            String[] getting = new String[5];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            longR[0] = (long)Obin.readObject();
                                            Admin admin = new Admin(longR[1], getting[0], getting[1], getting[2], getting[3]);
                                            ad.deleteAdmin(longR[0], connection);
                                            break;  
                                        }
                                    case "addAdminToDataBase":
                                        {
                                            longR = new Long[1];
                                            longR[0] = (long)Obin.readObject();
                                            String[] getting = new String[5];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            getting[4] = (String)Obin.readObject();
                                            Admin admin = new Admin(longR[0], getting[0], getting[1], getting[2], getting[3], getting[4]);
                                            admin.addAdminToDataBase(admin, connection);
                                            break;                                    
                                        }
                                    case "editAdmin":
                                        {
                                            longR = new Long[2];
                                            longR[1] = (long)Obin.readObject();
                                            String[] getting = new String[5];
                                            getting[0] = (String)Obin.readObject();
                                            getting[1] = (String)Obin.readObject();
                                            getting[2] = (String)Obin.readObject();
                                            getting[3] = (String)Obin.readObject();
                                            longR[0] = (long)Obin.readObject();
                                            Admin admin = new Admin(longR[1], getting[0], getting[1], getting[2], getting[3]);
                                            admin.editAdmin(admin, longR[0], connection);
                                            break;
                                        }
                                    default:
                                        break;
                                }
                                break;
                            case "LoanBook":
                                switch (RecievedTextS[2]) {
                                    case "loanBookInDatabase":

                                        intR = new int[1];
                                        longR = new Long[2];
                                        String[] getting = new String[2];
                                        Date[] reDate = new Date[1];
                                        
                                        intR[0] = (int)Obin.readObject();
                                        longR[0] = (long)Obin.readObject();
                                        getting[0] = (String)Obin.readObject();
                                        reDate[0] = (Date)Obin.readObject();
                                        getting[1] = (String)Obin.readObject();
                                        longR[1] = (long)Obin.readObject();
                                        
                                        LoanBook loanBook = new LoanBook(reDate[0]);
                                        lnBk.loanBookInDatabaseR(intR[0], longR[0], getting[0], loanBook, lnBk.getUserDatabaseIdR(longR[1], connection), getting[1], connection);
                                        break;
                                    case "setLoanBookJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(lnBk.searchLoanBookCatalogueResult(lnBk.searchLoanBookCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "setUserJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(lnBk.searchUsersCatalogueResult(lnBk.searchLoanUserCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "getNumberOfLoanDays":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = lnBk.getNumberOfLoanDays(Sread);
                                        Obout.writeObject(intR[0]);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "LoanDoc":
                                switch (RecievedTextS[2]) {
                                    case "loanDocInDatabase":
                                        intR = new int[1];
                                        longR = new Long[2];
                                        String[] getting = new String[2];
                                        Date[] reDate = new Date[1];
                                        
                                        intR[0] = (int)Obin.readObject();
                                        longR[0] = (long)Obin.readObject();
                                        getting[0] = (String)Obin.readObject();
                                        reDate[0] = (Date)Obin.readObject();
                                        getting[1] = (String)Obin.readObject();
                                        longR[1] = (long)Obin.readObject();
                                        
                                        LoanDoc loanDoc = new LoanDoc(reDate[0]);
                                        lnDc.loanDocInDatabaseR(intR[0], longR[0], getting[0], loanDoc, lnDc.getUserDatabaseIdR(longR[1], connection), getting[1], connection);
                                        break;
                                    case "setLoanDocJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();

                                        Obout.writeObject(lnDc.searchLoanDocCatalogueResult(lnDc.searchLoanDocCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "setUserJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        Obout.writeObject(lnDc.searchUsersCatalogueResult(lnDc.searchLoanUserCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "getNumberOfLoanDays":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = lnDc.getNumberOfLoanDays(Sread);
                                        Obout.writeObject(intR[0]);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "ReturnBook":
                                switch (RecievedTextS[2]) {
                                    case "setUserJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(rnBk.searchUsersCatalogueResult(rnBk.searchReturnBookUserCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "setReturnBookJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(rnBk.searchReturnBookCatalogueResult(rnBk.searchReturnBookCatalogueR(Sread, 0, intR[0]), connection));
                                        break;
                                    case "setReturnBookJTable2":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        longR = new Long[1];
                                        longR[0] = (long)Obin.readObject();
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(rnBk.searchReturnBookCatalogueResult(rnBk.searchReturnBookCatalogueR(Sread, rnBk.getUserDatabaseIdR(longR[0], connection), intR[0]), connection));
                                        break;
                                    case "penalty":
                                        intR = new int[4];
                                        intR[0] = (int)Obin.readObject(); 
   
                                        Obout.writeObject(rnBk.getBookStateR(intR[0], connection));
                                        
                                        intR[1] = (int)Obin.readObject();  
                                        intR[2] = (int)Obin.readObject();  
                                        intR[3] = rnBk.penaltyR(intR[1], intR[2], intR[0], connection);
                                        Obout.writeObject(intR[3]);

                                        break;
                                    case "setpenalty":
                                        intR = new int[1];
                                        longR = new Long[1];
                                        longR[0] = (long)Obin.readObject();
                                        intR[0] = (int)Obin.readObject();

                                        rnBk.setpenaltyR(longR[0], intR[0], connection);
                                        break;
                                    case "returnBookInDatabase":
                                        intR = new int[1];
                                        longR = new Long[1];
                                        String[] getting = new String[2];
                                        
                                        intR[0] = (int) Obin.readObject();
                                        longR[0] = (long)Obin.readObject();
                                        getting[0] = (String)Obin.readObject();
                                        getting[1] = (String)Obin.readObject();
                                        
                                        rnBk.returnBookInDatabaseR(intR[0], longR[0], getting[0], getting[1], connection);
                                        break;
                                    case "checkIdJTable":
                                        boolean checkIdJTable = (boolean)Obin.readObject();
                                        if (checkIdJTable) {
                                            Obout.writeObject("yes");
                                        }
                                        else {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "editBookReturnDate":
                                        dateR = new Date[1];
                                        intR = new int[1];
                                        dateR[0] = (Date)Obin.readObject();
                                        intR[0] = (int)Obin.readObject();
                                        ReturnBook returnBook = new ReturnBook(dateR[0]);
                                        returnBook.editBookReturnDateR(returnBook, intR[0], connection);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "ReturnDoc":
                                switch (RecievedTextS[2]) {
                                    case "setUserJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(rnDc.searchUsersCatalogueResult(rnDc.searchReturnDocUserCatalogueR(Sread, intR[0]), connection));
                                        break;
                                    case "setReturnDocJTable":
                                        Sread = (String) Obin.readObject();
                                        intR = new int[1];
                                        longR = new Long[1];
                                        longR[0] = (long)Obin.readObject();
                                        intR[0] = (int)Obin.readObject();
                                        
                                        Obout.writeObject(rnDc.searchReturnDocCatalogueResult(rnDc.searchReturnDocCatalogueR(Sread, rnDc.getUserDatabaseIdR(longR[0], connection), intR[0]), connection));
                                        break;
                                    case "penalty":
                                        
                                        intR = new int[4];
                                        intR[0] = (int)Obin.readObject();
   
                                        Obout.writeObject(rnDc.getDocStateR(intR[0], connection));
                                        
                                        intR[1] = (int)Obin.readObject();  
                                        intR[2] = (int)Obin.readObject();  

                                        intR[3] = rnDc.penaltyR(intR[1], intR[2], intR[0], connection);
                                        Obout.writeObject(intR[3]);
                                        break;
                                    case "setpenalty":
                                        intR = new int[1];
                                        longR = new Long[1];
                                        longR[0] = (long)Obin.readObject();
                                        intR[0] = (int)Obin.readObject();
                                        rnDc.setpenaltyR(longR[0], intR[0], connection);
                                        break;
                                    case "returnDocInDatabase":
                                        intR = new int[1];
                                        longR = new Long[1];
                                        String[] getting = new String[2];
                                        
                                        intR[0] = (int) Obin.readObject();
                                        longR[0] = (long)Obin.readObject();
                                        getting[0] = (String)Obin.readObject();
                                        getting[1] = (String)Obin.readObject();
                                        
                                        rnDc.returnDocInDatabase(intR[0], longR[0], getting[0], getting[1], connection);
                                        break;
                                    case "checkIdJTable":
                                        boolean checkIdJTable = (boolean)Obin.readObject();
                                        if (checkIdJTable)
                                        {
                                            Obout.writeObject("yes");
                                        }
                                        else
                                        {
                                            Obout.writeObject("no");
                                        }
                                        break;
                                    case "editDocReturnDate":
                                        dateR = new Date[1];
                                        intR = new int[1];
                                        dateR[0] = (Date)Obin.readObject();
                                        intR[0] = (int)Obin.readObject();
                                        ReturnDoc returnDoc = new ReturnDoc(dateR[0]);
                                        returnDoc.editDocReturnDate(returnDoc, intR[0], connection);
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            case "Punishment":
                                switch (RecievedTextS[2]) {
                                    case "setPunishmentJTable":
                                        Sread = (String) Obin.readObject();
                                        String userState = new String();
                                        intR = new int[1];
                                        intR[0] = (int)Obin.readObject();
                                        userState = (String)Obin.readObject();
                                        Obout.writeObject(pu.searchUsersCataloguePuniResult(pu.searchUserCataloguePuniR(Sread, intR[0])+userState, connection));      
                                        break;
                                    case "banUser":
                                        {
                                            longR = new Long[1];
                                            dateR = new Date[2];
                                            longR[0] = (Long)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            dateR[1] = (Date)Obin.readObject();
                                            User user = new User(longR[0], dateR[0], dateR[1]);
                                            pu.banUser(user, connection);
                                            break;
                                        }
                                    case "unbanUser":
                                        {
                                            longR = new Long[1];
                                            dateR = new Date[2];
                                            longR[0] = (Long)Obin.readObject();
                                            dateR[0] = (Date)Obin.readObject();
                                            dateR[1] = (Date)Obin.readObject();
                                            User user = new User(longR[0], dateR[0], dateR[1]);
                                            pu.unbanUser(user, connection);
                                            break;
                                        }
                                    case "penaltyUserPunishment":
                                        {
                                            longR = new Long[1];
                                            intR = new int[1];
                                            longR[0] = (Long)Obin.readObject();
                                            intR[0] = (int)Obin.readObject();
                                            User user = new User(longR[0], intR[0]);
                                            pu.penaltyUserPunishment(user, connection);
                                            break;
                                        }
                                    default:
                                        break;
                                }
                                break;
                            default:
                                client.close();
                                break;
                        }
                        
                        Recieved= (String) Obin.readObject();
                        RecievedTextS= Recieved.split(" ");
                    }
                }
                if (!ison)
                {
                    for (int i=0; i<admins.size(); i++)
                    {
                        admins.remove(i);
                    } 
                }
            client.close();
        } catch (IOException ex) {
            Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Threads.class.getName()).log(Level.SEVERE, null, ex);
        }
             //To change body of generated methods, choose Tools | Templates.
    }
    
}

    public server_frame() 
    {
        initComponents();
        svrstate.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        serverState = new javax.swing.JTextArea();
        b_start = new javax.swing.JButton();
        b_end = new javax.swing.JButton();
        b_users = new javax.swing.JButton();
        b_clear = new javax.swing.JButton();
        svrstate = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Chat - Server's frame");
        setName("server"); // NOI18N
        setResizable(false);

        serverState.setColumns(20);
        serverState.setRows(5);
        jScrollPane1.setViewportView(serverState);

        b_start.setText("START");
        b_start.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_startActionPerformed(evt);
            }
        });

        b_end.setText("END");
        b_end.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_endActionPerformed(evt);
            }
        });

        b_users.setText("Online Users");
        b_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_usersActionPerformed(evt);
            }
        });

        b_clear.setText("Clear");
        b_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                b_clearActionPerformed(evt);
            }
        });

        svrstate.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_end, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(b_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(b_users, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(202, 202, 202)
                .addComponent(svrstate)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_start)
                    .addComponent(b_users))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(b_clear)
                    .addComponent(b_end))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(svrstate))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void b_endActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_endActionPerformed
        try 
        {
            Thread.sleep(1000);                 //5000 milliseconds is five second.
        } 
        catch(InterruptedException ex) {Thread.currentThread().interrupt();}
        
        ison =false;
       try {
           serverSock.close();
        } catch (IOException ex) {
            Logger.getLogger(server_frame.class.getName()).log(Level.SEVERE, null, ex);
        }
        serverState.append("Server stopping... \n");
        
        serverState.setText("");
        
        svrstate.setText("Server is Offline");
        svrstate.setForeground(Color.red);
        for (int i=0; i<admins.size(); i++) {
            admins.remove(i);
        }
        b_start.setEnabled(true);
    }//GEN-LAST:event_b_endActionPerformed

    private void b_startActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_startActionPerformed
        Thread starter = new Thread(new ServerStart());
        starter.start();
        
        ison = true;
        svrstate.setVisible(true);
        svrstate.setText("Server is Online");
        svrstate.setForeground(Color.green);
        
        serverState.append("Server started...\n");
        b_start.setEnabled(false);
    }//GEN-LAST:event_b_startActionPerformed

    private void b_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_usersActionPerformed
        serverState.append("\n Online admins : \n");
        for (String current_user : admins)
        {
            serverState.append(current_user);
            serverState.append("\n");
        }    
        
    }//GEN-LAST:event_b_usersActionPerformed

    private void b_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_b_clearActionPerformed
        serverState.setText("");
    }//GEN-LAST:event_b_clearActionPerformed

    public static void main(String args[]) 
    {
        java.awt.EventQueue.invokeLater(new Runnable() 
        {
            @Override
            public void run() {
                new server_frame().setVisible(true);
            }
        });
    }
    
    public class ServerStart implements Runnable 
    {
        @Override
        public void run() 
        {
            try 
            {
                serverSock = new ServerSocket(5000);

                while (true) 
                {
				Socket clientSock = serverSock.accept();
				Thread listener = new Thread(new Threads(clientSock));
				listener.start();
                                
                }
            }
            catch (Exception ex)
            {
                serverState.append("Error making a connection. \n");
            }
        }
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton b_clear;
    private javax.swing.JButton b_end;
    private javax.swing.JButton b_start;
    private javax.swing.JButton b_users;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea serverState;
    private javax.swing.JLabel svrstate;
    // End of variables declaration//GEN-END:variables
}

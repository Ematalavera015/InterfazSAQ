/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;




/**
 *
 * @author Ema
 */
public class Pool {
    
    public DataSource dataSource;
    
    public String db = "basedatosaq";
    public String url = "jdbc:mysql://localhost/" + db;
    public String user = "root";
    public String password = "";
    
    public Pool(){
        inicializarDataSource();
    }
    
    
    private void inicializarDataSource(){
        
        BasicDataSource basicDataSource = new BasicDataSource();
        //basicDataSource.setDriverClassName("org.gjt.mm.mysql.Driver");
        basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");;
        basicDataSource.setUsername(user);
        basicDataSource.setPassword(password);
        basicDataSource.setUrl(url);
        basicDataSource.setMaxActive(2);
        
        dataSource = basicDataSource;
        
        
        
        
        
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Liga;

import Equipos.Equipo;
import Liga.Liga;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author jaime
 */
public class LigaController {
    ArrayList<Liga> ligas;
    String BDLig = "DBLigas.yap";
    public ArrayList<Liga> getLigas() {
        return ligas;
    }

    public void setLigas(ArrayList<Liga> ligas) {
        this.ligas = ligas;
    }
    
    public Liga getLigas(int i) {
        return ligas.get(i);
    }
    public LigaController() {
        this.ligas = new ArrayList<Liga>();
    }

    public String getBDLig() {
        return BDLig;
    }

    public void setBDLig(String BDLig) {
        this.BDLig = BDLig;
    }
    
    public void guardarLigas(Liga lig){
            ligas.add(lig);
    }
    
    public void guardarLigasFichero(Liga lig) throws FileNotFoundException, IOException, SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDLig);
        db.store(lig);
        db.close();
    }
    public void ModificarLiga(Liga lig) throws SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDLig);

	ObjectSet<Liga> result = db.queryByExample(new Liga(lig.getLiga_ID(),null,0,null,null,null));
		if(result.isEmpty()) 
		        System.out.println("No existe el equipo");
		else {	 
		        Liga existe = (Liga) result.next();    
			  existe.setLiga_ID(lig.getLiga_ID());
                          existe.setNombre_liga(lig.getNombre_liga());
                          existe.setNumero_equipos(lig.getNumero_equipos());
                          existe.setFecha_inicio(lig.getFecha_inicio());
                          existe.setFecha_fin(lig.getFecha_fin());
                          existe.setId_equipos(lig.getId_equipos());
			  db.store(existe); 
		}    

	db.close();
    }
    public void LeerFichero() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
	ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDLig);

            Liga fut = new Liga(null,null,0,null,null,null);
		ObjectSet<Liga> result = db.queryByExample(fut);
		if (result.isEmpty())
			System.out.println("No existen Registros de Ligas..");
		else {
			System.out.printf("Número de registros: %d %n", result.size());

			while (result.hasNext()) {
				Liga p = result.next();
				guardarLigas(p);
			}
		}
		db.close(); // cerrar base de datos
    }
    public void EliminarLiga (Liga lig) throws SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDLig);

            ObjectSet<Liga> result = db.queryByExample(lig);

		if (result.isEmpty())
			System.out.println("No existe el Equipo");
		else {
			System.out.printf("Registros a borrar: %d %n", result.size());

			while (result.hasNext()) {
				Liga p = result.next();
				db.delete(p);
				System.out.println("Borrado....");
			}
		}

	db.close();
    }
    public String[][] datos_liga(){         
        String contenido[][] = new String[getLigas().size()][5];
        for(int i = 0;i < this.getLigas().size();i++){             
            contenido[i][0] = String.valueOf(this.getLigas(i).getNombre_liga());
            contenido[i][1] = String.valueOf(this.getLigas(i).getNumero_equipos()); 
        }
        return contenido;
    }
}

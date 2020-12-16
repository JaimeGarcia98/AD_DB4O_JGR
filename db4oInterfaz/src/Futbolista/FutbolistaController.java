/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Futbolista;


import Futbolista.Futbolista;
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
public class FutbolistaController {
    ArrayList<Futbolista> futbolistas;
    String BDFut = "DBFutbolistas.yap";
    public ArrayList<Futbolista> getFutbolistas() {
        return futbolistas;
    }

    public void setFutbolistas(ArrayList<Futbolista> futbolistas) {
        this.futbolistas = futbolistas;
    }


    public Futbolista getFutbolistas(int i) {
        return futbolistas.get(i);
    }
    public FutbolistaController() {
        this.futbolistas = new ArrayList<Futbolista>();
    }
    
    public void guardarFutbolistas(Futbolista fut){
            futbolistas.add(fut);
    }
    public String getBDFut() {
        return BDFut;
    }

    public void setBDFut(String BDFut) {
        this.BDFut = BDFut;
    }
    public void guardarFutbolistasFichero(Futbolista fut) throws FileNotFoundException, IOException, SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDFut);
        db.store(fut);
        db.close();
    }
    public void ModificarFutbolista(Futbolista fut) throws SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDFut);

	ObjectSet<Futbolista> result = db.queryByExample(new Futbolista(fut.getDni(),null,null,null,0));
		if(result.isEmpty()) 
		        System.out.println("No existe el futbolista");
		else {	 
		        Futbolista existe = (Futbolista) result.next();    
			  existe.setDni(fut.getDni());
                          existe.setNombre(fut.getNombre());
                          existe.setApellido1(fut.getApellido1());
                          existe.setApellido2(fut.getApellido2());
                          existe.setEdad(fut.getEdad());
			  db.store(existe); 
		}    

	db.close();
    }
    public void LeerFichero() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
	ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDFut);

            Futbolista fut = new Futbolista(null,null,null,null,0);
		ObjectSet<Futbolista> result = db.queryByExample(fut);
		if (result.isEmpty())
			System.out.println("No existen Registros de Futbolistas..");
		else {
			System.out.printf("Número de registros: %d %n", result.size());

			while (result.hasNext()) {
				Futbolista p = result.next();
				guardarFutbolistas(p);
			}
		}
		db.close(); // cerrar base de datos
    }
    public void EliminarFut (Futbolista fut) throws SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDFut);

            ObjectSet<Futbolista> result = db.queryByExample(fut);

		if (result.isEmpty())
			System.out.println("No existe el Futbolista");
		else {
			System.out.printf("Registros a borrar: %d %n", result.size());

			while (result.hasNext()) {
				Futbolista p = result.next();
				db.delete(p);
				System.out.println("Borrado....");
			}
		}

	db.close();
    }
    public String[][] datos_fut(){         
        String contenido[][] = new String[getFutbolistas().size()][5];
        for(int i = 0;i < this.getFutbolistas().size();i++){             
            contenido[i][0] = String.valueOf(this.getFutbolistas(i).getNombre());
            contenido[i][1] = String.valueOf(this.getFutbolistas(i).getApellido1()); 

        }
        return contenido;
    }
}

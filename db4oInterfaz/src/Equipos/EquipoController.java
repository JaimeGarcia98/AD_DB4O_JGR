/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Equipos;


import Equipos.Equipo;
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
public class EquipoController {
    ArrayList<Equipo> equipos;
    String BDEqui = "DBEquipos.yap";
    public ArrayList<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(ArrayList<Equipo> equipos) {
        this.equipos = equipos;
    }

    public Equipo getEquipos(int i) {
        return equipos.get(i);
    }

    public EquipoController() {
        this.equipos = new ArrayList<Equipo>();
    }

    public void guardarEquipos(Equipo equip){
            equipos.add(equip);
    }

    public String getBDEqui() {
        return BDEqui;
    }

    public void setBDEqui(String BDEqui) {
        this.BDEqui = BDEqui;
    }
        
    public void guardarEquiposFichero(Equipo equi) throws FileNotFoundException, IOException, SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDEqui);
        db.store(equi);
        db.close();
    }
    public void ModificarEquipo(Equipo equi) throws SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDEqui);

	ObjectSet<Equipo> result = db.queryByExample(new Equipo(equi.getEquipo_ID(),null,null,null,0,null));
		if(result.isEmpty()) 
		        System.out.println("No existe el equipo");
		else {	 
		        Equipo existe = (Equipo) result.next();    
			  existe.setEquipo_ID(equi.getEquipo_ID());
                          existe.setNombre_equipo(equi.getNombre_equipo());
                          existe.setFecha_fundacion(equi.getFecha_fundacion());
                          existe.setNombre_campo(equi.getNombre_campo());
                          existe.setNumero_socios(equi.getNumero_socios());
                          existe.setDniFut(equi.getDniFut());
			  db.store(existe); 
		}    

	db.close();
    }
    public void LeerFichero() throws FileNotFoundException, IOException, ClassNotFoundException, SQLException {
	ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDEqui);

            Equipo fut = new Equipo(null,null,null,null,0,null);
		ObjectSet<Equipo> result = db.queryByExample(fut);
		if (result.isEmpty())
			System.out.println("No existen Registros de Equipos..");
		else {
			System.out.printf("Número de registros: %d %n", result.size());

			while (result.hasNext()) {
				Equipo p = result.next();
				guardarEquipos(p);
			}
		}
		db.close(); // cerrar base de datos
    }
    public void EliminarEqui (Equipo equi) throws SQLException{
        ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), BDEqui);

            ObjectSet<Equipo> result = db.queryByExample(equi);

		if (result.isEmpty())
			System.out.println("No existe el Equipo");
		else {
			System.out.printf("Registros a borrar: %d %n", result.size());

			while (result.hasNext()) {
				Equipo p = result.next();
				db.delete(p);
				System.out.println("Borrado....");
			}
		}

	db.close();
    }
    public String[][] datos_equi(){         
        String contenido[][] = new String[getEquipos().size()][3];
        for(int i = 0;i < this.getEquipos().size();i++){             
            contenido[i][0] = String.valueOf(this.getEquipos(i).getNombre_equipo());
            contenido[i][1] = String.valueOf(this.getEquipos(i).getNombre_campo());
        }
        return contenido;
    }
}

package com.example.mycontactos20232.SQLite.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.mycontactos20232.Contacto;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NOMBRE = "nueva_agenda.db";
    public static final String TABLE_CONTACTOS = "t_contactos_nuevo";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+ TABLE_CONTACTOS + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "alias TEXT," +
                "tipo INT CHECK (tipo IN (0,1)))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTOS);
        onCreate(sqLiteDatabase);
    }

    public int actualizarContacto(int id, String nombre, String alias, int tipo){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("alias", alias);
        values.put("tipo", tipo);

        return db.update(TABLE_CONTACTOS, values, "id = ?", new String[]{String.valueOf(id)});
    }

    public ArrayList<Contacto> obtenerTodosLosContactos() {
        ArrayList<Contacto> contactos = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex("id");
                int nombreIndex = cursor.getColumnIndex("nombre");
                int aliasIndex = cursor.getColumnIndex("alias");
                int tipoIndex = cursor.getColumnIndex("tipo");

                if (idIndex != -1 && nombreIndex != -1 && aliasIndex != -1 && tipoIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String nombre = cursor.getString(nombreIndex);
                    String alias = cursor.getString(aliasIndex);
                    int tipo = cursor.getInt(tipoIndex);

                    Contacto contacto = new Contacto(id, nombre, alias, tipo);
                    contactos.add(contacto);
                } else {
                    // Manejar el caso en que una o más columnas no se encontraron
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return contactos;
    }

    public long insertarContacto(String nombre, String alias, int tipo){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("alias", alias);
            values.put("tipo", tipo);

            return db.insert(TABLE_CONTACTOS, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    public int eliminarContacto(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CONTACTOS, "id = ?", new String[]{String.valueOf(id)});
    }
}
